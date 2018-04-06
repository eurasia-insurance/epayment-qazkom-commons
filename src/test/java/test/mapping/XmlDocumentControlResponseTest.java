package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Base64;
import java.util.Currency;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlCommand;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlCommand.XmlType;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlPayment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlReason;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlResonseDocument;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlResonseDocument.XmlBank;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlResonseDocument.XmlBank.XmlResponse;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlResonseDocument.XmlBankSign;
import tech.lapsa.epayment.qazkom.xml.bind.XmlSignType;
import tech.lapsa.java.commons.io.MyResources;

public class XmlDocumentControlResponseTest {

    private static final String RESOURCE = "/document-control-response-variant-1.xml";

    private static final String XML = "<document><bank name=\"Kazkommertsbank JSC\"><merchant id=\"92061103\"><command type=\"reverse\"/><payment amount=\"1000\" approval_code=\"151802\" currency_code=\"398\" orderid=\"484902574738032\" reference=\"160614151802\"/><reason>Неверная сумма</reason></merchant><merchant_sign cert_id=\"c183d70b\" type=\"RSA\">8uqRUt4dgB1VVGoxhylnafkn6FenR/kVwUf1Ek4/uC3GGQ/SAkRPfOUruFi55f+pGulV0t/aGFVTGt9xWtTccGM5yffl7pZG2Ox+KAoClsHmJwRvmubcvavsrtcmQKLqEfx2JEIl6tSdABYXaEyS3P+XhvDTBW2yPn75OGb4pmQ=</merchant_sign><response code=\"00\" message=\"Approved\" remaining_amount=\"50.0\"/></bank><bank_sign cert_id=\"c183d70b\" type=\"RSA\">p25i1rUH7StnhOfnkHSOHguuPMePaGXtiPGEOrJE4bof1gFVH19mhDyHjfWa6OeJ80fidyvVf1X4ewyP0yG4GxJSl0VyXz7+PNLsbs1lJe42d1fixvozhJSSYN6fAxMN8hhDht6S81YK3GbDTE7GH498pU9HGuGAoDVjB+NtrHk=</bank_sign></document>";

    private static final XmlControlResonseDocument DOCUMENT;

    static {
	final XmlPayment payment = new XmlPayment("160614151802", "151802", "484902574738032", 1000d,
		Currency.getInstance("KZT"));
	final XmlCommand command = new XmlCommand(XmlType.REVERSE);

	final XmlReason reason = new XmlReason("Неверная сумма");
	final XmlMerchant merchant = new XmlMerchant("92061103", command, payment, reason);

	final XmlMerchantSign merchantSign = new XmlMerchantSign(XmlSignType.RSA,
		Base64.getDecoder().decode(
			"8uqRUt4dgB1VVGoxhylnafkn6FenR/kVwUf1Ek4/uC3GGQ/SAkRPfOUruFi55f+pGulV0t/aGFVTGt9xWtTccGM5yffl7pZG2Ox+KAoClsHmJwRvmubcvavsrtcmQKLqEfx2JEIl6tSdABYXaEyS3P+XhvDTBW2yPn75OGb4pmQ="),
		new BigInteger("c183d70b", 16));
	final XmlResponse response = new XmlResponse("00", "Approved", 50d);

	final XmlBank bank = new XmlBank("Kazkommertsbank JSC", merchant, merchantSign, response);
	final XmlBankSign bankSign = new XmlBankSign(XmlSignType.RSA,
		Base64.getDecoder()
			.decode("p25i1rUH7StnhOfnkHSOHguuPMePaGXtiPGEOrJE4bof1gFVH19mhDyHjfWa6OeJ80fidyvVf1X4" +
				"ewyP0yG4GxJSl0VyXz7+PNLsbs1lJe42d1fixvozhJSSYN6fAxMN8hhDht6S81YK3GbDTE7GH498" +
				"pU9HGuGAoDVjB+NtrHk="),
		new BigInteger("c183d70b", 16));

	DOCUMENT = new XmlControlResonseDocument(bank, bankSign);
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
	final XmlControlResonseDocument loaded = loadDocument(RESOURCE);
	dumpDocument(loaded, true);
    }

    private void dumpDocument(final XmlControlResonseDocument document, final boolean formatted) throws JAXBException {
	System.out.println(getDocumentString(document, formatted));
    }

    private String getDocumentString(final XmlControlResonseDocument document, final boolean formatted)
	    throws JAXBException {
	return XmlControlResonseDocument.getTool().serializeToString(document);
    }

    private XmlControlResonseDocument loadDocument(final String resourceName) throws JAXBException {
	return XmlControlResonseDocument.getTool()
		.deserializeFrom(MyResources.getAsStream(this.getClass(), resourceName));
    }
}
