package com.lapsa.kkb.xml;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.lapsa.kkb.xml.adapter.KKBTimestampXmlAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "error")
public class KKBXmlError implements Serializable {
    private static final long serialVersionUID = -5333156242528681085L;

    // type - тип ошибки:
    // system - ошибка при работе в системе авторизации, например неправильно
    // введеный параметр
    // auth - ошибка авторизации, в данном случае указывается код ошибки в
    // атрибуте code
    @XmlAttribute(name = "type")
    private KKBXmlErrorType type;

    // time - время отправки ответа
    @XmlAttribute(name = "time")
    @XmlJavaTypeAdapter(value = KKBTimestampXmlAdapter.class)
    private Date time;

    // code - код ошибки для типа ошибки auth, в случае ошибки типа system
    // значение всегда равно 99
    @XmlAttribute(name = "code")
    private String code;

    // текст ошибки
    @XmlValue
    private String message;

    // GENERATED

    public KKBXmlErrorType getType() {
	return type;
    }

    public void setType(KKBXmlErrorType type) {
	this.type = type;
    }

    public Date getTime() {
	return time;
    }

    public void setTime(Date time) {
	this.time = time;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }
}
