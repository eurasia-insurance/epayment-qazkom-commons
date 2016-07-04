package com.lapsa.kkb.services.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public abstract class KKBGenericCryptographyService extends KKBGenericService {

    protected static byte[] invertedByteArray(final byte[] value) {
	byte[] ret = new byte[value.length];
	for (int i = 0; i < value.length; i++)
	    ret[i] = value[value.length - 1 - i];
	return ret;
    }

    protected static X509Certificate loadCertificate(String certstoreFile, String certstoreType, String certstorePass,
	    String certAlias) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
	KeyStore keystore = loadKeyStore(certstoreFile, certstoreType, certstorePass);
	X509Certificate certificate = (X509Certificate) keystore.getCertificate(certAlias);
	if (certificate == null)
	    throw new KeyStoreException(String.format("There is no Certificate entry with alias '%1$s'", certAlias));
	return certificate;
    }

    protected static PrivateKey loadPrivateKey(String keystoreFile, String keystoreType, String keystorePassword,
	    String keyAlias) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException,
	    UnrecoverableKeyException {
	KeyStore keystore = loadKeyStore(keystoreFile, keystoreType, keystorePassword);
	PrivateKey privatekey = (PrivateKey) keystore.getKey(keyAlias, keystorePassword.toCharArray());
	if (privatekey == null)
	    throw new KeyStoreException(String.format("There is no PrivateKey entry with alias '%1$s'", keyAlias));
	return privatekey;
    }

    protected static KeyStore loadKeyStore(String keystoreFile, String keystoreType, String keystorePassword)
	    throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
	KeyStore keystore = KeyStore.getInstance(keystoreType);
	try (InputStream is = new FileInputStream(keystoreFile)) {
	    keystore.load(is, keystorePassword.toCharArray());
	    return keystore;
	}
    }

}
