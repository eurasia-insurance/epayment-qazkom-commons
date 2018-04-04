package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(17)
public class XmlCustomer extends AXmlBase {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "name")
    private final String name;

    public String getName() {
	return name;
    }

    @XmlAttribute(name = "mail")
    private final String emailAddress;

    public String getEmailAddress() {
	return emailAddress;
    }

    @XmlAttribute(name = "phone")
    private final String phone;

    public String getPhone() {
	return phone;
    }

    @XmlElement(name = "merchant")
    private final XmlMerchant sourceMerchant;

    public XmlMerchant getSourceMerchant() {
	return sourceMerchant;
    }

    @XmlElement(name = "merchant_sign")
    private final XmlMerchantSign sourceMerchantSign;

    public XmlMerchantSign getSourceMerchantSign() {
	return sourceMerchantSign;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlCustomer() {
	this.name = null;
	this.emailAddress = null;
	this.phone = null;
	this.sourceMerchant = null;
	this.sourceMerchantSign = null;
    }

    public XmlCustomer(final String name, 
	    final String emailAddress, 
	    final String phone, 
	    final XmlMerchant sourceMerchant,
	    final XmlMerchantSign sourceMerchantSign) {
	super();
	this.name = name;
	this.emailAddress = emailAddress;
	this.phone = phone;
	this.sourceMerchant = sourceMerchant;
	this.sourceMerchantSign = sourceMerchantSign;
    }
}
