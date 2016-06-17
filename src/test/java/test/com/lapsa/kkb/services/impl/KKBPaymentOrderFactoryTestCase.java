package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.api.KKBPaymentOrderFactory;
import com.lapsa.kkb.core.KKBPaymentOperation;
import com.lapsa.kkb.core.KKBPaymentOrder;

public class KKBPaymentOrderFactoryTestCase extends ArquillianBaseTestCase {

    @EJB
    private KKBPaymentOrderFactory paymentOrderBuilderService;

    @Test
    public void testGenerateNewOrderId() {
	String id = paymentOrderBuilderService.generateNewOrderId();
	assertThat(id, allOf(not(nullValue()), not(is(""))));
	assertThat(id.length(), lessThanOrEqualTo(15));
    }

    @Test
    public void testBuildNewPaymentOrder_Amount() {
	double amount = 500;
	KKBPaymentOrder order = paymentOrderBuilderService.buildNewPaymentOrder(amount);
	assertThat(order, not(nullValue()));
	assertThat(order.getOrderId(), not(nullValue()));
	assertThat(order.getTotalAmount(), is(amount));
	assertThat(order.getCurrency(), is(FinCurrency.KZT));
	assertThat(order.getOperationsList(),
		allOf(not(nullValue()), not(emptyCollectionOf(KKBPaymentOperation.class)), hasSize(1)));
	assertTrue(order.hasOperationMerchant(TestConstants.TEST_MERCHANT_ID));
	KKBPaymentOperation oper = order.getOperationToMerchant(TestConstants.TEST_MERCHANT_ID);
	assertThat(oper, not(nullValue()));
	assertThat(oper.getAmount(), is(amount));
	assertThat(oper.getMerchantId(), is(TestConstants.TEST_MERCHANT_ID));
    }

    @Test
    public void testBuildNewPaymentOrder_AmountAndOrderId() {
	double amount = 500;
	String orderId = "21321312";
	KKBPaymentOrder order = paymentOrderBuilderService.buildNewPaymentOrder(orderId, amount);
	assertThat(order, not(nullValue()));
	assertThat(order.getOrderId(), allOf(not(nullValue()), is(orderId)));
	assertThat(order.getTotalAmount(), is(amount));
	assertThat(order.getCurrency(), is(FinCurrency.KZT));
	assertThat(order.getOperationsList(),
		allOf(not(nullValue()), not(emptyCollectionOf(KKBPaymentOperation.class)), hasSize(1)));
	assertTrue(order.hasOperationMerchant(TestConstants.TEST_MERCHANT_ID));
	KKBPaymentOperation oper = order.getOperationToMerchant(TestConstants.TEST_MERCHANT_ID);
	assertThat(oper, not(nullValue()));
	assertThat(oper.getAmount(), is(amount));
	assertThat(oper.getMerchantId(), is(TestConstants.TEST_MERCHANT_ID));
    }

    @Test
    public void testBuildNewPaymentOrder_AmountAndOrderIdAndCurrency() {
	double amount = 500;
	String orderId = "21321312";
	KKBPaymentOrder order = paymentOrderBuilderService.buildNewPaymentOrder(orderId, amount, FinCurrency.KZT);
	assertThat(order, not(nullValue()));
	assertThat(order.getOrderId(), not(nullValue()));
	assertThat(order.getTotalAmount(), is(amount));
	assertThat(order.getCurrency(), is(FinCurrency.KZT));
	assertThat(order.getOperationsList(),
		allOf(not(nullValue()), not(emptyCollectionOf(KKBPaymentOperation.class)), hasSize(1)));
	assertTrue(order.hasOperationMerchant(TestConstants.TEST_MERCHANT_ID));
	KKBPaymentOperation oper = order.getOperationToMerchant(TestConstants.TEST_MERCHANT_ID);
	assertThat(oper, not(nullValue()));
	assertThat(oper.getAmount(), is(amount));
	assertThat(oper.getMerchantId(), is(TestConstants.TEST_MERCHANT_ID));
    }

}
