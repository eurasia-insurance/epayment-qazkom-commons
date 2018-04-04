package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@HashCodePrime(73)
public class XmlSession extends AXmlBase {

    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "id")
    private final String id;

    public String getId() {
	return id;
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlSession() {
	this.id = null;
    }

    public XmlSession(String id) {
	super();
	this.id = id;
    }
}