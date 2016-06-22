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
@XmlRootElement(name = "bank")
public class KKBXmlBank extends KKBXmlBase {
    private static final long serialVersionUID = -5468834860872828233L;
    private static final int PRIME = 3;
    private static final int MULTIPLIER = 3;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElementRef
    private KKBXmlCustomer customer;

    @XmlElementRef
    private KKBXmlCustomerSign customerSign;

    @XmlElementRef
    private KKBXmlResults results;

    @Override
    protected int getPrime() {
	return PRIME;
    }

    @Override
    protected int getMultiplier() {
	return MULTIPLIER;
    }

    // GENERATED

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public KKBXmlCustomer getCustomer() {
	return customer;
    }

    public void setCustomer(KKBXmlCustomer customer) {
	this.customer = customer;
    }

    public KKBXmlCustomerSign getCustomerSign() {
	return customerSign;
    }

    public void setCustomerSign(KKBXmlCustomerSign customerSign) {
	this.customerSign = customerSign;
    }

    public KKBXmlResults getResults() {
	return results;
    }

    public void setResults(KKBXmlResults results) {
	this.results = results;
    }
}
