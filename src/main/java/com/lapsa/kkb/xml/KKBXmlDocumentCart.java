package com.lapsa.kkb.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class KKBXmlDocumentCart implements Serializable {
    private static final long serialVersionUID = 3608474540394112006L;

    @XmlElementRef
    private List<KKBXmlItem> items;

    // GENERATED

    public List<KKBXmlItem> getItems() {
	return items;
    }

    public void setItems(List<KKBXmlItem> items) {
	this.items = items;
    }
}
