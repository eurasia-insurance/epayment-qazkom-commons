package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.lapsa.kkb.api.KKBAuthoirzationRequestService;
import com.lapsa.kkb.api.KKBAuthorization;
import com.lapsa.kkb.api.KKBAuthorizationPayment;
import com.lapsa.kkb.api.KKBEncodingException;
import com.lapsa.kkb.api.KKBMerchantSignatureService;
import com.lapsa.kkb.api.KKBSignatureOperationFailed;
import com.lapsa.kkb.xml.KKBXmlDepartment;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;
import com.lapsa.kkb.xml.KKBXmlMerchantSign;
import com.lapsa.kkb.xml.KKBXmlOrder;
import com.lapsa.kkb.xml.KKBXmlSignType;

@Singleton
public class DefaultKKBAuthoirzationRequestService implements KKBAuthoirzationRequestService {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private String merchantName;

    private Marshaller jaxbMarshaller;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

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
	merchantName = configurationProperties.getProperty(PROPERTY_MERCHANT_NAME);
    }

    private void initJAXBMarshaller() throws JAXBException {
	JAXBContext jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
	jaxbMarshaller = jaxbContext.createMarshaller();
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    }

    @Override
    public String encodeRequest(KKBAuthorization request) throws KKBEncodingException {
	try {
	    KKBXmlMerchant merchant = generateMerchant(request);
	    KKBXmlMerchantSign merchantSign = generateMerchantSign(merchant);
	    KKBXmlDocument document = generateDocument(merchant, merchantSign);
	    String xml = generateDocumentXML(document);
	    return xml;
	} catch (JAXBException | KKBSignatureOperationFailed e) {
	    throw new KKBEncodingException(e);
	}
    }

    // PRIVATE & PROTECTED

    private KKBXmlMerchant generateMerchant(KKBAuthorization request) {
	KKBXmlMerchant merchant = new KKBXmlMerchant();
	BigInteger serialNumber = merchantSignatureService.getMerchantCertificate().getSerialNumber();
	merchant.setCertificateSerialNumber(serialNumber);
	merchant.setName(merchantName);

	KKBXmlOrder order = new KKBXmlOrder();
	merchant.setOrder(order);
	order.setOrderId(request.getOrderId());
	order.setAmount(request.getTotalAmount());
	order.setFinCurrency(request.getCurrency());
	order.setDepartments(new ArrayList<>());
	for (KKBAuthorizationPayment p : request.getPayments().values()) {
	    KKBXmlDepartment department = new KKBXmlDepartment();
	    order.getDepartments().add(department);
	    department.setMerchantId(p.getMerchantId());
	    department.setAmount(p.getAmount());
	}
	return merchant;
    }

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
}
