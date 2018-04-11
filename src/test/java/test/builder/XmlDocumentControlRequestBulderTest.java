package test.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Currency;

import org.junit.BeforeClass;
import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentControlRequest;

public class XmlDocumentControlRequestBulderTest {

    private static X509Certificate certificate;
    private static PrivateKey key;

    @BeforeClass
    public static void loadKeys() throws Exception {
	key = TestKeystoreUtils.getMerchantKey();
	certificate = TestKeystoreUtils.getMerchantCert();
    }

    private static final String RAW_XML = ""
	    + "<document>"
	    + "<merchant id=\"92061103\">"
	    + "<command type=\"reverse\"/>"
	    + "<payment amount=\"1000\" approval_code=\"151802\" currency_code=\"398\" orderid=\"484902574738032\" reference=\"160614151802\"/>"
	    + "<reason>Неверная сумма</reason>"
	    + "</merchant>"
	    + "<merchant_sign cert_id=\"c183d70b\" type=\"RSA\">"
	    + "8uqRUt4dgB1VVGoxhylnafkn6FenR/kVwUf1Ek4/uC3GGQ/SAkRPfOUruFi55f+pGulV0t/aGFVTGt9xWtTccGM5yffl7pZG2Ox+KAoClsHmJwRvmubcvavsrtcmQKLqEfx2JEIl6tSdABYXaEyS3P+XhvDTBW2yPn75OGb4pmQ="
	    + "</merchant_sign>"
	    + "</document>";

    @Test
    public void basicTest() {
	final XmlDocumentControlRequest o = XmlDocumentControlRequest.builder() //
		.withPaymentReference("160614151802") //
		.withApprovalCode("151802") //
		.withOrderNumber("484902574738032") //
		.withAmount(1000d) //
		.withCurrency(Currency.getInstance("KZT")) //
		.withMerchantId("92061103") //
		.signWith(key, certificate) //
		.prepareCancel("Неверная сумма") //
		.build();

	assertThat(o, not(nullValue()));
	final String rawXml = o.getRawXml();
	System.out.println(rawXml);
	assertThat(rawXml, allOf(not(isEmptyOrNullString()),
		equalTo(RAW_XML)));
    }

    @Test
    public void deserializeTest() {
	final XmlDocumentControlRequest o = XmlDocumentControlRequest.of(RAW_XML);
	assertThat(o, not(nullValue()));
	final String rawXml = o.getRawXml();
	assertThat(rawXml, allOf(not(isEmptyOrNullString()), equalTo(RAW_XML)));
    }

    @Test
    public void signatureVerificationTest() {
	final XmlDocumentControlRequest o = XmlDocumentControlRequest.of(RAW_XML);
	assertTrue("Signature must be VALID", o.validSignature(certificate));

	o.requreValidSignature(certificate);

	o.getMerchantSign().getSignature()[0]++; // break the signature
						 // (actually breaking will
						 // failed cause getSignature()
						 // returns a copy)
	assertTrue("Signature must be VALID", o.validSignature(certificate));
    }
}
