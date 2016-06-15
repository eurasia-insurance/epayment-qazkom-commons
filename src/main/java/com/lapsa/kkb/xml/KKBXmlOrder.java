package com.lapsa.kkb.xml;

import java.io.Serializable;
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
public class KKBXmlOrder extends KKBXmlGenericAmountAndCurrency implements Serializable {

    private static final long serialVersionUID = -2920050474132203303L;

    // order_id - Номер заказа(должен состоять не менее чем из 6 ЧИСЛОВЫХ
    // знаков, максимально -15)
    @XmlAttribute(name = "order_id")
    private String orderId;

    @XmlElementRef
    private List<KKBXmlDepartment> departments;

    // GENERATED

    public String getOrderId() {
	return orderId;
    }

    public void setOrderId(String orderId) {
	this.orderId = orderId;
    }

    public List<KKBXmlDepartment> getDepartments() {
	return departments;
    }

    public void setDepartments(List<KKBXmlDepartment> departments) {
	this.departments = departments;
    }
}
