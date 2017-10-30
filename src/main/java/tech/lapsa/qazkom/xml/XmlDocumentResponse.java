package com.lapsa.kkb.xml;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class KKBXmlDocumentResponse extends KKBXmlBase {
    private static final long serialVersionUID = -4584256521781984693L;
    private static final int PRIME = 23;
    private static final int MULTIPLIER = 23;

    @XmlElementRef
    private KKBXmlBank bank;

    @XmlElementRef
    private KKBXmlBankSign bankSign;

    @Override
    protected int getPrime() {
	return PRIME;
    }

    @Override
    protected int getMultiplier() {
	return MULTIPLIER;
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
