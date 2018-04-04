package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "department")
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
@HashCodePrime(103)
public class XmlDepartment extends AXmlAmount {

    private static final long serialVersionUID = 1L;

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

    // GENERATED

    public String getMerchantId() {
	return merchantId;
    }

    public void setMerchantId(final String merchantId) {
	this.merchantId = merchantId;
    }

    public String getAbonentId() {
	return abonentId;
    }

    public void setAbonentId(final String abonentId) {
	this.abonentId = abonentId;
    }

    public String getTerminal() {
	return terminal;
    }

    public void setTerminal(final String terminal) {
	this.terminal = terminal;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(final String phone) {
	this.phone = phone;
    }

    public String getAirticketBookingNumber() {
	return airticketBookingNumber;
    }

    public void setAirticketBookingNumber(final String airticketBookingNumber) {
	this.airticketBookingNumber = airticketBookingNumber;
    }
}
