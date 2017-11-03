package tech.lapsa.qazkom.xml.mapping;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;

public final class XmlDocumentTool<T> {

    static <Q> XmlDocumentTool<Q> forClass(Class<Q> clazz) {
	return forClass(clazz, null);
    }

    static <Q> XmlDocumentTool<Q> forClass(final Class<Q> clazz, Schema schema) {
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

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;
    private final Class<T> clazz;

    private XmlDocumentTool(Class<T> clazz, Marshaller marshaller, Unmarshaller unmarshaller) {
	this.clazz = clazz;
	this.marshaller = marshaller;
	this.unmarshaller = unmarshaller;
    }

    public T deserializeFrom(InputStream is) throws IllegalArgumentException {
	MyObjects.requireNonNull(is, "is");
	try {
	    return clazz.cast(unmarshaller.unmarshal(is));
	} catch (UnmarshalException e) {
	    throw new IllegalArgumentException("Can't deserialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while de-serializing document", e);
	}
    }

    public T deserializeFrom(Reader reader) throws IllegalArgumentException {
	MyObjects.requireNonNull(reader, "reader");
	try {
	    return clazz.cast(unmarshaller.unmarshal(reader));
	} catch (UnmarshalException e) {
	    throw new IllegalArgumentException("Can't deserialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while de-serializing document", e);
	}
    }

    public T deserializeFrom(String rawXml) throws IllegalArgumentException {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return deserializeFrom(new StringReader(rawXml));
    }

    public String serializeToString(T document) throws IllegalArgumentException {
	MyObjects.requireNonNull(document, "document");
	StringWriter output = new StringWriter();
	try {
	    marshaller.marshal(document, output);
	} catch (MarshalException e) {
	    throw new IllegalArgumentException("Can't serialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while serializing document", e);
	}
	return output.toString();
    }

    public byte[] serializeToBytes(T document) {
	MyObjects.requireNonNull(document, "document");
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	try {
	    marshaller.marshal(document, output);
	} catch (MarshalException e) {
	    throw new IllegalArgumentException("Can't serialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while serializing document", e);
	}
	return output.toByteArray();
    }

    public void serializeTo(T document, Writer output) {
	MyObjects.requireNonNull(document, "document");
	try {
	    marshaller.marshal(document, output);
	} catch (MarshalException e) {
	    throw new IllegalArgumentException("Can't serialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while serializing document", e);
	}
    }

    public void serializeTo(T document, OutputStream output) {
	MyObjects.requireNonNull(document, "document");
	try {
	    marshaller.marshal(document, output);
	} catch (MarshalException e) {
	    throw new IllegalArgumentException("Can't serialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while serializing document", e);
	}
    }
}