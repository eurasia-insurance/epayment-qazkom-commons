package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;

import com.lapsa.kkb.api.KKBBankSignatureService;

@Singleton
public class DefaultKKBBankSignatureService extends KKBGenericSignatureVerifierService
	implements KKBBankSignatureService {
    private String signatureAlgorithm;

    private X509Certificate certificate;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @PostConstruct
    public void init() {
	try {
	    initProperties();
	    initCertificate();
	} catch (NoSuchAlgorithmException | CertificateException | KeyStoreException
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

    // PRIVATE

    private void initProperties() {
	signatureAlgorithm = configurationProperties.getProperty(PROPERTY_SIGNATURE_ALGORITHM,
		DEFAULT_SIGNATURE_ALGORITHM);
    }

    private void initCertificate()
	    throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
	String certstoreFile = configurationProperties.getProperty(PROPERTY_BANK_CERTSTORE_FILE);
	String certstoreType = configurationProperties.getProperty(PROPERTY_BANK_CERTSTORE_TYPE,
		DEFAULT_BANK_CERTSTORE_TYPE);
	String certstorePass = configurationProperties.getProperty(PROPERTY_BANK_CERTSTORE_PASSWORD);
	String certAlias = configurationProperties.getProperty(PROPERTY_BANK_CERTSTORE_CERTALIAS);
	certificate = loadCertificate(certstoreFile, certstoreType, certstorePass, certAlias);
    }

}
