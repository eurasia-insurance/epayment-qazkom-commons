package com.lapsa.kkb.xml;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.lapsa.kkb.xml.adapter.KKBTimestampXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "results")
public class KKBXmlResults extends KKBXmlBase {
    private static final long serialVersionUID = 6908878974430643451L;
    private static final int PRIME = 59;
    private static final int MULTIPLIER = 59;

    // timestamp - время проведения платежа
    @XmlAttribute(name = "timestamp")
    @XmlJavaTypeAdapter(value = KKBTimestampXmlAdapter.class)
    private Date timestamp;

    @XmlElementRef
    private List<KKBXmlPayment> payments;

    @Override
    protected int getPrime() {
	return PRIME;
    }

    @Override
    protected int getMultiplier() {
	return MULTIPLIER;
    }

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	KKBXmlResults that = (KKBXmlResults) other;
	return new EqualsBuilder()
		.append(timestamp, that.timestamp)
		.append(payments, that.payments)
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(getPrime(), getMultiplier())
		.append(timestamp)
		.append(payments)
		.toHashCode();
    }

    // GENERATED

    public Date getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    public List<KKBXmlPayment> getPayments() {
	return payments;
    }

    public void setPayments(List<KKBXmlPayment> payments) {
	this.payments = payments;
    }

}
