package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Currency;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import com.lapsa.international.country.Country;
import com.lapsa.international.phone.PhoneNumber;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder.XmlMerchant.XmlOrder;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder.XmlMerchant.XmlOrder.XmlDepartment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment.XmlBank;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment.XmlBank.XmlCustomer;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment.XmlBank.XmlCustomerSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment.XmlBank.XmlResults;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment.XmlBank.XmlResults.XmlPayment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment.XmlBankSign;
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

	final XmlDepartment department = new XmlDepartment(Double.valueOf(3100), "90028101", null, null, null, "ASDFG");

	final XmlOrder order = new XmlOrder(Double.valueOf(3100), Currency.getInstance("KZT"), "000282",
		Arrays.asList(department));

	final XmlMerchant sourceMerchant = new XmlMerchant(new BigInteger("7269C18D00010000005E", 16), "Shop Name",
		order);

	final XmlMerchantSign sourceMerchantSign = new XmlMerchantSign(XmlSignType.RSA);

	final XmlCustomer customer = new XmlCustomer("John Cardholder", "klient@mymail.com",
		PhoneNumber.assertValid("223322"), sourceMerchant,
		sourceMerchantSign);

	final XmlCustomerSign customerSign = new XmlCustomerSign(XmlSignType.CERTIFICATE, "4817C411000100000084");

	final XmlPayment payment = new XmlPayment(320.5,
		"90050801",
		"109600746891",
		"730190",
		"00",
		XmlSecureType.NON_SECURED,
		Country.KAZ,
		null,
		"6A2D7673A8EEF25A2C33D67CB5AAD091",
		null);

	// 2006-11-22 12:20:30
	final LocalDateTime ldt = LocalDateTime.of(2006, 11, 22, 12, 20, 30);
	final Instant timestamp = ldt.atZone(ZoneId.of("Asia/Almaty")).toInstant();

	final XmlResults results = new XmlResults(timestamp, Arrays.asList(payment));

	final XmlBank bank = new XmlBank("Kazkommertsbank JSC", customer, customerSign, results);

	final XmlBankSign bankSign = new XmlBankSign(XmlSignType.SHA_RSA,
		new byte[] { 36, -115, -47, 100, -63, 47, 123, 19, 101, 14, 98, -84, 57, 8, 94, -46, -100, -57, -72,
			-88, -93, -99, -72, 111, -100, -69, -67, -10, -88, 123, -121, -30, 110, -16, 123, -46, 124, 99,
			91, 16, -100, 87, 80, -66, 124, 51, -34, 45, 94, -5, -69, -61, 64, 87, 73, -114, -52, 29, -29,
			-58, -85, 61, -10, 38, -29, 11, 2, 119, 46, -49, 35, 48, -85, -50, 57, -106, 41, -42, -2, -63,
			-1, 90, -10, -39, -5, -92, -110, 97, -4, 67, 50, 123, -32, 95, 68, -65, -92, -84, 91, -123, 37,
			-20, -18, 81, -107, 6, 120, 23, 59, 10, -69, 6, 27, 36, -16, 103, 3, 82, 69, -128, 51, -107, 0,
			-80, -86, 68, 42, -126 },
		new BigInteger("c183d690", 16));

	TEST_DOCUMENT_AS_OBJECT = new XmlDocumentPayment(bank, bankSign);

    }

    @Test
    public void testSerializeDocument() throws JAXBException {
	final String documentString = getDocumentString(TEST_DOCUMENT_AS_OBJECT, false);
	System.out.println();
	System.out.println(documentString);
	System.out.println();
	System.out.println(TEST_DOCUMENT_AS_PLAINTEXT);
	assertThat(documentString, allOf(not(nullValue()), is(TEST_DOCUMENT_AS_PLAINTEXT)));
    }

    private String getDocumentString(final XmlDocumentPayment document, final boolean formatted) throws JAXBException {
	return XmlDocumentPayment.getTool().serializeToString(document);
    }
}
