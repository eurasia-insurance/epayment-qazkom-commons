package tech.lapsa.epayment.qazkom.xml.bind;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class AXmlBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int hashCode() {
	return HcEqUtil.hashCode(this);
    }

    @Override
    public boolean equals(final Object other) {
	return HcEqUtil.equals(this, other);
    }
}
