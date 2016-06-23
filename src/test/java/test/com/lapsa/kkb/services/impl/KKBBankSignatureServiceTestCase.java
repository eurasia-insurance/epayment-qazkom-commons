package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.kkb.services.KKBBankSignatureService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBWrongSignature;
import com.sun.pkg.util.Base64;

public class KKBBankSignatureServiceTestCase extends ArquillianBaseTestCase {

    @EJB
    private KKBBankSignatureService bankSignatureService;

    @Test
    public void testGetSignatureCertificate() {
	X509Certificate cert = bankSignatureService.getSignatureCertificate();
	assertThat(cert, not(nullValue()));
	assertThat(cert.getSerialNumber(), is(new BigInteger("3246642832", 10)));
    }

    private static final byte[] VERIFY_SUCCESS_SIGNATURE_DATA = (""
	    + "<bank name=\"Kazkommertsbank JSC\">"
	    + "<customer name=\"MR CARD\" mail=\"vadim.isaev@me.com\" phone=\"\">"
	    + "<merchant cert_id=\"c183d70b\" name=\"Test shop 3\">"
	    + "<order order_id=\"484902574738032\" amount=\"2382.05\" currency=\"398\">"
	    + "<department merchant_id=\"92061103\" amount=\"2382.05\"/>"
	    + "</order>"
	    + "</merchant>"
	    + "<merchant_sign type=\"RSA\"/>"
	    + "</customer>"
	    + "<customer_sign type=\"RSA\"/>"
	    + "<results timestamp=\"2016-06-14 15:18:02\">"
	    + "<payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"2382.05\" "
	    + "reference=\"160614151802\" approval_code=\"151802\" response_code=\"00\" Secure=\"No\" "
	    + "card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/>"
	    + "</results>"
	    + "</bank>").getBytes();

    private static final byte[] VERIFY_SUCCESS_SIGNATURE_DIGEST = Base64
	    .base64ToByteArray("uHWuUQ938FNwU3ZkEip2/HGSL6niFomLvmk"
		    + "lkg1mWOiGPCzEcQoFc5XFfTYnLwin3qtl3JsnO/yysFAjXF"
		    + "OBYfe5txQIWo5rnCzQ7/97n7jDHUHx58rqTLPzWwb70bYE3DuZch/cvS2gyFBbstUkzik+0YDJ/FuMwmTU4Kl/dBc=");

    @Test
    public void testVerify_Success() throws KKBServiceError, KKBWrongSignature {
	bankSignatureService.verify(VERIFY_SUCCESS_SIGNATURE_DATA, VERIFY_SUCCESS_SIGNATURE_DIGEST);
    }

    private static final byte[] VERIFY_FAIL_INVALID_SIGNATURE_DATA = (""
	    + "<bank name=\"Kazkommertsbank JSC\">"
	    + "<customer name=\"MR CARD\" mail=\"vadim.isaev@me.com\" phone=\"\">"
	    + "<merchant cert_id=\"c183d70b\" name=\"Test shop 3\">"
	    + "<order order_id=\"484902574738032\" amount=\"2382.05\" currency=\"398\">"
	    + "<department merchant_id=\"92061103\" amount=\"2382.05\"/>"
	    + "</order>"
	    + "</merchant>"
	    + "<merchant_sign type=\"RSA\"/>"
	    + "</customer>"
	    + "<customer_sign type=\"RSA\"/>"
	    + "<results timestamp=\"2016-06-14 15:18:02\">"
	    + "<payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"2382.05\" "
	    + "reference=\"160614151802\" approval_code=\"151802\" response_code=\"00\" Secure=\"No\" "
	    + "card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/>"
	    + "</results>"
	    + "</bank>").getBytes();

    private static final byte[] VERIFY_FAIL_INVALID_SIGNATURE_DIGEST = new byte[] { -14, 118, 54, -102, -23, -19, 77,
	    12, 92, 56, -51, 74, -115, -23, 70, 120, -63, -118, 36, 38, -72, -92, -109, 95, -7, 97, 126, 75, -102, 67,
	    -63, 21, -27, -11, -27, -29, -45, -20, 125, -102, -9, 63, 116, -25, 111, -62, -14, -95, -118, -127, -65, 50,
	    -34, -59, -125, -107, -3, 124, -4, 122, 71, 120, -4, 17, -74, 28, 77, -53, 59, 72, -121, -38, 122, -120, 14,
	    1, 9, -68, 106, -105, 35, 67, 62, 73, -119, 76, 109, 91, 87, 67, -60, -9, -93, -107, -64, 26, -14, -89,
	    -117, -38, 68, 76, 17, 91, -29, -24, -93, 37, -43, -30, 72, 107, -11, 54, 101, 66, -20, 32, -37, 70, 54,
	    -79, -21, 23, -99, 116, -74, 75 };

    @Test(expected = KKBWrongSignature.class)
    public void testVerify_Fail_Invalid() throws KKBServiceError, KKBWrongSignature {
	bankSignatureService.verify(VERIFY_FAIL_INVALID_SIGNATURE_DATA, VERIFY_FAIL_INVALID_SIGNATURE_DIGEST);
    }

    private static final byte[] VERIFY_FAIL_BROKEN_SIGNATURE_DATA = (""
	    + "<bank name=\"Kazkommertsbank JSC\">"
	    + "<customer name=\"MR CARD\" mail=\"vadim.isaev@me.com\" phone=\"\">"
	    + "<merchant cert_id=\"c183d70b\" name=\"Test shop 3\">"
	    + "<order order_id=\"484902574738032\" amount=\"2382.05\" currency=\"398\">"
	    + "<department merchant_id=\"92061103\" amount=\"2382.05\"/>"
	    + "</order>"
	    + "</merchant>"
	    + "<merchant_sign type=\"RSA\"/>"
	    + "</customer>"
	    + "<customer_sign type=\"RSA\"/>"
	    + "<results timestamp=\"2016-06-14 15:18:02\">"
	    + "<payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"2382.05\" "
	    + "reference=\"160614151802\" approval_code=\"151802\" response_code=\"00\" Secure=\"No\" "
	    + "card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/>"
	    + "</results>"
	    + "</bank>").getBytes();

    private static final byte[] VERIFY_FAIL_BROKEN_SIGNATURE_DIGEST = new byte[] { -14, 118, -102, -23, -19, 77, 12, 92,
	    56, -51, 74, -115, -23, 70, 120, -63, -118, 36, 38, -72, -92, -109, 95, -7, 97, 126, 75, -102, 67, -63, 21,
	    -27, -11, -27, -29, -45, -20, 125, -102, -9, 63, 116, -25, 111, -62, -14, -95, -118, -127, -65, 50, -34,
	    -59, -125, -107, -3, 124, -4, 122, 71, 120, -4, 17, -74, 28, 77, -53, 59, 72, -121, -38, 122, -120, 14, 1,
	    9, -68, 106, -105, 35, 67, 62, 73, -119, 76, 109, 91, 87, 67, -60, -9, -93, -107, -64, 26, -14, -89, -117,
	    -38, 68, 76, 17, 91, -29, -24, -93, 37, -43, -30, 72, 107, -11, 54, 101, 66, -20, 32, -37, 70, 54, -79, -21,
	    23, -99, 116, -74, 75 };

    @Test(expected = KKBWrongSignature.class)
    public void testVerify_Fail_Broken() throws KKBServiceError, KKBWrongSignature {
	bankSignatureService.verify(VERIFY_FAIL_BROKEN_SIGNATURE_DATA, VERIFY_FAIL_BROKEN_SIGNATURE_DIGEST);
    }
}
