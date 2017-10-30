package tech.lapsa.qazkom.xml;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "response")
public class XmlResponse extends AXmlBase {

    private static final long serialVersionUID = 1L;

    public XmlResponse() {
	super(67);
    }

    // order_id - номер заказа
    @XmlAttribute(name = "order_id")
    private String orderId;

    @XmlElementRef
    private XmlError error;

    @XmlElementRef
    private XmlSession session;

    // GENERATED

    public String getOrderId() {
	return orderId;
    }

    public void setOrderId(String orderId) {
	this.orderId = orderId;
    }

    public XmlError getError() {
	return error;
    }

    public void setError(XmlError error) {
	this.error = error;
    }

    public XmlSession getSession() {
	return session;
    }

    public void setSession(XmlSession session) {
	this.session = session;
    }

}
