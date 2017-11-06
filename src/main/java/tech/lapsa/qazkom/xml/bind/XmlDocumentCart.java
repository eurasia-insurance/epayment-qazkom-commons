package tech.lapsa.qazkom.xml.bind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.qazkom.xml.schema.XmlSchemas;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class XmlDocumentCart extends AXmlBase {

    private static final long serialVersionUID = 1L;
    private static final XmlDocumentTool<XmlDocumentCart> TOOL = XmlDocumentTool.forClass(XmlDocumentCart.class,
	    XmlSchemas.CART_SCHEMA);

    public static XmlDocumentCart of(String rawXml) {
	return TOOL.deserializeFrom(rawXml);
    }

    public XmlDocumentCart() {
	super(29);
    }

    @XmlElementRef
    private List<XmlItem> items;

    public List<XmlItem> getItems() {
	return items;
    }

    public void setItems(List<XmlItem> items) {
	this.items = items;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }

    public static XmlDocumentCartBuilder builder() {
	return new XmlDocumentCartBuilder();
    }

    public static final class XmlDocumentCartBuilder {
	private XmlDocumentCartBuilder() {
	}

	private class Its {
	    private final String name;
	    private final Integer quantity;
	    private final Double amount;

	    private Its(String name, Integer quantity, Double amount) {
		this.name = name;
		this.quantity = quantity;
		this.amount = amount;
	    }
	}

	private List<Its> itms = new ArrayList<>();

	public XmlDocumentCartBuilder withItem(String name, Integer quantity, Double amount) {
	    MyStrings.requireNonEmpty(name, "name");
	    MyNumbers.requireNonZero(quantity, "quantity");
	    MyNumbers.requireNonZero(amount, "amount");
	    itms.add(new Its(name, quantity, amount));
	    return this;
	}

	public <T> XmlDocumentCartBuilder withItems(Collection<T> items, Function<T, String> nameMapper,
		Function<T, Integer> quantityMapper,
		Function<T, Double> amountMapper) {
	    MyCollections.requireNonEmpty(items, "items");
	    MyObjects.requireNonNull(nameMapper, "nameMapper");
	    MyObjects.requireNonNull(quantityMapper, "quantityMapper");
	    MyObjects.requireNonNull(amountMapper, "amountMapper");

	    MyOptionals.streamOf(items) //
		    .get()
		    .forEach(t -> withItem(nameMapper.apply(t), quantityMapper.apply(t), amountMapper.apply(t)));

	    return this;
	}

	public XmlDocumentCart build() {
	    AtomicInteger i = new AtomicInteger(1);
	    XmlDocumentCart doc = new XmlDocumentCart();
	    doc.setItems(MyOptionals.streamOf(itms) //
		    .orElseThrow(() -> new IllegalArgumentException("Cart must have at least one item")) //
		    .map(t -> {
			XmlItem r = new XmlItem();
			r.setName(t.name);
			r.setAmount(t.amount);
			r.setQuantity(t.quantity);
			r.setNumber(i.getAndIncrement());
			return r;
		    }) //
		    .collect(MyCollectors.unmodifiableList()));
	    return doc;
	}
    }
}
