package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.lapsa.kkb.services.KKBMerchantSignatureService;

@Singleton
@Startup
public class DefaultKKBMerchantSignatureService extends KKBGenericSignatureSignerAndVerifierService
	implements KKBMerchantSignatureService {

    private String signatureAlgorithm;

    private PrivateKey privateKey;

    private X509Certificate certificate;

    @Resource(lookup = JNDI_PROPERTIES_CONFIGURATION)
    private Properties configurationProperties;

    @PostConstruct
    public void init() {
	signatureAlgorithm = configurationProperties.getProperty(PROPERTY_SIGNATURE_ALGORITHM,
		DEFAULT_SIGNATURE_ALGORITHM);

	String keystoreFile = configurationProperties.getProperty(PROPERTY_MERCHANT_KEYSTORE_FILE);
	String keystoreType = configurationProperties.getProperty(PROPERTY_MERCHANT_KEYSTORE_TYPE,
		DEFAULT_MERCHANT_KEYSTORE_TYPE);
	String keystorePassword = configurationProperties.getProperty(PROPERTY_MERCHANT_KEYSTORE_PASSWORD);
	String keyAlias = configurationProperties.getProperty(PROPERTY_MERCHANT_KEYSTORE_KEYALIAS);

	String certstoreFile = configurationProperties.getProperty(PROPERTY_MERCHANT_CERTSTORE_FILE);
	String certstoreType = configurationProperties.getProperty(PROPERTY_MERCHANT_CERTSTORE_TYPE,
		DEFAULT_MERCHANT_CERTSTORE_TYPE);
	String certstorePass = configurationProperties.getProperty(PROPERTY_MERCHANT_CERTSTORE_PASSWORD);
	String certAlias = configurationProperties.getProperty(PROPERTY_MERCHANT_CERTSTORE_CERTALIAS);

	try {
	    privateKey = loadPrivateKey(keystoreFile, keystoreType, keystorePassword, keyAlias);
	    certificate = loadCertificate(certstoreFile, certstoreType, certstorePass, certAlias);
	} catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException
		| UnrecoverableKeyException e) {
	    String message = String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName());
	    logger.log(Level.SEVERE, message, e);
	    throw new RuntimeException(message, e);
	}
    }

    @Override
    public X509Certificate getSignatureCertificate() {
	return certificate;
    }

    @Override
    public String getSignatureAlgorithm() {
	return signatureAlgorithm;
    }

    @Override
    protected PrivateKey getPrivateKey() {
	return privateKey;
    }
}