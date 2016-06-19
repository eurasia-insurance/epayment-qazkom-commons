package com.lapsa.kkb.services.impl.composers;

import java.util.ArrayList;

import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.core.KKBOrderItem;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.impl.KKBXmlDocumentComposer;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlItem;

public class KKBCartDocumentComposer implements KKBXmlDocumentComposer {

    @Override
    public KKBXmlDocument composeXmlDocument(KKBOrder order) throws KKBServiceError {
	KKBXmlDocument xmlDocument = new KKBXmlDocument();
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
	return xmlDocument;
    }

}
