package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
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

import com.lapsa.international.country.Country;
import com.lapsa.international.phone.PhoneNumber;
import com.lapsa.international.phone.converter.jaxb.XmlPhoneNumberAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCardExpirationDateAdapter;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCountryAlpha3CodeAdapter;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlTimestampAdapter;
import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(37)
public class XmlDocumentPayment extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlDocumentPayment> TOOL = SerializationTool.forClass(
	    XmlDocumentPayment.class,
	    XmlSchemas.PAYMENT_SCHEMA);

    public static final SerializationTool<XmlDocumentPayment> getTool() {
	return TOOL;
    }

    public static XmlDocumentPayment of(final String rawXml) {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    public static XmlDocumentPaymentBuilder builder() {
	return new XmlDocumentPaymentBuilder();
    }

    public static final class XmlDocumentPaymentBuilder {
	private String rawXml;
	private X509Certificate certificate;

	private XmlDocumentPaymentBuilder() {
	}

	public XmlDocumentPaymentBuilder ofRawXml(final String rawXml) throws IllegalArgumentException {
	    this.rawXml = MyStrings.requireNonEmpty(rawXml, "rawXml");
	    return this;
	}

	public XmlDocumentPaymentBuilder withBankCertificate(final X509Certificate certificate)
		throws IllegalArgumentException {
	    this.certificate = MyObjects.requireNonNull(certificate, "certificate");
	    return this;
	}

	public XmlDocumentPayment build() throws IllegalArgumentException, IllegalStateException {
	    MyStrings.requireNonEmpty(rawXml, "rawXml");
	    final XmlDocumentPayment document = TOOL.deserializeFrom(rawXml);

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
    @HashCodePrime(11)
    public static class XmlBank extends AXmlBase {

	private static final long serialVersionUID = -5468834860872828233L;

	private static final SerializationTool<XmlBank> TOOL = SerializationTool.forClass(XmlBank.class,
		XmlSchemas.PAYMENT_SCHEMA);

	public static final SerializationTool<XmlBank> getTool() {
	    return TOOL;
	}

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

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(17)
	public static class XmlCustomer extends AXmlBase {

	    private static final long serialVersionUID = 1L;

	    @XmlAttribute(name = "name")
	    private final String name;

	    public String getName() {
		return name;
	    }

	    @XmlAttribute(name = "mail")
	    private final String emailAddress;

	    public String getEmailAddress() {
		return emailAddress;
	    }

	    @XmlAttribute(name = "phone")
	    @XmlJavaTypeAdapter(XmlPhoneNumberAdapter.class)
	    private final PhoneNumber phone;

	    public PhoneNumber getPhone() {
		return phone;
	    }

	    @XmlElement(name = "merchant")
	    private final XmlMerchant sourceMerchant;

	    public XmlMerchant getSourceMerchant() {
		return sourceMerchant;
	    }

	    @XmlElement(name = "merchant_sign")
	    private final XmlMerchantSign sourceMerchantSign;

	    public XmlMerchantSign getSourceMerchantSign() {
		return sourceMerchantSign;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlCustomer() {
		this.name = null;
		this.emailAddress = null;
		this.phone = null;
		this.sourceMerchant = null;
		this.sourceMerchantSign = null;
	    }

	    public XmlCustomer(final String name,
		    final String emailAddress,
		    final PhoneNumber phone,
		    final XmlMerchant sourceMerchant,
		    final XmlMerchantSign sourceMerchantSign) {
		super();
		this.name = name;
		this.emailAddress = emailAddress;
		this.phone = phone;
		this.sourceMerchant = sourceMerchant;
		this.sourceMerchantSign = sourceMerchantSign;
	    }
	}

	@XmlElement(name = "customer")
	private final XmlCustomer customer;

	public XmlCustomer getCustomer() {
	    return customer;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(19)
	public static class XmlCustomerSign extends AXmlSignBase {

	    private static final long serialVersionUID = 1L;

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlCustomerSign() {
		super();
	    }

	    public XmlCustomerSign(XmlSignType signType, byte[] signature) {
		super(signType, signature);
	    }

	    public XmlCustomerSign(XmlSignType signType, String signatureEncoded) {
		super(signType, signatureEncoded);
	    }
	}

	@XmlElement(name = "customer_sign")
	private final XmlCustomerSign customerSign;

	public XmlCustomerSign getCustomerSign() {
	    return customerSign;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(71)
	public static class XmlResults extends AXmlBase {

	    private static final long serialVersionUID = 1L;

	    // timestamp - время проведения платежа
	    @XmlAttribute(name = "timestamp")
	    @XmlJavaTypeAdapter(XmlTimestampAdapter.class)
	    private final Instant timestamp;

	    public Instant getTimestamp() {
		return timestamp;
	    }

	    @XmlAccessorType(XmlAccessType.FIELD)
	    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	    @HashCodePrime(61)
	    public static class XmlPayment extends AXmlAmount {

		private static final long serialVersionUID = 1L;

		// Результат транзакции
		// merchant_id - ID продавца в платежной системе
		@XmlAttribute(name = "merchant_id")
		private final String merchantId;

		public String getMerchantId() {
		    return merchantId;
		}

		// reference - номер обращения к платежной системе
		@XmlAttribute(name = "reference")
		private final String reference;

		public String getReference() {
		    return reference;
		}

		// approval_code - код авторизации
		@XmlAttribute(name = "approval_code")
		private final String approvalCode;

		public String getApprovalCode() {
		    return approvalCode;
		}

		// response_code - код результата авторизации.
		// Должен иметь значение "00" (два нуля), в противном случае
		// свяжитесь,
		// пожалуйста, с администратором системы авторизации
		@XmlAttribute(name = "response_code")
		private final String responseCode;

		public String getResponseCode() {
		    return responseCode;
		}

		// Secure- Yes/No признак, что транзакция была 3DSecure или нет
		@XmlAttribute(name = "Secure")
		private final XmlSecureType secureType;

		public XmlSecureType getSecureType() {
		    return secureType;
		}

		// card_bin- Страна эмитент карты
		@XmlAttribute(name = "card_bin")
		@XmlJavaTypeAdapter(XmlCountryAlpha3CodeAdapter.class)
		private final Country cardCountry;

		public Country getCardCountry() {
		    return cardCountry;
		}

		// card - маскированный номер карты
		@XmlAttribute(name = "card")
		private final String cardNumberMasked;

		public String getCardNumberMasked() {
		    return cardNumberMasked;
		}

		// c_hash- Хэш карты
		@XmlAttribute(name = "c_hash")
		private final String cardHash;

		public String getCardHash() {
		    return cardHash;
		}

		// exp_date - срок действия карты
		@XmlAttribute(name = "exp_date")
		@XmlJavaTypeAdapter(XmlCardExpirationDateAdapter.class)
		private final LocalDate expirationDate;

		public LocalDate getExpirationDate() {
		    return expirationDate;
		}

		/*
		 * Default no-args constructor due to JAXB requirements
		 */
		@Deprecated
		public XmlPayment() {
		    super(null);
		    this.merchantId = null;
		    this.reference = null;
		    this.approvalCode = null;
		    this.responseCode = null;
		    this.secureType = null;
		    this.cardCountry = null;
		    this.cardNumberMasked = null;
		    this.cardHash = null;
		    this.expirationDate = null;
		}

		public XmlPayment(Double amount, String merchantId, String reference, String approvalCode,
			String responseCode,
			XmlSecureType secureType, Country cardCountry, String cardNumberMasked, String cardHash,
			LocalDate expirationDate) {
		    super(amount);
		    this.merchantId = merchantId;
		    this.reference = reference;
		    this.approvalCode = approvalCode;
		    this.responseCode = responseCode;
		    this.secureType = secureType;
		    this.cardCountry = cardCountry;
		    this.cardNumberMasked = cardNumberMasked;
		    this.cardHash = cardHash;
		    this.expirationDate = expirationDate;
		}
	    }

	    @XmlElement(name = "payment")
	    private final List<XmlPayment> payments;

	    public List<XmlPayment> getPayments() {
		return payments;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlResults() {
		this.timestamp = null;
		this.payments = null;
	    }

	    public XmlResults(Instant timestamp, List<XmlPayment> payments) {
		super();
		this.timestamp = timestamp;
		this.payments = payments == null ? null : MyCollections.unmodifiableOrEmptyList(payments);
	    }
	}

	@XmlElement(name = "results")
	private final XmlResults results;

	public XmlResults getResults() {
	    return results;
	}

	/*
	 * Default no-args constructor due to JAXB requirements
	 */
	@Deprecated
	public XmlBank() {
	    this.name = null;
	    this.customer = null;
	    this.customerSign = null;
	    this.results = null;
	}

	public XmlBank(final String name,
		final XmlCustomer customer,
		final XmlCustomerSign customerSign,
		final XmlResults results) {
	    super();
	    this.name = name;
	    this.customer = customer;
	    this.customerSign = customerSign;
	    this.results = results;
	}
    }

    @XmlElement(name = "bank")
    private final XmlBank bank;

    public XmlBank getBank() {
	return bank;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(13)
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

	public XmlBankSign(final XmlSignType signType, final byte[] signature,
		final BigInteger certificateSerialNumber) {
	    super(signType, signature);
	    this.certificateSerialNumber = certificateSerialNumber;
	}

	public XmlBankSign(final XmlSignType signType, final String signatureEncoded,
		final BigInteger certificateSerialNumber) {
	    super(signType, signatureEncoded);
	    this.certificateSerialNumber = certificateSerialNumber;
	}
    }

    @XmlElement(name = "bank_sign")
    private final XmlBankSign bankSign;

    public XmlBankSign getBankSign() {
	return bankSign;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlDocumentPayment() {
	this.bank = null;
	this.bankSign = null;
    }

    public XmlDocumentPayment(XmlBank bank, XmlBankSign bankSign) {
	super();
	this.bank = bank;
	this.bankSign = bankSign;
    }
}
