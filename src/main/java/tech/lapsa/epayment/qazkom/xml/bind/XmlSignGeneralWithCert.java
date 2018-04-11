package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(5)
public class XmlSignGeneralWithCert extends XmlSignGeneral {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "cert_id")
    @XmlJavaTypeAdapter(XmlCertificateSeriaNumberToHEXStringAdapter.class)
    private final BigInteger certificateSerialNumber;

    public BigInteger getCertificateSerialNumber() {
	return certificateSerialNumber;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlSignGeneralWithCert() {
	super();
	this.certificateSerialNumber = null;
    }

    public XmlSignGeneralWithCert(final XmlSignType signType,
	    final byte[] signature,
	    final BigInteger certificateSerialNumber) {
	super(signType, signature);
	this.certificateSerialNumber = certificateSerialNumber;
    }

    public XmlSignGeneralWithCert(final XmlSignType signType,
	    final String signatureEncoded,
	    final BigInteger certificateSerialNumber) {
	super(signType, signatureEncoded);
	this.certificateSerialNumber = certificateSerialNumber;
    }

    public XmlSignGeneralWithCert(final XmlSignType signType,
	    final BigInteger certificateSerialNumber) {
	super(signType);
	this.certificateSerialNumber = certificateSerialNumber;
    }

}
