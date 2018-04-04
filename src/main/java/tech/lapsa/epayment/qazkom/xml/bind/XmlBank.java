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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "bank")
@HashCodePrime(11)
public class XmlBank extends AXmlBase {

    private static final long serialVersionUID = -5468834860872828233L;

    private static final SerializationTool<XmlBank> TOOL = SerializationTool.forClass(XmlBank.class,
	    XmlSchemas.PAYMENT_SCHEMA);

    public static final SerializationTool<XmlBank> getTool() {
	return TOOL;
    }

    private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
    private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE
	    | Pattern.DOTALL;
    private static final Pattern BANK_REGEX_PATTERN = Pattern.compile(BANK_REGEX, BANK_REGEX_FLAGS);

    public static String[] bankXmlElementsFrom(final String rawXml) {
	final Matcher matcher = BANK_REGEX_PATTERN.matcher(rawXml);
	final Builder<String> sb = Stream.builder();
	while (matcher.find())
	    sb.accept(matcher.group());
	return sb.build().filter(xml -> {
	    try {
		return TOOL.deserializeFrom(xml) != null;
	    } catch (final IllegalArgumentException e) {
		return false;
	    }

	}).toArray(String[]::new);
    }

    @XmlAttribute(name = "name")
    private final String name;

    public String getName() {
	return name;
    }

    @XmlElement(name = "customer")
    private final XmlCustomer customer;

    public XmlCustomer getCustomer() {
	return customer;
    }

    @XmlElement(name = "customer_sign")
    private final XmlCustomerSign customerSign;

    public XmlCustomerSign getCustomerSign() {
	return customerSign;
    }

    @XmlElement(name = "results")
    private final XmlResults results;

    public XmlResults getResults() {
	return results;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlBank() {
	this.name = null;
	this.customer = null;
	this.customerSign = null;
	this.results = null;
    }

    public XmlBank(final String name,
	    final XmlCustomer customer,
	    final XmlCustomerSign customerSign,
	    final XmlResults results) {
	super();
	this.name = name;
	this.customer = customer;
	this.customerSign = customerSign;
	this.results = results;
    }
}
