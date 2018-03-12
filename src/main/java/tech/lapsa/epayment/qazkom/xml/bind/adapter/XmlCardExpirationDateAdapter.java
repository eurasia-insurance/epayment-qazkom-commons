package tech.lapsa.epayment.qazkom.xml.bind.adapter;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import tech.lapsa.java.commons.function.MyExceptions;

public class XmlCardExpirationDateAdapter extends XmlAdapter<String, LocalDate> {

    // 11/2022
    @Override
    public LocalDate unmarshal(String v) throws Exception {
	if (v == null)
	    return null;
	final String[] c = v.split("/");
	if (c.length != 2)
	    throw MyExceptions.illegalArgumentFormat("Invalid card expiration date format '%1$s'", v);
	int year = Integer.parseInt(c[1]);
	int month = Integer.parseInt(c[0]);
	return LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
	if (v == null)
	    return null;
	return v.getMonthValue() + "/" + v.getYear();
    }
}
