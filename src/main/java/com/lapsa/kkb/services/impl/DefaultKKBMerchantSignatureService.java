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

import com.lapsa.kkb.services.KKBMerchantSignatureService;

@Singleton
public class DefaultKKBMerchantSignatureService extends KKBGenericSignatureSignerAndVerifierService
	implements KKBMerchantSignatureService {

    private String signatureAlgorithm;

    private PrivateKey privateKey;

    private X509Certificate certificate;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @PostConstruct
    public void init() {
	try {
	    initProperties();
	    initPrivateKey();
	    initCertificate();
	} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
		| IOException e) {
	    logger.log(Level.SEVERE, String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName()),
		    e);
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

    // PRIVATE

    private void initProperties() {
	signatureAlgorithm = configurationProperties.getProperty(PROPERTY_SIGNATURE_ALGORITHM,
		DEFAULT_SIGNATURE_ALGORITHM);
    }

    protected void initCertificate()
	    throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
	String certstoreFile = configurationProperties.getProperty(PROPERTY_MERCHANT_CERTSTORE_FILE);
	String certstoreType = configurationProperties.getProperty(PROPERTY_MERCHANT_CERTSTORE_TYPE,
		DEFAULT_MERCHANT_CERTSTORE_TYPE);
	String certstorePass = configurationProperties.getProperty(PROPERTY_MERCHANT_CERTSTORE_PASSWORD);
	String certAlias = configurationProperties.getProperty(PROPERTY_MERCHANT_CERTSTORE_CERTALIAS);
	certificate = loadCertificate(certstoreFile, certstoreType, certstorePass, certAlias);
    }

    protected void initPrivateKey() throws NoSuchAlgorithmException, CertificateException, KeyStoreException,
	    IOException, UnrecoverableKeyException {
	String keystoreFile = configurationProperties.getProperty(PROPERTY_MERCHANT_KEYSTORE_FILE);
	String keystoreType = configurationProperties.getProperty(PROPERTY_MERCHANT_KEYSTORE_TYPE,
		DEFAULT_MERCHANT_KEYSTORE_TYPE);
	String keystorePassword = configurationProperties.getProperty(PROPERTY_MERCHANT_KEYSTORE_PASSWORD);
	String keyAlias = configurationProperties.getProperty(PROPERTY_MERCHANT_KEYSTORE_KEYALIAS);
	privateKey = loadPrivateKey(keystoreFile, keystoreType, keystorePassword, keyAlias);
    }
}
