package tech.lapsa.epayment.qazkom.xml.bind;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.lapsa.international.country.Country;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCardExpirationDateAdapter;
import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlCountryAlpha3CodeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(61)
public class XmlPayment extends AXmlAmount {

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
    // Должен иметь значение "00" (два нуля), в противном случае свяжитесь,
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

    public XmlPayment(Double amount, String merchantId, String reference, String approvalCode, String responseCode,
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
