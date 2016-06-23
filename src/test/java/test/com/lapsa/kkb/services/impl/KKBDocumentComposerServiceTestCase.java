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
    private static final String COMPOSE_REQUEST_DOCUMENT = "<document><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order amount=\"2382.05\" currency=\"398\" order_id=\"484902574738032\"><department amount=\"2382.05\" merchant_id=\"92061103\"/></order></merchant><merchant_sign type=\"RSA\">qiqkubnAmw2lf9hAxk1vQh/00oUPSp+XiEjC9wQYegF2vMYW5ag33IxnbWOov5r4L2Fn0EZo8aXNYnljVOTO9I7D8bwZ1ftl2IFs40fVkhsZqmA+Rk5AeWHekZPl4CYnLX0gDwGj3BN1SQsbntnS4YVy9eXuY/ftX1xcrT9avSw=</merchant_sign></document>";

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
    private static final Object COMPOSE_CART_DOCUMENT = "<document><item amount=\"2382.05\" name=\"POLICY\" number=\"1\" quantity=\"1\"/></document>";

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
