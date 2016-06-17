package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.api.KKBBankSignatureService;
import com.lapsa.kkb.api.KKBPaymentOrderFactory;
import com.lapsa.kkb.api.KKBPaymentOrderService;
import com.lapsa.kkb.api.KKBServiceError;
import com.lapsa.kkb.core.KKBPaymentOrder;

public class KKBPaymentOrderServiceTestCase_Request extends ArquillianBaseTestCase {

    private static final String TEST_ORDER_ID = "484902574738032";
    private static final double TEST_AMOUNT = 2382.05;
    private static final String TEST_REQUEST_VALID = "<document><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"484902574738032\" currency=\"398\" amount=\"2382.05\"><department merchant_id=\"92061103\" amount=\"2382.05\"/></order></merchant><merchant_sign type=\"RSA\">0SeH7sjQH1U/wYRn9AKM8q1Zujjs1zMaF5M0Gm+6k4KiPG6yAXaqazBzcUU/LC/fMR5n4CoqFv/+MMvHaQw+htvBDH0Fe6svazqZZMQnKVQVkfXg9Z2y88xipGt+daya5OK/lqTvMGh1ACgEObGv95/nXledaPDpU4oexQcaySg=</merchant_sign></document>";
    private static final FinCurrency TEST_CURRENCY = FinCurrency.KZT;

    @EJB
    private KKBPaymentOrderService paymentOrderService;

    @EJB
    private KKBPaymentOrderFactory paymentOrderBuilder;

    @EJB
    private KKBBankSignatureService bankSignatureService;

    @Test
    public void testComposeRequest() throws KKBServiceError {
	KKBPaymentOrder order = paymentOrderBuilder.buildNewPaymentOrder(TEST_ORDER_ID, TEST_AMOUNT, TEST_CURRENCY);
	String encodedRequest = paymentOrderService.composeRequest(order);
	assertThat("Wrong encoded request", encodedRequest, is(TEST_REQUEST_VALID));
    }
}
