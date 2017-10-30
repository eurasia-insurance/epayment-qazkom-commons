package tech.lapsa.qazkom.xml;

import java.io.StringReader;
import java.io.StringWriter;
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

public final class XmlDocuments {

    private XmlDocuments() {
    }

    private static final Processor<XmlDocumentPayment> PAYMENT_PROCESSOR = Processor.forClass(XmlDocumentPayment.class,
	    XmlSchemas.PAYMENT_SCHEMA);

    // payment

    public static XmlDocumentPayment parsePayment(String rawXml) throws JAXBException {
	return PAYMENT_PROCESSOR.parse(rawXml);
    }

    public static Optional<XmlDocumentPayment> optionalParsePayment(String rawXml) {
	try {
	    return MyOptionals.of(parsePayment(rawXml));
	} catch (JAXBException e) {
	    return Optional.empty();
	}
    }

    public static String composePayment(XmlDocumentPayment document) throws JAXBException {
	return PAYMENT_PROCESSOR.compose(document);
    }

    public static Optional<String> optionalComposePayment(XmlDocumentPayment document) {
	try {
	    return MyOptionals.of(composePayment(document));
	} catch (JAXBException e) {
	    return Optional.empty();
	}
    }

    // order

    private static final Processor<XmlDocumentOrder> ORDER_PROCESSOR = Processor.forClass(XmlDocumentOrder.class,
	    XmlSchemas.ORDER_SCHEMA);

    public static XmlDocumentOrder parseOrder(String rawXml) throws JAXBException {
	return ORDER_PROCESSOR.parse(rawXml);
    }

    public static Optional<XmlDocumentOrder> optionalParseOrder(String rawXml) {
	try {
	    return MyOptionals.of(parseOrder(rawXml));
	} catch (JAXBException e) {
	    return Optional.empty();
	}
    }

    public static String composeOrder(XmlDocumentOrder document) throws JAXBException {
	return ORDER_PROCESSOR.compose(document);
    }

    public static Optional<String> optionalComposeOrder(XmlDocumentOrder document) {
	try {
	    return MyOptionals.of(composeOrder(document));
	} catch (JAXBException e) {
	    return Optional.empty();
	}
    }

    // helper class

    private static class Processor<T> {

	private final Marshaller marshaller;
	private final Unmarshaller unmarshaller;
	private final Class<T> clazz;

	private Processor(Class<T> clazz, Marshaller marshaller, Unmarshaller unmarshaller) {
	    this.clazz = clazz;
	    this.marshaller = marshaller;
	    this.unmarshaller = unmarshaller;
	}

	private T parse(String rawXml) throws JAXBException {
	    MyStrings.requireNonEmpty(rawXml, "rawXml");
	    return clazz.cast(unmarshaller.unmarshal(new StringReader(rawXml)));
	}

	private String compose(T document) throws JAXBException {
	    MyObjects.requireNonNull(document, "document");
	    StringWriter output = new StringWriter();
	    marshaller.marshal(document, output);
	    return output.toString();
	}

	private static <Q> Processor<Q> forClass(final Class<Q> clazz, Schema schema) {
	    try {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setSchema(schema);
		return new Processor<Q>(clazz, marshaller, unmarshaller);
	    } catch (JAXBException e) {
		throw new RuntimeException(e);
	    }
	}
    }
}
