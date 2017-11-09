package tech.lapsa.epayment.qazkom.xml.bind;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "bank")
public class XmlBank extends AXmlBase {

    private static final long serialVersionUID = -5468834860872828233L;
    private static final int PRIME = 11;

    private static final SerializationTool<XmlBank> TOOL = SerializationTool.forClass(XmlBank.class,
	    XmlSchemas.PAYMENT_SCHEMA);

    public static final SerializationTool<XmlBank> getTool() {
	return TOOL;
    }

    private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
    private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE
	    | Pattern.DOTALL;
    private static final Pattern BANK_REGEX_PATTERN = Pattern.compile(BANK_REGEX, BANK_REGEX_FLAGS);

    public static String[] bankXmlElementsFrom(String rawXml) {
	Matcher matcher = BANK_REGEX_PATTERN.matcher(rawXml);
	Builder<String> sb = Stream.builder();
	while (matcher.find())
	    sb.accept(matcher.group());
	return sb.build().filter(xml -> {
	    try {
		return TOOL.deserializeFrom(xml) != null;
	    } catch (IllegalArgumentException e) {
		return false;
	    }

	}).toArray(String[]::new);
    }

    @Override
    protected int prime() {
	return PRIME;
    }

    @XmlAttribute(name = "name")
    private String name;

    @XmlElementRef
    private XmlCustomer customer;

    @XmlElementRef
    private XmlCustomerSign customerSign;

    @XmlElementRef
    private XmlResults results;

    // GENERATED

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public XmlCustomer getCustomer() {
	return customer;
    }

    public void setCustomer(final XmlCustomer customer) {
	this.customer = customer;
    }

    public XmlCustomerSign getCustomerSign() {
	return customerSign;
    }

    public void setCustomerSign(final XmlCustomerSign customerSign) {
	this.customerSign = customerSign;
    }

    public XmlResults getResults() {
	return results;
    }

    public void setResults(final XmlResults results) {
	this.results = results;
    }
}
