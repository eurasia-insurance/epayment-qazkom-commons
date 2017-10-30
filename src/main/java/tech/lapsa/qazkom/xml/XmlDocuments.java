package tech.lapsa.qazkom.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.qazkom.xml.mapping.XmlDocumentOrder;
import tech.lapsa.qazkom.xml.mapping.XmlDocumentPayment;
import tech.lapsa.qazkom.xml.validation.XmlSchemas;

public final class XmlDocuments {

    private XmlDocuments() {
    }

    private static final Marshaller PAYMENT_MARSHALLER;

    private static final Unmarshaller PAYMENT_UNMARSHALLER;

    static {
	try {
	    JAXBContext context = JAXBContext.newInstance(XmlDocumentPayment.class);
	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setSchema(XmlSchemas.PAYMENT_SCHEMA);
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	    PAYMENT_MARSHALLER = marshaller;
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    unmarshaller.setSchema(XmlSchemas.PAYMENT_SCHEMA);
	    PAYMENT_UNMARSHALLER = unmarshaller;
	} catch (JAXBException e) {
	    throw new RuntimeException(e);
	}
    }

    public static XmlDocumentPayment parsePayment(String rawXml) throws JAXBException {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return (XmlDocumentPayment) PAYMENT_UNMARSHALLER.unmarshal(new StringReader(rawXml));
    }

    public static Optional<XmlDocumentPayment> optionalParsePayment(String rawXml) {
	try {
	    return MyOptionals.of(parsePayment(rawXml));
	} catch (JAXBException e) {
	    return Optional.empty();
	}
    }

    public static String composePayment(XmlDocumentPayment document) throws JAXBException {
	MyObjects.requireNonNull(document, "document");
	StringWriter output = new StringWriter();
	PAYMENT_MARSHALLER.marshal(document, output);
	return output.toString();
    }

    public static Optional<String> optionalComposePayment(XmlDocumentPayment document) {
	try {
	    return MyOptionals.of(composePayment(document));
	} catch (JAXBException e) {
	    return Optional.empty();
	}
    }

    private static final Marshaller ORDER_MARSHALLER;

    private static final Unmarshaller ORDER_UNMARSHALLER;

    static {
	try {
	    JAXBContext context = JAXBContext.newInstance(XmlDocumentOrder.class);
	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setSchema(XmlSchemas.ORDER_SCHEMA);
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	    ORDER_MARSHALLER = marshaller;
	    Unmarshaller unmarshaller = context.createUnmarshaller();
	    unmarshaller.setSchema(XmlSchemas.ORDER_SCHEMA);
	    ORDER_UNMARSHALLER = unmarshaller;
	} catch (JAXBException e) {
	    throw new RuntimeException(e);
	}
    }

    public static XmlDocumentOrder parseOrder(String rawXml) throws JAXBException {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return (XmlDocumentOrder) ORDER_UNMARSHALLER.unmarshal(new StringReader(rawXml));
    }

    public static Optional<XmlDocumentOrder> optionalParseOrder(String rawXml) {
	try {
	    return MyOptionals.of(parseOrder(rawXml));
	} catch (JAXBException e) {
	    return Optional.empty();
	}
    }

    public static String composeOrder(XmlDocumentOrder document) throws JAXBException {
	MyObjects.requireNonNull(document, "document");
	StringWriter output = new StringWriter();
	ORDER_MARSHALLER.marshal(document, output);
	return output.toString();
    }

    public static Optional<String> optionalComposeOrder(XmlDocumentOrder document) {
	try {
	    return MyOptionals.of(composeOrder(document));
	} catch (JAXBException e) {
	    return Optional.empty();
	}
    }

}
