package com.lapsa.kkb.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public abstract class KKBGenericService {
    protected Logger logger = Logger.getLogger(this.getClass().getName());

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
