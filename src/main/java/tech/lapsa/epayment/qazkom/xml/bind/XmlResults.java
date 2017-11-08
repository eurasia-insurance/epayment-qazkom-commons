package tech.lapsa.epayment.qazkom.xml.bind;

import java.time.Instant;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlTimestampAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "results")
public class XmlResults extends AXmlBase {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 71;

    @Override
    protected int prime() {
	return PRIME;
    }

    // timestamp - время проведения платежа
    @XmlAttribute(name = "timestamp")
    @XmlJavaTypeAdapter(XmlTimestampAdapter.class)
    private Instant timestamp;

    @XmlElementRef
    private List<XmlPayment> payments;

    // GENERATED

    public Instant getTimestamp() {
	return timestamp;
    }

    public void setTimestamp(final Instant timestamp) {
	this.timestamp = timestamp;
    }

    public List<XmlPayment> getPayments() {
	return payments;
    }

    public void setPayments(final List<XmlPayment> payments) {
	this.payments = payments;
    }

}
