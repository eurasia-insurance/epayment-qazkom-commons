package tech.lapsa.epayment.qazkom.xml.bind;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder.XmlMerchant.XmlOrder;
import tech.lapsa.epayment.qazkom.xml.bind.XmlDocumentOrder.XmlMerchant.XmlOrder.XmlDepartment;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCertificateSeriaNumberToHEXStringAdapter;
import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyArrays;
import tech.lapsa.java.commons.function.MyCollections;
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

	    final XmlSignGeneral xmlMerchantSign = new XmlSignGeneral(SIGN_TYPE, digest);

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

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @XmlRootElement(name = "merchant")
    @HashCodePrime(47)
    public static class XmlMerchant extends AXmlBase {

	private static final long serialVersionUID = 1L;

	private static final SerializationTool<XmlMerchant> TOOL = SerializationTool.forClass(XmlMerchant.class,
		XmlSchemas.ORDER_SCHEMA);

	public static final SerializationTool<XmlMerchant> getTool() {
	    return TOOL;
	}

	public static XmlMerchant of(final String rawXml) {
	    return TOOL.deserializeFrom(rawXml);
	}

	// cert_id - Серийный номер сертификата
	@XmlAttribute(name = "cert_id")
	@XmlJavaTypeAdapter(XmlCertificateSeriaNumberToHEXStringAdapter.class)
	private final BigInteger certificateSerialNumber;

	public BigInteger getCertificateSerialNumber() {
	    return certificateSerialNumber;
	}

	// name - имя магазина(сайта)
	@XmlAttribute(name = "name")
	private final String name;

	public String getName() {
	    return name;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	@HashCodePrime(49)
	public static class XmlOrder extends AXmlAmountAndCurrency {

	    private static final long serialVersionUID = 1L;

	    // order_id - Номер заказа(должен состоять не менее чем из 6
	    // ЧИСЛОВЫХ
	    // знаков, максимально -15)
	    @XmlAttribute(name = "order_id")
	    private final String orderId;

	    public String getOrderId() {
		return orderId;
	    }

	    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
	    @XmlAccessorType(XmlAccessType.FIELD)
	    @HashCodePrime(103)
	    public static class XmlDepartment extends AXmlAmount {

		private static final long serialVersionUID = 1L;

		// merchant_id - ID продавца в платежной системе
		@XmlAttribute(name = "merchant_id")
		private final String merchantId;

		public String getMerchantId() {
		    return merchantId;
		}

		// abonent_id - дополнительные поля для продавца, можно не
		// указывать
		@XmlAttribute(name = "abonent_id")
		private final String abonentId;

		public String getAbonentId() {
		    return abonentId;
		}

		// terminal - дополнительные поля для продавца, можно не
		// указывать
		@XmlAttribute(name = "terminal")
		private final String terminal;

		public String getTerminal() {
		    return terminal;
		}

		// phone - дополнительные поля для продавца, можно не указывать
		@XmlAttribute(name = "phone")
		private final String phone;

		public String getPhone() {
		    return phone;
		}

		// RL - дополнительное поле, для транспортных компаний- Номер
		// брони, можно
		// не указывать. Транслуруется по всем отчетам и выпискам
		@XmlAttribute(name = "RL")
		private final String airticketBookingNumber;

		public String getAirticketBookingNumber() {
		    return airticketBookingNumber;
		}

		/*
		 * Default no-args constructor due to JAXB requirements
		 */
		@Deprecated
		public XmlDepartment() {
		    super(null);
		    this.merchantId = null;
		    this.abonentId = null;
		    this.terminal = null;
		    this.phone = null;
		    this.airticketBookingNumber = null;
		}

		public XmlDepartment(Double amount, String merchantId, String abonentId, String terminal, String phone,
			String airticketBookingNumber) {
		    super(amount);
		    this.merchantId = merchantId;
		    this.abonentId = abonentId;
		    this.terminal = terminal;
		    this.phone = phone;
		    this.airticketBookingNumber = airticketBookingNumber;
		}

		public XmlDepartment(Double amount, String merchantId) {
		    super(amount);
		    this.merchantId = merchantId;
		    this.abonentId = null;
		    this.terminal = null;
		    this.phone = null;
		    this.airticketBookingNumber = null;
		}
	    }

	    @XmlElement(name = "department")
	    private final List<XmlDepartment> departments;

	    public List<XmlDepartment> getDepartments() {
		return departments;
	    }

	    /*
	     * Default no-args constructor due to JAXB requirements
	     */
	    @Deprecated
	    public XmlOrder() {
		super(null, null);
		this.orderId = null;
		this.departments = null;
	    }

	    public XmlOrder(Double amount, Currency currency, String orderId, List<XmlDepartment> departments) {
		super(amount, currency);
		this.orderId = orderId;
		this.departments = departments == null ? null : MyCollections.unmodifiableOrEmptyList(departments);
	    }
	}

	@XmlElement(name = "order")
	private final XmlOrder order;

	public XmlOrder getOrder() {
	    return order;
	}

	public String getRawXml() {
	    return TOOL.serializeToString(this);
	}

	/*
	 * Default no-args constructor due to JAXB requirements
	 */
	@Deprecated
	public XmlMerchant() {
	    this.certificateSerialNumber = null;
	    this.name = null;
	    this.order = null;
	}

	public XmlMerchant(BigInteger certificateSerialNumber, String name, XmlOrder order) {
	    super();
	    this.certificateSerialNumber = certificateSerialNumber;
	    this.name = name;
	    this.order = order;
	}
    }

    @XmlElement(name = "merchant")
    private final XmlMerchant merchant;

    public XmlMerchant getMerchant() {
	return merchant;
    }

    @XmlElement(name = "merchant_sign")
    private final XmlSignGeneral merchantSign;

    public XmlSignGeneral getMerchantSign() {
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

    public XmlDocumentOrder(XmlMerchant merchant, XmlSignGeneral merchantSign) {
	super();
	this.merchant = merchant;
	this.merchantSign = merchantSign;
    }
}
