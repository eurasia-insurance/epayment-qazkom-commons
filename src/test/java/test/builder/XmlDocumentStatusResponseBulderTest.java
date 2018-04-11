package test.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.security.cert.X509Certificate;

import org.junit.BeforeClass;
import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentStatusResponse;

public class XmlDocumentStatusResponseBulderTest {

    private static X509Certificate bankCert;

    @BeforeClass
    public static void loadKeys() throws Exception {
	bankCert = TestKeystoreUtils.getBankCert();
    }

    private static final String RESPONSE_XML = ""
	    + "<document>"
	    + "<bank name=\"Kazkommertsbank JSC\">"
	    + "<merchant id=\"92061103\">"
	    + "<order id=\"503390238687304\"/>"
	    + "</merchant>"
	    + "<merchant_sign cert_id=\"c183d70b\" type=\"RSA\">K77XoJlAkAAxDZekLpkG2wKR8kxGaaLSIHI4D18CrlOcka4p48BM3OgZ+lRoIZ3nBxL9qjV618QR0BIPEGdZdjJjxHGFmfakz2TgcONTrjS6PyGjrmSzMMpJ0zwyT02V4iU7sgiOgeDMfkee9LmzSzIZ0yBgNtK+XakjiWuMdXU=</merchant_sign>"
	    + "<response payment=\"true\" status=\"2\" result=\"0\" amount=\"26431\" currencycode=\"398\" timestamp=\"2018-04-06 18:15:30.0\" reference=\"180406181530\" cardhash=\"440564-XX-XXXX-6150\" card_to=\"\" approval_code=\"181530\" msg=\"accept\" secure=\"No\" card_bin=\"null\" payername=\"mr cardholder\" payermail=\"vadim.o.isaev@gmail.com\" payerphone=\"7019377979\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\" recur=\"0\" OrderID=\"503390238687304\" SessionID=\"227A6AE29920008C1EECEF7953163790\" intreference=\"FA20180406181530\" AcceptRejectCode=\"1\"/>"
	    + "</bank>"
	    + "<bank_sign cert_id=\"00c183d690\" type=\"RSA\">0bcBa53e+7HmO0IjPPh3E0GDMo+voYYbX8uWpW2PYk+x1PmTM0GaLJWi2BsrIswo/WnY5nnGmhjV3bVrJqOAwKvTnCyVKkvg6lfPel50aGi9R3ykgOpTVHg3tVE4s7JdEdHrIL0OXiTkKiU89x6d4dFVxOzMQ49KJufZ2HqnPzo=</bank_sign>"
	    + "</document>"
	    + "";

    @Test
    public void basicTest() {
	final XmlDocumentStatusResponse o = XmlDocumentStatusResponse.builder() //
		.ofRawXml(RESPONSE_XML) //
		.withBankCertificate(bankCert) //
		.build();
	assertThat(o, not(nullValue()));
    }

}
