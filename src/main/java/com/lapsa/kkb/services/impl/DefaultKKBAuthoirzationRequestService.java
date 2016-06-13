package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.api.KKBAuthoirzationRequestService;
import com.lapsa.kkb.api.KKBAuthorizationRequest;
import com.lapsa.kkb.api.KKBEncodingException;
import com.lapsa.kkb.api.KKBMerchantSignatureService;
import com.lapsa.kkb.api.KKBSignatureOperationFailed;
import com.lapsa.kkb.xml.KKBXmlDepartment;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;
import com.lapsa.kkb.xml.KKBXmlMerchantSign;
import com.lapsa.kkb.xml.KKBXmlOrder;
import com.lapsa.kkb.xml.KKBXmlSignType;

@Startup
@Singleton
public class DefaultKKBAuthoirzationRequestService implements KKBAuthoirzationRequestService {
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private String merchantId;
    private String merchantName;

    private Marshaller jaxbMarshaller;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @Override
    public String generateNewOrderId() {
	UUID uuid = UUID.randomUUID();
	long lng = Math.abs(uuid.getLeastSignificantBits() / 10000);
	String id = String.format("%015d", lng);
	return id;
    }

    @PostConstruct
    public void init() {
	try {
	    initJAXBMarshaller();
	    initMerchantProperties();
	} catch (JAXBException e) {
	    logger.log(Level.SEVERE, String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName()),
		    e);
	}
    }

    private void initMerchantProperties() {
	merchantId = configurationProperties.getProperty(PROPERTY_MERCANT_ID);
	merchantName = configurationProperties.getProperty(PROPERTY_MERCANT_NAME);
    }

    private void initJAXBMarshaller() throws JAXBException {
	JAXBContext jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
	jaxbMarshaller = jaxbContext.createMarshaller();
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    }

    @Override
    public String encodeRequest(KKBAuthorizationRequest request) throws KKBEncodingException {
	return encodeRequest(request.getOrderId(), request.getAmount(), request.getCurrency());
    }

    private KKBXmlMerchant generateMerchant(String orderId, double amount, FinCurrency currency) {
	KKBXmlMerchant merchant = new KKBXmlMerchant();
	BigInteger serialNumber = merchantSignatureService.getMerchantCertificate().getSerialNumber();
	merchant.setCertificateSerialNumber(serialNumber);
	merchant.setName(merchantName);

	KKBXmlOrder order = new KKBXmlOrder();
	merchant.setOrders(new ArrayList<>());
	merchant.getOrders().add(order);
	order.setOrderId(orderId);
	order.setAmount(amount);
	order.setFinCurrency(currency);

	KKBXmlDepartment department = new KKBXmlDepartment();
	order.setDepartments(new ArrayList<>());
	order.getDepartments().add(department);
	department.setMerchantId(merchantId);
	department.setAmount(amount);
	return merchant;
    }

    @Override
    public String encodeRequest(String orderId, double amount, FinCurrency currency) throws KKBEncodingException {
	try {
	    KKBXmlMerchant merchant = generateMerchant(orderId, amount, currency);
	    KKBXmlMerchantSign merchantSign = generateSignature(merchant);
	    KKBXmlDocument document = generateDocument(merchant, merchantSign);
	    String xml = generateDocumentXML(document);
	    return xml;
	} catch (JAXBException | KKBSignatureOperationFailed e) {
	    throw new KKBEncodingException(e);
	}
    }

    @SuppressWarnings("unused")
    private byte[] generateDocumentBytes(KKBXmlDocument document) throws JAXBException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	jaxbMarshaller.marshal(document, baos);
	return baos.toByteArray();
    }

    private String generateDocumentXML(KKBXmlDocument document) throws JAXBException {
	StringWriter baos = new StringWriter();
	jaxbMarshaller.marshal(document, baos);
	return baos.toString();
    }

    private KKBXmlDocument generateDocument(KKBXmlMerchant merchant, KKBXmlMerchantSign merchantSign) {
	KKBXmlDocument document = new KKBXmlDocument();
	document.setMerchant(merchant);
	document.setMerchantSign(merchantSign);
	return document;
    }

    private KKBXmlMerchantSign generateSignature(KKBXmlMerchant merchant)
	    throws JAXBException, KKBSignatureOperationFailed {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	jaxbMarshaller.marshal(merchant, baos);
	KKBXmlMerchantSign sign = new KKBXmlMerchantSign();
	sign.setSignType(KKBXmlSignType.RSA);
	byte[] signature = merchantSignatureService.sign(baos.toByteArray());
	sign.setSignature(signature);
	return sign;
    }
}
