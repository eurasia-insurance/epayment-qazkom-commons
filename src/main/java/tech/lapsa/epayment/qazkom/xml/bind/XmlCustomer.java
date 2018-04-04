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
@XmlRootElement(name = "customer")
@HashCodePrime(17)
public class XmlCustomer extends AXmlBase {

    private static final long serialVersionUID = 1L;

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

    public void setName(final String name) {
	this.name = name;
    }

    public String getEmailAddress() {
	return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
	this.emailAddress = emailAddress;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(final String phone) {
	this.phone = phone;
    }

    public XmlMerchant getSourceMerchant() {
	return sourceMerchant;
    }

    public void setSourceMerchant(final XmlMerchant sourceMerchant) {
	this.sourceMerchant = sourceMerchant;
    }

    public XmlMerchantSign getSourceMerchantSign() {
	return sourceMerchantSign;
    }

    public void setSourceMerchantSign(final XmlMerchantSign sourceMerchantSign) {
	this.sourceMerchantSign = sourceMerchantSign;
    }

}
