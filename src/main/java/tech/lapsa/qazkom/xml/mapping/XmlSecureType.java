package tech.lapsa.qazkom.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum XmlSecureType {
    @XmlEnumValue("Yes") SECURED_3D,
    @XmlEnumValue("No") NON_SECURED;
}
