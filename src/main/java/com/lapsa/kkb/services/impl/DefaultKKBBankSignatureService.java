package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.QazkomConstants.*;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.lapsa.kkb.services.KKBBankSignatureService;

import tech.lapsa.java.commons.logging.MyLogger;

@Singleton
@Startup
public class DefaultKKBBankSignatureService extends KKBGenericSignatureVerifierService
	implements KKBBankSignatureService {

    private static final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(KKBBankSignatureService.class) //
	    .build();

    private String signatureAlgorithm;

    private X509Certificate certificate;

    @Resource(lookup = JNDI_QAZKOM_CONFIG)
    private Properties qazkomConfig;

    @PostConstruct
    public void init() {
	signatureAlgorithm = qazkomConfig.getProperty(PROPERTY_SIGNATURE_ALGORITHM,
		DEFAULT_SIGNATURE_ALGORITHM);

	String certstoreFile = qazkomConfig.getProperty(PROPERTY_BANK_CERTSTORE_FILE);
	String certstoreType = qazkomConfig.getProperty(PROPERTY_BANK_CERTSTORE_TYPE,
		DEFAULT_BANK_CERTSTORE_TYPE);
	String certstorePass = qazkomConfig.getProperty(PROPERTY_BANK_CERTSTORE_PASSWORD);
	String certAlias = qazkomConfig.getProperty(PROPERTY_BANK_CERTSTORE_CERTALIAS);
	try {
	    certificate = loadCertificate(certstoreFile, certstoreType, certstorePass, certAlias);
	} catch (NoSuchAlgorithmException | CertificateException | KeyStoreException
		| IOException e) {
	    String message = String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName());
	    logger.SEVERE.log(e, message);
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
}
