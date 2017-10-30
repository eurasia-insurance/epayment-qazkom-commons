package tech.lapsa.qazkom.xml;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class AXmlSign implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlTransient
    private final int PRIME;
    
    @XmlTransient
    private final int MULTIPLIER;

    public AXmlSign(int prime, int multiplier) {
	this.PRIME = prime;
	this.MULTIPLIER = multiplier;
    }

    public AXmlSign(int prime) {
	this(prime, prime);
    }

    public AXmlSign() {
	this(7);
    }

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(PRIME, MULTIPLIER, this, false);
    }

    @Override
    public boolean equals(Object other) {
	return EqualsBuilder.reflectionEquals(this, other, false);
    }

    @XmlAttribute(name = "type")
    private XmlSignType signType;

    @XmlValue
    private byte[] signature;

    // GENERATED

    // type - тип подписи
    public XmlSignType getSignType() {
	return signType;
    }

    public void setSignType(XmlSignType signType) {
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
