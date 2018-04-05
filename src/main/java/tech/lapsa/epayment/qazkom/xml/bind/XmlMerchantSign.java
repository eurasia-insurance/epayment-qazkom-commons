package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(53)
public class XmlMerchantSign extends AXmlSignBase {

    private static final long serialVersionUID = 1L;

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlMerchantSign() {
	super(null);
    }

    public XmlMerchantSign(XmlSignType signType) {
	super(signType);
    }

    public XmlMerchantSign(final XmlSignType signType, final byte[] signature) {
	super(signType, signature);
    }

    public XmlMerchantSign(final XmlSignType signType, final String signatureEncoded) {
	super(signType, signatureEncoded);
    }

}
