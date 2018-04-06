package tech.lapsa.epayment.qazkom.xml.bind;

import java.io.Serializable;
import java.util.Base64;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public abstract class AXmlSignBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
	return HcEqUtil.hashCode(this);
    }

    @Override
    public boolean equals(final Object other) {
	return HcEqUtil.equals(this, other);
    }

    @XmlAttribute(name = "type")
    private final XmlSignType signType;

    public XmlSignType getSignType() {
	return signType;
    }

    @XmlValue
    private final byte[] signature;

    // подпись
    public byte[] getSignature() {
	return signature.clone();
    }

    public String getSignatureEncoded() {
	return Base64.getEncoder().encodeToString(signature);
    }

    protected AXmlSignBase(final XmlSignType signType) {
	super();
	this.signType = signType;
	this.signature = null;
    }

    protected AXmlSignBase(final XmlSignType signType,
	    final byte[] signature) {
	super();
	this.signType = signType;
	this.signature = signature;
    }

    protected AXmlSignBase(final XmlSignType signType,
	    final String signatureEncoded) {
	super();
	this.signType = signType;
	this.signature = Base64.getDecoder().decode(signatureEncoded);
    }

}
