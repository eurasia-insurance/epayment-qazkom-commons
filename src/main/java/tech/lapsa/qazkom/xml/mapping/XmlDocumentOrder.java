package tech.lapsa.qazkom.xml.mapping;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class XmlDocumentRequest extends AXmlBase {

    private static final long serialVersionUID = 1L;

    public XmlDocumentRequest() {
	super(31);
    }

    @XmlElementRef
    private XmlMerchant merchant;

    @XmlElementRef
    private XmlMerchantSign merchantSign;

    // GENERATED

    public XmlMerchant getMerchant() {
	return merchant;
    }

    public void setMerchant(XmlMerchant merchant) {
	this.merchant = merchant;
    }

    public XmlMerchantSign getMerchantSign() {
	return merchantSign;
    }

    public void setMerchantSign(XmlMerchantSign merchantSign) {
	this.merchantSign = merchantSign;
    }
}
