package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyStrings;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "response")
@HashCodePrime(67)
public class XmlDocumentError extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlDocumentError> TOOL = SerializationTool.forClass(XmlDocumentError.class,
	    XmlSchemas.ERROR_SCHEMA);

    public static final SerializationTool<XmlDocumentError> getTool() {
	return TOOL;
    }

    public static XmlDocumentError of(final String rawXml) throws IllegalArgumentException {
	MyStrings.requireNonEmpty(rawXml, "rawXml");
	return TOOL.deserializeFrom(rawXml);
    }

    // order_id - номер заказа
    @XmlAttribute(name = "order_id")
    private final String orderId;

    public String getOrderId() {
	return orderId;
    }

    @XmlElement(name = "error")
    private final XmlError error;

    public XmlError getError() {
	return error;
    }

    @XmlElement(name = "session")
    private final XmlSession session;

    public XmlSession getSession() {
	return session;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlDocumentError() {
	this.orderId = null;
	this.error = null;
	this.session = null;
    }

    public XmlDocumentError(String orderId, XmlError error, XmlSession session) {
	this.orderId = orderId;
	this.error = error;
	this.session = session;
    }
}
