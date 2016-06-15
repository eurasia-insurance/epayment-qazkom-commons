package test.com.lapsa.kkb.xml;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.xml.KKBXmlDepartment;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;
import com.lapsa.kkb.xml.KKBXmlMerchantSign;
import com.lapsa.kkb.xml.KKBXmlOrder;
import com.lapsa.kkb.xml.KKBXmlSignType;

public class KKBDocumentReuestTest {

    private static final String EXAMPLE_DOCUMENT_AUTH_XML = "/example-document-request.xml";

    private static final String TEST_DOCUMENT_AS_PLAINTEXT = ""
	    + "<document>"
	    + "<merchant cert_id=\"c183d70b\" name=\"Shop Name\">"
	    + "<order order_id=\"000282\" currency=\"398\" amount=\"3100\">"
	    + "<department RL=\"ASDFG\" merchant_id=\"92061101\" phone=\"22233355\" amount=\"1300\"/>"
	    + "</order>"
	    + "</merchant>"
	    + "<merchant_sign type=\"RSA\">"
	    + "p25i1rUH7StnhOfnkHSOHguuPMePaGXtiPGEOrJE4bof1gFVH19mhDyHj"
	    + "fWa6OeJ80fidyvVf1X4ewyP0yG4GxJSl0VyXz7+PNLsbs1lJe42d1fixv"
	    + "ozhJSSYN6fAxMN8hhDht6S81YK3GbDTE7GH498pU9HGuGAoDVjB+NtrHk="
	    + "</merchant_sign>"
	    + "</document>";

    private static final KKBXmlDocument TEST_DOCUMENT_AS_OBJECT;

    static {
	TEST_DOCUMENT_AS_OBJECT = new KKBXmlDocument();

	KKBXmlMerchant merchant = new KKBXmlMerchant();
	TEST_DOCUMENT_AS_OBJECT.setMerchant(merchant);
	merchant.setCertificateSerialNumber(new BigInteger("00c183d70b", 16));
	merchant.setName("Shop Name");

	KKBXmlOrder order = new KKBXmlOrder();
	merchant.setOrder(order);
	order.setOrderId("000282");
	order.setAmount(3100);
	order.setFinCurrency(FinCurrency.KZT);

	KKBXmlDepartment department = new KKBXmlDepartment();
	order.setDepartments(new ArrayList<>());
	order.getDepartments().add(department);
	department.setMerchantId("92061101");
	department.setAmount(1300);
	department.setPhone("22233355");
	department.setAirticketBookingNumber("ASDFG");

	KKBXmlMerchantSign sign = new KKBXmlMerchantSign();
	TEST_DOCUMENT_AS_OBJECT.setMerchantSign(sign);
	sign.setSignType(KKBXmlSignType.RSA);
	sign.setSignature(new byte[] { -89, 110, 98, -42, -75, 7, -19, 43, 103, -124, -25, -25, -112, 116, -114, 30, 11,
		-82, 60, -57, -113, 104, 101, -19, -120, -15, -124, 58, -78, 68, -31, -70, 31, -42, 1, 85, 31, 95, 102,
		-124, 60, -121, -115, -11, -102, -24, -25, -119, -13, 71, -30, 119, 43, -43, 127, 85, -8, 123, 12, -113,
		-45, 33, -72, 27, 18, 82, -105, 69, 114, 95, 62, -2, 60, -46, -20, 110, -51, 101, 37, -18, 54, 119, 87,
		-30, -58, -6, 51, -124, -108, -110, 96, -34, -97, 3, 19, 13, -14, 24, 67, -122, -34, -110, -13, 86, 10,
		-36, 102, -61, 76, 78, -58, 31, -113, 124, -91, 79, 71, 26, -31, -128, -96, 53, 99, 7, -29, 109, -84,
		121 });

    }

    private JAXBContext jaxbContext;

    @Before
    public void init() throws JAXBException {
	jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
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
	KKBXmlDocument loaded = loadDocument(EXAMPLE_DOCUMENT_AUTH_XML);
	dumpDocument(loaded, true);
    }

    @Test
    public void testGetMerchantString() throws JAXBException {
	System.out.println();
	System.out.println("Merchant string");
	String merchantString = getMerchantString(TEST_DOCUMENT_AS_OBJECT);
	System.out.println(merchantString);
    }

    private String getMerchantString(KKBXmlDocument document) throws JAXBException {
	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	StringWriter sw = new StringWriter();
	jaxbMarshaller.marshal(document.getMerchant(), sw);
	return sw.toString();
    }

    private void dumpDocument(KKBXmlDocument document, boolean formatted) throws JAXBException {
	System.out.println(getDocumentString(document, formatted));
    }

    private String getDocumentString(KKBXmlDocument document, boolean formatted) throws JAXBException {
	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
	jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	StringWriter sw = new StringWriter();
	jaxbMarshaller.marshal(document, sw);
	return sw.toString();
    }

    private KKBXmlDocument loadDocument(String resourceName) throws JAXBException {
	File resourceFile = new File(KKBXmlDocument.class.getResource(resourceName).getFile());
	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	KKBXmlDocument document = (KKBXmlDocument) jaxbUnmarshaller.unmarshal(resourceFile);
	return document;
    }

    @SuppressWarnings("unused")
    private KKBXmlDocument loadDocumentFromString(String documentString) throws JAXBException {
	Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	StringReader sr = new StringReader(TEST_DOCUMENT_AS_PLAINTEXT);
	KKBXmlDocument document = (KKBXmlDocument) jaxbUnmarshaller.unmarshal(sr);
	return document;
    }

}
