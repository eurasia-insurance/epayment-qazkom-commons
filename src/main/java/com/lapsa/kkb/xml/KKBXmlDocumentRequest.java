package com.lapsa.kkb.xml;

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
public class KKBXmlDocumentRequest extends KKBXmlBase {
    private static final long serialVersionUID = -731496654503607621L;
    private static final int PRIME = 19;
    private static final int MULTIPLIER = 19;

    @XmlElementRef
    private KKBXmlMerchant merchant;

    @XmlElementRef
    private KKBXmlMerchantSign merchantSign;

    @Override
    protected int getPrime() {
	return PRIME;
    }

    @Override
    protected int getMultiplier() {
	return MULTIPLIER;
    }

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	KKBXmlDocumentRequest that = (KKBXmlDocumentRequest) other;
	return new EqualsBuilder()
		.append(merchant, that.merchant)
		.append(merchantSign, that.merchantSign)
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(getPrime(), getMultiplier())
		.append(merchant.hashCode())
		.append(merchantSign.hashCode())
		.toHashCode();
    }

    // GENERATED

    public KKBXmlMerchant getMerchant() {
	return merchant;
    }

    public void setMerchant(KKBXmlMerchant merchant) {
	this.merchant = merchant;
    }

    public KKBXmlMerchantSign getMerchantSign() {
	return merchantSign;
    }

    public void setMerchantSign(KKBXmlMerchantSign merchantSign) {
	this.merchantSign = merchantSign;
    }
}
