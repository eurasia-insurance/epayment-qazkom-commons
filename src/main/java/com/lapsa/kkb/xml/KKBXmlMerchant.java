package com.lapsa.kkb.xml;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.lapsa.kkb.xml.adapter.KKBCertificateSeriaNumberToHEXStringXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "merchant")
public class KKBXmlMerchant extends KKBXmlBase {
    private static final long serialVersionUID = -671497891323516467L;
    private static final int PRIME = 37;
    private static final int MULTIPLIER = 37;

    // cert_id - Серийный номер сертификата
    @XmlAttribute(name = "cert_id")
    @XmlJavaTypeAdapter(KKBCertificateSeriaNumberToHEXStringXmlAdapter.class)
    private BigInteger certificateSerialNumber;

    // name - имя магазина(сайта)
    @XmlAttribute(name = "name")
    private String name;

    @XmlElementRef
    private KKBXmlOrder order;

    @Override
    protected int getPrime() {
	return PRIME;
    }

    @Override
    protected int getMultiplier() {
	return MULTIPLIER;
    }

    // GENERATED

    public BigInteger getCertificateSerialNumber() {
	return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(BigInteger certificateSerialNumber) {
	this.certificateSerialNumber = certificateSerialNumber;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public KKBXmlOrder getOrder() {
	return order;
    }

    public void setOrder(KKBXmlOrder order) {
	this.order = order;
    }

}
