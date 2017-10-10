package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.lapsa.kkb.services.KKBEpayConfigurationService;
import com.lapsa.kkb.services.KKBMerchantSignatureService;

@Singleton
public class DefaultKKBEpayConfigurationService extends KKBGenericService implements KKBEpayConfigurationService {

    private URI bankEpayURI;

    private String templateName;

    @Resource(lookup = JNDI_PROPERTIES_CONFIGURATION)
    private Properties configurationProperties;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @PostConstruct
    public void init() {
	try {
	    String bankEpayURLString = configurationProperties.getProperty(PROPERTY_BANK_EPAY_URL, DEFAULT_EPAY_URL);
	    bankEpayURI = new URI(bankEpayURLString);
	    templateName = configurationProperties.getProperty(PROPERTY_BANK_EPAY_TEMPLATE, DEFAULT_EPAY_TEMPLATE_NAME);
	} catch (URISyntaxException e) {
	    String message = String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName());
	    logger.log(Level.SEVERE, message, e);
	    throw new RuntimeException(message, e);
	}
    }

    @Override
    public URI getEpayURI() {
	return bankEpayURI;
    }

    @Override
    public String getTemplateName() {
	return templateName;
    }
}
