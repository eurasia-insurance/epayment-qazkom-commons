package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.lapsa.fin.FinCurrency;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDepartment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder;
import tech.lapsa.epayment.qazkom.xml.bind.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlOrder;
import tech.lapsa.epayment.qazkom.xml.bind.XmlSignType;
import tech.lapsa.java.commons.resources.Resources;

public class XmlDocumentOrderTest {

    private static final String EXAMPLE_DOCUMENT_AUTH_XML = "/document-request-variant-1.xml";

    private static final String TEST_DOCUMENT_AS_PLAINTEXT = "<document><merchant cert_id=\"c183d70b\" name=\"Shop Name\"><order order_id=\"000282\" currency=\"398\" amount=\"3100\"><department RL=\"ASDFG\" merchant_id=\"92061101\" phone=\"22233355\" amount=\"1300\"/></order></merchant><merchant_sign type=\"RSA\">p25i1rUH7StnhOfnkHSOHguuPMePaGXtiPGEOrJE4bof1gFVH19mhDyHjfWa6OeJ80fidyvVf1X4ewyP0yG4GxJSl0VyXz7+PNLsbs1lJe42d1fixvozhJSSYN6fAxMN8hhDht6S81YK3GbDTE7GH498pU9HGuGAoDVjB+NtrHk=</merchant_sign></document>";

    private static final XmlDocumentOrder TEST_DOCUMENT_AS_OBJECT;

    static {
	TEST_DOCUMENT_AS_OBJECT = new XmlDocumentOrder();

	XmlMerchant merchant = new XmlMerchant();
	TEST_DOCUMENT_AS_OBJECT.setMerchant(merchant);
	merchant.setCertificateSerialNumber(new BigInteger("00c183d70b", 16));
	merchant.setName("Shop Name");

	XmlOrder order = new XmlOrder();
	merchant.setOrder(order);
	order.setOrderId("000282");
	order.setAmount(Double.valueOf(3100));
	order.setFinCurrency(FinCurrency.KZT);

	XmlDepartment department = new XmlDepartment();
	order.setDepartments(new ArrayList<>());
	order.getDepartments().add(department);
	department.setMerchantId("92061101");
	department.setAmount(Double.valueOf(1300));
	department.setPhone("22233355");
	department.setAirticketBookingNumber("ASDFG");

	XmlMerchantSign sign = new XmlMerchantSign();
	TEST_DOCUMENT_AS_OBJECT.setMerchantSign(sign);
	sign.setSignType(XmlSignType.RSA);
	sign.setSignature(new byte[] { -89, 110, 98, -42, -75, 7, -19, 43, 103, -124, -25, -25, -112, 116, -114, 30, 11,
		-82, 60, -57, -113, 104, 101, -19, -120, -15, -124, 58, -78, 68, -31, -70, 31, -42, 1, 85, 31, 95, 102,
		-124, 60, -121, -115, -11, -102, -24, -25, -119, -13, 71, -30, 119, 43, -43, 127, 85, -8, 123, 12, -113,
		-45, 33, -72, 27, 18, 82, -105, 69, 114, 95, 62, -2, 60, -46, -20, 110, -51, 101, 37, -18, 54, 119, 87,
		-30, -58, -6, 51, -124, -108, -110, 96, -34, -97, 3, 19, 13, -14, 24, 67, -122, -34, -110, -13, 86, 10,
		-36, 102, -61, 76, 78, -58, 31, -113, 124, -91, 79, 71, 26, -31, -128, -96, 53, 99, 7, -29, 109, -84,
		121 });

    }

    @Test
    public void testSerializeDocument() throws JAXBException {
	System.out.println();
	System.out.println("Generated document");
	String documentString = getDocumentString(TEST_DOCUMENT_AS_OBJECT, false);
	System.out.println(documentString);
	System.out.println();
	assertThat(documentString, allOf(not(nullValue()), is(TEST_DOCUMENT_AS_PLAINTEXT)));
    }

    @Test
    public void testLoadDocument() throws JAXBException {
	System.out.println();
	System.out.println("Loaded document");
	XmlDocumentOrder loaded = loadDocument(EXAMPLE_DOCUMENT_AUTH_XML);
	dumpDocument(loaded, true);
    }

    private void dumpDocument(final XmlDocumentOrder document, final boolean formatted) throws JAXBException {
	System.out.println(getDocumentString(document, formatted));
    }

    private String getDocumentString(final XmlDocumentOrder document, final boolean formatted) throws JAXBException {
	return XmlDocumentOrder.getTool().serializeToString(document);
    }

    private XmlDocumentOrder loadDocument(final String resourceName) throws JAXBException {
	return XmlDocumentOrder.getTool().deserializeFrom(Resources.getAsStream(this.getClass(), resourceName));
    }
}
