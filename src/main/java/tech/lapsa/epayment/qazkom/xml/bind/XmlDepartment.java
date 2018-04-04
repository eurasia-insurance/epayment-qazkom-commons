package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlAccessorType(XmlAccessType.FIELD)
@HashCodePrime(103)
public class XmlDepartment extends AXmlAmount {

    private static final long serialVersionUID = 1L;

    // merchant_id - ID продавца в платежной системе
    @XmlAttribute(name = "merchant_id")
    private final String merchantId;

    public String getMerchantId() {
	return merchantId;
    }

    // abonent_id - дополнительные поля для продавца, можно не указывать
    @XmlAttribute(name = "abonent_id")
    private final String abonentId;

    public String getAbonentId() {
	return abonentId;
    }

    // terminal - дополнительные поля для продавца, можно не указывать
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

    // RL - дополнительное поле, для транспортных компаний- Номер брони, можно
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
