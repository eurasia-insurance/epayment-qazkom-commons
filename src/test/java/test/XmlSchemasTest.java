package test;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;

public class XmlSchemasTest {

    @Test
    public void allSchemasAvailableTest() {
	Stream.of(XmlSchemas.all()) //
		.forEach(Assert::assertNotNull);
    }
}
