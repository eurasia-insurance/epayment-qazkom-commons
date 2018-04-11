package test.builder;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import tech.lapsa.java.commons.io.MyResources;
import tech.lapsa.java.commons.security.MyCertificates;
import tech.lapsa.java.commons.security.MyKeyStores;
import tech.lapsa.java.commons.security.MyKeyStores.StoreType;
import tech.lapsa.java.commons.security.MyPrivateKeys;

public final class TestKeystoreUtils {

    private TestKeystoreUtils() {
    }

    public static final StoreType STORETYPE = StoreType.JKS;
    public static final String KEYSTORE = "/kkb.jks";
    public static final String STOREPASS = "1q2w3e4r";

    public static final String BANK_ENTRY_ALIAS = "kkbca-test";
    public static final String MERCHANT_ENTRY_ALIAS = "cert";

    private static final X509Certificate BANK_CER;
    private static final X509Certificate MERCHANT_CERT;
    private static final PrivateKey MERCHANT_KEY;

    static {
	try (InputStream storeStream = MyResources.optAsStream(XmlDocumentPaymentBulderTest.class, KEYSTORE) //
		.orElseThrow(() -> new RuntimeException("Keystore not found"))) {

	    final KeyStore keystore = MyKeyStores.from(storeStream, STORETYPE, STOREPASS) //
		    .orElseThrow(() -> new RuntimeException("Can not load keystore"));

	    BANK_CER = MyCertificates.from(keystore, BANK_ENTRY_ALIAS) //
		    .orElseThrow(() -> new RuntimeException("Can find key entry"));

	    MERCHANT_CERT = MyCertificates.from(keystore, MERCHANT_ENTRY_ALIAS) //
		    .orElseThrow(() -> new RuntimeException("Can find key entry"));

	    MERCHANT_KEY = MyPrivateKeys.from(keystore, MERCHANT_ENTRY_ALIAS, STOREPASS) //
		    .orElseThrow(() -> new RuntimeException("Can't find key entry"));

	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public static X509Certificate getBankCert() {
	return BANK_CER;
    }

    public static X509Certificate getMerchantCert() {
	return MERCHANT_CERT;
    }

    public static PrivateKey getMerchantKey() {
	return MERCHANT_KEY;
    }
}
