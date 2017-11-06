package tech.lapsa.qazkom.xml.bind;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "merchant")
public class XmlMerchant extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final XmlDocumentTool<XmlMerchant> TOOL = XmlDocumentTool.forClass(XmlMerchant.class);

    public static final XmlDocumentTool<XmlMerchant> getTool() {
	return TOOL;
    }

    public static XmlMerchant of(final String rawXml) {
	return TOOL.deserializeFrom(rawXml);
    }

    @Override
    protected int prime() {
	return 47;
    }

    // cert_id - Серийный номер сертификата
    @XmlAttribute(name = "cert_id")
    @XmlJavaTypeAdapter(XmlCertificateSeriaNumberToHEXStringAdapter.class)
    private BigInteger certificateSerialNumber;

    // name - имя магазина(сайта)
    @XmlAttribute(name = "name")
    private String name;

    @XmlElementRef
    private XmlOrder order;

    public BigInteger getCertificateSerialNumber() {
	return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(final BigInteger certificateSerialNumber) {
	this.certificateSerialNumber = certificateSerialNumber;
    }

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public XmlOrder getOrder() {
	return order;
    }

    public void setOrder(final XmlOrder order) {
	this.order = order;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }
}
