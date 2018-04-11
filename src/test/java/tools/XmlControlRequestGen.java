package tools;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Currency;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentControlRequest;
import tech.lapsa.java.commons.io.MyResources;
import tech.lapsa.java.commons.security.MyCertificates;
import tech.lapsa.java.commons.security.MyKeyStores;
import tech.lapsa.java.commons.security.MyKeyStores.StoreType;
import tech.lapsa.java.commons.security.MyPrivateKeys;
import test.builder.XmlDocumentStatusRequestBulderTest;

public class XmlControlRequestGen {

    private static final StoreType STORETYPE = StoreType.JKS;
    private static final String KEYSTORE = "/kkb.jks";
    private static final String STOREPASS = "1q2w3e4r";
    private static final String ALIAS = "cert";

    private static X509Certificate certificate;
    private static PrivateKey key;

    public static void main(String[] args) {
	final InputStream storeStream = MyResources.optAsStream(XmlDocumentStatusRequestBulderTest.class, KEYSTORE) //
		.orElseThrow(() -> new RuntimeException("Keystore not found"));

	final KeyStore keystore = MyKeyStores.from(storeStream, STORETYPE, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can not load keystore"));

	key = MyPrivateKeys.from(keystore, ALIAS, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can't find key entry"));

	certificate = MyCertificates.from(keystore, ALIAS) //
		.orElseThrow(() -> new RuntimeException("Can find key entry"));

	final XmlDocumentControlRequest o = XmlDocumentControlRequest.builder() //
		.withMerchantId("92061103") //
		.signWith(key, certificate) //
		.withOrderNumber("503390238687304") //
		.withPaymentReference("180406181530") //
		.withApprovalCode("181530") //
		.withAmount(26431d) //
		.withCurrency(Currency.getInstance("KZT")) //
		.prepareCharge() //
		.build();

	final String rawXml = o.getRawXml();
	System.out.println(rawXml);
    }
}
