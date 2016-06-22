package com.lapsa.kkb.xml;

import java.io.Serializable;

public abstract class KKBXmlBase implements Serializable {
    private static final long serialVersionUID = -1060980014181989772L;

    protected abstract int getPrime();

    protected abstract int getMultiplier();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object other);

}
