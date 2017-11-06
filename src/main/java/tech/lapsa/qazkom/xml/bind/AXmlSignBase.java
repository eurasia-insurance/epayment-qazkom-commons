package tech.lapsa.qazkom.xml.bind;

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
public abstract class AXmlSign implements Serializable {

    private static final long serialVersionUID = 1L;

    protected abstract int prime();

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(prime(), prime(), this, false);
    }

    @Override
    public boolean equals(final Object other) {
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

    public void setSignType(final XmlSignType signType) {
	this.signType = signType;
    }

    // подпись
    public byte[] getSignature() {
	return signature;
    }

    public void setSignature(final byte[] signature) {
	this.signature = signature != null && signature.length == 0 ? null : signature;
    }

    public String getSignatureEncoded() {
	return Base64.getEncoder().encodeToString(signature);
    }

    public void setSignatureEncoded(final String signatureEncoded) {
	signature = Base64.getDecoder().decode(signatureEncoded);
    }
}
