package tech.lapsa.qazkom.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.qazkom.xml.mapping.XmlDocumentOrder;
import tech.lapsa.qazkom.xml.mapping.XmlDocumentPayment;
import tech.lapsa.qazkom.xml.mapping.XmlMerchant;

public final class XmlDocuments {

    private XmlDocuments() {
    }

    public static final XmlDocumentTool<XmlDocumentPayment> PAYMENT = //
	    XmlDocumentTool.forClass(XmlDocumentPayment.class, XmlSchemas.PAYMENT_SCHEMA);

    public static final XmlDocumentTool<XmlDocumentOrder> ORDER = //
	    XmlDocumentTool.forClass(XmlDocumentOrder.class, XmlSchemas.ORDER_SCHEMA);

    public static final XmlDocumentTool<XmlMerchant> MERCHANG = XmlDocumentTool.forClass(XmlMerchant.class);

    // helper class

    public static class XmlDocumentTool<T> {

	private final Marshaller marshaller;
	private final Unmarshaller unmarshaller;
	private final Class<T> clazz;

	private XmlDocumentTool(Class<T> clazz, Marshaller marshaller, Unmarshaller unmarshaller) {
	    this.clazz = clazz;
	    this.marshaller = marshaller;
	    this.unmarshaller = unmarshaller;
	}

	public T parse(InputStream is) {
	    MyObjects.requireNonNull(is, "is");
	    try {
		return clazz.cast(unmarshaller.unmarshal(is));
	    } catch (JAXBException e) {
		throw new IllegalArgumentException("Can not parse input stream", e);
	    }
	}

	public T parse(Reader reader) {
	    MyObjects.requireNonNull(reader, "reader");
	    try {
		return clazz.cast(unmarshaller.unmarshal(reader));
	    } catch (JAXBException e) {
		throw new IllegalArgumentException("Can not parse character stream", e);
	    }
	}

	public T parse(String rawXml) {
	    MyStrings.requireNonEmpty(rawXml, "rawXml");
	    return parse(new StringReader(rawXml));
	}

	public Optional<T> optionalParse(InputStream is) {
	    try {
		return MyOptionals.of(parse(is));
	    } catch (IllegalArgumentException e) {
		return Optional.empty();
	    }
	}

	public Optional<T> optionalParse(Reader reader) {
	    try {
		return MyOptionals.of(parse(reader));
	    } catch (IllegalArgumentException e) {
		return Optional.empty();
	    }
	}

	public Optional<T> optionalParse(String rawXml) {
	    try {
		return MyOptionals.of(parse(rawXml));
	    } catch (IllegalArgumentException e) {
		return Optional.empty();
	    }
	}

	public String composeToString(T document) throws JAXBException {
	    MyObjects.requireNonNull(document, "document");
	    StringWriter output = new StringWriter();
	    marshaller.marshal(document, output);
	    return output.toString();
	}

	public void composeTo(T document, Writer output) throws JAXBException {
	    MyObjects.requireNonNull(document, "document");
	    marshaller.marshal(document, output);
	}

	public void composeTo(T document, OutputStream output) throws JAXBException {
	    MyObjects.requireNonNull(document, "document");
	    marshaller.marshal(document, output);
	}

	public Optional<String> optionalComposeToString(T document) {
	    try {
		return MyOptionals.of(composeToString(document));
	    } catch (JAXBException e) {
		return Optional.empty();
	    }
	}

	private static <Q> XmlDocumentTool<Q> forClass(Class<Q> clazz) {
	    return forClass(clazz, null);
	}

	private static <Q> XmlDocumentTool<Q> forClass(final Class<Q> clazz, Schema schema) {
	    try {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Marshaller marshaller = context.createMarshaller();
		if (schema != null)
		    marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		if (schema != null)
		    unmarshaller.setSchema(schema);
		return new XmlDocumentTool<Q>(clazz, marshaller, unmarshaller);
	    } catch (JAXBException e) {
		throw new RuntimeException(e);
	    }
	}
    }
}
