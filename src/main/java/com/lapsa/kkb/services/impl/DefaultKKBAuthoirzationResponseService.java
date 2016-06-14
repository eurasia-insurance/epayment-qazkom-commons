package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.lapsa.kkb.api.KKBAuthorizationResponseService;
import com.lapsa.kkb.api.KKBBankSignatureService;
import com.lapsa.kkb.api.KKBInvalidResponseFormatException;
import com.lapsa.kkb.api.KKBSignatureOperationFailed;
import com.lapsa.kkb.api.KKBWrongSignature;
import com.lapsa.kkb.xml.KKBXmlBankSign;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;

@Singleton
public class DefaultKKBAuthoirzationResponseService implements KKBAuthorizationResponseService {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
    private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE;
    private static final Pattern BANK_REGEX_PATTERN = Pattern.compile(BANK_REGEX, BANK_REGEX_FLAGS);

    private Unmarshaller jaxbUnmarshaller;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @EJB
    private KKBBankSignatureService bankSignatureService;

    @PostConstruct
    public void init() {
	try {
	    initJAXB();
	} catch (JAXBException e) {
	    logger.log(Level.SEVERE, String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName()),
		    e);
	}
    }

    private void initJAXB() throws JAXBException {
	JAXBContext jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
	jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	jaxbUnmarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	jaxbUnmarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    }

    @Override
    public void checkSinature(String response) throws KKBInvalidResponseFormatException, KKBSignatureOperationFailed, KKBWrongSignature {
	try {
	    String bankElementXML = parseBankElementXML(response);
	    KKBXmlDocument document = parseResponseDocument(response);
	    KKBXmlBankSign sign = document.getBankSign();
	    byte[] bankSignature = sign.getSignature();
	    bankSignatureService.verify(bankElementXML.getBytes(), bankSignature);
	} catch (JAXBException e) {
	    throw new KKBSignatureOperationFailed(e);
	}
    }

    private KKBXmlDocument parseResponseDocument(String response)
	    throws JAXBException, KKBInvalidResponseFormatException {
	Reader sr = new StringReader(response);
	KKBXmlDocument document = (KKBXmlDocument) jaxbUnmarshaller.unmarshal(sr);
	if (document != null)
	    return document;
	throw new KKBInvalidResponseFormatException(
		String.format("'%1$s' object parsed to null", KKBXmlDocument.class.getSimpleName()));
    }

    private static String parseBankElementXML(String response) throws KKBInvalidResponseFormatException {
	Matcher matcher = BANK_REGEX_PATTERN.matcher(response);
	if (matcher.find())
	    return matcher.group();
	throw new KKBInvalidResponseFormatException(
		String.format("<bank /> element is null or empty at the response string: '%1$s'", response));
    }

}
