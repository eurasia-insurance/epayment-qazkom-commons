package test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment.XmlBank;

public class XmlBankParserTest {

    @SuppressWarnings("unchecked")
    @Test
    public void simpleTest() {
	final String BANK_XML = ""
		+ "<bank name=\"Kazkommertsbank JSC\">"
		+ "<customer mail=\"klient@mymail.com\" name=\"John Cardholder\" phone=\"223322\">"
		+ "<merchant cert_id=\"7269c18d00010000005e\" name=\"Shop Name\">"
		+ "<order order_id=\"000282\" currency=\"398\" amount=\"3100\">"
		+ "<department RL=\"ASDFG\" merchant_id=\"90028101\" amount=\"3100\"/>"
		+ "</order>"
		+ "</merchant>"
		+ "<merchant_sign type=\"RSA\"/>"
		+ "</customer><customer_sign type=\"SSL\">4817C411000100000084</customer_sign>"
		+ "<results timestamp=\"2006-11-22 12:20:30\">"
		+ "<payment approval_code=\"730190\" card_bin=\"KAZ\" c_hash=\"6A2D7673A8EEF25A2C33D67CB5AAD091\" merchant_id=\"90050801\" reference=\"109600746891\" response_code=\"00\" Secure=\"No\" amount=\"320.5\"/>"
		+ "</results>"
		+ "</bank>";

	final String PAYMENT_XML = ""
		+ "<document>"
		+ BANK_XML
		+ "<bank_sign cert_id=\"c183d690\" type=\"SHA/RSA\">JI3RZMEvexNlDmKsOQhe0pzHuKijnbhvnLu99qh7h+Ju8HvSfGNbEJxXUL58M94tXvu7w0BXSY7MHePGqz32JuMLAncuzyMwq845linW/sH/WvbZ+6SSYfxDMnvgX0S/pKxbhSXs7lGVBngXOwq7Bhsk8GcDUkWAM5UAsKpEKoI=</bank_sign>"
		+ "</document>";

	final String[] banks = XmlBank.bankXmlElementsFrom(PAYMENT_XML);
	assertThat(banks, allOf(not(nullValue()), array(equalTo(BANK_XML))));
    }
}
