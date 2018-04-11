package tech.lapsa.epayment.qazkom.xml.bind;

import java.io.Serializable;
import java.time.Instant;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlTimestampAdapter;
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

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(41)
    public static class XmlError implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
	    return HcEqUtil.hashCode(this);
	}

	@Override
	public boolean equals(final Object other) {
	    return HcEqUtil.equals(this, other);
	}

	@XmlEnum
	public enum XmlErrorType {
	    // system - ошибка при работе в системе авторизации, например
	    // неправильно
	    // введеный параметр
	    @XmlEnumValue(value = "system")
	    SYSTEM,
	    // auth - ошибка авторизации, в данном случае указывается код ошибки
	    // в
	    // атрибуте code
	    @XmlEnumValue(value = "auth")
	    AUTHORIZATION;
	}

	@XmlAttribute(name = "type")
	private final XmlErrorType type;

	public XmlErrorType getType() {
	    return type;
	}

	// time - время отправки ответа
	@XmlAttribute(name = "time")
	@XmlJavaTypeAdapter(XmlTimestampAdapter.class)
	private final Instant time;

	public Instant getTime() {
	    return time;
	}

	// code - код ошибки для типа ошибки auth, в случае ошибки типа system
	// значение всегда равно 99
	@XmlAttribute(name = "code")
	private final String code;

	public String getCode() {
	    return code;
	}

	// текст ошибки
	@XmlValue
	private final String message;

	public String getMessage() {
	    return message;
	}

	/*
	 * Default no-args constructor due to JAXB requirements
	 */
	@Deprecated
	public XmlError() {
	    this.type = null;
	    this.time = null;
	    this.code = null;
	    this.message = null;
	}

	public XmlError(XmlErrorType type, Instant time, String code, String message) {
	    super();
	    this.type = type;
	    this.time = time;
	    this.code = code;
	    this.message = message;
	}
    }

    @XmlElement(name = "error")
    private final XmlError error;

    public XmlError getError() {
	return error;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(73)
    public static class XmlSession extends AXmlBase {

	private static final long serialVersionUID = 1L;

	@XmlAttribute(name = "id")
	private final String id;

	public String getId() {
	    return id;
	}

	/*
	 * Default no-args constructor due to JAXB requirements
	 */
	@Deprecated
	public XmlSession() {
	    this.id = null;
	}

	public XmlSession(String id) {
	    super();
	    this.id = id;
	}
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
