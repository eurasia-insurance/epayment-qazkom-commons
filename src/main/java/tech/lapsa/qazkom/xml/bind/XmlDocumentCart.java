package tech.lapsa.qazkom.xml.bind;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class XmlDocumentCart extends AXmlBase {

    private static final long serialVersionUID = 1L;

    public XmlDocumentCart() {
	super(29);
    }

    @XmlElementRef
    private List<XmlItem> items;

    // GENERATED

    public List<XmlItem> getItems() {
	return items;
    }

    public void setItems(List<XmlItem> items) {
	this.items = items;
    }
}
