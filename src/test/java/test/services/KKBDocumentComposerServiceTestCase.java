package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.fin.FinCurrency;
import com.lapsa.kkb.core.KKBCartDocument;
import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.core.KKBOrderItem;
import com.lapsa.kkb.core.KKBPaymentRequestDocument;
import com.lapsa.kkb.services.KKBBankSignatureService;
import com.lapsa.kkb.services.KKBDocumentComposerService;
import com.lapsa.kkb.services.KKBFactory;
import com.lapsa.kkb.services.KKBServiceError;

public class KKBDocumentComposerServiceTestCase extends ArquillianBaseTestCase {

    @EJB
    private KKBDocumentComposerService documentComposerService;

    @EJB
    private KKBFactory factory;

    @EJB
    private KKBBankSignatureService bankSignatureService;

    private static final String COMPOSE_RESPONSE_ORDER_ID = "484902574738032";
    private static final FinCurrency COMPOSE_RESPONSE_CURRENCY = FinCurrency.KZT;
    private static final double COMPOSE_RESPONSE_AMOUNT = 2382.05;
    private static final String COMPOSE_REQUEST_DOCUMENT = "<document><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"484902574738032\" currency=\"398\" amount=\"2382.05\"><department merchant_id=\"92061103\" amount=\"2382.05\"/></order></merchant><merchant_sign type=\"RSA\">0SeH7sjQH1U/wYRn9AKM8q1Zujjs1zMaF5M0Gm+6k4KiPG6yAXaqazBzcUU/LC/fMR5n4CoqFv/+MMvHaQw+htvBDH0Fe6svazqZZMQnKVQVkfXg9Z2y88xipGt+daya5OK/lqTvMGh1ACgEObGv95/nXledaPDpU4oexQcaySg=</merchant_sign></document>";

    @Test
    public void testComposeRequest() throws KKBServiceError {
	KKBOrder order = new KKBOrder();
	order.setId(COMPOSE_RESPONSE_ORDER_ID);
	order.setCurrency(COMPOSE_RESPONSE_CURRENCY);

	KKBOrderItem item = new KKBOrderItem();
	item.setCost(COMPOSE_RESPONSE_AMOUNT);
	item.setQuantity(1);
	item.setName("POLICY");

	order.addItem(item);

	KKBPaymentRequestDocument request = documentComposerService.composeRequest(order);
	assertThat(request, not(nullValue()));
	assertThat("Wrong encoded request", request.getContent(), is(COMPOSE_REQUEST_DOCUMENT));
    }

    private static final String COMPOSE_CART_ORDER_ID = "484902574738032";
    private static final FinCurrency COMPOSE_CART_CURRENCY = FinCurrency.KZT;
    private static final double COMPOSE_CART_AMOUNT = 2382.05;
    private static final Object COMPOSE_CART_DOCUMENT = "<document><item name=\"POLICY\" number=\"1\" quantity=\"1\" amount=\"2382.05\"/></document>";

    @Test
    public void testComposeCart() throws KKBServiceError {
	KKBOrder order = new KKBOrder();
	order.setId(COMPOSE_CART_ORDER_ID);
	order.setCurrency(COMPOSE_CART_CURRENCY);

	KKBOrderItem item = new KKBOrderItem();
	item.setCost(COMPOSE_CART_AMOUNT);
	item.setQuantity(1);
	item.setName("POLICY");

	order.addItem(item);

	KKBCartDocument cart = documentComposerService.composeCart(order);
	assertThat(cart, not(nullValue()));
	System.out.println(cart.getContent());
	assertThat("Wrong encoded request", cart.getContent(), is(COMPOSE_CART_DOCUMENT));
    }
}
