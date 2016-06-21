package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;

import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBResponseParserService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;

@Singleton
public class DefaultKKBResponseParserService extends KKBGenericService
	implements KKBResponseParserService {

    private Unmarshaller unmarshaller;

    // @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    // private Properties configurationProperties;
    //
    // @EJB
    // private KKBMerchantSignatureService merchantSignatureService;

    @PostConstruct
    public void init() {
	try  {
	    Schema schema = loadSchemaFromResource(SCHEMA_RESPONSE);
	    JAXBContext jaxbContext = JAXBContext.newInstance(KKBXmlMerchant.class, KKBXmlDocument.class);
	    unmarshaller = jaxbContext.createUnmarshaller();
	    unmarshaller.setSchema(schema);
	} catch (SAXException | IOException | JAXBException e) {
	    String message = String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName());
	    logger.log(Level.SEVERE, message, e);
	    throw new RuntimeException(message, e);
	}
    }

    @Override
    public String parseOrderId(String response) throws KKBServiceError, KKBFormatException {
	try {
	    KKBXmlDocument xmlDocument = (KKBXmlDocument) unmarshaller.unmarshal(new StringReader(response));
	    if (xmlDocument == null || xmlDocument.getBank() == null || xmlDocument.getBank().getCustomer() == null
		    || xmlDocument.getBank().getCustomer().getSourceMerchant() == null
		    || xmlDocument.getBank().getCustomer().getSourceMerchant().getOrder() == null
		    || xmlDocument.getBank().getCustomer().getSourceMerchant().getOrder().getOrderId() == null)
		throw new KKBFormatException("Response format error");
	    return xmlDocument.getBank().getCustomer().getSourceMerchant().getOrder().getOrderId();
	} catch (JAXBException e) {
	    throw new KKBFormatException(e);
	}
    }

}
