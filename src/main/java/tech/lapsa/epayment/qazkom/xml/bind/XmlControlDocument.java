package tech.lapsa.epayment.qazkom.xml.bind;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Currency;

import javax.xml.bind.JAXBException;
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
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.jaxb.adapter.XmlCurrencyNumericAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(79)
public class XmlControlDocument extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlControlDocument> TOOL = SerializationTool.forClass(
	    XmlControlDocument.class);

    public static final SerializationTool<XmlControlDocument> getTool() {
	return TOOL;
    }

    public static XmlControlDocument of(final String rawXml) {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(83)
    public static class XmlMerchant extends AXmlBase {

	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "id")
	private final String id;

	public String getId() {
	    return id;
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

    public static void main(String[] args) throws JAXBException, UnsupportedEncodingException {
	final XmlCommand command = new XmlCommand(XmlType.COMPLETE);
	final XmlPayment payment = new XmlPayment("21312321", "00", "4546546545645646546", 340.1d,
		Currency.getInstance("KZT"));
	final XmlReason reason = new XmlReason("Так надобно");
	final XmlMerchant merchant = new XmlMerchant("92061103", command, payment, reason);
	final XmlMerchantSign merchantSign = new XmlMerchantSign(XmlSignType.RSA, "adsadsadsadsa".getBytes(),
		new BigInteger("sadsadsadsa".getBytes()));
	final XmlControlDocument doc = new XmlControlDocument(merchant, merchantSign);
	getTool().serializeTo(doc, System.out);
    }

}
