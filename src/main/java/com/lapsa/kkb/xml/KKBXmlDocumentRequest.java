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
public class KKBXmlDocumentRequest implements Serializable {
    private static final long serialVersionUID = -731496654503607621L;

    @XmlElementRef
    private KKBXmlMerchant merchant;

    @XmlElementRef
    private KKBXmlMerchantSign merchantSign;

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
