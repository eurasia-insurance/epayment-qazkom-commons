package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "item")
public class XmlItem extends AXmlAmount {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 43;

    @Override
    protected int prime() {
	return PRIME;
    }

    // item number= необходимо перечислить все пункты корзины
    @XmlAttribute(name = "number")
    private int number;

    // name -Наименование товара/услуги
    @XmlAttribute(name = "name")
    private String name;

    // quantity - количество
    @XmlAttribute(name = "quantity")
    private int quantity;

    // GENERATED

    public int getNumber() {
	return number;
    }

    public void setNumber(final int number) {
	this.number = number;
    }

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public int getQuantity() {
	return quantity;
    }

    public void setQuantity(final int quantity) {
	this.quantity = quantity;
    }
}
