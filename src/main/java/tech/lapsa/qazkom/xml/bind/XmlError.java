package tech.lapsa.qazkom.xml.bind;

import java.time.Instant;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import tech.lapsa.qazkom.xml.bind.adapter.XmlTimestampAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "error")
public class XmlError extends AXmlBase {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 41;

    @Override
    protected int prime() {
	return PRIME;
    }

    // type - тип ошибки:
    // system - ошибка при работе в системе авторизации, например неправильно
    // введеный параметр
    // auth - ошибка авторизации, в данном случае указывается код ошибки в
    // атрибуте code
    @XmlAttribute(name = "type")
    private XmlErrorType type;

    // time - время отправки ответа
    @XmlAttribute(name = "time")
    @XmlJavaTypeAdapter(XmlTimestampAdapter.class)
    private Instant time;

    // code - код ошибки для типа ошибки auth, в случае ошибки типа system
    // значение всегда равно 99
    @XmlAttribute(name = "code")
    private String code;

    // текст ошибки
    @XmlValue
    private String message;

    // GENERATED

    public XmlErrorType getType() {
	return type;
    }

    public void setType(final XmlErrorType type) {
	this.type = type;
    }

    public Instant getTime() {
	return time;
    }

    public void setTime(final Instant time) {
	this.time = time;
    }

    public String getCode() {
	return code;
    }

    public void setCode(final String code) {
	this.code = code;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(final String message) {
	this.message = message;
    }
}
