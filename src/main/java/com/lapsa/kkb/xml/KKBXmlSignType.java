package com.lapsa.kkb.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum KKBXmlSignType {
    // Если Тип подписи RSA то это цифровая подпись
    @XmlEnumValue(value = "RSA") RSA,

    @XmlEnumValue(value = "SHA/RSA") SHA_RSA,
    @XmlEnumValue(value = "SHA") SHA,

    // Если Тип подписи SSL то это серийный номер сертификата
    @XmlEnumValue(value = "SSL") CERTIFICATE,

    // Если Тип подписи none то поле остается пустым
    @XmlEnumValue(value = "none") NONE;
}
