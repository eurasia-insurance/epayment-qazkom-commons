package com.lapsa.kkb.xml;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement(name = "department")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
public class KKBXmlDepartment extends KKBXmlGenericAmount {
    private static final long serialVersionUID = -1021696352173990619L;
    private static final int PRIME =  13;
    private static final int MULTIPLIER = 13;

    // merchant_id - ID продавца в платежной системе
    @XmlAttribute(name = "merchant_id")
    private String merchantId;

    // abonent_id - дополнительные поля для продавца, можно не указывать
    @XmlAttribute(name = "abonent_id")
    private String abonentId;

    // terminal - дополнительные поля для продавца, можно не указывать
    @XmlAttribute(name = "terminal")
    private String terminal;

    // phone - дополнительные поля для продавца, можно не указывать
    @XmlAttribute(name = "phone")
    private String phone;

    // RL - дополнительное поле, для транспортных компаний- Номер брони, можно
    // не указывать. Транслуруется по всем отчетам и выпискам
    @XmlAttribute(name = "RL")
    private String airticketBookingNumber;

    @Override
    protected int getPrime() {
	return PRIME;
    }

    @Override
    protected int getMultiplier() {
	return MULTIPLIER;
    }

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	KKBXmlDepartment that = (KKBXmlDepartment) other;
	return new EqualsBuilder()
		.appendSuper(super.equals(that))
		.append(merchantId, that.merchantId)
		.append(abonentId, that.abonentId)
		.append(terminal, that.terminal)
		.append(phone, that.phone)
		.append(airticketBookingNumber, that.airticketBookingNumber)
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder(getPrime(), getMultiplier())
		.appendSuper(super.hashCode())
		.append(merchantId)
		.append(abonentId)
		.append(terminal)
		.append(phone)
		.append(airticketBookingNumber)
		.toHashCode();
    }

    // GENERATED

    public String getMerchantId() {
	return merchantId;
    }

    public void setMerchantId(String merchantId) {
	this.merchantId = merchantId;
    }

    public String getAbonentId() {
	return abonentId;
    }

    public void setAbonentId(String abonentId) {
	this.abonentId = abonentId;
    }

    public String getTerminal() {
	return terminal;
    }

    public void setTerminal(String terminal) {
	this.terminal = terminal;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getAirticketBookingNumber() {
	return airticketBookingNumber;
    }

    public void setAirticketBookingNumber(String airticketBookingNumber) {
	this.airticketBookingNumber = airticketBookingNumber;
    }
}
