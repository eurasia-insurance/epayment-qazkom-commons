package com.lapsa.kkb.xml;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "item")
public class KKBXmlItem extends KKBXmlGenericAmount {
    private static final long serialVersionUID = -5095655869788179295L;
    private static final int PRIME = 31;
    private static final int MULTIPLIER = 31;

    // item number= необходимо перечислить все пункты корзины
    @XmlAttribute(name = "number")
    private int number;

    // name -Наименование товара/услуги
    @XmlAttribute(name = "name")
    private String name;

    // quantity - количество
    @XmlAttribute(name = "quantity")
    private int quantity;

    @Override
    protected int getPrime() {
	return PRIME;
    }

    @Override
    protected int getMultiplier() {
	return MULTIPLIER;
    }

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
