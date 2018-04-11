package test.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.junit.BeforeClass;
import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentStatusRequest;
import tech.lapsa.java.commons.io.MyResources;
import tech.lapsa.java.commons.security.MyCertificates;
import tech.lapsa.java.commons.security.MyKeyStores;
import tech.lapsa.java.commons.security.MyKeyStores.StoreType;
import tech.lapsa.java.commons.security.MyPrivateKeys;

public class XmlDocumentStatusRequestBulderTest {

    private static final StoreType STORETYPE = StoreType.JKS;
    private static final String KEYSTORE = "/kkb.jks";
    private static final String STOREPASS = "1q2w3e4r";
    private static final String ALIAS = "cert";

    private static X509Certificate certificate;
    private static PrivateKey key;

    @BeforeClass
    public static void loadKeys() throws Exception {

	final InputStream storeStream = MyResources.optAsStream(XmlDocumentStatusRequestBulderTest.class, KEYSTORE) //
		.orElseThrow(() -> new RuntimeException("Keystore not found"));

	final KeyStore keystore = MyKeyStores.from(storeStream, STORETYPE, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can not load keystore"));

	key = MyPrivateKeys.from(keystore, ALIAS, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can't find key entry"));

	certificate = MyCertificates.from(keystore, ALIAS) //
		.orElseThrow(() -> new RuntimeException("Can find key entry"));
    }

    private static final String RAW_XML = ""
	    + "<document>"
	    + "<merchant id=\"92061103\">"
	    + "<order id=\"484902574738032\"/>"
	    + "</merchant>"
	    + "<merchant_sign cert_id=\"c183d70b\" type=\"RSA\">"
	    + "MD/ci+9LW8MrMP5o1uSbX+rgDxKcX4TZSuF065i1JbSZMyW6IW5LwwytunW/NaA//DXjnnfYuB1wfJarI4vIpEQzX4Eh1Ld/nWpQ/RjVeSXJT9qlJY9ka/Tky1Kej/6i17U4ognYC5QQzf3wwkXMFBM0Nhz0kPeW50sorgX1bDI="
	    + "</merchant_sign>"
	    + "</document>"
	    + "";

    @Test
    public void basicTest() {
	final XmlDocumentStatusRequest o = XmlDocumentStatusRequest.builder() //
		.withMerchantId("92061103") //
		.withOrderNumber("484902574738032") //
		.signWith(key, certificate) //
		.build();

	assertThat(o, not(nullValue()));
	final String rawXml = o.getRawXml();
	System.out.println(rawXml);
	assertThat(rawXml, allOf(not(isEmptyOrNullString()),
		equalTo(RAW_XML)));
    }

    @Test
    public void deserializeTest() {
	final XmlDocumentStatusRequest o = XmlDocumentStatusRequest.of(RAW_XML);
	assertThat(o, not(nullValue()));
	final String rawXml = o.getRawXml();
	assertThat(rawXml, allOf(not(isEmptyOrNullString()), equalTo(RAW_XML)));
    }

    @Test
    public void signatureVerificationTest() {
	final XmlDocumentStatusRequest o = XmlDocumentStatusRequest.of(RAW_XML);
	assertTrue("Signature must be VALID", o.validSignature(certificate));

	o.requreValidSignature(certificate);

	o.getMerchantSign().getSignature()[0]++; // break the signature
						 // (actually breaking will
						 // failed cause getSignature()
						 // returns a copy)
	assertTrue("Signature must be VALID", o.validSignature(certificate));
    }
}
