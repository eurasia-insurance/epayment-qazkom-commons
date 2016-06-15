package com.lapsa.kkb.xml.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class KKBTimestampXmlAdapter extends XmlAdapter<String, Date> {

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date unmarshal(String v) throws Exception {
	if (v == null)
	    return null;
	return format.parse(v.trim());
    }

    @Override
    public String marshal(Date v) throws Exception {
	if (v == null)
	    return null;
	return format.format(v);
    }
}
