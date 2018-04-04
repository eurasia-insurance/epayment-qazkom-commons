package tech.lapsa.epayment.qazkom.xml.bind;

import java.util.Currency;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.java.jaxb.adapter.XmlCurrencyNumericAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public abstract class AXmlAmountAndCurrency extends AXmlAmount {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "currency")
    @XmlJavaTypeAdapter(XmlCurrencyNumericAdapter.class)
    private final Currency currency;

    public Currency getCurrency() {
	return currency;
    }

    protected AXmlAmountAndCurrency(Double amount, Currency currency) {
	super(amount);
	this.currency = currency;
    }
}