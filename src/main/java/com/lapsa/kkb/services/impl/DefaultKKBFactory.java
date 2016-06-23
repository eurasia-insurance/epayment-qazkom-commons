package com.lapsa.kkb.services.impl;

import java.util.UUID;

import javax.ejb.Singleton;

import com.lapsa.kkb.core.KKBPaymentResponseDocument;
import com.lapsa.kkb.services.KKBFactory;
import com.lapsa.kkb.services.KKBFormatException;

@Singleton
public class DefaultKKBFactory extends KKBGenericService implements KKBFactory {

    @Override
    public String generateNewOrderId() {
	UUID uuid = UUID.randomUUID();
	long lng = Math.abs(uuid.getLeastSignificantBits() / 10000);
	String id = String.format("%015d", lng);
	return id;
    }

    @Override
    public KKBPaymentResponseDocument buildResponseDocument(String response) throws KKBFormatException {
	if (response == null || response.equals(""))
	    throw new KKBFormatException("Response is empty");
	KKBPaymentResponseDocument doc = new KKBPaymentResponseDocument();
	doc.setContent(response);
	return doc;
    }

}
