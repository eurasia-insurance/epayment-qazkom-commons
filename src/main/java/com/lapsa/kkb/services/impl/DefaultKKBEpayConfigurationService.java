package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.Constants.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.lapsa.kkb.api.KKBEpayConfigurationService;
import com.lapsa.kkb.api.KKBMerchantSignatureService;

@Singleton
public class DefaultKKBEpayConfigurationService implements KKBEpayConfigurationService {
    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    private URL bankEpayURL;
    private String templateName;

    @Resource(lookup = KKB_PKI_CONFIGURATION_PROPERTIES_LOOKUP)
    private Properties configurationProperties;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @PostConstruct
    public void init() {
	try {
	    String bankEpayURLString = configurationProperties.getProperty(PROPERTY_EPAY_URL, DEFAULT_EPAY_URL);
	    bankEpayURL = new URL(bankEpayURLString);
	    templateName = configurationProperties.getProperty(PROPERTY_EPAY_TEMPLATE_NAME, DEFAULT_EPAY_TEMPLATE_NAME);
	} catch (MalformedURLException e) {
	    logger.log(Level.SEVERE, String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName()),
		    e);
	}
    }

    @Override
    public URL getEpayURL() {
	return bankEpayURL;
    }

    @Override
    public String getTemplateName() {
	return templateName;
    }
}
