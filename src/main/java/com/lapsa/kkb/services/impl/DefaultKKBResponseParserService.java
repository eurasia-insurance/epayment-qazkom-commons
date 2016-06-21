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
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import com.lapsa.kkb.services.KKBBankSignatureService;
import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBResponseParserService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBWrongSignature;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;

@Singleton
public class DefaultKKBResponseParserService extends KKBGenericService
	implements KKBResponseParserService {

    private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
    private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE
	    | Pattern.DOTALL;
    private static final Pattern BANK_REGEX_PATTERN = Pattern.compile(BANK_REGEX, BANK_REGEX_FLAGS);

    private Unmarshaller unmarshaller;

    // @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    // private Properties configurationProperties;

    @EJB
    private KKBBankSignatureService bankSignatureService;

    @PostConstruct
    public void init() {
	try {
	    Schema schema = loadSchemaFromResource(SCHEMA_RESPONSE);
	    JAXBContext jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
	    unmarshaller = jaxbContext.createUnmarshaller();
	    unmarshaller.setSchema(schema);
	} catch (SAXException | IOException | JAXBException e) {
	    String message = String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName());
	    logger.log(Level.SEVERE, message, e);
	    throw new RuntimeException(message, e);
	}
    }

    @Override
    public String parseOrderId(String response) throws KKBServiceError, KKBFormatException {
	try {
	    KKBXmlDocument xmlDocument = (KKBXmlDocument) unmarshaller.unmarshal(new StringReader(response));
	    return xmlDocument.getBank().getCustomer().getSourceMerchant().getOrder().getOrderId();
	} catch (JAXBException e) {
	    throw new KKBFormatException(e);
	}
    }

    @Override
    public void validateSignature(String response) throws KKBFormatException, KKBServiceError, KKBWrongSignature {
	byte[] data = parseBankElementDataFromResponse(response);
	byte[] sign = parseBankSignDataFromResponse(response);
	bankSignatureService.verify(data, sign);
    }

    // PRIVATE

    private byte[] parseBankSignDataFromResponse(String response) throws KKBFormatException {
	try {
	    KKBXmlDocument xmlDocument = (KKBXmlDocument) unmarshaller.unmarshal(new StringReader(response));
	    return xmlDocument.getBankSign().getSignature();
	} catch (JAXBException e) {
	    throw new KKBFormatException(e);
	}
    }

    private byte[] parseBankElementDataFromResponse(String response) throws KKBFormatException {
	Matcher matcher = BANK_REGEX_PATTERN.matcher(response);
	if (matcher.find())
	    return matcher.group().getBytes();
	throw new KKBFormatException(
		String.format("<bank /> element is null or empty at the response string: '%1$s'", response));
    }

}
