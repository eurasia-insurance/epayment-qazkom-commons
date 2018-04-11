package test.mapping;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentError;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentError.XmlError;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentError.XmlError.XmlErrorType;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentError.XmlSession;

public class XmlDocumentErrorTest {

    private static final String TEST_DOCUMENT_AS_PLAINTEXT //
	    = "<response order_id=\"740954651955272\">"
		    + "<error code=\"05\" time=\"2017-12-01 15:01:51\" type=\"auth\">Transaction declined</error>"
		    + "<session id=\"11429DD3085E5E2A92A64C93FD199C48\"/></response>";

    private static final XmlDocumentError TEST_DOCUMENT_AS_OBJECT;

    static {

	final XmlError error = new XmlError(XmlErrorType.AUTHORIZATION,
		// 2017-12-01 15:01:51
		LocalDateTime.of(2017, 12, 1, 15, 1, 51).atZone(ZoneId.of("Asia/Almaty")).toInstant(),
		"05",
		"Transaction declined");

	final XmlSession session = new XmlSession("11429DD3085E5E2A92A64C93FD199C48");

	TEST_DOCUMENT_AS_OBJECT = new XmlDocumentError("740954651955272", error, session);

    }

    @Test
    public void testSerializeDocument() throws JAXBException {
	final String documentString = XmlDocumentError.getTool().serializeToString(TEST_DOCUMENT_AS_OBJECT);
	assertThat(documentString, allOf(not(nullValue()), is(TEST_DOCUMENT_AS_PLAINTEXT)));
    }
}
