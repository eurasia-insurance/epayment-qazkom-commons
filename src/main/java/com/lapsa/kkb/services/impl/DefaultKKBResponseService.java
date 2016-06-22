package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXException;

import com.lapsa.kkb.core.KKBPaymentRequestDocument;
import com.lapsa.kkb.core.KKBPaymentResponseDocument;
import com.lapsa.kkb.services.KKBBankSignatureService;
import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBResponseService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBValidationErrorException;
import com.lapsa.kkb.services.KKBWrongSignature;
import com.lapsa.kkb.services.impl.validators.KKBRequestResponseValidator;
import com.lapsa.kkb.services.impl.validators.KKBXmlMerchantEqualsValidator;
import com.lapsa.kkb.services.impl.validators.KKBRequestResponsePaymentLinesEqualsValidator;
import com.lapsa.kkb.xml.KKBXmlDocumentRequest;
import com.lapsa.kkb.xml.KKBXmlDocumentResponse;

@Singleton
public class DefaultKKBResponseService extends KKBGenericService
	implements KKBResponseService {

    private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
    private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE
	    | Pattern.DOTALL;
    private static final Pattern BANK_REGEX_PATTERN = Pattern.compile(BANK_REGEX, BANK_REGEX_FLAGS);

    private Unmarshaller responseUnmarshaller;
    private Unmarshaller requestUnmarshaller;

    private KKBRequestResponseValidator[] validators = new KKBRequestResponseValidator[] {
	    new KKBXmlMerchantEqualsValidator(), new KKBRequestResponsePaymentLinesEqualsValidator()
    };

    // @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    // private Properties configurationProperties;

    @EJB
    private KKBBankSignatureService bankSignatureService;

    @PostConstruct
    public void init() {
	try {
	    JAXBContext responseJAXBContext = JAXBContext.newInstance(KKBXmlDocumentResponse.class);
	    responseUnmarshaller = responseJAXBContext.createUnmarshaller();
	    responseUnmarshaller.setSchema(loadSchemaFromResource(SCHEMA_RESPONSE));

	    JAXBContext requestJAXBContext = JAXBContext.newInstance(KKBXmlDocumentRequest.class);
	    requestUnmarshaller = requestJAXBContext.createUnmarshaller();
	    requestUnmarshaller.setSchema(loadSchemaFromResource(SCHEMA_REQUEST));
	} catch (SAXException | IOException | JAXBException e) {
	    String message = String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName());
	    logger.log(Level.SEVERE, message, e);
	    throw new RuntimeException(message, e);
	}
    }

    @Override
    public String parseOrderId(KKBPaymentResponseDocument response) throws KKBServiceError, KKBFormatException {
	return parseOrderId(response.getContent());
    }

    @Override
    public String parseOrderId(String response) throws KKBServiceError, KKBFormatException {
	try {
	    KKBXmlDocumentResponse xmlDocument = (KKBXmlDocumentResponse) responseUnmarshaller
		    .unmarshal(new StringReader(response));
	    return xmlDocument.getBank().getCustomer().getSourceMerchant().getOrder().getOrderId();
	} catch (JAXBException e) {
	    throw new KKBFormatException(e);
	}
    }

    @Override
    public void validateSignature(KKBPaymentResponseDocument response)
	    throws KKBServiceError, KKBFormatException, KKBWrongSignature {
	validateSignature(response.getContent());
    }

    @Override
    public void validateSignature(String response) throws KKBFormatException, KKBServiceError, KKBWrongSignature {
	byte[] data = parseBankElementDataFromResponse(response);
	byte[] sign = parseBankSignDataFromResponse(response);
	bankSignatureService.verify(data, sign);
    }

    @Override
    public void validateResponse(KKBPaymentRequestDocument request, KKBPaymentResponseDocument response)
	    throws KKBFormatException, KKBValidationErrorException {
	try {
	    KKBXmlDocumentRequest xmlRequestDocument = (KKBXmlDocumentRequest) requestUnmarshaller
		    .unmarshal(new StringReader(request.getContent()));
	    KKBXmlDocumentResponse xmlResponseDocument = (KKBXmlDocumentResponse) responseUnmarshaller
		    .unmarshal(new StringReader(response.getContent()));
	    for (KKBRequestResponseValidator validator : validators)
		validator.validate(xmlRequestDocument, xmlResponseDocument);
	} catch (JAXBException e) {
	    throw new KKBFormatException(e);
	}
    }


    @Override
    public KKBPaymentResponseDocument parseResponseDocument(String response) throws KKBFormatException {
	return new KKBPaymentResponseDocument(response);
    }

    // PRIVATE

    private byte[] parseBankSignDataFromResponse(String response) throws KKBFormatException {
	try {
	    KKBXmlDocumentResponse xmlDocument = (KKBXmlDocumentResponse) responseUnmarshaller
		    .unmarshal(new StringReader(response));
	    return xmlDocument.getBankSign().getSignature();
	} catch (JAXBException e) {
	    throw new KKBFormatException(e);
	}
    }

    private byte[] parseBankElementDataFromResponse(String response) throws KKBFormatException {
	Matcher matcher = BANK_REGEX_PATTERN.matcher(response);
	if (matcher.find())
	    return matcher.group().getBytes();
	throw ResponseParserErrorCode.FRMT001.generateFormatException(response);
    }
}
