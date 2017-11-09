package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.lapsa.fin.FinCurrency;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MySignatures;
import tech.lapsa.java.commons.security.MySignatures.SigningSignature;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class XmlDocumentOrder extends AXmlBase {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 31;

    private static final SerializationTool<XmlDocumentOrder> TOOL = SerializationTool.forClass(XmlDocumentOrder.class,
	    XmlSchemas.ORDER_SCHEMA);

    public static final SerializationTool<XmlDocumentOrder> getTool() {
	return TOOL;
    }

    public static XmlDocumentOrder of(final String rawXml) {
	return TOOL.deserializeFrom(rawXml);
    }

    public static XmlDocumentOrderBuilder builder() {
	return new XmlDocumentOrderBuilder();
    }

    public static final class XmlDocumentOrderBuilder {

	private static final XmlSignType SIGN_TYPE = XmlSignType.RSA;

	private String orderNumber;
	private Double amount;
	private FinCurrency currency;
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

	public XmlDocumentOrderBuilder withCurrency(final FinCurrency currency) throws IllegalArgumentException {
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
	    this.merchantCertificate = MyObjects.requireNonNull(certificate, "certificate");
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

	    final XmlMerchant xmlMerchant = new XmlMerchant();
	    final BigInteger serialNumber = merchantCertificate.getSerialNumber();
	    xmlMerchant.setCertificateSerialNumber(serialNumber);
	    xmlMerchant.setName(merchantName);

	    final XmlOrder xmlOrder = new XmlOrder();
	    xmlMerchant.setOrder(xmlOrder);
	    xmlOrder.setOrderId(orderNumber);
	    xmlOrder.setAmount(amount);
	    xmlOrder.setFinCurrency(currency);
	    xmlOrder.setDepartments(new ArrayList<>());

	    final XmlDepartment xmlDepartment = new XmlDepartment();
	    xmlOrder.getDepartments().add(xmlDepartment);
	    xmlDepartment.setMerchantId(merchantId);
	    xmlDepartment.setAmount(amount);

	    String signatureAlgorithm = SIGN_TYPE.getSignatureAlgorithmName().get();

	    SigningSignature signature = MySignatures
		    .forSignature(MyObjects.requireNonNull(merchantKey, "merchantKey"), signatureAlgorithm)
		    .orElseThrow(() -> new IllegalArgumentException("Failed proces with private key"));

	    final byte[] data = XmlMerchant.getTool().serializeToBytes(xmlMerchant);
	    byte[] digest = signature.sign(data);
	    MyArrays.reverse(digest);

	    final XmlMerchantSign xmlMerchantSign = new XmlMerchantSign();
	    xmlMerchantSign.setSignType(SIGN_TYPE);
	    xmlMerchantSign.setSignature(digest);

	    final XmlDocumentOrder doc = new XmlDocumentOrder();
	    doc.setMerchant(xmlMerchant);
	    doc.setMerchantSign(xmlMerchantSign);

	    return doc;
	}

    }

    public boolean validSignature(final X509Certificate certificate) {

	String algorithmName = getMerchantSign().getSignType().getSignatureAlgorithmName() //
		.orElseThrow(() -> new IllegalArgumentException("No such algorithm"));

	VerifyingSignature signature = MySignatures
		.forVerification(MyObjects.requireNonNull(certificate, "certificate"), algorithmName) //
		.orElseThrow(() -> new IllegalArgumentException("Failed process with certificate"));

	if (merchant == null || merchantSign == null || merchantSign.getSignature() == null)
	    throw new IllegalStateException("Document is corrupted");

	final byte[] data = XmlMerchant.getTool().serializeToBytes(merchant);
	final byte[] digest = merchantSign.getSignature().clone();
	MyArrays.reverse(digest);
	return signature.verify(data, digest);
    }

    public XmlDocumentOrder requreValidSignature(final X509Certificate certificate) {
	if (validSignature(certificate))
	    return this;
	throw new IllegalStateException("Signature is invalid");
    }

    @Override
    protected int prime() {
	return PRIME;
    }

    @XmlElementRef
    private XmlMerchant merchant;

    @XmlElementRef
    private XmlMerchantSign merchantSign;

    public XmlMerchant getMerchant() {
	return merchant;
    }

    public void setMerchant(final XmlMerchant merchant) {
	this.merchant = merchant;
    }

    public XmlMerchantSign getMerchantSign() {
	return merchantSign;
    }

    public void setMerchantSign(final XmlMerchantSign merchantSign) {
	this.merchantSign = merchantSign;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }
}
