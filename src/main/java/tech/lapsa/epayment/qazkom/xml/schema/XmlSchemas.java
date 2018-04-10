package tech.lapsa.epayment.qazkom.xml.schema;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import tech.lapsa.java.commons.io.MyResources;

public final class XmlSchemas {

    private XmlSchemas() {
    }

    private static final SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    public static final Schema CART_SCHEMA = fromResource("/META-INF/qazkom/document-cart-schema.xsd");
    public static final Schema ERROR_SCHEMA = fromResource("/META-INF/qazkom/document-error-schema.xsd");
    public static final Schema ORDER_SCHEMA = fromResource("/META-INF/qazkom/document-order-schema.xsd");
    public static final Schema PAYMENT_SCHEMA = fromResource("/META-INF/qazkom/document-payment-schema.xsd");
    public static final Schema CONTROL_REQUEST_SCHEMA = fromResource("/META-INF/qazkom/document-control-request-schema.xsd");
    public static final Schema CONTROL_RESPONSE_SCHEMA = fromResource("/META-INF/qazkom/document-control-response-schema.xsd");
    public static final Schema STATUS_REQUEST_SCHEMA = fromResource("/META-INF/qazkom/document-status-request-schema.xsd");

    public static final Schema[] all() {
	return new Schema[] {
		CART_SCHEMA,
		ERROR_SCHEMA,
		ORDER_SCHEMA,
		PAYMENT_SCHEMA,
		CONTROL_REQUEST_SCHEMA,
		CONTROL_RESPONSE_SCHEMA,
		STATUS_REQUEST_SCHEMA };
    }

    private static final Schema fromResource(final String resource) {
	final InputStream is = MyResources.optAsStream(XmlSchemas.class, resource) //
		.orElseThrow(() -> new RuntimeException("Resource not found " + resource));
	final Source source = new StreamSource(is);
	try {
	    return SCHEMA_FACTORY.newSchema(source);
	} catch (final SAXException e) {
	    throw new RuntimeException(e);
	}
    }
}