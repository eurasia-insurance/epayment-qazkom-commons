package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.net.MalformedURLException;
import java.net.URL;
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

    @Resource(lookup = JNDI_PROPERTIES_CONFIGURATION)
    private Properties configurationProperties;

    @Override
    public URL generatePaymentPageUrl(String orderId) {
	String paymentUrlPattern = configurationProperties.getProperty(Constants.PROPERTY_MARKET_PAYMENT_URL);
	String parsed = paymentUrlPattern.replace("$ORDER_ID", orderId);
	URL ret;
	try {
	    ret = new URL(parsed);
	    return ret;
	} catch (MalformedURLException e) {
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
	KKBOrder order = new KKBOrder();
	order.setId(orderId);
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
