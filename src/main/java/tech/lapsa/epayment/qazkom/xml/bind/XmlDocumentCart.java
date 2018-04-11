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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.qazkom.xml.schema.XmlSchemas;
import tech.lapsa.java.commons.function.MyCollections;
import tech.lapsa.java.commons.function.MyCollectors;
import tech.lapsa.java.commons.function.MyExceptions;
import tech.lapsa.java.commons.function.MyNumbers;
import tech.lapsa.java.commons.function.MyObjects;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
@XmlRootElement(name = "document")
@HashCodePrime(29)
public class XmlDocumentCart extends AXmlBase {

    private static final long serialVersionUID = 1L;

    private static final SerializationTool<XmlDocumentCart> TOOL = SerializationTool.forClass(XmlDocumentCart.class,
	    XmlSchemas.CART_SCHEMA);

    public static XmlDocumentCart of(final String rawXml) throws IllegalArgumentException {
	return TOOL.deserializeFrom(rawXml);
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

	    private Its(final String name, final Integer quantity, final Double amount)
		    throws IllegalArgumentException {
		this.name = MyStrings.requireNonEmpty(name, "name");
		this.quantity = MyNumbers.requirePositive(quantity, "quantity");
		this.amount = MyNumbers.requirePositive(amount, "amount");
	    }
	}

	private final List<Its> itms = new ArrayList<>();

	public XmlDocumentCartBuilder withItem(final String name, final Integer quantity, final Double amount)
		throws IllegalArgumentException {
	    itms.add(new Its(name, quantity, amount));
	    return this;
	}

	public <T> XmlDocumentCartBuilder withItems(final Collection<T> items,
		final Function<T, String> nameMapper,
		final Function<T, Integer> quantityMapper,
		final Function<T, Double> amountMapper) throws IllegalArgumentException {

	    MyCollections.requireNonEmpty(items, "items");

	    MyObjects.requireNonNull(nameMapper, "nameMapper");
	    MyObjects.requireNonNull(quantityMapper, "quantityMapper");
	    MyObjects.requireNonNull(amountMapper, "amountMapper");

	    MyOptionals.streamOf(items) //
		    .get()
		    .forEach(t -> withItem(nameMapper.apply(t), quantityMapper.apply(t), amountMapper.apply(t)));

	    return this;
	}

	public XmlDocumentCart build() throws IllegalArgumentException {
	    final AtomicInteger i = new AtomicInteger(1);
	    final XmlDocumentCart doc = new XmlDocumentCart(MyOptionals.streamOf(itms) //
		    .orElseThrow(MyExceptions.illegalArgumentSupplier("Cart must have at least one item")) //
		    .map(t -> new XmlItem(t.amount, i.getAndIncrement(), t.name, t.quantity)) //
		    .collect(MyCollectors.unmodifiableList()));
	    return doc;
	}
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
    @HashCodePrime(43)
    public static class XmlItem extends AXmlAmount {

	private static final long serialVersionUID = 1L;

	// item number= необходимо перечислить все пункты корзины
	@XmlAttribute(name = "number")
	private final Integer number;

	public Integer getNumber() {
	    return number;
	}

	// name -Наименование товара/услуги
	@XmlAttribute(name = "name")
	private final String name;

	public String getName() {
	    return name;
	}

	// quantity - количество
	@XmlAttribute(name = "quantity")
	private final Integer quantity;

	public Integer getQuantity() {
	    return quantity;
	}

	/*
	 * Default no-args constructor due to JAXB requirements
	 */
	@Deprecated
	public XmlItem() {
	    super(null);
	    this.number = null;
	    this.name = null;
	    this.quantity = null;
	}

	public XmlItem(Double amount, int number, String name, int quantity) {
	    super(amount);
	    this.number = number;
	    this.name = name;
	    this.quantity = quantity;
	}
    }

    @XmlElement(name = "item")
    private final List<XmlItem> items;

    public List<XmlItem> getItems() {
	return items;
    }

    public String getRawXml() {
	return TOOL.serializeToString(this);
    }

    /*
     * Default no-args constructor due to JAXB requirements
     */
    @Deprecated
    public XmlDocumentCart() {
	this.items = null;
    }

    public XmlDocumentCart(List<XmlItem> items) {
	this.items = MyObjects.nullOrGet(items, MyCollections::unmodifiableOrEmptyList);
    }
}
