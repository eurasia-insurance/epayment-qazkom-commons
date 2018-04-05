package tech.lapsa.epayment.qazkom.xml.bind;

import java.io.Serializable;
import java.time.Instant;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.epayment.qazkom.xml.bind.adapter.XmlTimestampAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(41)
public class XmlError implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
	return HcEqUtil.hashCode(this);
    }

    @Override
    public boolean equals(final Object other) {
	return HcEqUtil.equals(this, other);
    }

    // type - тип ошибки:
    // system - ошибка при работе в системе авторизации, например неправильно
    // введеный параметр
    // auth - ошибка авторизации, в данном случае указывается код ошибки в
    // атрибуте code
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
