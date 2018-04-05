package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(19)
public class XmlCustomerSign extends AXmlSignBase {

    private static final long serialVersionUID = 1L;

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlCustomerSign() {
	super(null);
    }

    public XmlCustomerSign(XmlSignType signType, byte[] signature) {
	super(signType, signature);
    }

    public XmlCustomerSign(XmlSignType signType, String signatureEncoded) {
	super(signType, signatureEncoded);
    }
}