package tech.lapsa.epayment.qazkom.xml.bind.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.lapsa.international.country.Country;

public class XmlCountryAlpha3CodeAdapter extends XmlAdapter<String, Country> {

    @Override
    public Country unmarshal(final String v) throws Exception {
	if (v == null)
	    return null;
	final Country c = Country.forAlpha3Code(v);
	if (c != null)
	    return c;
	return Country.OTHR;
    }

    @Override
    public String marshal(final Country v) throws Exception {
	if (v == null)
	    return null;
	return v.getAlpha3Code();
    }

}
