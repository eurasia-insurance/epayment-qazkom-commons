package com.lapsa.kkb.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	KKBXmlItem that = (KKBXmlItem) other;
	return new EqualsBuilder()
		.appendSuper(super.equals(that))
		.append(number, that.number)
		.append(name, that.name)
		.append(quantity, that.quantity)
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(17, 37)
		.appendSuper(super.hashCode())
		.append(number)
		.append(name)
		.append(quantity)
		.toHashCode();
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
