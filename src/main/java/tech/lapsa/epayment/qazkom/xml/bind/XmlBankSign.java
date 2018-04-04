package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "bank_sign")
@HashCodePrime(13)
public class XmlBankSign extends AXmlSignBase {

    private static final long serialVersionUID = 1L;

    // cert_id - серийный номер сертификата
    @XmlAttribute(name = "cert_id")
    @XmlJavaTypeAdapter(XmlCertificateSeriaNumberToHEXStringAdapter.class)
    private BigInteger certificateSerialNumber;

    // GENERATED

    public BigInteger getCertificateSerialNumber() {
	return certificateSerialNumber;
    }

    public void setCertificateSerialNumber(final BigInteger certificateSerialNumber) {
	this.certificateSerialNumber = certificateSerialNumber;
    }
}
