package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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

}
