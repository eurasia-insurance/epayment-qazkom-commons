package com.lapsa.kkb.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class KKBXmlDocumentResponse implements Serializable {
    private static final long serialVersionUID = -4584256521781984693L;

    @XmlElementRef
    private KKBXmlBank bank;

    @XmlElementRef
    private KKBXmlBankSign bankSign;

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	KKBXmlDocumentResponse that = (KKBXmlDocumentResponse) other;
	return new EqualsBuilder()
		.append(bank, that.bank)
		.append(bankSign, that.bankSign)
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(17, 37)
		.append(bank.hashCode())
		.append(bankSign.hashCode())
		.toHashCode();
    }

    // GENERATED

    public KKBXmlBank getBank() {
	return bank;
    }

    public void setBank(KKBXmlBank bank) {
	this.bank = bank;
    }

    public KKBXmlBankSign getBankSign() {
	return bankSign;
    }

    public void setBankSign(KKBXmlBankSign bankSign) {
	this.bankSign = bankSign;
    }
}
