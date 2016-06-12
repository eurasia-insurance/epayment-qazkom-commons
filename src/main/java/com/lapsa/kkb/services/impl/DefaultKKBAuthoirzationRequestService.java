package com.lapsa.kkb.services.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
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

@Singleton
public class DefaultKKBAuthoirzationRequestService implements KKBAuthoirzationRequestService, Constants {

    private String merchantId;
    private String merchantName;

    private Marshaller jaxbMarshaller;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @PostConstruct
    public void init() throws JAXBException {
	initJAXBMarshaller();
	initMerchantProperties();
    }

    private void initMerchantProperties() {
	merchantId = configurationProperties.getProperty(PROPERTY_MERCANT_ID);
	merchantName = configurationProperties.getProperty(PROPERTY_MERCANT_NAME);
    }

    private void initJAXBMarshaller() throws JAXBException {
	JAXBContext jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
	jaxbMarshaller = jaxbContext.createMarshaller();
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
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
	    byte[] documentBytes = generateDocumentBytes(document);
	    return Base64.getEncoder().encodeToString(documentBytes);
	} catch (JAXBException | KKBSignatureOperationFailed e) {
	    throw new KKBEncodingException(e);
	}
    }

    private byte[] generateDocumentBytes(KKBXmlDocument document) throws JAXBException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	jaxbMarshaller.marshal(document, baos);
	return baos.toByteArray();
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
