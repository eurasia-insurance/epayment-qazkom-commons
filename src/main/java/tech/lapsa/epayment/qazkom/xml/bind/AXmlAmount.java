package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlAmountAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public abstract class AXmlAmount extends AXmlBase {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "amount")
    @XmlJavaTypeAdapter(XmlAmountAdapter.class)
    private final Double amount;

    public Double getAmount() {
	return amount;
    }

    protected AXmlAmount(Double amount) {
	super();
	this.amount = amount;
    }
}