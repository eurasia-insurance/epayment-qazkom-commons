package com.lapsa.kkb.xml;

import java.io.Serializable;
import java.util.Base64;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class KKBXmlGenericSign implements Serializable {
    private static final long serialVersionUID = -2302481811822001881L;

    @XmlAttribute(name = "type")
    private KKBXmlSignType signType;

    @XmlValue
    private byte[] signature;

    protected abstract int getPrime();

    protected abstract int getMultiplier();

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(getPrime(), getMultiplier(), this, false);
    }

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	return EqualsBuilder.reflectionEquals(this, other, false);
    }

    // GENERATED

    // type - тип подписи
    public KKBXmlSignType getSignType() {
	return signType;
    }

    public void setSignType(KKBXmlSignType signType) {
	this.signType = signType;
    }

    // подпись
    public byte[] getSignature() {
	return signature;
    }

    public void setSignature(byte[] signature) {
	this.signature = (signature != null && signature.length == 0) ? null : signature;
    }

    public String getSignatureEncoded() {
	return Base64.getEncoder().encodeToString(signature);
    }

    public void setSignatureEncoded(String signatureEncoded) {
	this.signature = Base64.getDecoder().decode(signatureEncoded);
    }
}
