package com.lapsa.kkb.xml;

import java.io.Serializable;
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

import com.lapsa.kkb.xml.adapter.KKBTimestampXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "results")
public class KKBXmlResults implements Serializable {
    private static final long serialVersionUID = 6908878974430643451L;

    // timestamp - время проведения платежа
    @XmlAttribute(name = "timestamp")
    @XmlJavaTypeAdapter(value = KKBTimestampXmlAdapter.class)
    private Date timestamp;

    @XmlElementRef
    private List<KKBXmlPayment> payments;

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
