package tech.lapsa.qazkom.xml.mapping;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "customer")
public class XmlCustomer extends AXmlBase {

    private static final long serialVersionUID = 1L;

    public XmlCustomer() {
	super(17);
    }

    // Имя покупателя указанное в системе авторизации
    @XmlAttribute(name = "name")
    private String name;

    // Email адрес покупателя указанный в системе авторизации
    @XmlAttribute(name = "mail")
    private String emailAddress;

    // Телефон покупателя указанный в системе авторизации
    @XmlAttribute(name = "phone")
    private String phone;

    // исходный запрос
    @XmlElementRef
    private XmlMerchant sourceMerchant;

    // подпись исходного запроса
    @XmlElementRef
    private XmlMerchantSign sourceMerchantSign;

    // GENERATED

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getEmailAddress() {
	return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
	this.emailAddress = emailAddress;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public XmlMerchant getSourceMerchant() {
	return sourceMerchant;
    }

    public void setSourceMerchant(XmlMerchant sourceMerchant) {
	this.sourceMerchant = sourceMerchant;
    }

    public XmlMerchantSign getSourceMerchantSign() {
	return sourceMerchantSign;
    }

    public void setSourceMerchantSign(XmlMerchantSign sourceMerchantSign) {
	this.sourceMerchantSign = sourceMerchantSign;
    }

}
