package com.lapsa.kkb.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum KKBXmlErrorType {
    // system - ошибка при работе в системе авторизации, например неправильно
    // введеный параметр
    @XmlEnumValue(value = "system") SYSTEM,
    // auth - ошибка авторизации, в данном случае указывается код ошибки в
    // атрибуте code
    @XmlEnumValue(value = "auth") AUTHORIZATION;
}
