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
@XmlRootElement(name = "item")
public class KKBXmlItem extends KKBXmlGenericAmount implements Serializable {
    private static final long serialVersionUID = -5095655869788179295L;

    // item number= необходимо перечислить все пункты корзины
    @XmlAttribute(name = "number")
    private int number;

    // name -Наименование товара/услуги
    @XmlAttribute(name = "name")
    private String name;

    // quantity - количество
    @XmlAttribute(name = "quantity")
    private int quantity;

    // GENERATED

    public int getNumber() {
	return number;
    }

    public void setNumber(int number) {
	this.number = number;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getQuantity() {
	return quantity;
    }

    public void setQuantity(int quantity) {
	this.quantity = quantity;
    }
}
