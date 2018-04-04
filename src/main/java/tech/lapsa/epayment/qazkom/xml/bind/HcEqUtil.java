package tech.lapsa.epayment.qazkom.xml.bind;

import java.lang.annotation.IncompleteAnnotationException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import tech.lapsa.java.commons.reflect.MyAnnotations;

public final class HcEqUtil {

    public final static int primeOf(final Class<?> clazz) {
	return MyAnnotations.optionalOf(clazz, HashCodePrime.class) //
		.map(HashCodePrime::value) //
		.orElseThrow(() -> new IncompleteAnnotationException(HashCodePrime.class, "value")) //
		.intValue();
    }

    public final static int multiplierOf(final Class<?> clazz, final Integer prime) {
	return MyAnnotations.optionalOf(clazz, HashCodeMultiplier.class) //
		.map(HashCodeMultiplier::value) //
		.orElse(prime) //
		.intValue();
    }

    //

    public static final int hashCode(final Object obj, final int prime, final int multiplier) {
	return HashCodeBuilder.reflectionHashCode(prime, multiplier, obj, false);
    }

    public static int hashCode(final Object obj, final int prime) {
	return hashCode(obj, prime, prime);
    }

    public static int hashCode(final Object obj) {
	final int prime = primeOf(obj.getClass());
	final int multiplier = multiplierOf(obj.getClass(), prime);
	return hashCode(obj, prime, multiplier);
    }

    //

    public static final boolean equals(final Object obj, final Object other) {
	return EqualsBuilder.reflectionEquals(obj, other, false);
    }
}
