package com.lapsa.kkb.services.impl.composers;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.impl.KKBGenericService;

public abstract class BaseDocumentHelper {

    protected String generateXML(Object document, Marshaller marshaller) throws KKBServiceError {
	try {
	    StringWriter output = new StringWriter();
	    marshaller.marshal(document, output);
	    return output.toString();
	} catch (JAXBException e) {
	    throw new KKBServiceError(e);
	}
    }

    protected Schema loadSchemaFromResource(String resourceName) throws SAXException, IOException {
	try (InputStream is = KKBGenericService.class
		.getResourceAsStream(resourceName)) {
	    Source source = new StreamSource(is);
	    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    Schema schema = schemaFactory.newSchema(source);
	    return schema;
	}
    }
}
