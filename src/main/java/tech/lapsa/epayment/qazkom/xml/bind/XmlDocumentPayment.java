package tech.lapsa.epayment.qazkom.xml.bind;

import java.security.cert.X509Certificate;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.security.MySignatures.VerifyingSignature;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class XmlDocumentPayment extends AXmlBase {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 37;

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
	private VerifyingSignature signature;
	private X509Certificate certificate;

	private XmlDocumentPaymentBuilder() {
	}

	public XmlDocumentPaymentBuilder ofRawXml(String rawXml) {
	    this.rawXml = MyStrings.requireNonEmpty(rawXml, "rawXml");
	    return this;
	}

	public XmlDocumentPaymentBuilder checkingSignatureWith(VerifyingSignature signature) {
	    this.signature = MyObjects.requireNonNull(signature, "signature");
	    return this;
	}

	public XmlDocumentPaymentBuilder checkingCertificateWith(X509Certificate certificate) {
	    this.certificate = MyObjects.requireNonNull(certificate, "certificate");
	    return this;
	}

	public XmlDocumentPayment build() {
	    MyStrings.requireNonEmpty(rawXml, "rawXml");
	    XmlDocumentPayment document = TOOL.deserializeFrom(rawXml);

	    if (MyObjects.nonNull(signature)) {
		String[] banks = XmlBank.bankXmlElementsFrom(rawXml);
		if (banks.length != 1)
		    throw new IllegalArgumentException(
			    "Failed to parse for single element <bank> from source XML document");
		byte[] data = banks[0].getBytes();
		byte[] digest = document.getBankSign().getSignature();
		MyArrays.reverse(digest);
		boolean signatureValid = signature.verify(data, digest);
		if (!signatureValid)
		    throw new IllegalStateException("Signature is invalid");
	    }
	    if (MyObjects.nonNull(certificate)) {
		boolean certNumberValid = certificate.getSerialNumber()
			.equals(document.getBankSign().getCertificateSerialNumber());
		if (!certNumberValid)
		    throw new IllegalStateException("Certificate serial number is not valid");
	    }
	    return document;
	}

	public XmlDocumentPaymentBuilder checkingWith(X509Certificate certificate, VerifyingSignature signature) {
	    checkingCertificateWith(certificate);
	    checkingSignatureWith(signature);
	    return this;
	}

    }

    @Override
    protected int prime() {
	return PRIME;
    }

    @XmlElementRef
    private XmlBank bank;

    @XmlElementRef
    private XmlBankSign bankSign;

    public XmlBank getBank() {
	return bank;
    }

    public void setBank(final XmlBank bank) {
	this.bank = bank;
    }

    public XmlBankSign getBankSign() {
	return bankSign;
    }

    public void setBankSign(final XmlBankSign bankSign) {
	this.bankSign = bankSign;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }
}
