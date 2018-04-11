package tech.lapsa.epayment.qazkom.xml.bind;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentStatusRequest.XmlMerchant.XmlOrder;
import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.SigningSignature;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(131)
public class XmlDocumentStatusRequest extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlDocumentStatusRequest> TOOL = SerializationTool.forClass(
	    XmlDocumentStatusRequest.class, XmlSchemas.STATUS_REQUEST_SCHEMA);

    public static final SerializationTool<XmlDocumentStatusRequest> getTool() {
	return TOOL;
    }

    public static XmlDocumentStatusRequest of(final String rawXml) {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    public static XmlStatusRequestDocumentBuilder builder() {
	return new XmlStatusRequestDocumentBuilder();
    }

    public static final class XmlStatusRequestDocumentBuilder {

	private static final XmlSignType SIGN_TYPE = XmlSignType.RSA;

	private String orderNumber;
	private String merchantId;
	private X509Certificate merchantCertificate;
	private PrivateKey merchantKey;

	private XmlStatusRequestDocumentBuilder() {
	}

	public XmlStatusRequestDocumentBuilder withOrderNumber(String orderNumber) throws IllegalArgumentException {
	    this.orderNumber = MyStrings.requireNonEmpty(orderNumber, "orderNumber");
	    return this;
	}

	public XmlStatusRequestDocumentBuilder withMerchantId(String merchantId) throws IllegalArgumentException {
	    this.merchantId = MyStrings.requireNonEmpty(merchantId, "merchantId");
	    return this;
	}

	public XmlStatusRequestDocumentBuilder withMerchantCertificate(X509Certificate merchantCertificate)
		throws IllegalArgumentException {
	    this.merchantCertificate = MyObjects.requireNonNull(merchantCertificate, "merchantCertificate");
	    return this;
	}

	public XmlStatusRequestDocumentBuilder withMerchantKey(PrivateKey merchantKey) throws IllegalArgumentException {
	    this.merchantKey = MyObjects.requireNonNull(merchantKey, "merchantKey");
	    return this;
	}

	public XmlStatusRequestDocumentBuilder signWith(final PrivateKey merchantCertificate,
		final X509Certificate merchantKey)
		throws IllegalArgumentException {
	    withMerchantCertificate(merchantKey);
	    withMerchantKey(merchantCertificate);
	    return this;
	}

	public XmlDocumentStatusRequest build() throws IllegalArgumentException {
	    MyStrings.requireNonEmpty(orderNumber, "orderNumber");
	    MyStrings.requireNonEmpty(merchantId, "merchantId");
	    MyObjects.requireNonNull(merchantCertificate, "merchantCertificate");
	    MyObjects.requireNonNull(merchantKey, "merchantKey");

	    final XmlOrder order = new XmlOrder(orderNumber);
	    final XmlMerchant merchant = new XmlMerchant(merchantId, order);

	    final String signatureAlgorithm = SIGN_TYPE.getSignatureAlgorithmName().get();

	    final SigningSignature signature = MySignatures
		    .forSignature(MyObjects.requireNonNull(merchantKey, "merchantKey"), signatureAlgorithm)
		    .orElseThrow(MyExceptions.illegalArgumentSupplier("Failed proces with private key"));

	    final byte[] data = XmlMerchant.getTool().serializeToBytes(merchant);
	    final byte[] digest = signature.sign(data);
	    MyArrays.reverse(digest);

	    final XmlSignGeneralWithCert merchantSign = new XmlSignGeneralWithCert(SIGN_TYPE, digest,
		    merchantCertificate.getSerialNumber());
	    return new XmlDocumentStatusRequest(merchant, merchantSign);
	}

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlRootElement(name = "merchant")
    @HashCodePrime(137)
    public static class XmlMerchant extends AXmlBase {

	private static final long serialVersionUID = 1L;

	private static final SerializationTool<XmlMerchant> TOOL = SerializationTool.forClass(XmlMerchant.class,
		XmlSchemas.STATUS_REQUEST_SCHEMA);

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
	@HashCodePrime(139)
	public static class XmlOrder extends AXmlBase {

	    private static final long serialVersionUID = 1L;

	    @XmlAttribute(name = "id")
	    private final String orderId;

	    public String getOrderId() {
		return orderId;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlOrder() {
		super();
		this.orderId = null;
	    }

	    public XmlOrder(String orderId) {
		super();
		this.orderId = orderId;
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
    private final XmlSignGeneralWithCert merchantSign;

    public XmlSignGeneralWithCert getMerchantSign() {
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

    public XmlDocumentStatusRequest requreValidSignature(final X509Certificate certificate)
	    throws IllegalStateException, IllegalArgumentException {
	if (validSignature(certificate))
	    return this;
	throw MyExceptions.illegalStateFormat("Signature is invalid");
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlDocumentStatusRequest() {
	this.merchant = null;
	this.merchantSign = null;
    }

    public XmlDocumentStatusRequest(XmlMerchant merchant, XmlSignGeneralWithCert merchantSign) {
	super();
	this.merchant = merchant;
	this.merchantSign = merchantSign;
    }
}
