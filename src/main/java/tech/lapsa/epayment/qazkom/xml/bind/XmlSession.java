package tech.lapsa.epayment.qazkom.xml.bind;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "session")
public class XmlSession extends AXmlBase {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 73;

    @Override
    protected int prime() {
	return PRIME;
    }

    @XmlAttribute(name = "id")
    private String id;

    // GENERATED

    public String getId() {
	return id;
    }

    public void setId(final String id) {
	this.id = id;
    }
}