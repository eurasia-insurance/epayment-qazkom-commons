package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchant;
import tech.lapsa.epayment.qazkom.xml.bind.XmlControlDocument.XmlMerchantSign;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;
import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyStrings;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(79)
public class XmlControlResonseDocument extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlControlResonseDocument> TOOL = SerializationTool.forClass(XmlControlResonseDocument.class, XmlSchemas.CONTROL_RESPONSE_SCHEMA);

    public static final SerializationTool<XmlControlResonseDocument> getTool() {
	return TOOL;
    }

    public static XmlControlResonseDocument of(final String rawXml) {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(-1)
    public static class XmlBank extends AXmlBase {

	private static final long serialVersionUID = 1L;

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

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlResponse() {
		super();
		this.code = null;
		this.message = null;
		this.remainingAmount = null;
	    }

	    public XmlResponse(String code, String message, Double remainingAmount) {
		super();
		this.code = code;
		this.message = message;
		this.remainingAmount = remainingAmount;
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
    public XmlControlResonseDocument() {
	super();
	this.bank = null;
	this.bankSign = null;
    }

    public XmlControlResonseDocument(XmlBank bank, XmlBankSign bankSign) {
	super();
	this.bank = bank;
	this.bankSign = bankSign;
    }
}
