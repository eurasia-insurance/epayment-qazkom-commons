package tech.lapsa.epayment.qazkom.xml.bind;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "order")
public class XmlOrder extends AXmlAmountAndCurrency {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 49;

    @Override
    protected int prime() {
	return PRIME;
    }

    // order_id - Номер заказа(должен состоять не менее чем из 6 ЧИСЛОВЫХ
    // знаков, максимально -15)
    @XmlAttribute(name = "order_id")
    private String orderId;

    @XmlElementRef
    private List<XmlDepartment> departments;

    // GENERATED

    public String getOrderId() {
	return orderId;
    }

    public void setOrderId(final String orderId) {
	this.orderId = orderId;
    }

    public List<XmlDepartment> getDepartments() {
	return departments;
    }

    public void setDepartments(final List<XmlDepartment> departments) {
	this.departments = departments;
    }
}
