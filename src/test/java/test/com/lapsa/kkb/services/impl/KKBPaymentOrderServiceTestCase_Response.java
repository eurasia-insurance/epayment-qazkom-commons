package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.core.KKBPaymentOperation;
import com.lapsa.kkb.core.KKBPaymentOrder;
import com.lapsa.kkb.services.KKBBankSignatureService;
import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBPaymentOrderFactory;
import com.lapsa.kkb.services.KKBPaymentOrderService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBWrongSignature;

public class KKBPaymentOrderServiceTestCase_Response extends ArquillianBaseTestCase {

    private static final double TEST_AMOUNT = 2382.05;

    private static final String TEST_RESPONSE = "<document>"
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
	    + "<payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" "
	    + "amount=\"2382.05\" reference=\"160614151802\" approval_code=\"151802\" "
	    + "response_code=\"00\" Secure=\"No\" card_bin=\"\" "
	    + "c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/>"
	    + "</results>"
	    + "</bank>"
	    + "<bank_sign cert_id=\"00c183d690\" type=\"SHA/RSA\">"
	    + "uHWuUQ938FNwU3ZkEip2/HGSL6niFomLvmklkg1mWOiGPCzEcQoFc5XFfTYnLwin3qtl3Js"
	    + "nO/yysFAjXFOBYfe5txQIWo5rnCzQ7/97n7jDHUHx58rqTLPzWwb70bYE3DuZch/cvS2gyF"
	    + "BbstUkzik+0YDJ/FuMwmTU4Kl/dBc="
	    + "</bank_sign></document>";

    private static final String TEST_RESPONSE_INVALID_SIGNATURE = "<document>"
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
	    + "<payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" "
	    + "amount=\"2382.05\" reference=\"160614151802\" approval_code=\"151802\" "
	    + "response_code=\"00\" Secure=\"No\" card_bin=\"\" "
	    + "c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/>"
	    + "</results>"
	    + "</bank>"
	    + "<bank_sign cert_id=\"00c183d690\" type=\"SHA/RSA\">"
	    + "p25i1rUH7StnhOfnkHSOHguuPMePaGXtiPGEOrJE4bof1gFVH19mhDyHjfWa"
	    + "6OeJ80fidyvVf1X4ewyP0yG4GxJSl0VyXz7+PNLsbs1lJe42d1fixvozhJSS"
	    + "YN6fAxMN8hhDht6S81YK3GbDTE7GH498pU9HGuGAoDVjB+NtrHk="
	    + "</bank_sign>"
	    + "</document>";

    @EJB
    private KKBPaymentOrderService paymentOrderService;

    @EJB
    private KKBPaymentOrderFactory paymentOrderBuilder;

    @EJB
    private KKBBankSignatureService bankSignatureService;

    @Test
    public void testParseResponse_Success() throws KKBServiceError, KKBFormatException, KKBWrongSignature {
	KKBPaymentOrder order = paymentOrderService.parseResponse(TEST_RESPONSE);
	bankSignatureService.verify(order.getResponseSignature());
	assertThat(order, not(nullValue()));
	assertThat(order.getOrderId(), not(nullValue()));
	assertThat(order.getTotalAmount(), is(TEST_AMOUNT));
	assertThat(order.getCurrency(), is(FinCurrency.KZT));
	assertThat(order.getOperationsList(),
		allOf(not(nullValue()), not(emptyCollectionOf(KKBPaymentOperation.class)), hasSize(1)));
	assertTrue(order.hasOperationMerchant(TestConstants.TEST_MERCHANT_ID));
	KKBPaymentOperation oper = order.getOperationToMerchant(TestConstants.TEST_MERCHANT_ID);
	assertThat(oper, not(nullValue()));
	assertThat(oper.getAmount(), is(TEST_AMOUNT));
	assertThat(oper.getMerchantId(), is(TestConstants.TEST_MERCHANT_ID));

    }

    @Test(expected = KKBWrongSignature.class)
    public void testParseResponse_Fail_InvalidSignature()
	    throws KKBServiceError, KKBFormatException, KKBWrongSignature {
	KKBPaymentOrder order = paymentOrderService.parseResponse(TEST_RESPONSE_INVALID_SIGNATURE);
	bankSignatureService.verify(order.getResponseSignature());
    }
}
