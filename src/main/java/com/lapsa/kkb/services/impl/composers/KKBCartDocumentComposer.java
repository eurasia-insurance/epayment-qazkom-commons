package com.lapsa.kkb.services.impl.composers;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.xml.sax.SAXException;

import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.core.KKBOrderItem;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.xml.KKBXmlDocumentCart;
import com.lapsa.kkb.xml.KKBXmlItem;

public class KKBCartDocumentComposer extends BaseDocumentHelper implements KKBXmlDocumentComposer {

    private Marshaller marshaller;

    public KKBCartDocumentComposer() throws KKBServiceError {
	try {
	    JAXBContext cartJAXBContext = JAXBContext.newInstance(KKBXmlDocumentCart.class);
	    marshaller = cartJAXBContext.createMarshaller();
	    marshaller.setSchema(loadSchemaFromResource(SCHEMA_CART));
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
	    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
	} catch (JAXBException | SAXException | IOException e) {
	    throw new KKBServiceError(e);
	}
    }

    @Override
    public String composeXmlDocument(KKBOrder order) throws KKBServiceError {
	KKBXmlDocumentCart xmlDocument = new KKBXmlDocumentCart();
	xmlDocument.setItems(new ArrayList<>());
	int i = 1;
	for (KKBOrderItem item : order.getItems()) {
	    KKBXmlItem xmlItem = new KKBXmlItem();
	    xmlDocument.getItems().add(xmlItem);
	    xmlItem.setNumber(i++);
	    xmlItem.setName(item.getName());
	    xmlItem.setQuantity(item.getQuantity());
	    xmlItem.setAmount(item.getCost());
	}
	return generateXML(xmlDocument, marshaller);
    }

}
