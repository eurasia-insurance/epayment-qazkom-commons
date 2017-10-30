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
public class XmlDocumentPayment extends AXmlBase {

    private static final long serialVersionUID = 1L;

    public XmlDocumentPayment() {
	super(37);
    }

    @XmlElementRef
    private XmlBank bank;

    @XmlElementRef
    private XmlBankSign bankSign;

    // GENERATED

    public XmlBank getBank() {
	return bank;
    }

    public void setBank(XmlBank bank) {
	this.bank = bank;
    }

    public XmlBankSign getBankSign() {
	return bankSign;
    }

    public void setBankSign(XmlBankSign bankSign) {
	this.bankSign = bankSign;
    }
}
