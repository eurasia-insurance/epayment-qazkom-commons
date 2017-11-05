package tech.lapsa.qazkom.xml.bind;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement
public abstract class AXmlBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlTransient
    private final int PRIME;
    
    @XmlTransient
    private final int MULTIPLIER;

    public AXmlBase(int prime, int multiplier) {
	this.PRIME = prime;
	this.MULTIPLIER = multiplier;
    }

    public AXmlBase(int prime) {
	this(prime, prime);
    }
    
    public AXmlBase() {
	this(79);
    }

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(PRIME, MULTIPLIER, this, false);
    }

    @Override
    public boolean equals(Object other) {
	return EqualsBuilder.reflectionEquals(this, other, false);
    }
}
