package test.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.junit.BeforeClass;
import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment;
import tech.lapsa.java.commons.resources.Resources;
import tech.lapsa.java.commons.security.MyCertificates;
import tech.lapsa.java.commons.security.MyKeyStores;
import tech.lapsa.java.commons.security.MyKeyStores.StoreType;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.Algorithm;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;

public class XmlDocumentPaymentBulderTest {

    private static final StoreType STORETYPE = StoreType.JKS;
    private static final String KEYSTORE = "/kkb.jks";
    private static final String STOREPASS = "1q2w3e4r";
    private static final String ALIAS = "kkbca-test";
    private static final Algorithm ALGORITHM = Algorithm.SHA1withRSA;

    private static X509Certificate certificate;
    private static VerifyingSignature signature;

    @BeforeClass
    public static void loadKeys() throws Exception {

	InputStream storeStream = Resources.optionalAsStream(XmlDocumentPaymentBulderTest.class, KEYSTORE) //
		.orElseThrow(() -> new RuntimeException("Keystore not found"));

	KeyStore keystore = MyKeyStores.from(storeStream, STORETYPE, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can not load keystore"));

	certificate = MyCertificates.from(keystore, ALIAS) //
		.orElseThrow(() -> new RuntimeException("Can find key entry"));

	signature = MySignatures.forVerification(certificate, ALGORITHM) //
		.orElseThrow(() -> new RuntimeException("Can't process with signing signature"));

    }

    private static final String BANK_XML = ""
	    + "<bank name=\"Kazkommertsbank JSC\">"
	    + "<customer name=\"MR CARD\" mail=\"vadim.isaev@me.com\" phone=\"\">"
	    + "<merchant cert_id=\"c183d70b\" name=\"Test shop 3\">"
	    + "<order order_id=\"484902574738032\" amount=\"2382.05\" currency=\"398\">"
	    + "<department merchant_id=\"92061103\" amount=\"2382.05\"/>"
	    + "</order>"
	    + "</merchant>"
	    + "<merchant_sign type=\"RSA\"/>"
	    + "</customer>"
	    + "<customer_sign type=\"RSA\"/>"
	    + "<results timestamp=\"2016-06-14 15:18:02\">"
	    + "<payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"2382.05\" "
	    + "reference=\"160614151802\" approval_code=\"151802\" response_code=\"00\" Secure=\"No\" "
	    + "card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/>"
	    + "</results>"
	    + "</bank>";

    private static final String PAYMENT_XML = ""
	    + "<document>"
	    + BANK_XML
	    + "<bank_sign cert_id=\"c183d690\" type=\"SHA/RSA\">"
	    + "uHWuUQ938FNwU3ZkEip2/HGSL6niFomLvmk"
	    + "lkg1mWOiGPCzEcQoFc5XFfTYnLwin3qtl3JsnO/yysFAjXF"
	    + "OBYfe5txQIWo5rnCzQ7/97n7jDHUHx58rqTLPzWwb70bYE3DuZch/cvS2gyFBbstUkzik+0YDJ/FuMwmTU4Kl/dBc="
	    + "</bank_sign>"
	    + "</document>";

    @Test
    public void basicTest() {
	XmlDocumentPayment o = XmlDocumentPayment.builder() //
		.ofRawXml(PAYMENT_XML) //
		.checkingWith(certificate, signature) //
		.build();
	assertThat(o, not(nullValue()));
    }

}
