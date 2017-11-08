package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.lapsa.fin.FinCurrency;
import com.lapsa.international.country.Country;

import tech.lapsa.epayment.qazkom.xml.bind.XmlBank;
import tech.lapsa.epayment.qazkom.xml.bind.XmlBankSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlCustomer;
import tech.lapsa.epayment.qazkom.xml.bind.XmlCustomerSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDepartment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlOrder;
import tech.lapsa.epayment.qazkom.xml.bind.XmlPayment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlResults;
import tech.lapsa.epayment.qazkom.xml.bind.XmlSecureType;
import tech.lapsa.epayment.qazkom.xml.bind.XmlSignType;

public class XmlDocumentPaymentTest {

    private static final XmlDocumentPayment TEST_DOCUMENT_AS_OBJECT;

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
	    + "<customer_sign type=\"SSL\">4817C411000100000084</customer_sign>"
	    + "<results timestamp=\"2006-11-22 12:20:30\">"
	    + "<payment approval_code=\"730190\" card_bin=\"KAZ\" c_hash=\"6A2D7673A8EEF25A2C33D67CB5AAD091\" merchant_id=\"90050801\" reference=\"109600746891\" response_code=\"00\" Secure=\"No\" amount=\"320.5\"/>"
	    + "</results>"
	    + "</bank>"
	    + "<bank_sign cert_id=\"c183d690\" type=\"SHA/RSA\">JI3RZMEvexNlDmKsOQhe0pzHuKijnbhvnLu99qh7h+Ju8HvSfGNbEJxXUL58M94tXvu7w0BXSY7MHePGqz32JuMLAncuzyMwq845linW/sH/WvbZ+6SSYfxDMnvgX0S/pKxbhSXs7lGVBngXOwq7Bhsk8GcDUkWAM5UAsKpEKoI=</bank_sign>"
	    + "</document>";

    static {
	TEST_DOCUMENT_AS_OBJECT = new XmlDocumentPayment();

	XmlBank bank = new XmlBank();
	TEST_DOCUMENT_AS_OBJECT.setBank(bank);
	bank.setName("Kazkommertsbank JSC");

	XmlCustomer customer = new XmlCustomer();
	bank.setCustomer(customer);
	customer.setEmailAddress("klient@mymail.com");
	customer.setName("John Cardholder");
	customer.setPhone("223322");

	XmlMerchant sourceMerchant = new XmlMerchant();
	customer.setSourceMerchant(sourceMerchant);
	sourceMerchant.setCertificateSerialNumber(new BigInteger("7269C18D00010000005E", 16));
	sourceMerchant.setName("Shop Name");

	XmlOrder order = new XmlOrder();
	sourceMerchant.setOrder(order);
	order.setOrderId("000282");
	order.setFinCurrency(FinCurrency.KZT);
	order.setAmount(Double.valueOf(3100));

	XmlDepartment department = new XmlDepartment();
	order.setDepartments(new ArrayList<>());
	order.getDepartments().add(department);
	department.setMerchantId("90028101");
	department.setAmount(Double.valueOf(3100));
	department.setAirticketBookingNumber("ASDFG");

	XmlMerchantSign sourceMerchantSign = new XmlMerchantSign();
	customer.setSourceMerchantSign(sourceMerchantSign);
	sourceMerchantSign.setSignType(XmlSignType.RSA);

	XmlCustomerSign customerSign = new XmlCustomerSign();
	bank.setCustomerSign(customerSign);
	customerSign.setSignType(XmlSignType.CERTIFICATE);
	customerSign.setSignatureEncoded("4817C411000100000084");

	XmlResults results = new XmlResults();
	bank.setResults(results);

	// 2006-11-22 12:20:30
	LocalDateTime ldt = LocalDateTime.of(2006, 11, 22, 12, 20, 30);
	Instant timestamp = ldt.atZone(ZoneId.of("Asia/Almaty")).toInstant();
	results.setTimestamp(timestamp);

	XmlPayment payment = new XmlPayment();
	results.setPayments(new ArrayList<>());
	results.getPayments().add(payment);
	payment.setAmount(320.5);
	payment.setApprovalCode("730190");
	payment.setCardCountry(Country.KAZ);
	payment.setCardHash("6A2D7673A8EEF25A2C33D67CB5AAD091");
	payment.setMerchantId("90050801");
	payment.setReference("109600746891");
	payment.setResponseCode("00");
	payment.setSecureType(XmlSecureType.NON_SECURED);

	XmlBankSign bankSign = new XmlBankSign();
	TEST_DOCUMENT_AS_OBJECT.setBankSign(bankSign);
	bankSign.setCertificateSerialNumber(new BigInteger("c183d690", 16));
	bankSign.setSignType(XmlSignType.SHA_RSA);
	bankSign.setSignature(new byte[] { 36, -115, -47, 100, -63, 47, 123, 19, 101, 14, 98, -84, 57, 8, 94, -46, -100,
		-57, -72, -88, -93, -99, -72, 111, -100, -69, -67, -10, -88, 123, -121, -30, 110, -16, 123, -46, 124,
		99, 91, 16, -100, 87, 80, -66, 124, 51, -34, 45, 94, -5, -69, -61, 64, 87, 73, -114, -52, 29, -29, -58,
		-85, 61, -10, 38, -29, 11, 2, 119, 46, -49, 35, 48, -85, -50, 57, -106, 41, -42, -2, -63, -1, 90, -10,
		-39, -5, -92, -110, 97, -4, 67, 50, 123, -32, 95, 68, -65, -92, -84, 91, -123, 37, -20, -18, 81, -107,
		6, 120, 23, 59, 10, -69, 6, 27, 36, -16, 103, 3, 82, 69, -128, 51, -107, 0, -80, -86, 68, 42, -126 });
    }

    @Test
    public void testSerializeDocument() throws JAXBException {
	String documentString = getDocumentString(TEST_DOCUMENT_AS_OBJECT, false);
	System.out.println(documentString);
	assertThat(documentString, allOf(not(nullValue()), is(TEST_DOCUMENT_AS_PLAINTEXT)));
    }

    private String getDocumentString(final XmlDocumentPayment document, final boolean formatted) throws JAXBException {
	return XmlDocumentPayment.getTool().serializeToString(document);
    }
}
