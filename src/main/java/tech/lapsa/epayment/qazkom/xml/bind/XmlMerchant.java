package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;
import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "merchant")
@HashCodePrime(47)
public class XmlMerchant extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlMerchant> TOOL = SerializationTool.forClass(XmlMerchant.class,
	    XmlSchemas.ORDER_SCHEMA);

    public static final SerializationTool<XmlMerchant> getTool() {
	return TOOL;
    }

    public static XmlMerchant of(final String rawXml) {
	return TOOL.deserializeFrom(rawXml);
    }

    // cert_id - Серийный номер сертификата
    @XmlAttribute(name = "cert_id")
    @XmlJavaTypeAdapter(XmlCertificateSeriaNumberToHEXStringAdapter.class)
    private final BigInteger certificateSerialNumber;

    public BigInteger getCertificateSerialNumber() {
	return certificateSerialNumber;
    }

    // name - имя магазина(сайта)
    @XmlAttribute(name = "name")
    private final String name;

    public String getName() {
	return name;
    }

    @XmlElement(name = "order")
    private final XmlOrder order;

    public XmlOrder getOrder() {
	return order;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlMerchant() {
	this.certificateSerialNumber = null;
	this.name = null;
	this.order = null;
    }

    public XmlMerchant(BigInteger certificateSerialNumber, String name, XmlOrder order) {
	super();
	this.certificateSerialNumber = certificateSerialNumber;
	this.name = name;
	this.order = order;
    }
}
