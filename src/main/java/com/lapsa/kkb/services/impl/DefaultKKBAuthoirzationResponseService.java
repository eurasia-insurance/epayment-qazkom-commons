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

import com.lapsa.kkb.api.KKBAuthorization;
import com.lapsa.kkb.api.KKBAuthorizationPayment;
import com.lapsa.kkb.api.KKBAuthorizationPaymentResult;
import com.lapsa.kkb.api.KKBAuthorizationResponseService;
import com.lapsa.kkb.api.KKBBankSignatureService;
import com.lapsa.kkb.api.KKBResponseFormatValidationError;
import com.lapsa.kkb.api.KKBSignatureOperationFailed;
import com.lapsa.kkb.api.KKBWrongSignature;
import com.lapsa.kkb.xml.KKBXmlBank;
import com.lapsa.kkb.xml.KKBXmlBankSign;
import com.lapsa.kkb.xml.KKBXmlCustomer;
import com.lapsa.kkb.xml.KKBXmlDepartment;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;
import com.lapsa.kkb.xml.KKBXmlOrder;
import com.lapsa.kkb.xml.KKBXmlPayment;
import com.lapsa.kkb.xml.KKBXmlResults;
import com.lapsa.kkb.xml.KKBXmlSecureType;

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
    public KKBAuthorization parseResponse(String response)
	    throws KKBWrongSignature, KKBSignatureOperationFailed, KKBResponseFormatValidationError {
	try {
	    KKBXmlDocument document = parseResponseDocument(response);
	    KKBAuthorization authorization = buildAuthorization(document);
	    return authorization;
	} catch (JAXBException e) {
	    throw new KKBResponseFormatValidationError(e);
	}
    }

    @Override
    public void checkSinature(String response)
	    throws KKBResponseFormatValidationError, KKBWrongSignature, KKBSignatureOperationFailed {
	try {
	    String bankElementXML = parseBankElementXML(response);
	    KKBXmlDocument document = parseResponseDocument(response);
	    KKBXmlBankSign sign = document.getBankSign();
	    byte[] bankSignature = sign.getSignature();
	    bankSignatureService.verify(bankElementXML.getBytes(), bankSignature);
	} catch (JAXBException e) {
	    throw new KKBResponseFormatValidationError(e);
	}
    }

    @Override
    public void validate(KKBAuthorization request, KKBAuthorization response) {
	// TODO Auto-generated method stub

    }

    // PRIVATE

    private KKBAuthorization buildAuthorization(KKBXmlDocument document) throws KKBResponseFormatValidationError {
	KKBXmlBank bank = document.getBank();
	KKBXmlCustomer customer = bank.getCustomer();

	KKBAuthorization res = new KKBAuthorization();
	res.setCustomerName(customer.getName());
	res.setCustomerPhone(customer.getPhone());
	res.setCustomerMail(customer.getEmailAddress());

	KKBXmlOrder order = customer.getSourceMerchant().getOrder();

	res.setOrderId(order.getOrderId());
	res.setCurrency(order.getFinCurrency());

	for (KKBXmlDepartment d : order.getDepartments()) {
	    if (res.getPayments().containsKey(d.getMerchantId()))
		throw new KKBResponseFormatValidationError(String.format(
			"Multipe <department /> element with the same merchant_id = '%1$s'", d.getMerchantId()));
	    KKBAuthorizationPayment payment = new KKBAuthorizationPayment();
	    payment.setAmount(d.getAmount());
	    payment.setMerchantId(d.getMerchantId());
	    res.getPayments().put(payment.getMerchantId(), payment);
	}

	KKBXmlResults results = bank.getResults();
	res.setPaymentsTimestamp(results.getTimestamp());
	for (KKBXmlPayment p : results.getPayments()) {
	    KKBAuthorizationPayment payment = res.getPayments().get(p.getMerchantId());
	    if (payment == null)
		throw new KKBResponseFormatValidationError(
			String.format(
				"Unexpected <payment/> element with merchant_id = '%1$s' (is not present as <department/> elements section)",
				p.getMerchantId()));
	    if (payment.getAmount() != p.getAmount())
		throw new KKBResponseFormatValidationError(
			String.format(
				"<department/> amount = '%1$d' with merchant_id = '%2$s' is not equals to <payment/> amount = '%3$d' with merchant_id = '%4$s'",
				payment.getAmount(), payment.getMerchantId(), p.getAmount(), p.getMerchantId()));
	    payment.setResult(parsePaymentResult(p));
	}
	return res;
    }

    protected KKBAuthorizationPaymentResult parsePaymentResult(KKBXmlPayment p) {
	KKBAuthorizationPaymentResult result = new KKBAuthorizationPaymentResult();
	result.setApprovalCode(p.getApprovalCode());
	result.setCardHash(p.getCardHash());
	result.setCardIssuerConutry(p.getCardCountry());
	result.setCardNumberMasked(p.getCardNumberMasked());
	result.setReference(p.getReference());
	result.setResponseCode(p.getResponseCode());
	result.setSecured(p.getSecureType().equals(KKBXmlSecureType.SECURED_3D));
	return result;
    }

    private KKBXmlDocument parseResponseDocument(String response)
	    throws JAXBException, KKBResponseFormatValidationError {
	Reader sr = new StringReader(response);
	KKBXmlDocument document = (KKBXmlDocument) jaxbUnmarshaller.unmarshal(sr);
	if (document != null)
	    return document;
	throw new KKBResponseFormatValidationError(
		String.format("'%1$s' object parsed to null", KKBXmlDocument.class.getSimpleName()));
    }

    private static String parseBankElementXML(String response) throws KKBResponseFormatValidationError {
	Matcher matcher = BANK_REGEX_PATTERN.matcher(response);
	if (matcher.find())
	    return matcher.group();
	throw new KKBResponseFormatValidationError(
		String.format("<bank /> element is null or empty at the response string: '%1$s'", response));
    }

}
