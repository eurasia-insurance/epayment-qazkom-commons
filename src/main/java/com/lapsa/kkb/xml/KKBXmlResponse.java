package com.lapsa.kkb.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "response")
public class KKBXmlResponse implements Serializable {
    private static final long serialVersionUID = -4672326400778882641L;

    // order_id - номер заказа
    @XmlAttribute(name = "order_id")
    private String orderId;

    @XmlElementRef
    private KKBXmlError error;

    @XmlElementRef
    private KKBXmlSession session;

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	KKBXmlResponse that = (KKBXmlResponse) other;
	return new EqualsBuilder()
		.append(orderId, that.orderId)
		.append(error, that.error)
		.append(session, that.session)
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(19, 39)
		.append(orderId)
		.append(error)
		.append(session)
		.toHashCode();
    }

    // GENERATED

    public String getOrderId() {
	return orderId;
    }

    public void setOrderId(String orderId) {
	this.orderId = orderId;
    }

    public KKBXmlError getError() {
	return error;
    }

    public void setError(KKBXmlError error) {
	this.error = error;
    }

    public KKBXmlSession getSession() {
	return session;
    }

    public void setSession(KKBXmlSession session) {
	this.session = session;
    }

}
