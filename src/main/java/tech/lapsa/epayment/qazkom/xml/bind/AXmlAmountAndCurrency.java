package tech.lapsa.qazkom.xml.bind;

import java.util.Currency;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.lapsa.fin.FinCurrency;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class AXmlAmountAndCurrency extends AXmlAmount {
    private static final long serialVersionUID = 1L;

    // currency - код валюты оплаты [ 398 - тенге ]
    @XmlAttribute(name = "currency")
    private int currencyCode;

    public int getCurrencyCode() {
	return currencyCode;
    }

    public void setCurrencyCode(final int currencyCode) {
	this.currencyCode = currencyCode;
    }

    public Currency getCurrency() {
	for (final Currency c : Currency.getAvailableCurrencies())
	    if (c.getNumericCode() == currencyCode)
		return c;
	return null;
    }

    public void setCurrency(final Currency currency) {
	currencyCode = currency == null ? 0 : currency.getNumericCode();
    }

    public FinCurrency getFinCurrency() {
	return FinCurrency.byNumericCode(currencyCode);
    }

    public void setFinCurrency(final FinCurrency finCurrency) {
	currencyCode = finCurrency == null ? 0 : finCurrency.getCurrency().getNumericCode();
    }
}