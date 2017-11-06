package tech.lapsa.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "customer_sign")
public class XmlCustomerSign extends AXmlSignBase {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 19;

    @Override
    protected int prime() {
	return PRIME;
    }
}