package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;

import com.lapsa.kkb.api.KKBBankSignatureService;
import com.lapsa.kkb.api.KKBSignatureOperationFailed;
import com.lapsa.kkb.api.KKBWrongSignature;

@Singleton
public class DefaultKKBBankSignatureService implements KKBBankSignatureService {
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

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
    public X509Certificate getBankCertificate() {
	return certificate;
    }

    @Override
    public void verify(final byte[] data, final byte[] signature)
	    throws KKBSignatureOperationFailed, KKBWrongSignature {
	verify(data, signature, true);
    }

    @Override
    public void verify(final byte[] data, final byte[] signature, boolean inverted)
	    throws KKBSignatureOperationFailed, KKBWrongSignature {
	try {
	    Signature sig = Signature.getInstance(signatureAlgorithm);
	    sig.initVerify(certificate);
	    sig.update(data);
	    boolean result = (inverted) ? sig.verify(invertedByteArray(signature)) : sig.verify(signature);
	    if (!result)
		throw new KKBWrongSignature();
	} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
	    throw new KKBSignatureOperationFailed(e);
	}
    }

    private void initProperties() {
	signatureAlgorithm = configurationProperties.getProperty(PROPERTY_SIGNATURE_ALGORITHM,
		DEFAULT_SIGNATURE_ALGORITHM);
    }

    protected void initCertificate()
	    throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
	String certstoreFile = configurationProperties.getProperty(PROPERTY_BANK_CERTSTORE_FILE);
	String certstoreType = configurationProperties.getProperty(PROPERTY_BANK_CERTSTORE_TYPE,
		DEFAULT_BANK_CERTSTORE_TYPE);
	String certstorePass = configurationProperties.getProperty(PROPERTY_BANK_CERTSTORE_PASSWORD);
	String certAlias = configurationProperties.getProperty(PROPERTY_BANK_CERTSTORE_CERTALIAS);
	certificate = loadCertificate(certstoreFile, certstoreType, certstorePass, certAlias);
    }

    private X509Certificate loadCertificate(String certstoreFile, String certstoreType, String certstorePass,
	    String certAlias) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
	KeyStore keystore = loadKeyStore(certstoreFile, certstoreType, certstorePass);
	return (X509Certificate) keystore.getCertificate(certAlias);
    }

    private KeyStore loadKeyStore(String keystoreFile, String keystoreType, String keystorePassword)
	    throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
	KeyStore keystore = KeyStore.getInstance(keystoreType);
	try (InputStream is = new FileInputStream(keystoreFile)) {
	    keystore.load(is, keystorePassword.toCharArray());
	    return keystore;
	}
    }

    private static byte[] invertedByteArray(final byte[] value) {
	byte[] ret = new byte[value.length];
	for (int i = 0; i < value.length; i++)
	    ret[i] = value[value.length - 1 - i];
	return ret;
    }
}
