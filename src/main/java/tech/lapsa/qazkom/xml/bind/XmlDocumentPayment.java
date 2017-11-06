package tech.lapsa.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.qazkom.xml.schema.XmlSchemas;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class XmlDocumentPayment extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final XmlDocumentTool<XmlDocumentPayment> TOOL = XmlDocumentTool.forClass(XmlDocumentPayment.class,
	    XmlSchemas.PAYMENT_SCHEMA);

    public static final XmlDocumentTool<XmlDocumentPayment> getTool() {
	return TOOL;
    }

    public static XmlDocumentPayment of(final String rawXml) {
	return TOOL.deserializeFrom(rawXml);
    }

    @Override
    protected int prime() {
	return 37;
    }

    @XmlElementRef
    private XmlBank bank;

    @XmlElementRef
    private XmlBankSign bankSign;

    public XmlBank getBank() {
	return bank;
    }

    public void setBank(final XmlBank bank) {
	this.bank = bank;
    }

    public XmlBankSign getBankSign() {
	return bankSign;
    }

    public void setBankSign(final XmlBankSign bankSign) {
	this.bankSign = bankSign;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }
}
