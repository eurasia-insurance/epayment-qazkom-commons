package tech.lapsa.qazkom.xml.mapping;

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

    public XmlItem() {
	super(43);
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

    public void setNumber(int number) {
	this.number = number;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public int getQuantity() {
	return quantity;
    }

    public void setQuantity(int quantity) {
	this.quantity = quantity;
    }
}
