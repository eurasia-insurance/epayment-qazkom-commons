package tech.lapsa.epayment.qazkom.xml.bind;

import java.time.Instant;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlTimestampAdapter;
import tech.lapsa.java.commons.function.MyCollections;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(71)
public class XmlResults extends AXmlBase {

    private static final long serialVersionUID = 1L;

    // timestamp - время проведения платежа
    @XmlAttribute(name = "timestamp")
    @XmlJavaTypeAdapter(XmlTimestampAdapter.class)
    private final Instant timestamp;

    public Instant getTimestamp() {
	return timestamp;
    }

    @XmlElement(name = "payment")
    private final List<XmlPayment> payments;

    public List<XmlPayment> getPayments() {
	return payments;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlResults() {
	this.timestamp = null;
	this.payments = null;
    }

    public XmlResults(Instant timestamp, List<XmlPayment> payments) {
	super();
	this.timestamp = timestamp;
	this.payments = payments == null ? null : MyCollections.unmodifiableOrEmptyList(payments);
    }
}
