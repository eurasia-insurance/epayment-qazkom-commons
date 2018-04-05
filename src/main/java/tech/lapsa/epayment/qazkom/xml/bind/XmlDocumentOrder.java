package tech.lapsa.epayment.qazkom.xml.bind;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Currency;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.SigningSignature;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(31)
public class XmlDocumentOrder extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlDocumentOrder> TOOL = SerializationTool.forClass(XmlDocumentOrder.class,
	    XmlSchemas.ORDER_SCHEMA);

    public static final SerializationTool<XmlDocumentOrder> getTool() {
	return TOOL;
    }

    public static XmlDocumentOrder of(final String rawXml) throws IllegalArgumentException {
	return TOOL.deserializeFrom(rawXml);
    }

    public static XmlDocumentOrderBuilder builder() {
	return new XmlDocumentOrderBuilder();
    }

    public static final class XmlDocumentOrderBuilder {

	private static final XmlSignType SIGN_TYPE = XmlSignType.RSA;

	private String orderNumber;
	private Double amount;
	private Currency currency;
	private String merchantId;
	private String merchantName;
	private X509Certificate merchantCertificate;
	private PrivateKey merchantKey;

	private XmlDocumentOrderBuilder() {
	}

	public XmlDocumentOrderBuilder withOrderNumber(final String orderNumber) throws IllegalArgumentException {
	    this.orderNumber = MyStrings.requireNonEmpty(orderNumber, "orderNumber");
	    return this;
	}

	public XmlDocumentOrderBuilder withAmount(final Double amount) throws IllegalArgumentException {
	    this.amount = MyNumbers.requireNonZero(amount, "amount");
	    return this;
	}

	public XmlDocumentOrderBuilder withCurrency(final Currency currency) throws IllegalArgumentException {
	    this.currency = MyObjects.requireNonNull(currency, "currency");
	    return this;
	}

	public XmlDocumentOrderBuilder withMerchchant(final String merchantId, final String merchantName)
		throws IllegalArgumentException {
	    this.merchantId = MyStrings.requireNonEmpty(merchantId, "merchantId");
	    this.merchantName = MyStrings.requireNonEmpty(merchantName, "merchantName");
	    return this;
	}

	public XmlDocumentOrderBuilder signWith(final PrivateKey merchantKey, final X509Certificate certificate)
		throws IllegalArgumentException {
	    merchantCertificate = MyObjects.requireNonNull(certificate, "certificate");
	    this.merchantKey = MyObjects.requireNonNull(merchantKey, "merchantKey");
	    return this;
	}

	public XmlDocumentOrder build() throws IllegalArgumentException {
	    MyStrings.requireNonEmpty(orderNumber, "orderNumber");
	    MyNumbers.requireNonZero(amount, "amount");
	    MyObjects.requireNonNull(currency, "currency");
	    MyStrings.requireNonEmpty(merchantId, "merchantId");
	    MyStrings.requireNonEmpty(merchantName, "merchantName");
	    MyObjects.requireNonNull(merchantKey, "merchantKey");
	    MyObjects.requireNonNull(merchantCertificate, "merchantCertificate");

	    final XmlDepartment xmlDepartment = new XmlDepartment(amount, merchantId);
	    final XmlOrder xmlOrder = new XmlOrder(amount, currency, orderNumber, Arrays.asList(xmlDepartment));

	    final XmlMerchant xmlMerchant = new XmlMerchant(merchantCertificate.getSerialNumber(), merchantName,
		    xmlOrder);

	    final String signatureAlgorithm = SIGN_TYPE.getSignatureAlgorithmName().get();

	    final SigningSignature signature = MySignatures
		    .forSignature(MyObjects.requireNonNull(merchantKey, "merchantKey"), signatureAlgorithm)
		    .orElseThrow(MyExceptions.illegalArgumentSupplier("Failed proces with private key"));

	    final byte[] data = XmlMerchant.getTool().serializeToBytes(xmlMerchant);
	    final byte[] digest = signature.sign(data);
	    MyArrays.reverse(digest);

	    final XmlMerchantSign xmlMerchantSign = new XmlMerchantSign(SIGN_TYPE, digest);

	    final XmlDocumentOrder doc = new XmlDocumentOrder(xmlMerchant, xmlMerchantSign);

	    return doc;
	}

    }

    public boolean validSignature(final X509Certificate certificate)
	    throws IllegalStateException, IllegalArgumentException {

	final String algorithmName = getMerchantSign().getSignType().getSignatureAlgorithmName() //
		.orElseThrow(MyExceptions.illegalStateSupplier("No such algorithm"));

	final VerifyingSignature signature = MySignatures
		.forVerification(MyObjects.requireNonNull(certificate, "certificate"),
			algorithmName) //
		.orElseThrow(MyExceptions.illegalArgumentSupplier("Failed process with certificate"));

	if (merchant == null || merchantSign == null || merchantSign.getSignature() == null)
	    throw MyExceptions.illegalStateFormat("Document is corrupted");

	final byte[] data = XmlMerchant.getTool().serializeToBytes(merchant);
	final byte[] digest = merchantSign.getSignature().clone();
	MyArrays.reverse(digest);
	return signature.verify(data, digest);
    }

    public XmlDocumentOrder requreValidSignature(final X509Certificate certificate)
	    throws IllegalStateException, IllegalArgumentException {
	if (validSignature(certificate))
	    return this;
	throw MyExceptions.illegalStateFormat("Signature is invalid");
    }

    @XmlElement(name = "merchant")
    private final XmlMerchant merchant;

    @XmlElement(name = "merchant_sign")
    private final XmlMerchantSign merchantSign;

    public XmlMerchant getMerchant() {
	return merchant;
    }

    public XmlMerchantSign getMerchantSign() {
	return merchantSign;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlDocumentOrder() {
	this.merchant = null;
	this.merchantSign = null;
    }

    public XmlDocumentOrder(XmlMerchant merchant, XmlMerchantSign merchantSign) {
	super();
	this.merchant = merchant;
	this.merchantSign = merchantSign;
    }
}
