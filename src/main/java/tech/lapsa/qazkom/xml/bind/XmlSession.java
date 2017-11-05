package tech.lapsa.qazkom.xml.bind;

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

    public XmlSession() {
	super(73);
    }

    @XmlAttribute(name = "id")
    private String id;

    // GENERATED

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }
}