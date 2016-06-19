package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.lapsa.kkb.core.KKBCartDocument;
import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.core.KKBPaymentRequestDocument;
import com.lapsa.kkb.services.KKBDocumentComposerService;
import com.lapsa.kkb.services.KKBMerchantSignatureService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.impl.composers.KKBCartDocumentComposer;
import com.lapsa.kkb.services.impl.composers.KKBPaymentRequestDocumentComposer;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;

@Singleton
public class DefaultKKBDocumentComposerService extends KKBGenericService
	implements KKBDocumentComposerService {

    private String merchantId;
    private String merchantName;

    private Marshaller jaxbMarshaller;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @PostConstruct
    public void init() {
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
	    jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	} catch (JAXBException e) {
	    String message = String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName());
	    logger.log(Level.SEVERE, message, e);
	    throw new RuntimeException(message, e);
	}
	merchantId = configurationProperties.getProperty(PROPERTY_MERCHANT_ID);
	merchantName = configurationProperties.getProperty(PROPERTY_MERCHANT_NAME);
    }

    @Override
    public KKBPaymentRequestDocument composeRequest(KKBOrder order) throws KKBServiceError {
	try {
	    KKBXmlDocumentComposer composer = new KKBPaymentRequestDocumentComposer(merchantId, merchantName,
		    jaxbMarshaller, merchantSignatureService);
	    KKBXmlDocument xmlDocument = composer.composeXmlDocument(order);
	    String xml = generateXML(xmlDocument);
	    KKBPaymentRequestDocument request = new KKBPaymentRequestDocument();
	    request.setContent(xml);
	    order.addRequest(request);
	    return request;
	} catch (JAXBException e) {
	    throw new KKBServiceError(e);
	}
    }

    @Override
    public KKBCartDocument composeCart(KKBOrder order) throws KKBServiceError {
	try {
	    KKBXmlDocumentComposer composer = new KKBCartDocumentComposer();
	    KKBXmlDocument xmlDocument = composer.composeXmlDocument(order);
	    String xml = generateXML(xmlDocument);
	    KKBCartDocument cart = new KKBCartDocument();
	    cart.setContent(xml);
	    order.setCart(cart);
	    return cart;
	} catch (JAXBException e) {
	    throw new KKBServiceError(e);
	}
    }

    // PRIVATE

    private String generateXML(KKBXmlDocument xmlDocument) throws JAXBException {
	StringWriter output = new StringWriter();
	jaxbMarshaller.marshal(xmlDocument, output);
	return output.toString();
    }

}
