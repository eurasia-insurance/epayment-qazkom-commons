package tech.lapsa.epayment.qazkom.xml.bind;

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

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentControlRequest.XmlMerchant;
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
@HashCodePrime(107)
public class XmlDocumentControlResponse extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlDocumentControlResponse> TOOL = SerializationTool
	    .forClass(XmlDocumentControlResponse.class, XmlSchemas.CONTROL_RESPONSE_SCHEMA);

    public static final SerializationTool<XmlDocumentControlResponse> getTool() {
	return TOOL;
    }

    public static XmlDocumentControlResponse of(final String rawXml) {
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

	public XmlDocumentControlResponse build() throws IllegalArgumentException, IllegalStateException {
	    MyStrings.requireNonEmpty(rawXml, "rawXml");
	    final XmlDocumentControlResponse document = TOOL.deserializeFrom(rawXml);

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
    @HashCodePrime(109)
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
	private final XmlSignGeneralWithCert merchantSign;

	public XmlSignGeneralWithCert getMerchantSign() {
	    return merchantSign;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(113)
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

	public XmlBank(String name, XmlMerchant merchant, XmlSignGeneralWithCert merchantSign, XmlResponse response) {
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

    @XmlElement(name = "bank_sign")
    private final XmlSignGeneralWithCert bankSign;

    public XmlSignGeneralWithCert getBankSign() {
	return bankSign;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlDocumentControlResponse() {
	super();
	this.bank = null;
	this.bankSign = null;
    }

    public XmlDocumentControlResponse(XmlBank bank, XmlSignGeneralWithCert bankSign) {
	super();
	this.bank = bank;
	this.bankSign = bankSign;
    }
}
