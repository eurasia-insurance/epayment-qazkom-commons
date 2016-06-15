package com.lapsa.kkb.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "session")
public class KKBXmlSession implements Serializable {

    private static final long serialVersionUID = -5333156242528681085L;

    @XmlAttribute(name = "id")
    private String id;

    // GENERATED

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }
}