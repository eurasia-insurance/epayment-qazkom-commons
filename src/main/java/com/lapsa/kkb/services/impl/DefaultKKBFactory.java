package com.lapsa.kkb.services.impl;

import java.util.Date;
import java.util.UUID;

import javax.ejb.Singleton;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.core.KKBOrderItem;
import com.lapsa.kkb.core.KKBPaymentResponseDocument;
import com.lapsa.kkb.core.KKBPaymentStatus;
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
	doc.setCreated(new Date());
	doc.setContent(response);
	return doc;
    }

    @Override
    public KKBOrder generateNewOrder(FinCurrency currency, double cost, String product) {
	return generateNewOrder(generateNewOrderId(), currency, cost, product);
    }

    @Override
    public KKBOrder generateNewOrder(String orderId, FinCurrency currency, double cost, String product) {
	KKBOrder order = new KKBOrder();
	order.setId(orderId);
	order.setCreated(new Date());
	order.setStatus(KKBPaymentStatus.NEW);
	order.setCurrency(currency);
	generateNewOrderItem(product, cost, 1, order);
	order.calculateTotalAmount();
	return order;
    }

    @Override
    public KKBOrderItem generateNewOrderItem(String product, double cost, int quantity) {
	KKBOrderItem i = new KKBOrderItem();
	i.setCost(cost);
	i.setQuantity(quantity);
	i.setName(product);
	return i;
    }

    @Override
    public KKBOrderItem generateNewOrderItem(String product, double cost, int quantity, KKBOrder order) {
	KKBOrderItem i = generateNewOrderItem(product, cost, quantity);
	order.addItem(i);
	return i;
    }
}
