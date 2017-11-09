package tech.lapsa.epayment.qazkom.xml.bind;

import java.util.Optional;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MyDigests;
import tech.lapsa.java.commons.security.MySignatures;

@XmlEnum
public enum XmlSignType {
    // Если Тип подписи RSA то это цифровая подпись
    @XmlEnumValue(value = "RSA") RSA("RSA", null),

    @XmlEnumValue(value = "SHA/RSA") SHA_RSA("SHA1withRSA", null),
    @XmlEnumValue(value = "SHA") SHA(null, "SHA1"),

    // Если Тип подписи SSL то это серийный номер сертификата
    @XmlEnumValue(value = "SSL") CERTIFICATE,

    // Если Тип подписи none то поле остается пустым
    @XmlEnumValue(value = "none") NONE;

    private final Optional<String> signatureAlgorithmName;
    private final Optional<String> digestAlgorithmName;

    private XmlSignType(String signatureAlgorithmName, String digestAlgorithmName) {
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
	this.signatureAlgorithmName = Optional.empty();
	this.digestAlgorithmName = Optional.empty();
    }

    public Optional<String> getSignatureAlgorithmName() {
	return signatureAlgorithmName;
    }

    public Optional<String> getDigestAlgorithmName() {
	return digestAlgorithmName;
    }

}
