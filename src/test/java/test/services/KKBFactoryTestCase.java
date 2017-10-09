package test.services;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.kkb.services.KKBFactory;

public class KKBFactoryTestCase extends ArquillianBaseTestCase {

    @EJB
    private KKBFactory factory;

    @Test
    public void testGenerateNewOrderId() {
	String id = factory.generateNewOrderId();
	assertThat(id, allOf(not(nullValue()), not(is(""))));
	assertThat(id.length(), lessThanOrEqualTo(15));
    }

    
    @Test
    public void testGeneratePaymentURL() throws MalformedURLException {
	String id = "66778899";
	URL url = factory.generatePaymentPageUrl(id);
	assertThat(url, allOf(notNullValue(), is(new URL("http://localhost:8080/order/payment/?order=66778899"))));
    }
}
