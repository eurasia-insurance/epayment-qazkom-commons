package com.lapsa.kkb.xml;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class KKBXmlBase implements Serializable {
    private static final long serialVersionUID = -1060980014181989772L;

    protected abstract int getPrime();

    protected abstract int getMultiplier();

    @Override
    public int hashCode() {
	return HashCodeBuilder.reflectionHashCode(getPrime(), getMultiplier(), this, false);
    }

    @Override
    public boolean equals(Object other) {
	if (other == null || other.getClass() != getClass())
	    return false;
	if (other == this)
	    return true;
	return EqualsBuilder.reflectionEquals(this, other, false);
    }
}
