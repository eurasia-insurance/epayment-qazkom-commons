package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Base64;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlSignType;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentStatusRequest;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentStatusRequest.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentStatusRequest.XmlMerchant.XmlOrder;
import tech.lapsa.epayment.qazkom.xml.bind.XmlSignGeneralWithCert;
import tech.lapsa.java.commons.io.MyResources;

public class XmlDocumentStatusRequestTest {

    private static final String RESOURCE = "/document-status-request-variant-1.xml";

    private static final String XML = "<document><merchant id=\"92061103\"><order id=\"484902574738032\"/></merchant><merchant_sign cert_id=\"c183d70b\" type=\"RSA\">MD/ci+9LW8MrMP5o1uSbX+rgDxKcX4TZSuF065i1JbSZMyW6IW5LwwytunW/NaA//DXjnnfYuB1wfJarI4vIpEQzX4Eh1Ld/nWpQ/RjVeSXJT9qlJY9ka/Tky1Kej/6i17U4ognYC5QQzf3wwkXMFBM0Nhz0kPeW50sorgX1bDI=</merchant_sign></document>";

    private static final XmlDocumentStatusRequest DOCUMENT;

    static {
	final XmlOrder order = new XmlOrder("484902574738032");
	final XmlMerchant merchant = new XmlMerchant("92061103", order);

	final XmlSignGeneralWithCert merchantSign = new XmlSignGeneralWithCert(XmlSignType.RSA,
		Base64.getDecoder().decode(
			"MD/ci+9LW8MrMP5o1uSbX+rgDxKcX4TZSuF065i1JbSZMyW6IW5LwwytunW/NaA//DXjnnfYuB1wfJarI4vIpEQzX4Eh1Ld/nWpQ/RjVeSXJT9qlJY9ka/Tky1Kej/6i17U4ognYC5QQzf3wwkXMFBM0Nhz0kPeW50sorgX1bDI="),
		new BigInteger("c183d70b", 16));
	DOCUMENT = new XmlDocumentStatusRequest(merchant, merchantSign);
    }

    @Test
    public void testSerializeDocument() throws JAXBException {
	System.out.println();
	System.out.println("Generated document");
	final String documentString = getDocumentString(DOCUMENT, false);
	System.out.println(documentString);
	System.out.println();
	assertThat(documentString, allOf(not(nullValue()), is(XML)));
    }

    @Test
    public void testLoadDocument() throws JAXBException {
	System.out.println();
	System.out.println("Loaded document");
	final XmlDocumentStatusRequest loaded = loadDocument(RESOURCE);
	dumpDocument(loaded, true);
    }

    private void dumpDocument(final XmlDocumentStatusRequest document, final boolean formatted) throws JAXBException {
	System.out.println(getDocumentString(document, formatted));
    }

    private String getDocumentString(final XmlDocumentStatusRequest document, final boolean formatted)
	    throws JAXBException {
	return XmlDocumentStatusRequest.getTool().serializeToString(document);
    }

    private XmlDocumentStatusRequest loadDocument(final String resourceName) throws JAXBException {
	return XmlDocumentStatusRequest.getTool()
		.deserializeFrom(MyResources.getAsStream(this.getClass(), resourceName));
    }
}
