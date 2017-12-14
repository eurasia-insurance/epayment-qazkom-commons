package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyStrings;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "response")
public class XmlDocumentError extends AXmlBase {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 67;

    private static final SerializationTool<XmlDocumentError> TOOL = SerializationTool.forClass(XmlDocumentError.class,
	    XmlSchemas.ERROR_SCHEMA);

    public static final SerializationTool<XmlDocumentError> getTool() {
	return TOOL;
    }

    public static XmlDocumentError of(final String rawXml) throws IllegalArgumentException {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    @Override
    protected int prime() {
	return PRIME;
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

    public void setOrderId(final String orderId) {
	this.orderId = orderId;
    }

    public XmlError getError() {
	return error;
    }

    public void setError(final XmlError error) {
	this.error = error;
    }

    public XmlSession getSession() {
	return session;
    }

    public void setSession(final XmlSession session) {
	this.session = session;
    }

}
