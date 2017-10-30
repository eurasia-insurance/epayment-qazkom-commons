package test.validation;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import tech.lapsa.qazkom.xml.validation.QazkomXmlSchema;

public class QazkomXmlSchemaTest {

    @Test
    public void allSchemasAvailableTest() {
	Stream.of(QazkomXmlSchema.values()) //
		.map(QazkomXmlSchema::getSchema) //
		.forEach(Assert::assertNotNull);
    }
}
