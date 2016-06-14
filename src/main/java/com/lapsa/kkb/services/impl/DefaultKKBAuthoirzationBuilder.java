package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.util.Properties;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.api.KKBAuthoirzationBuilder;
import com.lapsa.kkb.api.KKBAuthorization;
import com.lapsa.kkb.api.KKBAuthorizationPayment;
import com.lapsa.kkb.api.KKBMerchantSignatureService;

@Singleton
public class DefaultKKBAuthoirzationBuilder implements KKBAuthoirzationBuilder {
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
    public KKBAuthorization buildAuthorization(double amount) {
	return buildAuthorization(generateNewOrderId(), amount, FinCurrency.KZT);
    }

    @Override
    public KKBAuthorization buildAuthorization(double amount, FinCurrency currency) {
	return buildAuthorization(generateNewOrderId(), amount, currency);
    }

    @Override
    public KKBAuthorization buildAuthorization(String orderId, double amount) {
	return buildAuthorization(orderId, amount, FinCurrency.KZT);
    }

    @Override
    public KKBAuthorization buildAuthorization(String orderId, double amount, FinCurrency currency) {
	KKBAuthorization authorization = new KKBAuthorization();
	authorization.setOrderId(orderId);
	authorization.setCurrency(currency);
	KKBAuthorizationPayment payment = new KKBAuthorizationPayment();
	payment.setAmount(amount);
	payment.setMerchantId(merchantId);
	authorization.getPayments().put(merchantId, payment);
	return authorization;
    }
}
