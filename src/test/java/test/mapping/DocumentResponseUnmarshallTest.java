package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentPayment;
import tech.lapsa.java.commons.io.MyResources;

public class DocumentResponseUnmarshallTest {

    @Test
    public void variant4Test() {
	XmlDocumentPayment c = XmlDocumentPayment
		.getTool()
		.deserializeFrom(MyResources.getAsStream(this.getClass(), "/document-response-variant-4.xml"));
	assertThat(c.getBank().getResults().getPayments().get(0).getExpirationDate(),
		allOf(not(nullValue()), is(LocalDate.of(2022, 11, 30))));
    }

}
