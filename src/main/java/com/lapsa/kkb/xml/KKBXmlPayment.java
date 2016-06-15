package com.lapsa.kkb.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.lapsa.country.Country;
import com.lapsa.country.CountryXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "payment")
public class KKBXmlPayment extends KKBXmlGenericAmount implements Serializable {
    private static final long serialVersionUID = 7475444480605786934L;

    // Результат транзакции
    // merchant_id - ID продавца в платежной системе
    @XmlAttribute(name = "merchant_id")
    private String merchantId;

    // reference - номер обращения к платежной системе
    @XmlAttribute(name = "reference")
    private String reference;

    // approval_code - код авторизации
    @XmlAttribute(name = "approval_code")
    private String approvalCode;

    // response_code - код результата авторизации.
    // Должен иметь значение "00" (два нуля), в противном случае свяжитесь,
    // пожалуйста, с администратором системы авторизации
    @XmlAttribute(name = "response_code")
    private String responseCode;

    // Secure- Yes/No признак, что транзакция была 3DSecure или нет
    @XmlAttribute(name = "Secure")
    private KKBXmlSecureType secureType;

    // card_bin- Страна эмитент карты
    @XmlAttribute(name = "card_bin")
    @XmlJavaTypeAdapter(CountryXmlAdapter.class)
    private Country cardCountry;

    // card - маскированный номер карты
    @XmlAttribute(name = "card")
    private String cardNumberMasked;

    // c_hash- Хэш карты
    @XmlAttribute(name = "c_hash")
    private String cardHash;

    // GENERATED

    public String getMerchantId() {
	return merchantId;
    }

    public void setMerchantId(String merchantId) {
	this.merchantId = merchantId;
    }

    public String getReference() {
	return reference;
    }

    public void setReference(String reference) {
	this.reference = reference;
    }

    public String getApprovalCode() {
	return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
	this.approvalCode = approvalCode;
    }

    public String getResponseCode() {
	return responseCode;
    }

    public void setResponseCode(String responseCode) {
	this.responseCode = responseCode;
    }

    public KKBXmlSecureType getSecureType() {
	return secureType;
    }

    public void setSecureType(KKBXmlSecureType secureType) {
	this.secureType = secureType;
    }

    public Country getCardCountry() {
	return cardCountry;
    }

    public void setCardCountry(Country cardCountry) {
	this.cardCountry = cardCountry;
    }

    public String getCardNumberMasked() {
	return cardNumberMasked;
    }

    public void setCardNumberMasked(String cardNumberMasked) {
	this.cardNumberMasked = cardNumberMasked;
    }

    public String getCardHash() {
	return cardHash;
    }

    public void setCardHash(String cardHash) {
	this.cardHash = cardHash;
    }
}
