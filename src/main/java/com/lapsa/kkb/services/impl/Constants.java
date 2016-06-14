package com.lapsa.kkb.services.impl;

public interface Constants {
    public static final String KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP = "resource/eurasia36/KKBPKIProperties";

    public static final String PROPERTY_SIGNATURE_ALGORITHM = "signature.algorithm";

    public static final String PROPERTY_MERCHANT_ID = "merchant.id";
    public static final String PROPERTY_MERCHANT_NAME = "merchant.name";

    public static final String PROPERTY_MERCHANT_KEYSTORE_FILE = "merchant.key.keystore";
    public static final String PROPERTY_MERCHANT_KEYSTORE_TYPE = "merchant.key.storetype";
    public static final String PROPERTY_MERCHANT_KEYSTORE_PASSWORD = "merchant.key.storepass";
    public static final String PROPERTY_MERCHANT_KEYSTORE_KEYALIAS = "merchant.key.alias";

    public static final String PROPERTY_MERCHANT_CERTSTORE_FILE = "merchant.cert.keystore";
    public static final String PROPERTY_MERCHANT_CERTSTORE_TYPE = "merchant.cert.storetype";
    public static final String PROPERTY_MERCHANT_CERTSTORE_PASSWORD = "merchant.cert.storepass";
    public static final String PROPERTY_MERCHANT_CERTSTORE_CERTALIAS = "merchant.cert.alias";

    public static final String PROPERTY_BANK_EPAY_URL = "bank.epay.url";
    public static final String PROPERTY_BANK_EPAY_TEMPLATE = "bank.epay.template";

    public static final String PROPERTY_BANK_CERTSTORE_FILE = "bank.cert.keystore";
    public static final String PROPERTY_BANK_CERTSTORE_TYPE = "bank.cert.storetype";
    public static final String PROPERTY_BANK_CERTSTORE_PASSWORD = "bank.cert.storepass";
    public static final String PROPERTY_BANK_CERTSTORE_CERTALIAS = "bank.cert.alias";

    public static final String DEFAULT_MERCHANT_KEYSTORE_TYPE = "JKS";
    public static final String DEFAULT_MERCHANT_CERTSTORE_TYPE = "JKS";
    public static final String DEFAULT_BANK_CERTSTORE_TYPE = "JKS";

    public static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static final String DEFAULT_HASH_ALGORITHM = "SHA";
    public static final String DEFAULT_EPAY_URL = "https://testpay.kkb.kz/jsp/process/logon.jsp";
    public static final String DEFAULT_EPAY_TEMPLATE_NAME = "default.xsl";
}
