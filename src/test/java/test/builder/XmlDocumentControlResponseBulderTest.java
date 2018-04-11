package test.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.junit.BeforeClass;
import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlControlResponseDocument;
import tech.lapsa.java.commons.io.MyResources;
import tech.lapsa.java.commons.security.MyCertificates;
import tech.lapsa.java.commons.security.MyKeyStores;
import tech.lapsa.java.commons.security.MyKeyStores.StoreType;

public class XmlDocumentControlResponseBulderTest {

    private static final StoreType STORETYPE = StoreType.JKS;
    private static final String KEYSTORE = "/kkb.jks";
    private static final String STOREPASS = "1q2w3e4r";
    private static final String ALIAS = "kkbca-test";

    private static X509Certificate bankCert;

    @BeforeClass
    public static void loadKeys() throws Exception {

	final InputStream storeStream = MyResources.optAsStream(XmlDocumentControlResponseBulderTest.class, KEYSTORE) //
		.orElseThrow(() -> new RuntimeException("Keystore not found"));

	final KeyStore keystore = MyKeyStores.from(storeStream, STORETYPE, STOREPASS) //
		.orElseThrow(() -> new RuntimeException("Can not load keystore"));

	bankCert = MyCertificates.from(keystore, ALIAS) //
		.orElseThrow(() -> new RuntimeException("Can find key entry"));
    }

    private static final String XML = ""
	    + "<document>"
	    + "<bank name=\"Kazkommertsbank JSC\">"
	    + "<merchant id=\"92061103\">"
	    + "<command type=\"complete\"/>"
	    + "<payment amount=\"26431\" approval_code=\"181530\" currency_code=\"398\" orderid=\"503390238687304\" reference=\"180406181530\"/>"
	    + "</merchant>"
	    + "<merchant_sign cert_id=\"c183d70b\" type=\"RSA\">"
	    + "8hk2Uf2XccaXwoYdRIJUbdlN/n5zDwMkGcpW8lgRbermq5HVa2qELoPRr8efe6t1EaPU8hguueONGyic+0ZLTDOU22rHmsyJbOw8755iu469jLC26c5K1Xb1qsqSo4vdpCBWlVLd/mUj0CyP8Nxk8YjqG91ZUoB0UOCWCWJ5E04="
	    + "</merchant_sign>"
	    + "<response code=\"00\" message=\"Dublicate request\" SessionID=\"227A6AE29920008C1EECEF7953163790\"/>"
	    + "</bank>"
	    + "<bank_sign type=\"RSA\" cert_id=\"00c183d690\">"
	    + "p0xlKe5r70BvCgohvnTfdXeWHY8nftxdJYQ5ZKaVUV2YyUX+kjRyCWi04bR/lo7n/nksJvJlD0gAfrtegPhq26DX9cJYftEX/Y+RoyfuA2RNPVfQ3aBtocy8A2T7rt1NtYC3rAzGi+iwOFlLTM6lRoyMVusvGhKRLhlj6x5m5Sc="
	    + "</bank_sign>"
	    + "</document>"
	    + "";

    @Test
    public void basicTest() {
	final XmlControlResponseDocument o = XmlControlResponseDocument.builder() //
		.ofRawXml(XML) //
		.withBankCertificate(bankCert) //
		.build();
	assertThat(o, not(nullValue()));
    }

}
