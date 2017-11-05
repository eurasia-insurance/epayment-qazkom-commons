package tech.lapsa.qazkom.xml.bind;

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

import tech.lapsa.qazkom.xml.bind.adapter.XmlTimestampAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "results")
public class XmlResults extends AXmlBase {

    private static final long serialVersionUID = 1L;

    public XmlResults() {
	super(71);
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

    public void setTimestamp(Instant timestamp) {
	this.timestamp = timestamp;
    }

    public List<XmlPayment> getPayments() {
	return payments;
    }

    public void setPayments(List<XmlPayment> payments) {
	this.payments = payments;
    }

}
