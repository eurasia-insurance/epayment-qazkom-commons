package com.lapsa.kkb.xml.adapter;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class KKBAmountXmlAdapter extends XmlAdapter<String, Double> {
    private static final NumberFormat format;

    static {
	format = NumberFormat.getNumberInstance(Locale.ENGLISH);
	format.setMaximumFractionDigits(2);
	format.setMinimumFractionDigits(0);
	format.setGroupingUsed(false);
	format.setRoundingMode(RoundingMode.UNNECESSARY);
    }

    @Override
    public Double unmarshal(String v) throws Exception {
	if (v == null)
	    return null;
	return format.parse(v).doubleValue();
    }

    @Override
    public String marshal(Double v) throws Exception {
	if (v == null)
	    return null;
	return format.format(v);
    }

    public static void main(String[] args) throws Exception {
	KKBAmountXmlAdapter z = new KKBAmountXmlAdapter();
	System.out.println(z.marshal(1234.00d));
	System.out.println(z.marshal(1234.12d));

	System.out.println(z.unmarshal(z.marshal(1234d)));
    }
}
