package tech.lapsa.epayment.qazkom.xml.bind;

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

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
public class XmlDocumentCart extends AXmlBase {

    private static final long serialVersionUID = 1L;
    private static final int PRIME = 29;

    private static final SerializationTool<XmlDocumentCart> TOOL = SerializationTool.forClass(XmlDocumentCart.class,
	    XmlSchemas.CART_SCHEMA);

    public static XmlDocumentCart of(final String rawXml) {
	return TOOL.deserializeFrom(rawXml);
    }

    @Override
    protected int prime() {
	return PRIME;
    }

    @XmlElementRef
    private List<XmlItem> items;

    public List<XmlItem> getItems() {
	return items;
    }

    public void setItems(final List<XmlItem> items) {
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

	    private Its(final String name, final Integer quantity, final Double amount) {
		this.name = name;
		this.quantity = quantity;
		this.amount = amount;
	    }
	}

	private final List<Its> itms = new ArrayList<>();

	public XmlDocumentCartBuilder withItem(final String name, final Integer quantity, final Double amount) {
	    MyStrings.requireNonEmpty(name, "name");
	    MyNumbers.requirePositive(quantity, "quantity");
	    MyNumbers.requirePositive(amount, "amount");
	    itms.add(new Its(name, quantity, amount));
	    return this;
	}

	public <T> XmlDocumentCartBuilder withItems(final Collection<T> items, final Function<T, String> nameMapper,
		final Function<T, Integer> quantityMapper,
		final Function<T, Double> amountMapper) {
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
	    final AtomicInteger i = new AtomicInteger(1);
	    final XmlDocumentCart doc = new XmlDocumentCart();
	    doc.setItems(MyOptionals.streamOf(itms) //
		    .orElseThrow(() -> new IllegalArgumentException("Cart must have at least one item")) //
		    .map(t -> {
			final XmlItem r = new XmlItem();
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
