package test.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Currency;

import org.junit.BeforeClass;
import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder;

public class XmlDocumentOrderBulderTest {

    private static X509Certificate certificate;
    private static PrivateKey key;

    @BeforeClass
    public static void loadKeys() throws Exception {
	key = TestKeystoreUtils.getMerchantKey();
	certificate = TestKeystoreUtils.getMerchantCert();
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
    public void basicTest() {
	final XmlDocumentOrder o = XmlDocumentOrder.builder() //
		.withAmount(1000d) //
		.withCurrency(Currency.getInstance("KZT")) //
		.withMerchchant("92061103", "Test shop 3") //
		.withOrderNumber("999999999999") //
		.signWith(key, certificate) //
		.build();
	assertThat(o, not(nullValue()));
	final String rawXml = o.getRawXml();
	assertThat(rawXml, allOf(not(isEmptyOrNullString()), equalTo(RAW_XML)));
    }

    @Test
    public void deserializeTest() {
	final XmlDocumentOrder o = XmlDocumentOrder.of(RAW_XML);
	assertThat(o, not(nullValue()));
	final String rawXml = o.getRawXml();
	assertThat(rawXml, allOf(not(isEmptyOrNullString()), equalTo(RAW_XML)));
    }

    @Test
    public void signatureVerificationTest() {
	final XmlDocumentOrder o = XmlDocumentOrder.of(RAW_XML);
	assertTrue("Signature must be VALID", o.validSignature(certificate));

	o.getMerchantSign().getSignature()[0]++; // break the signature
						 // (actually breaking will
						 // failed cause getSignature()
						 // returns a copy)
	assertTrue("Signature must be VALID", o.validSignature(certificate));
    }
}
