package tech.lapsa.epayment.qazkom.xml.bind;

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
public class XmlBank extends AXmlBase {

    private static final long serialVersionUID = -5468834860872828233L;
    private static final int PRIME = 11;

    @Override
    protected int prime() {
	return PRIME;
    }

    @XmlAttribute(name = "name")
    private String name;

    @XmlElementRef
    private XmlCustomer customer;

    @XmlElementRef
    private XmlCustomerSign customerSign;

    @XmlElementRef
    private XmlResults results;

    // GENERATED

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public XmlCustomer getCustomer() {
	return customer;
    }

    public void setCustomer(final XmlCustomer customer) {
	this.customer = customer;
    }

    public XmlCustomerSign getCustomerSign() {
	return customerSign;
    }

    public void setCustomerSign(final XmlCustomerSign customerSign) {
	this.customerSign = customerSign;
    }

    public XmlResults getResults() {
	return results;
    }

    public void setResults(final XmlResults results) {
	this.results = results;
    }
}
