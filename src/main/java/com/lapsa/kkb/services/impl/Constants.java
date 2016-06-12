package com.lapsa.kkb.services.impl;

public interface Constants {
    public static final String KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP = "resource/eurasia36/KKBPKIProperties";

    public static final String PROPERTY_SIGNATURE_ALGORYTHM = "signature.algorythm";

    public static final String PROPERTY_MERCANT_ID = "merchant.id";
    public static final String PROPERTY_MERCANT_NAME = "merchant.name";

    public static final String PROPERTY_MERCANT_KEYSTORE_FILE = "merchant.key.keystore";
    public static final String PROPERTY_MERCANT_KEYSTORE_TYPE = "merchant.key.storetype";
    public static final String PROPERTY_MERCANT_KEYSTORE_PASSWORD = "merchant.key.storepass";
    public static final String PROPERTY_MERCANT_KEYSTORE_KEYALIAS = "merchant.key.alias";

    public static final String PROPERTY_MERCANT_CERTSTORE_FILE = "merchant.cert.keystore";
    public static final String PROPERTY_MERCANT_CERTSTORE_TYPE = "merchant.cert.storetype";
    public static final String PROPERTY_MERCANT_CERTSTORE_PASSWORD = "merchant.cert.storepass";
    public static final String PROPERTY_MERCANT_CERTSTORE_CERTALIAS = "merchant.cert.alias";

    public static final String DEFAULT_MERCANT_KEYSTORE_TYPE = "JKS";
    public static final String DEFAULT_MERCANT_CERTSTORE_TYPE = "JKS";
    public static final String DEFAULT_SIGNATURE_ALG = "SHA1withRSA";
    public static final String DEFAULT_HASH_ALG = "SHA";
}
