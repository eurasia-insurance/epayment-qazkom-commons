package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
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

import com.lapsa.kkb.api.KKBBankSignatureService;
import com.lapsa.kkb.api.KKBFormatException;
import com.lapsa.kkb.api.KKBMerchantSignatureService;
import com.lapsa.kkb.api.KKBPaymentOrderService;
import com.lapsa.kkb.api.KKBServiceError;
import com.lapsa.kkb.api.KKBSignatureOperationFailed;
import com.lapsa.kkb.core.KKBPaymentCustomer;
import com.lapsa.kkb.core.KKBPaymentOperation;
import com.lapsa.kkb.core.KKBPaymentOperationResult;
import com.lapsa.kkb.core.KKBPaymentOrder;
import com.lapsa.kkb.core.KKBSignature;
import com.lapsa.kkb.core.KKBSingatureStatus;
import com.lapsa.kkb.xml.KKBXmlBank;
import com.lapsa.kkb.xml.KKBXmlBankSign;
import com.lapsa.kkb.xml.KKBXmlCustomer;
import com.lapsa.kkb.xml.KKBXmlDepartment;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;
import com.lapsa.kkb.xml.KKBXmlMerchantSign;
import com.lapsa.kkb.xml.KKBXmlOrder;
import com.lapsa.kkb.xml.KKBXmlPayment;
import com.lapsa.kkb.xml.KKBXmlResults;
import com.lapsa.kkb.xml.KKBXmlSecureType;
import com.lapsa.kkb.xml.KKBXmlSignType;

@Singleton
public class DefaultKKBPaymentOrderService extends KKBGenericService
	implements KKBPaymentOrderService {

    private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
    private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE;
    private static final Pattern BANK_REGEX_PATTERN = Pattern.compile(BANK_REGEX, BANK_REGEX_FLAGS);

    private String merchantName;

    private Unmarshaller jaxbUnmarshaller;
    private Marshaller jaxbMarshaller;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @EJB
    private KKBBankSignatureService bankSignatureService;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @PostConstruct
    public void init() {
	try {
	    initJAXB();
	    initMerchantProperties();
	} catch (JAXBException e) {
	    logger.log(Level.SEVERE, String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName()),
		    e);
	}
    }

    @Override
    public KKBPaymentOrder parseResponse(String response) throws KKBFormatException, KKBServiceError {
	try {
	    KKBXmlDocument document = parseResponseDocument(response);
	    KKBPaymentOrder paymentOrder = buildAuthorization(document.getBank());
	    KKBSignature bankSignature = parseBankSignature(response, document.getBankSign());
	    paymentOrder.setResponseSignature(bankSignature);
	    return paymentOrder;
	} catch (JAXBException e) {
	    throw new KKBServiceError(e);
	}
    }

    @Override
    public String composeRequest(KKBPaymentOrder request) throws KKBServiceError {
	try {
	    KKBXmlMerchant merchant = generateMerchant(request);
	    KKBXmlMerchantSign merchantSign = generateMerchantSign(merchant);
	    KKBXmlDocument document = generateDocument(merchant, merchantSign);
	    String xml = generateDocumentXML(document);
	    return xml;
	} catch (JAXBException e) {
	    throw new KKBServiceError(e);
	}
    }

    @Override
    public void validate(KKBPaymentOrder requestOrder, KKBPaymentOrder responseOrder) {
	// TODO Auto-generated method stub
    }

    // PRIVATE

    private KKBXmlMerchantSign generateMerchantSign(KKBXmlMerchant merchant)
	    throws JAXBException, KKBSignatureOperationFailed {
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	jaxbMarshaller.marshal(merchant, output);
	KKBXmlMerchantSign sign = new KKBXmlMerchantSign();
	sign.setSignType(KKBXmlSignType.RSA);
	byte[] signature = merchantSignatureService.sign(output.toByteArray());
	sign.setSignature(signature);
	return sign;
    }

    private KKBXmlDocument generateDocument(KKBXmlMerchant merchant, KKBXmlMerchantSign merchantSign) {
	KKBXmlDocument document = new KKBXmlDocument();
	document.setMerchant(merchant);
	document.setMerchantSign(merchantSign);
	return document;
    }

    private String generateDocumentXML(KKBXmlDocument document) throws JAXBException {
	StringWriter output = new StringWriter();
	jaxbMarshaller.marshal(document, output);
	return output.toString();
    }

    private KKBXmlMerchant generateMerchant(KKBPaymentOrder paymentOrder) {
	KKBXmlMerchant merchant = new KKBXmlMerchant();
	BigInteger serialNumber = merchantSignatureService.getSignatureCertificate().getSerialNumber();
	merchant.setCertificateSerialNumber(serialNumber);
	merchant.setName(merchantName);

	KKBXmlOrder order = new KKBXmlOrder();
	merchant.setOrder(order);
	order.setOrderId(paymentOrder.getOrderId());
	order.setAmount(paymentOrder.getTotalAmount());
	order.setFinCurrency(paymentOrder.getCurrency());
	order.setDepartments(new ArrayList<>());
	for (KKBPaymentOperation p : paymentOrder.getOperationsList()) {
	    KKBXmlDepartment department = new KKBXmlDepartment();
	    order.getDepartments().add(department);
	    department.setMerchantId(p.getMerchantId());
	    department.setAmount(p.getAmount());
	}
	return merchant;
    }

    @SuppressWarnings("deprecation")
    private KKBSignature parseBankSignature(String response, KKBXmlBankSign kkbXmlBankSign) throws KKBFormatException {
	String bankElXML = parseBankElementXML(response);
	byte[] data = bankElXML.getBytes();
	byte[] signature = kkbXmlBankSign.getSignature();
	KKBSignature res = new KKBSignature();
	res.setData(data);
	res.setSignature(signature);
	res.setInverted(true);
	res.setStatus(KKBSingatureStatus.UNCHECKED);
	return res;
    }

    private void initJAXB() throws JAXBException {
	JAXBContext jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
	jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	jaxbMarshaller = jaxbContext.createMarshaller();
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    }

    private void initMerchantProperties() {
	merchantName = configurationProperties.getProperty(PROPERTY_MERCHANT_NAME);
    }

    private KKBPaymentCustomer convertFrom(KKBXmlCustomer xmlCustomer) {
	KKBPaymentCustomer customer = new KKBPaymentCustomer();
	customer.setCustomerMail(xmlCustomer.getEmailAddress());
	customer.setCustomerName(xmlCustomer.getName());
	customer.setCustomerPhone(xmlCustomer.getPhone());
	return customer;
    }

    private KKBPaymentOrder buildAuthorization(KKBXmlBank bank)
	    throws KKBFormatException {
	KKBXmlCustomer customer = bank.getCustomer();

	KKBPaymentOrder res = new KKBPaymentOrder();
	res.setCustomer(convertFrom(customer));

	KKBXmlOrder order = customer.getSourceMerchant().getOrder();

	res.setOrderId(order.getOrderId());
	res.setCurrency(order.getFinCurrency());

	for (KKBXmlDepartment d : order.getDepartments()) {
	    KKBPaymentOperation paymentOperation = new KKBPaymentOperation();
	    paymentOperation.setAmount(d.getAmount());
	    paymentOperation.setMerchantId(d.getMerchantId());
	    res.addOperation(paymentOperation);
	}

	KKBXmlResults results = bank.getResults();
	res.setPaymentsTimestamp(results.getTimestamp());
	for (KKBXmlPayment p : results.getPayments()) {
	    KKBPaymentOperation payment = res.getOperationToMerchant(p.getMerchantId());
	    if (payment == null)
		throw new KKBFormatException(
			String.format(
				"Unexpected <payment/> element with merchant_id = '%1$s' (is not present as <department/> elements section)",
				p.getMerchantId()));
	    if (payment.getAmount() != p.getAmount())
		throw new KKBFormatException(
			String.format(
				"<department/> amount = '%1$d' with merchant_id = '%2$s' is not equals to <payment/> amount = '%3$d' with merchant_id = '%4$s'",
				payment.getAmount(), payment.getMerchantId(), p.getAmount(), p.getMerchantId()));
	    payment.setResult(parsePaymentResult(p));
	}
	return res;
    }

    protected KKBPaymentOperationResult parsePaymentResult(KKBXmlPayment p) {
	KKBPaymentOperationResult result = new KKBPaymentOperationResult();
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
	    throws JAXBException, KKBFormatException {
	Reader sr = new StringReader(response);
	KKBXmlDocument document = (KKBXmlDocument) jaxbUnmarshaller.unmarshal(sr);
	if (document != null)
	    return document;
	throw new KKBFormatException(
		String.format("'%1$s' object parsed to null", KKBXmlDocument.class.getSimpleName()));
    }

    private static String parseBankElementXML(String response) throws KKBFormatException {
	Matcher matcher = BANK_REGEX_PATTERN.matcher(response);
	if (matcher.find())
	    return matcher.group();
	throw new KKBFormatException(
		String.format("<bank /> element is null or empty at the response string: '%1$s'", response));
    }

}
