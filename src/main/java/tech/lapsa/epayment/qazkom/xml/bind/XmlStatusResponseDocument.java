package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Currency;
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
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.lapsa.international.phone.PhoneNumber;
import com.lapsa.international.phone.converter.jaxb.XmlPhoneNumberAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.XmlStatusRequestDocument.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlAmountAdapter;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlTimestampAdapterWithNano;
import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;
import tech.lapsa.java.jaxb.adapter.XmlCurrencyNumericAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(151)
public class XmlStatusResponseDocument extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlStatusResponseDocument> TOOL = SerializationTool.forClass(
	    XmlStatusResponseDocument.class, XmlSchemas.STATUS_RESPONSE_SCHEMA);

    public static final SerializationTool<XmlStatusResponseDocument> getTool() {
	return TOOL;
    }

    public static XmlStatusResponseDocument of(final String rawXml) {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    public static XmlStatusResonseDocumentBuilder builder() {
	return new XmlStatusResonseDocumentBuilder();
    }

    public static final class XmlStatusResonseDocumentBuilder {
	private String rawXml;
	private X509Certificate certificate;

	private XmlStatusResonseDocumentBuilder() {
	}

	public XmlStatusResonseDocumentBuilder ofRawXml(final String rawXml) throws IllegalArgumentException {
	    this.rawXml = MyStrings.requireNonEmpty(rawXml, "rawXml");
	    return this;
	}

	public XmlStatusResonseDocumentBuilder withBankCertificate(final X509Certificate certificate)
		throws IllegalArgumentException {
	    this.certificate = MyObjects.requireNonNull(certificate, "certificate");
	    return this;
	}

	public XmlStatusResponseDocument build() throws IllegalArgumentException, IllegalStateException {
	    MyStrings.requireNonEmpty(rawXml, "rawXml");
	    final XmlStatusResponseDocument document = TOOL.deserializeFrom(rawXml);

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
    @HashCodePrime(157)
    public static class XmlBank extends AXmlBase {

	private static final long serialVersionUID = 1L;

	private static final SerializationTool<XmlBank> TOOL = SerializationTool.forClass(XmlBank.class,
		XmlSchemas.STATUS_RESPONSE_SCHEMA);

	private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
	private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE
		| Pattern.DOTALL | Pattern.UNIX_LINES;
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
		    e.printStackTrace(System.err);
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
	@XmlRootElement(name = "merchant")
	@HashCodePrime(163)
	public static class XmlMerchant extends AXmlBase {

	    private static final long serialVersionUID = 1L;

	    private static final SerializationTool<XmlMerchant> TOOL = SerializationTool.forClass(XmlMerchant.class,
		    XmlSchemas.STATUS_REQUEST_SCHEMA);

	    public static SerializationTool<XmlMerchant> getTool() {
		return TOOL;
	    }

	    @XmlAttribute(name = "id")
	    private final String id;

	    public String getId() {
		return id;
	    }

	    @XmlAccessorType(XmlAccessType.FIELD)
	    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	    @HashCodePrime(167)
	    public static class XmlOrder extends AXmlBase {

		private static final long serialVersionUID = 1L;

		@XmlAttribute(name = "id")
		private final String orderId;

		public String getOrderId() {
		    return orderId;
		}

		@XmlAttribute(name = "amount")
		@XmlJavaTypeAdapter(XmlAmountAdapter.class)
		private final Double amount;

		public Double getAmount() {
		    return amount;
		}

		@XmlAttribute(name = "currency")
		@XmlJavaTypeAdapter(XmlCurrencyNumericAdapter.class)
		private final Currency currency;

		public Currency getCurrency() {
		    return currency;
		}

		/*
		 * Default no-args constructor due to JAXB requirements
		 */
		@Deprecated
		public XmlOrder() {
		    super();
		    this.orderId = null;
		    this.amount = null;
		    this.currency = null;
		}

		public XmlOrder(String orderId, Double amount, Currency currency) {
		    super();
		    this.orderId = orderId;
		    this.amount = amount;
		    this.currency = currency;
		}
	    }

	    @XmlElement(name = "order")
	    private final XmlOrder order;

	    public XmlOrder getOrder() {
		return order;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlMerchant() {
		this.id = null;
		this.order = null;
	    }

	    public XmlMerchant(String id, XmlOrder order) {
		super();
		this.id = id;
		this.order = order;
	    }

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
	@HashCodePrime(173)
	public static class XmlResponse extends AXmlBase {

	    private static final long serialVersionUID = 1L;

	    @XmlAttribute(name = "payment")
	    private final Boolean validPayment;

	    public Boolean getValidPayment() {
		return validPayment;
	    }

	    @XmlAttribute(name = "status")
	    private final String status;

	    public String getStatus() {
		return status;
	    }

	    @XmlAttribute(name = "result")
	    private final String result;

	    public String getResult() {
		return result;
	    }

	    @XmlAttribute(name = "amount")
	    private final Double amount;

	    public Double getAmount() {
		return amount;
	    }

	    @XmlAttribute(name = "currencycode")
	    @XmlJavaTypeAdapter(XmlCurrencyNumericAdapter.class)
	    private final Currency currency;

	    public Currency getCurrency() {
		return currency;
	    }

	    @XmlAttribute(name = "timestamp")
	    @XmlJavaTypeAdapter(XmlTimestampAdapterWithNano.class)
	    private final Instant timestamp;

	    public Instant getTimestamp() {
		return timestamp;
	    }

	    @XmlAttribute(name = "reference")
	    private final String reference;

	    public String getReference() {
		return reference;
	    }

	    @XmlAttribute(name = "card_to")
	    private final String recipientCardMasked;

	    public String getRecipientCardMasked() {
		return recipientCardMasked;
	    }

	    @XmlAttribute(name = "approval_code")
	    private final String approvalCode;

	    public String getApprovalCode() {
		return approvalCode;
	    }

	    @XmlAttribute(name = "msg")
	    private final String message;

	    public String getMessage() {
		return message;
	    }

	    @XmlAttribute(name = "secure")
	    private final XmlSecureType secure;

	    public XmlSecureType getSecure() {
		return secure;
	    }

	    @XmlAttribute(name = "card_bin")
	    private final String bin;

	    public String getBin() {
		return bin;
	    }

	    @XmlAttribute(name = "cardhash")
	    private final String payerCardMasked;

	    public String getPayerCardMasked() {
		return payerCardMasked;
	    }

	    @XmlAttribute(name = "payername")
	    private final String payerCardholderName;

	    public String getPayerCardholderName() {
		return payerCardholderName;
	    }

	    @XmlAttribute(name = "payermail")
	    private final String payerEmail;

	    public String getPayerEmail() {
		return payerEmail;
	    }

	    @XmlAttribute(name = "payerphone")
	    @XmlJavaTypeAdapter(XmlPhoneNumberAdapter.class)
	    private final PhoneNumber payerPhone;

	    public PhoneNumber getPayerPhone() {
		return payerPhone;
	    }

	    @XmlAttribute(name = "c_hash")
	    private final String cardHash;

	    public String getCardHash() {
		return cardHash;
	    }

	    public static class XmlBooleanRecurAdapter extends XmlAdapter<String, Boolean> {

		@Override
		public Boolean unmarshal(final String v) throws Exception {
		    if (v == null)
			return null;
		    if ("1".equals(v.trim()))
			return Boolean.TRUE;
		    if ("0".equals(v.trim()))
			return Boolean.FALSE;
		    throw MyExceptions.illegalArgumentFormat("Invalid value of recur '%1$s'", v);
		}

		@Override
		public String marshal(final Boolean v) throws Exception {
		    if (v == null)
			return null;
		    return v.booleanValue()
			    ? "1"
			    : "0";
		}
	    }

	    @XmlAttribute(name = "recur")
	    @XmlJavaTypeAdapter(XmlBooleanRecurAdapter.class)
	    private final Boolean recurrent;

	    public Boolean getRecurrent() {
		return recurrent;
	    }

	    public boolean isRecurrent() {
		return MyObjects.requireNonNull(recurrent, "recurrent").booleanValue();
	    }

	    @XmlAttribute(name = "recur_freq")
	    private final String requrentFrequency;

	    public String getRequrentFrequency() {
		return requrentFrequency;
	    }

	    @XmlAttribute(name = "recur_exp")
	    private final String requrentExpiration;

	    public String getRequrentExpiration() {
		return requrentExpiration;
	    }

	    @XmlAttribute(name = "person_id")
	    private final String requrentPersonId;

	    public String getRequrentPersonId() {
		return requrentPersonId;
	    }

	    @XmlAttribute(name = "cardid")
	    private final String savedCardId;

	    public String getSavedCardId() {
		return savedCardId;
	    }

	    @XmlEnum
	    public enum CardActivationStatus {
		@XmlEnumValue("0")
		ACTIVE,
		@XmlEnumValue("1")
		ACTIVATION_REQUIRED;
	    }

	    @XmlAttribute(name = "card_approve")
	    private final CardActivationStatus savedCardStatus;

	    public CardActivationStatus getSavedCardStatus() {
		return savedCardStatus;
	    }

	    @XmlAttribute(name = "OrderID")
	    private final String orderNumber;

	    public String getOrderNumber() {
		return orderNumber;
	    }

	    @XmlAttribute(name = "SessionID")
	    private final String sessionId;

	    public String getSessionId() {
		return sessionId;
	    }

	    @XmlAttribute(name = "intreference")
	    private final String intreference;

	    public String getIntreference() {
		return intreference;
	    }

	    @XmlEnum
	    public enum PaymentStatusType {
		@XmlEnumValue("0")
		HOLDED,
		@XmlEnumValue("1")
		CHARGED,
		@XmlEnumValue("2")
		REVERSED,
		@XmlEnumValue("3")
		REFUNDED;
	    }

	    @XmlAttribute(name = "AcceptRejectCode")
	    private final PaymentStatusType paymentStatus;

	    public PaymentStatusType getPaymentStatus() {
		return paymentStatus;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlResponse() {
		super();
		this.validPayment = null;
		this.status = null;
		this.result = null;
		this.amount = null;
		this.currency = null;
		this.timestamp = null;
		this.reference = null;
		this.recipientCardMasked = null;
		this.approvalCode = null;
		this.message = null;
		this.secure = null;
		this.bin = null;
		this.payerCardMasked = null;
		this.payerCardholderName = null;
		this.payerEmail = null;
		this.payerPhone = null;
		this.cardHash = null;
		this.recurrent = null;
		this.requrentFrequency = null;
		this.requrentExpiration = null;
		this.requrentPersonId = null;
		this.savedCardId = null;
		this.savedCardStatus = null;
		this.orderNumber = null;
		this.sessionId = null;
		this.intreference = null;
		this.paymentStatus = null;
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
    }

    @XmlElement(name = "bank")
    private final XmlBank bank;

    public XmlBank getBank() {
	return bank;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(179)
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
    public XmlStatusResponseDocument() {
	this.bank = null;
	this.bankSign = null;
    }
}
