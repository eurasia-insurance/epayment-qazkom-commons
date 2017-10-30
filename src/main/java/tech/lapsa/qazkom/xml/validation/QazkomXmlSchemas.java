package tech.lapsa.qazkom.xml.validation;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import tech.lapsa.java.commons.resources.Resources;

public enum QazkomXmlSchema {

    CART_SCHEMA("/META-INF/qazkom/document-cart-schema.xsd"),
    ERROR_SCHEMA("/META-INF/qazkom/document-error-schema.xsd"),
    REQUEST_SCHEMA("/META-INF/qazkom/document-request-schema.xsd"),
    RESPONSE_SCHEMA("/META-INF/qazkom/document-response-schema.xsd");

    private final Schema schema;

    private QazkomXmlSchema(String resource) {
	InputStream is = Resources.optionalAsStream(this.getClass(), resource) //
		.orElseThrow(() -> new RuntimeException("Resource not found " + resource));
	Source source = new StreamSource(is);
	SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	try {
	    schema = schemaFactory.newSchema(source);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	}
    }

    public Schema getSchema() {
	return schema;
    }
}
