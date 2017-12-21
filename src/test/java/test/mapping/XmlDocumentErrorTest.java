package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentError;
import tech.lapsa.epayment.qazkom.xml.bind.XmlError;
import tech.lapsa.epayment.qazkom.xml.bind.XmlErrorType;
import tech.lapsa.epayment.qazkom.xml.bind.XmlSession;

public class XmlDocumentErrorTest {

    private static final String TEST_DOCUMENT_AS_PLAINTEXT //
	    = "<response order_id=\"740954651955272\">"
		    + "<error code=\"05\" time=\"2017-12-01 15:01:51\" type=\"auth\">Transaction declined</error>"
		    + "<session id=\"11429DD3085E5E2A92A64C93FD199C48\"/></response>";

    private static final XmlDocumentError TEST_DOCUMENT_AS_OBJECT;

    static {
	TEST_DOCUMENT_AS_OBJECT = new XmlDocumentError();
	TEST_DOCUMENT_AS_OBJECT.setOrderId("740954651955272");

	{
	    final XmlError error = new XmlError();
	    error.setType(XmlErrorType.AUTHORIZATION);
	    {
		// 2017-12-01 15:01:51
		final LocalDateTime ldt = LocalDateTime.of(2017, 12, 1, 15, 1, 51);
		final Instant timestamp = ldt.atZone(ZoneId.of("Asia/Almaty")).toInstant();
		error.setTime(timestamp);
	    }
	    error.setCode("05");
	    error.setMessage("Transaction declined");
	    TEST_DOCUMENT_AS_OBJECT.setError(error);
	}

	{
	    final XmlSession session = new XmlSession();
	    session.setId("11429DD3085E5E2A92A64C93FD199C48");
	    TEST_DOCUMENT_AS_OBJECT.setSession(session);
	}
    }

    @Test
    public void testSerializeDocument() throws JAXBException {
	final String documentString = XmlDocumentError.getTool().serializeToString(TEST_DOCUMENT_AS_OBJECT);
	assertThat(documentString, allOf(not(nullValue()), is(TEST_DOCUMENT_AS_PLAINTEXT)));
    }
}
