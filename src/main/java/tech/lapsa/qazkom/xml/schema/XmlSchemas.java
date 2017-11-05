package tech.lapsa.qazkom.xml.schema;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import tech.lapsa.java.commons.resources.Resources;

public final class XmlSchemas {

    private XmlSchemas() {
    }

    private static final SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    public static final Schema CART_SCHEMA = fromResource("/META-INF/qazkom/document-cart-schema.xsd");
    public static final Schema ERROR_SCHEMA = fromResource("/META-INF/qazkom/document-error-schema.xsd");
    public static final Schema ORDER_SCHEMA = fromResource("/META-INF/qazkom/document-request-schema.xsd");
    public static final Schema PAYMENT_SCHEMA = fromResource("/META-INF/qazkom/document-response-schema.xsd");

    public static final Schema[] all() {
	return new Schema[] { CART_SCHEMA, ERROR_SCHEMA, ORDER_SCHEMA, PAYMENT_SCHEMA };
    }

    private static final Schema fromResource(String resource) {
	InputStream is = Resources.optionalAsStream(XmlSchemas.class, resource) //
		.orElseThrow(() -> new RuntimeException("Resource not found " + resource));
	Source source = new StreamSource(is);
	try {
	    return SCHEMA_FACTORY.newSchema(source);
	} catch (SAXException e) {
	    throw new RuntimeException(e);
	}
    }
}