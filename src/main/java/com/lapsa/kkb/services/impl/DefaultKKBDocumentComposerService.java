package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.QazkomConstants.*;

import java.time.Instant;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.lapsa.kkb.core.KKBCartDocument;
import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.core.KKBPaymentRequestDocument;
import com.lapsa.kkb.services.KKBDocumentComposerService;
import com.lapsa.kkb.services.KKBMerchantSignatureService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.impl.composers.KKBCartDocumentComposer;
import com.lapsa.kkb.services.impl.composers.KKBPaymentRequestDocumentComposer;
import com.lapsa.kkb.services.impl.composers.KKBXmlDocumentComposer;

@Singleton
public class DefaultKKBDocumentComposerService extends KKBGenericService
	implements KKBDocumentComposerService {

    private String merchantId;
    private String merchantName;

    @Resource(lookup = JNDI_QAZKOM_CONFIG)
    private Properties qazkomConfig;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @PostConstruct
    public void init() {
	merchantId = qazkomConfig.getProperty(PROPERTY_MERCHANT_ID);
	merchantName = qazkomConfig.getProperty(PROPERTY_MERCHANT_NAME);
    }

    @Override
    public KKBPaymentRequestDocument composeRequest(KKBOrder order) throws KKBServiceError {
	KKBXmlDocumentComposer composer = new KKBPaymentRequestDocumentComposer(merchantId, merchantName,
		merchantSignatureService);
	String xml = composer.composeXmlDocument(order);
	KKBPaymentRequestDocument doc = new KKBPaymentRequestDocument();
	doc.setCreated(Instant.now());
	doc.setContent(xml);
	order.setLastRequest(doc);
	return doc;
    }

    @Override
    public KKBCartDocument composeCart(KKBOrder order) throws KKBServiceError {
	KKBXmlDocumentComposer composer = new KKBCartDocumentComposer();
	String xml = composer.composeXmlDocument(order);
	KKBCartDocument doc = new KKBCartDocument();
	doc.setCreated(Instant.now());
	doc.setContent(xml);
	order.setLastCart(doc);
	return doc;
    }
}
