package test.com.lapsa.kkb.xml;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.Test;

import com.lapsa.country.Country;
import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.xml.KKBXmlBank;
import com.lapsa.kkb.xml.KKBXmlBankSign;
import com.lapsa.kkb.xml.KKBXmlCustomer;
import com.lapsa.kkb.xml.KKBXmlCustomerSign;
import com.lapsa.kkb.xml.KKBXmlDepartment;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;
import com.lapsa.kkb.xml.KKBXmlMerchantSign;
import com.lapsa.kkb.xml.KKBXmlOrder;
import com.lapsa.kkb.xml.KKBXmlPayment;
import com.lapsa.kkb.xml.KKBXmlResults;
import com.lapsa.kkb.xml.KKBXmlSecureType;
import com.lapsa.kkb.xml.KKBXmlSignType;

public class KKBDocumentResponseOKTest {

    private JAXBContext jaxbContext;

    private static final KKBXmlDocument TEST_DOCUMENT_AS_OBJECT;

    private static final String TEST_DOCUMENT_AS_PLAINTEXT = ""
	    + "<document>"
	    + "<bank name=\"Kazkommertsbank JSC\">"
	    + "<customer mail=\"klient@mymail.com\" name=\"John Cardholder\" phone=\"223322\">"
	    + "<merchant cert_id=\"7269c18d00010000005e\" name=\"Shop Name\">"
	    + "<order order_id=\"000282\" currency=\"398\" amount=\"3100\">"
	    + "<department RL=\"ASDFG\" merchant_id=\"90028101\" amount=\"3100\"/>"
	    + "</order>"
	    + "</merchant>"
	    + "<merchant_sign type=\"RSA\"/>"
	    + "</customer>"
	    + "<customer_sign type=\"SSL\">"
	    + "4817C411000100000084"
	    + "</customer_sign>"
	    + "<results timestamp=\"2006-11-22 12:20:30\">"
	    + "<payment approval_code=\"730190\" card_bin=\"KAZ\" "
	    + "c_hash=\"6A2D7673A8EEF25A2C33D67CB5AAD091\" merchant_id=\"90050801\" "
	    + "reference=\"109600746891\" response_code=\"00\" Secure=\"No\" amount=\"320.5\"/>"
	    + "</results>"
	    + "</bank>"
	    + "<bank_sign cert_id=\"c183d690\" type=\"SHA/RSA\">"
	    + "JI3RZMEvexNlDmKsOQhe0pzHuKijnbhvnLu99qh7h+Ju8HvSfGNbEJxXUL58"
	    + "M94tXvu7w0BXSY7MHePGqz32JuMLAncuzyMwq845linW/sH/WvbZ+6SSYfxD"
	    + "MnvgX0S/pKxbhSXs7lGVBngXOwq7Bhsk8GcDUkWAM5UAsKpEKoI="
	    + "</bank_sign>"
	    + "</document>";

    static {
	TEST_DOCUMENT_AS_OBJECT = new KKBXmlDocument();

	KKBXmlBank bank = new KKBXmlBank();
	TEST_DOCUMENT_AS_OBJECT.setBank(bank);
	bank.setName("Kazkommertsbank JSC");

	KKBXmlCustomer customer = new KKBXmlCustomer();
	bank.setCustomer(customer);
	customer.setEmailAddress("klient@mymail.com");
	customer.setName("John Cardholder");
	customer.setPhone("223322");

	KKBXmlMerchant sourceMerchant = new KKBXmlMerchant();
	customer.setSourceMerchant(sourceMerchant);
	sourceMerchant.setCertificateSerialNumber(new BigInteger("7269C18D00010000005E", 16));
	sourceMerchant.setName("Shop Name");

	KKBXmlOrder order = new KKBXmlOrder();
	sourceMerchant.setOrder(order);
	order.setOrderId("000282");
	order.setFinCurrency(FinCurrency.KZT);
	order.setAmount(3100);

	KKBXmlDepartment department = new KKBXmlDepartment();
	order.setDepartments(new ArrayList<>());
	order.getDepartments().add(department);
	department.setMerchantId("90028101");
	department.setAmount(3100);
	department.setAirticketBookingNumber("ASDFG");

	KKBXmlMerchantSign sourceMerchantSign = new KKBXmlMerchantSign();
	customer.setSourceMerchantSign(sourceMerchantSign);
	sourceMerchantSign.setSignType(KKBXmlSignType.RSA);

	KKBXmlCustomerSign customerSign = new KKBXmlCustomerSign();
	bank.setCustomerSign(customerSign);
	customerSign.setSignType(KKBXmlSignType.CERTIFICATE);
	customerSign.setSignatureEncoded("4817C411000100000084");

	KKBXmlResults results = new KKBXmlResults();
	bank.setResults(results);
	LocalDateTime ldt = LocalDateTime.of(2006, 11, 22, 12, 20, 30);
	Date timestamp = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	results.setTimestamp(timestamp);

	KKBXmlPayment payment = new KKBXmlPayment();
	results.setPayments(new ArrayList<>());
	results.getPayments().add(payment);
	payment.setAmount(320.5);
	payment.setApprovalCode("730190");
	payment.setCardCountry(Country.KAZ);
	payment.setCardHash("6A2D7673A8EEF25A2C33D67CB5AAD091");
	payment.setMerchantId("90050801");
	payment.setReference("109600746891");
	payment.setResponseCode("00");
	payment.setSecureType(KKBXmlSecureType.NON_SECURED);

	KKBXmlBankSign bankSign = new KKBXmlBankSign();
	TEST_DOCUMENT_AS_OBJECT.setBankSign(bankSign);
	bankSign.setCertificateSerialNumber(new BigInteger("c183d690", 16));
	bankSign.setSignType(KKBXmlSignType.SHA_RSA);
	bankSign.setSignature(new byte[] { 36, -115, -47, 100, -63, 47, 123, 19, 101, 14, 98, -84, 57, 8, 94, -46, -100,
		-57, -72, -88, -93, -99, -72, 111, -100, -69, -67, -10, -88, 123, -121, -30, 110, -16, 123, -46, 124,
		99, 91, 16, -100, 87, 80, -66, 124, 51, -34, 45, 94, -5, -69, -61, 64, 87, 73, -114, -52, 29, -29, -58,
		-85, 61, -10, 38, -29, 11, 2, 119, 46, -49, 35, 48, -85, -50, 57, -106, 41, -42, -2, -63, -1, 90, -10,
		-39, -5, -92, -110, 97, -4, 67, 50, 123, -32, 95, 68, -65, -92, -84, 91, -123, 37, -20, -18, 81, -107,
		6, 120, 23, 59, 10, -69, 6, 27, 36, -16, 103, 3, 82, 69, -128, 51, -107, 0, -80, -86, 68, 42, -126 });
    }

    @Before
    public void init() throws JAXBException {
	jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlBank.class, KKBXmlDocument.class);
    }

    @Test
    public void testSerializeDocument() throws JAXBException {
	String documentString = getDocumentString(TEST_DOCUMENT_AS_OBJECT, false);
	System.out.println(documentString);
	assertThat(documentString, allOf(not(nullValue()), is(TEST_DOCUMENT_AS_PLAINTEXT)));
    }

    private String getDocumentString(KKBXmlDocument document, boolean formatted) throws JAXBException {
	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
	jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	StringWriter sw = new StringWriter();
	jaxbMarshaller.marshal(document, sw);
	return sw.toString();
    }
}
