package com.lapsa.kkb.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.lapsa.kkb.xml.adapter.KKBAmountXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class KKBXmlGenericAmount implements Serializable {
    private static final long serialVersionUID = 2154576400016078128L;

    // amount - сумма заказа,
    @XmlAttribute(name = "amount")
    @XmlJavaTypeAdapter(KKBAmountXmlAdapter.class)
    private Double amount;

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	KKBXmlGenericAmount that = (KKBXmlGenericAmount) other;
	return new EqualsBuilder()
		.append(amount, that.amount)
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(23, 11)
		.append(amount)
		.toHashCode();
    }

    // GENERATED

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }
}