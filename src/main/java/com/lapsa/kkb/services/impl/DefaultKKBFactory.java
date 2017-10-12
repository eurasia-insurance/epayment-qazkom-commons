package com.lapsa.kkb.services.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.Singleton;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.core.KKBOrderItem;
import com.lapsa.kkb.core.KKBPaymentStatus;
import com.lapsa.kkb.services.KKBFactory;

@Singleton
public class DefaultKKBFactory extends KKBGenericService implements KKBFactory {

    @Resource(lookup = Constants.JNDI_CONFIG)
    private Properties epaymentConfig;

    @Override
    public URI generatePaymentPageUrl(String invoiceId) {
	String paymentUrlPattern = epaymentConfig.getProperty(Constants.PROPERTY_PAYMENT_URL_PATTERN);
	String parsed = paymentUrlPattern.replace("@INVOICE_ID@", invoiceId);
	try {
	    return new URI(parsed);
	} catch (URISyntaxException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public String generateNewOrderId() {
	UUID uuid = UUID.randomUUID();
	long lng = Math.abs(uuid.getLeastSignificantBits() / 10000);
	String id = String.format("%015d", lng);
	return id;
    }

    @Override
    public KKBOrder generateNewOrder(FinCurrency currency, double cost, String product) {
	return generateNewOrder(generateNewOrderId(), currency, cost, product);
    }

    @Override
    public KKBOrder generateNewOrder(String orderId, FinCurrency currency, double cost, String product) {
	KKBOrder order = new KKBOrder(orderId);
	order.setCreated(Instant.now());
	order.setStatus(KKBPaymentStatus.NEW);
	order.setCurrency(currency);
	generateNewOrderItem(product, cost, 1, order);
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
