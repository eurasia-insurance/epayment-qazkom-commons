package com.lapsa.kkb.xml;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "response")
public class KKBXmlResponse extends KKBXmlBase {
    private static final long serialVersionUID = -4672326400778882641L;
    private static final int PRIME = 53;
    private static final int MULTIPLIER = 53;

    // order_id - номер заказа
    @XmlAttribute(name = "order_id")
    private String orderId;

    @XmlElementRef
    private KKBXmlError error;

    @XmlElementRef
    private KKBXmlSession session;

    @Override
    protected int getPrime() {
	return PRIME;
    }

    @Override
    protected int getMultiplier() {
	return MULTIPLIER;
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
