package tech.lapsa.epayment.qazkom.xml.bind;

import java.util.Currency;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.java.jaxb.adapter.XmlCurrencyNumericAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class AXmlAmountAndCurrency extends AXmlAmount {
    private static final long serialVersionUID = 1L;

    // currency - код валюты оплаты [ 398 - тенге ]
    @XmlAttribute(name = "currency")
    @XmlJavaTypeAdapter(XmlCurrencyNumericAdapter.class)
    private Currency currency;

    public Currency getCurrency() {
	return currency;
    }

    public void setCurrency(final Currency currency) {
	this.currency = currency;
    }
}