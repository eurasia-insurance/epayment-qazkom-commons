package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBResponseParserService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBWrongSignature;

public class KKBResponseParserTestCase extends ArquillianBaseTestCase {

    @EJB
    private KKBResponseParserService responseParserService;

    private static final String PARSE_ORDER_ID_RESPONSE = "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARD\" mail=\"vadim.isaev@me.com\" phone=\"\"><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"484902574738032\" amount=\"2382.05\" currency=\"398\"><department merchant_id=\"92061103\" amount=\"2382.05\"/></order></merchant><merchant_sign type=\"RSA\"/></customer><customer_sign type=\"RSA\"/><results timestamp=\"2016-06-14 15:18:02\"><payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"2382.05\" reference=\"160614151802\" approval_code=\"151802\" response_code=\"00\" Secure=\"No\" card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results></bank><bank_sign cert_id=\"00c183d690\" type=\"SHA/RSA\">uHWuUQ938FNwU3ZkEip2/HGSL6niFomLvmklkg1mWOiGPCzEcQoFc5XFfTYnLwin3qtl3JsnO/yysFAjXFOBYfe5txQIWo5rnCzQ7/97n7jDHUHx58rqTLPzWwb70bYE3DuZch/cvS2gyFBbstUkzik+0YDJ/FuMwmTU4Kl/dBc=</bank_sign></document>";
    private static final String PARSE_ORDER_ID_ORDER_ID = "484902574738032";

    @Test
    public void testParseOrderId() throws KKBServiceError, KKBFormatException {
	String orderId = responseParserService.parseOrderId(PARSE_ORDER_ID_RESPONSE);
	assertThat(orderId, allOf(not(nullValue()), is(PARSE_ORDER_ID_ORDER_ID)));
    }

    private static final String PARSE_ORDER_ID_FAILED_RESPONSE = "<document><bank name=\"Kazkommertsbank JSC\"><customer2 name=\"MR CARD\" mail=\"vadim.isaev@me.com\" phone=\"\"><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"484902574738032\" amount=\"2382.05\" currency=\"398\"><department merchant_id=\"92061103\" amount=\"2382.05\"/></order></merchant><merchant_sign type=\"RSA\"/></customer2><customer2_sign type=\"RSA\"/><results2 timestamp=\"2016-06-14 15:18:02\"><payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"2382.05\" reference=\"160614151802\" approval_code=\"151802\" response_code=\"00\" Secure=\"No\" card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results2></bank><bank_sign cert_id=\"00c183d690\" type=\"SHA/RSA\">uHWuUQ938FNwU3ZkEip2/HGSL6niFomLvmklkg1mWOiGPCzEcQoFc5XFfTYnLwin3qtl3JsnO/yysFAjXFOBYfe5txQIWo5rnCzQ7/97n7jDHUHx58rqTLPzWwb70bYE3DuZch/cvS2gyFBbstUkzik+0YDJ/FuMwmTU4Kl/dBc=</bank_sign></document>";

    @Test(expected = KKBFormatException.class)
    public void testParseOrderId_Failed() throws KKBServiceError, KKBFormatException {
	responseParserService.parseOrderId(PARSE_ORDER_ID_FAILED_RESPONSE);
    }

    private static final String VALIDATE_SIGNATURE_OK_RESPONSE = "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARD\" mail=\"vadim.isaev@me.com\" phone=\"\"><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"484902574738032\" amount=\"2382.05\" currency=\"398\"><department merchant_id=\"92061103\" amount=\"2382.05\"/></order></merchant><merchant_sign type=\"RSA\"/></customer><customer_sign type=\"RSA\"/><results timestamp=\"2016-06-14 15:18:02\"><payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"2382.05\" reference=\"160614151802\" approval_code=\"151802\" response_code=\"00\" Secure=\"No\" card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results></bank><bank_sign cert_id=\"00c183d690\" type=\"SHA/RSA\">uHWuUQ938FNwU3ZkEip2/HGSL6niFomLvmklkg1mWOiGPCzEcQoFc5XFfTYnLwin3qtl3JsnO/yysFAjXFOBYfe5txQIWo5rnCzQ7/97n7jDHUHx58rqTLPzWwb70bYE3DuZch/cvS2gyFBbstUkzik+0YDJ/FuMwmTU4Kl/dBc=</bank_sign></document>";

    @Test
    public void testValidateSignature() throws KKBServiceError, KKBFormatException, KKBWrongSignature {
	responseParserService.validateSignature(VALIDATE_SIGNATURE_OK_RESPONSE);
    }

    private static final String VALIDATE_SIGNATURE_FAILED_RESPONSE = "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARD\" mail=\"vadim.isaev@me.com\" phone=\"\"><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"484902574738032\" amount=\"2382.05\" currency=\"398\"><department merchant_id=\"92061103\" amount=\"2382.05\"/></order></merchant><merchant_sign type=\"RSA\"/></customer><customer_sign type=\"RSA\"/><results timestamp=\"2016-06-14 15:18:02\"><payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"2382.05\" reference=\"160614151802\" approval_code=\"151802\" response_code=\"00\" Secure=\"No\" card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results></bank><bank_sign cert_id=\"00c183d690\" type=\"SHA/RSA\">uHWuUQ938FNwU3ZkEip2/HGSL6niFomLvmklkg1mWOiGPCzEcQoFc5XFfTYnLwin3qtl3JsnO/yysFAjXFOBYfe5txQIWo5rnCzQ7/97n7jDHUHx58rqQLPzWwb70bYE3DuZch/cvS2gyFBbstUkzik+0YDJ/FuMwmTU4Kl/dBc=</bank_sign></document>";

    @Test(expected = KKBWrongSignature.class)
    public void testValidateSignatureFailed() throws KKBServiceError, KKBFormatException, KKBWrongSignature {
	responseParserService.validateSignature(VALIDATE_SIGNATURE_FAILED_RESPONSE);
    }
}
