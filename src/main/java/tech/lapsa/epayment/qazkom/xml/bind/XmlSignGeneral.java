package tech.lapsa.epayment.qazkom.xml.bind;

import java.io.Serializable;
import java.util.Base64;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlValue;

import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MyDigests;
import tech.lapsa.java.commons.security.MySignatures;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(3)
public class XmlSignGeneral implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
	return HcEqUtil.hashCode(this);
    }

    @Override
    public boolean equals(final Object other) {
	return HcEqUtil.equals(this, other);
    }

    @XmlEnum
    public enum XmlSignType {

	// Если Тип подписи RSA то это цифровая подпись
	@XmlEnumValue(value = "RSA")
	RSA("SHA1withRSA", null),

	@XmlEnumValue(value = "SHA/RSA")
	SHA_RSA("SHA1withRSA", null),

	@XmlEnumValue(value = "SHA")
	SHA(null, "SHA1"),

	// Если Тип подписи SSL то это серийный номер сертификата
	@XmlEnumValue(value = "SSL")
	CERTIFICATE,

	// Если Тип подписи none то поле остается пустым
	@XmlEnumValue(value = "none")
	NONE;

	private final Optional<String> signatureAlgorithmName;
	private final Optional<String> digestAlgorithmName;

	private XmlSignType(final String signatureAlgorithmName, final String digestAlgorithmName) {
	    if (MyStrings.nonEmpty(signatureAlgorithmName)) {
		MySignatures.ofAlgorithm(signatureAlgorithmName) //
			.orElseThrow(() -> new IllegalArgumentException(
				"Signature algorithm is not supported " + signatureAlgorithmName));
		this.signatureAlgorithmName = Optional.of(signatureAlgorithmName);
	    } else
		this.signatureAlgorithmName = Optional.empty();

	    if (MyStrings.nonEmpty(digestAlgorithmName)) {
		MyDigests.ofAlgorithm(digestAlgorithmName) //
			.orElseThrow(() -> new IllegalArgumentException(
				"Digest algorithm is not supported " + digestAlgorithmName));
		this.digestAlgorithmName = Optional.of(digestAlgorithmName);
	    } else
		this.digestAlgorithmName = Optional.empty();
	}

	private XmlSignType() {
	    signatureAlgorithmName = Optional.empty();
	    digestAlgorithmName = Optional.empty();
	}

	public Optional<String> getSignatureAlgorithmName() {
	    return signatureAlgorithmName;
	}

	public Optional<String> getDigestAlgorithmName() {
	    return digestAlgorithmName;
	}

    }

    @XmlAttribute(name = "type")
    private final XmlSignType signType;

    public XmlSignType getSignType() {
	return signType;
    }

    @XmlValue
    private final byte[] signature;

    // подпись
    public byte[] getSignature() {
	return signature == null ? null : signature.clone();
    }

    public String getSignatureEncoded() {
	return Base64.getEncoder().encodeToString(signature);
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlSignGeneral() {
	super();
	this.signType = null;
	this.signature = null;
    }

    public XmlSignGeneral(final XmlSignType signType) {
	super();
	this.signType = signType;
	this.signature = null;
    }

    public XmlSignGeneral(final XmlSignType signType, final byte[] signature) {
	super();
	this.signType = signType;
	this.signature = signature == null ? null : signature.clone();
    }

    public XmlSignGeneral(final XmlSignType signType, final String signatureEncoded) {
	super();
	this.signType = signType;
	this.signature = signatureEncoded == null ? null : Base64.getDecoder().decode(signatureEncoded);
    }
}
