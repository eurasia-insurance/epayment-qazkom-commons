package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.XmlControlRequestDocument.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlRequestDocument.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;
import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(79)
public class XmlControlResponseDocument extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlControlResponseDocument> TOOL = SerializationTool
	    .forClass(XmlControlResponseDocument.class, XmlSchemas.CONTROL_RESPONSE_SCHEMA);

    public static final SerializationTool<XmlControlResponseDocument> getTool() {
	return TOOL;
    }

    public static XmlControlResponseDocument of(final String rawXml) {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    public static XmlControlResonseDocumentBuilder builder() {
	return new XmlControlResonseDocumentBuilder();
    }

    public static final class XmlControlResonseDocumentBuilder {
	private String rawXml;
	private X509Certificate certificate;

	private XmlControlResonseDocumentBuilder() {
	}

	public XmlControlResonseDocumentBuilder ofRawXml(final String rawXml) throws IllegalArgumentException {
	    this.rawXml = MyStrings.requireNonEmpty(rawXml, "rawXml");
	    return this;
	}

	public XmlControlResonseDocumentBuilder withBankCertificate(final X509Certificate certificate)
		throws IllegalArgumentException {
	    this.certificate = MyObjects.requireNonNull(certificate, "certificate");
	    return this;
	}

	public XmlControlResponseDocument build() throws IllegalArgumentException, IllegalStateException {
	    MyStrings.requireNonEmpty(rawXml, "rawXml");
	    final XmlControlResponseDocument document = TOOL.deserializeFrom(rawXml);

	    if (MyObjects.nonNull(certificate)) {

		// checking signature
		{
		    final String[] banks = XmlBank.bankXmlElementsFrom(rawXml);
		    if (banks.length != 1)
			throw new IllegalArgumentException(
				"Failed to parse for single element <bank> from source XML document");
		    final byte[] data = banks[0].getBytes();
		    final byte[] digest = document.getBankSign().getSignature();
		    MyArrays.reverse(digest);
		    final String algorithmName = document.getBankSign().getSignType().getSignatureAlgorithmName() //
			    .orElseThrow(MyExceptions.illegalArgumentSupplier("No such alogithm for signature"));
		    final VerifyingSignature signature = MySignatures.forVerification(certificate, algorithmName) //
			    .orElseThrow(MyExceptions.illegalArgumentSupplier("Algorithm is not supported %1$s",
				    algorithmName));
		    final boolean signatureValid = signature.verify(data, digest);
		    if (!signatureValid)
			throw MyExceptions.illegalStateFormat("Signature is invalid");
		}

		// checking certificate number
		{
		    final boolean certNumberValid = certificate.getSerialNumber()
			    .equals(document.getBankSign().getCertificateSerialNumber());
		    if (!certNumberValid)
			throw MyExceptions.illegalStateFormat("Certificate serial number is not valid");
		}
	    }
	    return document;
	}
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlRootElement(name = "bank")
    @HashCodePrime(-1)
    public static class XmlBank extends AXmlBase {

	private static final long serialVersionUID = 1L;

	private static final SerializationTool<XmlBank> TOOL = SerializationTool.forClass(XmlBank.class,
		XmlSchemas.CONTROL_RESPONSE_SCHEMA);

	private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
	private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE
		| Pattern.DOTALL;
	private static final Pattern BANK_REGEX_PATTERN = Pattern.compile(BANK_REGEX, BANK_REGEX_FLAGS);

	public static String[] bankXmlElementsFrom(final String rawXml) {
	    final Matcher matcher = BANK_REGEX_PATTERN.matcher(rawXml);
	    final Builder<String> sb = Stream.builder();
	    while (matcher.find())
		sb.accept(matcher.group());
	    return sb.build().filter(xml -> {
		try {
		    return TOOL.deserializeFrom(xml) != null;
		} catch (final IllegalArgumentException e) {
		    return false;
		}

	    }).toArray(String[]::new);
	}

	@XmlAttribute(name = "name")
	private final String name;

	public String getName() {
	    return name;
	}

	@XmlElement(name = "merchant")
	private final XmlMerchant merchant;

	public XmlMerchant getMerchant() {
	    return merchant;
	}

	@XmlElement(name = "merchant_sign")
	private final XmlMerchantSign merchantSign;

	public XmlMerchantSign getMerchantSign() {
	    return merchantSign;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(-1)
	public static class XmlResponse extends AXmlBase {

	    private static final long serialVersionUID = 1L;

	    @XmlAttribute(name = "code")
	    private final String code;

	    public String getCode() {
		return code;
	    }

	    @XmlAttribute(name = "message")
	    private final String message;

	    public String getMessage() {
		return message;
	    }

	    @XmlAttribute(name = "remaining_amount")
	    private final Double remainingAmount;

	    public Double getRemainingAmount() {
		return remainingAmount;
	    }

	    @XmlAttribute(name = "SessionID")
	    private final String sessionId;

	    public String getSessionId() {
		return sessionId;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlResponse() {
		super();
		this.code = null;
		this.message = null;
		this.remainingAmount = null;
		this.sessionId = null;
	    }

	    public XmlResponse(String code, String message, Double remainingAmount, String sessionId) {
		super();
		this.code = code;
		this.message = message;
		this.remainingAmount = remainingAmount;
		this.sessionId = sessionId;
	    }
	}

	@XmlElement(name = "response")
	private final XmlResponse response;

	public XmlResponse getResponse() {
	    return response;
	}

	/*
	 * Default no-args constructor due to JAXB requirements
	 */
	@Deprecated
	public XmlBank() {
	    super();
	    this.name = null;
	    this.merchant = null;
	    this.merchantSign = null;
	    this.response = null;
	}

	public XmlBank(String name, XmlMerchant merchant, XmlMerchantSign merchantSign, XmlResponse response) {
	    super();
	    this.name = name;
	    this.merchant = merchant;
	    this.merchantSign = merchantSign;
	    this.response = response;
	}
    }

    @XmlElement(name = "bank")
    private final XmlBank bank;

    public XmlBank getBank() {
	return bank;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(103)
    public static class XmlBankSign extends AXmlSignBase {

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
	public XmlBankSign() {
	    super();
	    this.certificateSerialNumber = null;
	}

	public XmlBankSign(XmlSignType signType, byte[] signature, BigInteger certificateSerialNumber) {
	    super(signType, signature);
	    this.certificateSerialNumber = certificateSerialNumber;
	}
    }

    @XmlElement(name = "bank_sign")
    private final XmlBankSign bankSign;

    public XmlBankSign getBankSign() {
	return bankSign;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlControlResponseDocument() {
	super();
	this.bank = null;
	this.bankSign = null;
    }

    public XmlControlResponseDocument(XmlBank bank, XmlBankSign bankSign) {
	super();
	this.bank = bank;
	this.bankSign = bankSign;
    }
}
