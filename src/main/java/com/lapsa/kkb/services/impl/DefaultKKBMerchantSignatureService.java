package com.lapsa.kkb.services.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;

import com.lapsa.kkb.api.KKBMerchantSignatureService;
import com.lapsa.kkb.api.KKBSignatureOperationFailed;
import com.lapsa.kkb.api.KKBWrongSignature;

@Singleton
public class DefaultKKBMerchantSignatureService implements KKBMerchantSignatureService, Constants {

    private X509Certificate certificate;
    private PrivateKey privatekey;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;
    private String signatureAlgorythm;

    @PostConstruct
    public void init() throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException,
	    KeyStoreException, IOException {
	initProperties();
	initPrivateKey();
	initCertificate();
    }

    private void initProperties() {
	signatureAlgorythm = configurationProperties.getProperty(PROPERTY_SIGNATURE_ALGORYTHM, DEFAULT_SIGNATURE_ALG);
    }

    protected void initCertificate()
	    throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
	String certstoreFile = configurationProperties.getProperty(PROPERTY_MERCANT_CERTSTORE_FILE);
	String certstoreType = configurationProperties.getProperty(PROPERTY_MERCANT_CERTSTORE_TYPE,
		DEFAULT_MERCANT_CERTSTORE_TYPE);
	String certstorePass = configurationProperties.getProperty(PROPERTY_MERCANT_CERTSTORE_PASSWORD);
	String certAlias = configurationProperties.getProperty(PROPERTY_MERCANT_CERTSTORE_CERTALIAS);
	certificate = loadCertificate(certstoreFile, certstoreType, certstorePass, certAlias);
    }

    protected void initPrivateKey() throws NoSuchAlgorithmException, CertificateException, KeyStoreException,
	    IOException, UnrecoverableKeyException {
	String keystoreFile = configurationProperties.getProperty(PROPERTY_MERCANT_KEYSTORE_FILE);
	String keystoreType = configurationProperties.getProperty(PROPERTY_MERCANT_KEYSTORE_TYPE,
		DEFAULT_MERCANT_KEYSTORE_TYPE);
	String keystorePassword = configurationProperties.getProperty(PROPERTY_MERCANT_KEYSTORE_PASSWORD);
	String keyAlias = configurationProperties.getProperty(PROPERTY_MERCANT_KEYSTORE_KEYALIAS);
	privatekey = loadPrivateKey(keystoreFile, keystoreType, keystorePassword, keyAlias);
    }

    private X509Certificate loadCertificate(String certstoreFile, String certstoreType, String certstorePass,
	    String certAlias) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
	KeyStore keystore = loadKeyStore(certstoreFile, certstoreType, certstorePass);
	return (X509Certificate) keystore.getCertificate(certAlias);
    }

    private PrivateKey loadPrivateKey(String keystoreFile, String keystoreType, String keystorePassword,
	    String keyAlias) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException,
	    UnrecoverableKeyException {
	KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, keystorePassword);
	PrivateKey privatekey = (PrivateKey) keystore.getKey(keyAlias, keystorePassword.toCharArray());
	return privatekey;
    }

    private KeyStore loadKeyStore(String keystoreFile, String keystoreType, String keystorePassword)
	    throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
	KeyStore keystore = KeyStore.getInstance(keystoreType);
	try (FileInputStream fileinputstream = new FileInputStream(keystoreFile)) {
	    keystore.load(fileinputstream, keystorePassword.toCharArray());
	    return keystore;
	}
    }

    @Override
    public byte[] sign(final byte[] data) throws KKBSignatureOperationFailed {
	return sign(data, true);
    }

    @Override
    public byte[] sign(final byte[] data, boolean inverted) throws KKBSignatureOperationFailed {
	try {
	    Signature sig = Signature.getInstance(signatureAlgorythm);
	    sig.initSign(privatekey);
	    sig.update(data);
	    byte[] signature = sig.sign();
	    return (inverted) ? invertedByteArray(signature) : signature;
	} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
	    throw new KKBSignatureOperationFailed(e);
	}
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
	    Signature sig = Signature.getInstance(signatureAlgorythm);
	    sig.initVerify(certificate);
	    sig.update(data);
	    boolean result = (inverted) ? sig.verify(invertedByteArray(signature)) : sig.verify(signature);
	    if (!result)
		throw new KKBWrongSignature();
	} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
	    throw new KKBSignatureOperationFailed(e);
	}
    }

    private static byte[] invertedByteArray(final byte[] value) {
	byte[] ret = new byte[value.length];
	for (int i = 0; i < value.length; i++)
	    ret[i] = value[value.length - 1 - i];
	return ret;
    }

    @Override
    public X509Certificate getMerchantCertificate() {
	return certificate;
    }
}
