package tech.lapsa.qazkom.xml.bind;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

public final class XmlDocumentTool<T> {

    static <Q> XmlDocumentTool<Q> forClass(final Class<Q> clazz) {
	return forClass(clazz, null);
    }

    static <Q> XmlDocumentTool<Q> forClass(final Class<Q> clazz, final Schema schema) {
	try {
	    final JAXBContext context = JAXBContext.newInstance(clazz);
	    final Marshaller marshaller = context.createMarshaller();
	    if (schema != null)
		marshaller.setSchema(schema);
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	    final Unmarshaller unmarshaller = context.createUnmarshaller();
	    if (schema != null)
		unmarshaller.setSchema(schema);
	    return new XmlDocumentTool<Q>(clazz, marshaller, unmarshaller);
	} catch (final JAXBException e) {
	    throw new RuntimeException(e);
	}
    }

    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;
    private final Class<T> clazz;

    private XmlDocumentTool(final Class<T> clazz, final Marshaller marshaller, final Unmarshaller unmarshaller) {
	this.clazz = clazz;
	this.marshaller = marshaller;
	this.unmarshaller = unmarshaller;
    }

    public T deserializeFrom(final InputStream is) throws IllegalArgumentException {
	MyObjects.requireNonNull(is, "is");
	try {
	    return clazz.cast(unmarshaller.unmarshal(is));
	} catch (final UnmarshalException e) {
	    throw new IllegalArgumentException("Can't deserialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while de-serializing document", e);
	}
    }

    public T deserializeFrom(final Reader reader) throws IllegalArgumentException {
	MyObjects.requireNonNull(reader, "reader");
	try {
	    return clazz.cast(unmarshaller.unmarshal(reader));
	} catch (final UnmarshalException e) {
	    throw new IllegalArgumentException("Can't deserialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while de-serializing document", e);
	}
    }

    public T deserializeFrom(final String rawXml) throws IllegalArgumentException {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return deserializeFrom(new StringReader(rawXml));
    }

    public Optional<String> optionalSerializeToString(final T document) {
	try {
	    return MyOptionals.of(serializeToString(document));
	} catch (final IllegalArgumentException e) {
	    return Optional.empty();
	}
    }

    public String serializeToString(final T document) throws IllegalArgumentException {
	MyObjects.requireNonNull(document, "document");
	final StringWriter output = new StringWriter();
	try {
	    marshaller.marshal(document, output);
	} catch (final MarshalException e) {
	    throw new IllegalArgumentException("Can't serialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while serializing document", e);
	}
	return output.toString();
    }

    public byte[] serializeToBytes(final T document) {
	MyObjects.requireNonNull(document, "document");
	final ByteArrayOutputStream output = new ByteArrayOutputStream();
	try {
	    marshaller.marshal(document, output);
	} catch (final MarshalException e) {
	    throw new IllegalArgumentException("Can't serialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while serializing document", e);
	}
	return output.toByteArray();
    }

    public void serializeTo(final T document, final Writer output) {
	MyObjects.requireNonNull(document, "document");
	try {
	    marshaller.marshal(document, output);
	} catch (final MarshalException e) {
	    throw new IllegalArgumentException("Can't serialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while serializing document", e);
	}
    }

    public void serializeTo(final T document, final OutputStream output) {
	MyObjects.requireNonNull(document, "document");
	try {
	    marshaller.marshal(document, output);
	} catch (final MarshalException e) {
	    throw new IllegalArgumentException("Can't serialize document", e);
	} catch (JAXBException | IllegalArgumentException e) {
	    throw new RuntimeException("Error while serializing document", e);
	}
    }
}