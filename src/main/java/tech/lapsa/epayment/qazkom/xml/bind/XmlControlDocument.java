package tech.lapsa.epayment.qazkom.xml.bind;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Currency;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlCommand;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlCommand.XmlType;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlPayment;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant.XmlReason;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlAmountAdapter;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;
import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.SigningSignature;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;
import tech.lapsa.java.jaxb.adapter.XmlCurrencyNumericAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(79)
public class XmlControlDocument extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlControlDocument> TOOL = SerializationTool.forClass(
	    XmlControlDocument.class, XmlSchemas.CONTROL_SCHEMA);

    public static final SerializationTool<XmlControlDocument> getTool() {
	return TOOL;
    }

    public static XmlControlDocument of(final String rawXml) {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    public static XmlControlDocumentBuilder builder() {
	return new XmlControlDocumentBuilder();
    }

    public static final class XmlControlDocumentBuilder {

	private static final XmlSignType SIGN_TYPE = XmlSignType.RSA;

	private String paymentReference;
	private String approvalCode;

	private String orderNumber;
	private Double amount;
	private Currency currency;
	private String merchantId;
	private X509Certificate merchantCertificate;
	private PrivateKey merchantKey;

	private XmlType commandType;

	private String reason;

	private XmlControlDocumentBuilder() {
	}

	public XmlControlDocumentBuilder withPaymentReference(String paymentReference) throws IllegalArgumentException {
	    this.paymentReference = MyStrings.requireNonEmpty(paymentReference, "paymentReference");
	    return this;
	}

	public XmlControlDocumentBuilder withApprovalCode(String approvalCode) throws IllegalArgumentException {
	    this.approvalCode = MyStrings.requireNonEmpty(approvalCode, "approvalCode");
	    return this;
	}

	public XmlControlDocumentBuilder withOrderNumber(String orderNumber) throws IllegalArgumentException {
	    this.orderNumber = MyStrings.requireNonEmpty(orderNumber, "orderNumber");
	    return this;
	}

	public XmlControlDocumentBuilder withAmount(Double amount) throws IllegalArgumentException {
	    this.amount = MyNumbers.requirePositive(amount, "amount");
	    return this;
	}

	public XmlControlDocumentBuilder withCurrency(Currency currency) throws IllegalArgumentException {
	    this.currency = MyObjects.requireNonNull(currency, "currency");
	    return this;
	}

	public XmlControlDocumentBuilder withPayment(String paymentReference, String approvalCode, String orderNumber,
		Double amount, Currency currency) throws IllegalArgumentException {
	    withPaymentReference(paymentReference);
	    withApprovalCode(approvalCode);
	    withOrderNumber(orderNumber);
	    withAmount(amount);
	    withCurrency(currency);
	    return this;
	}

	public XmlControlDocumentBuilder withMerchantId(String merchantId) throws IllegalArgumentException {
	    this.merchantId = MyStrings.requireNonEmpty(merchantId, "merchantId");
	    return this;
	}

	public XmlControlDocumentBuilder withMerchantCertificate(X509Certificate merchantCertificate)
		throws IllegalArgumentException {
	    this.merchantCertificate = MyObjects.requireNonNull(merchantCertificate, "merchantCertificate");
	    return this;
	}

	public XmlControlDocumentBuilder withMerchantKey(PrivateKey merchantKey) throws IllegalArgumentException {
	    this.merchantKey = MyObjects.requireNonNull(merchantKey, "merchantKey");
	    return this;
	}

	public XmlControlDocumentBuilder signWith(final PrivateKey merchantCertificate,
		final X509Certificate merchantKey)
		throws IllegalArgumentException {
	    withMerchantCertificate(merchantKey);
	    withMerchantKey(merchantCertificate);
	    return this;
	}

	public XmlControlDocumentBuilder prepareCancel(String cancelationReason) throws IllegalArgumentException {
	    this.commandType = XmlType.REVERSE;
	    this.reason = MyStrings.requireNonEmpty(cancelationReason, "cancelationReason");
	    return this;
	}

	public XmlControlDocumentBuilder prepareRefund(String refundationReason) throws IllegalArgumentException {
	    this.commandType = XmlType.REFUND;
	    this.reason = MyStrings.requireNonEmpty(refundationReason, "refundationReason");
	    return this;
	}

	public XmlControlDocumentBuilder prepareCharge() throws IllegalArgumentException {
	    this.commandType = XmlType.COMPLETE;
	    this.reason = null;
	    return this;
	}

	public XmlControlDocument build() throws IllegalArgumentException {
	    MyStrings.requireNonEmpty(paymentReference, "paymentReference");
	    MyStrings.requireNonEmpty(approvalCode, "approvalCode");
	    MyStrings.requireNonEmpty(orderNumber, "orderNumber");
	    MyNumbers.requirePositive(amount, "amount");
	    MyObjects.requireNonNull(currency, "currency");
	    MyStrings.requireNonEmpty(merchantId, "merchantId");
	    MyObjects.requireNonNull(merchantCertificate, "merchantCertificate");
	    MyObjects.requireNonNull(merchantKey, "merchantKey");
	    MyObjects.requireNonNullMsg(commandType, "Unknwon command type");
	    if (!commandType.equals(XmlType.COMPLETE))
		MyStrings.requireNonEmpty(reason, "reason");

	    final XmlCommand command = new XmlCommand(commandType);
	    final XmlPayment payment = new XmlPayment(paymentReference, approvalCode, orderNumber, amount, currency);

	    final XmlMerchant merchant;
	    if (!commandType.equals(XmlType.COMPLETE)) {
		final XmlReason reas = new XmlReason(reason);
		merchant = new XmlMerchant(merchantId, command, payment, reas);
	    } else
		merchant = new XmlMerchant("92061103", command, payment);

	    final String signatureAlgorithm = SIGN_TYPE.getSignatureAlgorithmName().get();

	    final SigningSignature signature = MySignatures
		    .forSignature(MyObjects.requireNonNull(merchantKey, "merchantKey"), signatureAlgorithm)
		    .orElseThrow(MyExceptions.illegalArgumentSupplier("Failed proces with private key"));

	    final byte[] data = XmlMerchant.getTool().serializeToBytes(merchant);
	    final byte[] digest = signature.sign(data);
	    MyArrays.reverse(digest);

	    final XmlMerchantSign merchantSign = new XmlMerchantSign(SIGN_TYPE, digest,
		    merchantCertificate.getSerialNumber());
	    return new XmlControlDocument(merchant, merchantSign);
	}

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlRootElement(name = "merchant")
    @HashCodePrime(83)
    public static class XmlMerchant extends AXmlBase {

	private static final long serialVersionUID = 1L;

	private static final SerializationTool<XmlMerchant> TOOL = SerializationTool.forClass(XmlMerchant.class,
		XmlSchemas.CONTROL_SCHEMA);

	@XmlAttribute(name = "id")
	private final String id;

	public String getId() {
	    return id;
	}

	public static SerializationTool<XmlMerchant> getTool() {
	    return TOOL;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(89)
	public static class XmlCommand extends AXmlBase {

	    private static final long serialVersionUID = 1L;

	    @XmlEnum
	    public static enum XmlType {
		@XmlEnumValue("complete")
		COMPLETE,
		@XmlEnumValue("reverse")
		REVERSE,
		@XmlEnumValue("refund")
		REFUND;
	    }

	    @XmlAttribute(name = "type")
	    private final XmlType type;

	    public XmlType getType() {
		return type;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlCommand() {
		super();
		this.type = null;
	    }

	    public XmlCommand(XmlType type) {
		super();
		this.type = type;
	    }
	}

	@XmlElement(name = "command")
	private final XmlCommand command;

	public XmlCommand getCommand() {
	    return command;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(97)
	public static class XmlPayment extends AXmlBase {

	    private static final long serialVersionUID = 1L;

	    @XmlAttribute(name = "reference")
	    private final String reference;

	    public String getReference() {
		return reference;
	    }

	    @XmlAttribute(name = "approval_code")
	    private final String approvalCode;

	    public String getApprovalCode() {
		return approvalCode;
	    }

	    @XmlAttribute(name = "orderid")
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

	    @XmlAttribute(name = "currency_code")
	    @XmlJavaTypeAdapter(XmlCurrencyNumericAdapter.class)
	    private final Currency currency;

	    public Currency getCurrency() {
		return currency;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlPayment() {
		super();
		this.reference = null;
		this.approvalCode = null;
		this.orderId = null;
		this.amount = null;
		this.currency = null;
	    }

	    public XmlPayment(String reference, String approvalCode, String orderId, Double amount, Currency currency) {
		super();
		this.reference = reference;
		this.approvalCode = approvalCode;
		this.orderId = orderId;
		this.amount = amount;
		this.currency = currency;
	    }
	}

	@XmlElement(name = "payment")
	private final XmlPayment payment;

	public XmlPayment getPayment() {
	    return payment;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(101)
	public static class XmlReason implements Serializable {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public int hashCode() {
		return HcEqUtil.hashCode(this);
	    }

	    @Override
	    public boolean equals(final Object other) {
		return HcEqUtil.equals(this, other);
	    }

	    @XmlValue
	    private final String text;

	    public String getText() {
		return text;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlReason() {
		super();
		this.text = null;
	    }

	    public XmlReason(String text) {
		super();
		this.text = text;
	    }
	}

	@XmlElement(name = "reason")
	private final XmlReason reason;

	public XmlReason getReason() {
	    return reason;
	}

	/*
	 * Default no-args constructor due to JAXB requirements
	 */
	@Deprecated
	public XmlMerchant() {
	    this.id = null;
	    this.command = null;
	    this.payment = null;
	    this.reason = null;
	}

	public XmlMerchant(String id, XmlCommand command, XmlPayment payment) {
	    super();
	    this.id = id;
	    this.command = command;
	    this.payment = payment;
	    this.reason = null;
	}

	public XmlMerchant(String id, XmlCommand command, XmlPayment payment, XmlReason reason) {
	    super();
	    this.id = id;
	    this.command = command;
	    this.payment = payment;
	    this.reason = reason;
	}
    }

    @XmlElement(name = "merchant")
    private final XmlMerchant merchant;

    public XmlMerchant getMerchant() {
	return merchant;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(103)
    public static class XmlMerchantSign extends AXmlSignBase {

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
	public XmlMerchantSign() {
	    super(null);
	    this.certificateSerialNumber = null;
	}

	public XmlMerchantSign(XmlSignType signType, byte[] signature, BigInteger certificateSerialNumber) {
	    super(signType, signature);
	    this.certificateSerialNumber = certificateSerialNumber;
	}
    }

    @XmlElement(name = "merchant_sign")
    private final XmlMerchantSign merchantSign;

    public XmlMerchantSign getMerchantSign() {
	return merchantSign;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }

    public boolean validSignature(final X509Certificate certificate)
	    throws IllegalStateException, IllegalArgumentException {

	MyObjects.requireNonNull(certificate, "certificate");

	final String algorithmName = getMerchantSign().getSignType().getSignatureAlgorithmName() //
		.orElseThrow(MyExceptions.illegalStateSupplier("No such algorithm"));

	final VerifyingSignature signature = MySignatures
		.forVerification(certificate, algorithmName) //
		.orElseThrow(MyExceptions.illegalArgumentSupplier("Failed process with certificate"));

	if (merchant == null || merchantSign == null || merchantSign.getSignature() == null)
	    throw MyExceptions.illegalStateFormat("Document is corrupted");

	final byte[] data = XmlMerchant.getTool().serializeToBytes(merchant);
	final byte[] digest = merchantSign.getSignature().clone();
	MyArrays.reverse(digest);
	return signature.verify(data, digest);
    }

    public XmlControlDocument requreValidSignature(final X509Certificate certificate)
	    throws IllegalStateException, IllegalArgumentException {
	if (validSignature(certificate))
	    return this;
	throw MyExceptions.illegalStateFormat("Signature is invalid");
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlControlDocument() {
	this.merchant = null;
	this.merchantSign = null;
    }

    public XmlControlDocument(XmlMerchant merchant, XmlMerchantSign merchantSign) {
	super();
	this.merchant = merchant;
	this.merchantSign = merchantSign;
    }
}
