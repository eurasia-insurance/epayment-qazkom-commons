package test.validation;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import tech.lapsa.qazkom.xml.validation.QazkomXmlSchemas;

public class QazkomXmlSchemaTest {

    @Test
    public void allSchemasAvailableTest() {
	Stream.of(QazkomXmlSchemas.all()) //
		.forEach(Assert::assertNotNull);
    }
}
