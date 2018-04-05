package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(43)
public class XmlItem extends AXmlAmount {

    private static final long serialVersionUID = 1L;

    // item number= необходимо перечислить все пункты корзины
    @XmlAttribute(name = "number")
    private final Integer number;

    public Integer getNumber() {
	return number;
    }

    // name -Наименование товара/услуги
    @XmlAttribute(name = "name")
    private final String name;

    public String getName() {
	return name;
    }

    // quantity - количество
    @XmlAttribute(name = "quantity")
    private final Integer quantity;

    public Integer getQuantity() {
	return quantity;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlItem() {
	super(null);
	this.number = null;
	this.name = null;
	this.quantity = null;
    }

    public XmlItem(Double amount, int number, String name, int quantity) {
	super(amount);
	this.number = number;
	this.name = name;
	this.quantity = quantity;
    }
}
