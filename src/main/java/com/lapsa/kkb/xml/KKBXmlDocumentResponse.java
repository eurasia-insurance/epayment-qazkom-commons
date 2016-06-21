package com.lapsa.kkb.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class KKBXmlDocumentResponse implements Serializable {
    private static final long serialVersionUID = -4584256521781984693L;

    @XmlElementRef
    private KKBXmlBank bank;

    @XmlElementRef
    private KKBXmlBankSign bankSign;

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
