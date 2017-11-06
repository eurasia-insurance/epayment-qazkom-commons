package test.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;

import org.junit.BeforeClass;
import org.junit.Test;

import com.lapsa.fin.FinCurrency;

import tech.lapsa.java.commons.resources.Resources;
import tech.lapsa.java.commons.security.MyCertificates;
import tech.lapsa.java.commons.security.MyKeyStores;
import tech.lapsa.java.commons.security.MyKeyStores.StoreType;
import tech.lapsa.java.commons.security.MyPrivateKeys;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.Algorithm;
import tech.lapsa.qazkom.xml.bind.XmlDocumentOrder;

public class XmlDocumentOrderBulderTest {

    private static final StoreType STORETYPE = StoreType.JKS;
    private static final String KEYSTORE = "/kkb.jks";
    private static final String STOREPASS = "1q2w3e4r";
    private static final String ALIAS = "cert";
    private static final Algorithm ALGORITHM = Algorithm.SHA1withRSA;

    private static X509Certificate certificate;
    private static Signature sigForSignature;
    private static Signature sigForVerification;

    @BeforeClass
    public static void loadKeys() throws Exception {

	InputStream storeStream = Resources.optionalAsStream(XmlDocumentOrderBulderTest.class, KEYSTORE) //
		.orElseThrow(() -> new RuntimeException("Keystore not found"));

	KeyStore keystore = MyKeyStores.from(storeStream, STORETYPE, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can not load keystore"));

	PrivateKey key = MyPrivateKeys.from(keystore, ALIAS, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can't find key entry"));

	sigForSignature = MySignatures.forSignature(key, ALGORITHM) //
		.orElseThrow(() -> new RuntimeException("Can't process with signing signature"));

	certificate = MyCertificates.from(keystore, ALIAS) //
		.orElseThrow(() -> new RuntimeException("Can find key entry"));

	sigForVerification = MySignatures.forVerification(certificate, ALGORITHM) //
		.orElseThrow(() -> new RuntimeException("Can't process with signing signature"));

    }

    private static final String RAW_XML = ""
	    + "<document>"
	    + "<merchant cert_id=\"c183d70b\" name=\"Test shop 3\">"
	    + "<order order_id=\"999999999999\" currency=\"398\" amount=\"1000\">"
	    + "<department merchant_id=\"92061103\" amount=\"1000\"/>"
	    + "</order>"
	    + "</merchant>"
	    + "<merchant_sign type=\"RSA\">"
	    + "qX0o4ZfcG8IiYQlb3fA4LVjshJEx5bH66frVlUiZBrpAFaDfBmxDrXhg/A5deQyVtQoa35x3FDAiAzOu8PPRQYAm9e7CStlUXnjqSdBRjEc7IAxsrz4kZQb340wJZft+5ZyBiHsvkkQVVUmWGvDP3/143d9BSCZ8SPsI7+tcuzw="
	    + "</merchant_sign>"
	    + "</document>";

    @Test
    public void simpleBuildTest() {

	XmlDocumentOrder o = XmlDocumentOrder.builder() //
		.withAmount(1000d) //
		.withCurrency(FinCurrency.KZT) //
		.withMerchchant("92061103", "Test shop 3") //
		.withOrderNumber("999999999999") //
		.signWith(sigForSignature, certificate) //
		.build();
	assertThat(o, not(nullValue()));
	String rawXml = o.getRawXml();
	assertThat(rawXml, allOf(not(isEmptyOrNullString()), equalTo(RAW_XML)));
    }

    @Test
    public void simpleDeserializeTest() {
	XmlDocumentOrder o = XmlDocumentOrder.of(RAW_XML);
	assertThat(o, not(nullValue()));
	String rawXml = o.getRawXml();
	assertThat(rawXml, allOf(not(isEmptyOrNullString()), equalTo(RAW_XML)));
    }

    @Test
    public void simpleSignatureVerificationTest() {
	XmlDocumentOrder o = XmlDocumentOrder.of(RAW_XML);
	assertTrue("Signature must be VALID", o.validSignature(sigForVerification));

	o.getMerchantSign().getSignature()[0]++; // break the signature
	assertFalse("Signature must be INVALID", o.validSignature(sigForVerification));
    }
}
