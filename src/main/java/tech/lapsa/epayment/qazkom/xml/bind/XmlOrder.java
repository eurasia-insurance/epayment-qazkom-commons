package tech.lapsa.epayment.qazkom.xml.bind;

import java.util.Currency;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import tech.lapsa.java.commons.function.MyCollections;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(49)
public class XmlOrder extends AXmlAmountAndCurrency {

    private static final long serialVersionUID = 1L;

    // order_id - Номер заказа(должен состоять не менее чем из 6 ЧИСЛОВЫХ
    // знаков, максимально -15)
    @XmlAttribute(name = "order_id")
    private final String orderId;

    public String getOrderId() {
	return orderId;
    }

    @XmlElement(name = "department")
    private final List<XmlDepartment> departments;

    public List<XmlDepartment> getDepartments() {
	return departments;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlOrder() {
	super(null, null);
	this.orderId = null;
	this.departments = null;
    }

    public XmlOrder(Double amount, Currency currency, String orderId, List<XmlDepartment> departments) {
	super(amount, currency);
	this.orderId = orderId;
	this.departments = departments == null ? null : MyCollections.unmodifiableOrEmptyList(departments);
    }
}
