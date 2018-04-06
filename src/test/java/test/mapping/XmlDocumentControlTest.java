package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Currency;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlCommand;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlCommand.XmlType;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlPayment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlReason;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlSignType;
import tech.lapsa.java.commons.io.MyResources;

public class XmlDocumentControlTest {

    private static final String RESOURCE = "/document-control-variant-1.xml";

    private static final String XML = "<document><merchant id=\"92061103\"><command type=\"complete\"/><payment amount=\"340.1\" approval_code=\"00\" currency_code=\"398\" orderid=\"123456789012345\" reference=\"21312321\"/><reason>Так надобно</reason></merchant><merchant_sign cert_id=\"7361647361647361647361\" type=\"RSA\">YWRzYWRzYWRzYWRzYQ==</merchant_sign></document>";

    private static final XmlControlDocument DOCUMENT;

    static {
	final XmlCommand command = new XmlCommand(XmlType.COMPLETE);
	final XmlPayment payment = new XmlPayment("21312321", "00", "123456789012345", 340.1d,
		Currency.getInstance("KZT"));

	final XmlReason reason = new XmlReason("Так надобно");
	final XmlMerchant merchant = new XmlMerchant("92061103", command, payment, reason);

	final XmlMerchantSign merchantSign = new XmlMerchantSign(XmlSignType.RSA, "adsadsadsadsa".getBytes(),
		new BigInteger("sadsadsadsa".getBytes()));
	DOCUMENT = new XmlControlDocument(merchant, merchantSign);
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
	final XmlControlDocument loaded = loadDocument(RESOURCE);
	dumpDocument(loaded, true);
    }

    private void dumpDocument(final XmlControlDocument document, final boolean formatted) throws JAXBException {
	System.out.println(getDocumentString(document, formatted));
    }

    private String getDocumentString(final XmlControlDocument document, final boolean formatted) throws JAXBException {
	return XmlControlDocument.getTool().serializeToString(document);
    }

    private XmlControlDocument loadDocument(final String resourceName) throws JAXBException {
	return XmlControlDocument.getTool().deserializeFrom(MyResources.getAsStream(this.getClass(), resourceName));
    }
}
