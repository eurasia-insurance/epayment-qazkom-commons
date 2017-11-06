package test.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import tech.lapsa.qazkom.xml.bind.XmlDocumentCart;

public class XmlDocumentCartBulderTest {

    private static final String RAW_XML = ""
	    + "<document>"
	    + "<item name=\"Apple iPhone X\" number=\"1\" quantity=\"1\" amount=\"1300\"/>"
	    + "<item name=\"Apple MacBook Pro\" number=\"2\" quantity=\"1\" amount=\"2400\"/>"
	    + "</document>";

    @Test
    public void simpleBuildTest() {
	XmlDocumentCart o = XmlDocumentCart.builder() //
		.withItem("Apple iPhone X", 1, 1300d) //
		.withItem("Apple MacBook Pro", 1, 2400d) //
		.build();
	assertThat(o, not(nullValue()));
	String rawXml = o.getRawXml();
	assertThat(rawXml, allOf(not(isEmptyOrNullString()), equalTo(RAW_XML)));
    }

    @Test
    public void simpleDeserializeTest() {
	XmlDocumentCart o = XmlDocumentCart.of(RAW_XML);
	assertThat(o, not(nullValue()));
	String rawXml = o.getRawXml();
	assertThat(rawXml, allOf(not(isEmptyOrNullString()), equalTo(RAW_XML)));
    }
}
