package com.lapsa.kkb.xml;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.lapsa.kkb.xml.adapter.KKBCertificateSeriaNumberToHEXStringXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "bank_sign")
public class KKBXmlBankSign extends KKBXmlGenericSign {
    private static final long serialVersionUID = -4925501165429637554L;
    private static final int PRIME =  5;
    private static final int MULTIPLIER = 5;

    // cert_id - серийный номер сертификата
    @XmlAttribute(name = "cert_id")
    @XmlJavaTypeAdapter(KKBCertificateSeriaNumberToHEXStringXmlAdapter.class)
    private BigInteger certificateSerialNumber;

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
	KKBXmlBankSign that = (KKBXmlBankSign) other;
	return new EqualsBuilder()
		.appendSuper(super.equals(that))
		.append(certificateSerialNumber, that.certificateSerialNumber)
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(getPrime(), getMultiplier())
		.appendSuper(super.hashCode())
		.append(certificateSerialNumber)
		.toHashCode();
    }

    // GENERATED

    public BigInteger getCertificateSerialNumber() {
	return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(BigInteger certificateSerialNumber) {
	this.certificateSerialNumber = certificateSerialNumber;
    }
}
