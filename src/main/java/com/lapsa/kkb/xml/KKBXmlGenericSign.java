package com.lapsa.kkb.xml;

import java.io.Serializable;
import java.util.Base64;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class KKBXmlGenericSign implements Serializable {

    private static final long serialVersionUID = -2302481811822001881L;

    private KKBXmlSignType signType;

    private byte[] signature;

    // type - тип подписи
    @XmlAttribute(name = "type")
    public KKBXmlSignType getSignType() {
	return signType;
    }

    public void setSignType(KKBXmlSignType signType) {
	this.signType = signType;
    }

    // подпись
    @XmlValue
    public byte[] getSignature() {
	return signature;
    }

    public void setSignature(byte[] signature) {
	this.signature = (signature != null && signature.length == 0) ? null : signature;
    }

    @XmlTransient
    public String getSignatureEncoded() {
	return Base64.getEncoder().encodeToString(signature);
    }

    public void setSignatureEncoded(String signatureEncoded) {
	this.signature = Base64.getDecoder().decode(signatureEncoded);
    }
}
