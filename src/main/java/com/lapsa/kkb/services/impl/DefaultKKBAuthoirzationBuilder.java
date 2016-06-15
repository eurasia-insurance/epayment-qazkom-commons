package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.api.KKBFormatException;
import com.lapsa.kkb.api.KKBMerchantSignatureService;
import com.lapsa.kkb.api.KKBPaymentOrderBuilder;
import com.lapsa.kkb.core.KKBPaymentOperation;
import com.lapsa.kkb.core.KKBPaymentOrder;

@Singleton
public class DefaultKKBAuthoirzationBuilder extends KKBGenericService implements KKBPaymentOrderBuilder {
    private String merchantId;

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
	initMerchantProperties();
    }

    private void initMerchantProperties() {
	merchantId = configurationProperties.getProperty(PROPERTY_MERCHANT_ID);
    }

    @Override
    public KKBPaymentOrder buildNewPayment(double amount) {
	return buildNewPayment(generateNewOrderId(), amount, FinCurrency.KZT);
    }

    @Override
    public KKBPaymentOrder buildNewPayment(double amount, FinCurrency currency) {
	return buildNewPayment(generateNewOrderId(), amount, currency);
    }

    @Override
    public KKBPaymentOrder buildNewPayment(String orderId, double amount) {
	return buildNewPayment(orderId, amount, FinCurrency.KZT);
    }

    @Override
    public KKBPaymentOrder buildNewPayment(String orderId, double amount, FinCurrency currency) {
	KKBPaymentOrder order = new KKBPaymentOrder();
	order.setOrderId(orderId);
	order.setCurrency(currency);
	KKBPaymentOperation operation = new KKBPaymentOperation();
	operation.setAmount(amount);
	operation.setMerchantId(merchantId);
	try {
	    order.addOperation(operation);
	} catch (KKBFormatException e) {
	    logger.log(Level.WARNING, "error while building new payment", e);
	}
	return order;
    }
}
