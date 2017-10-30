package com.lapsa.kkb.xml.adapter;

import java.math.BigInteger;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class KKBCertificateSeriaNumberToHEXStringXmlAdapter extends XmlAdapter<String, BigInteger> {

    private static final int RADIX = 16;

    @Override
    public BigInteger unmarshal(String v) throws Exception {
	if (v == null)
	    return null;
	return new BigInteger(v, RADIX);
    }

    @Override
    public String marshal(BigInteger v) throws Exception {
	if (v == null)
	    return null;
	return v.toString(RADIX).toLowerCase();
    }
}
