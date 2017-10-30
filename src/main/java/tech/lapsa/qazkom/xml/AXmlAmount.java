package tech.lapsa.qazkom.xml;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.qazkom.xml.adapter.XmlAmountAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class AXmlAmount extends AXmlBase {

    private static final long serialVersionUID = 1L;

    public AXmlAmount(int prime, int multiplier) {
	super(prime, multiplier);
    }

    public AXmlAmount(int prime) {
	super(prime);
    }
    
    public AXmlAmount() {
	this(3);
    }

    // amount - сумма заказа,
    @XmlAttribute(name = "amount")
    @XmlJavaTypeAdapter(XmlAmountAdapter.class)
    private Double amount;

    // GENERATED

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }
}