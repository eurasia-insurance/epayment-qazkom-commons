package com.lapsa.kkb.services.impl;

import static com.lapsa.kkb.services.impl.QazkomConstants.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.lapsa.kkb.services.KKBEpayConfigurationService;
import com.lapsa.kkb.services.KKBMerchantSignatureService;

import tech.lapsa.java.commons.logging.MyLogger;

@Singleton
public class DefaultKKBEpayConfigurationService extends KKBGenericService implements KKBEpayConfigurationService {

    private static final MyLogger logger = MyLogger.newBuilder() //
	    .withPackageNameOf(KKBEpayConfigurationService.class) //
	    .build();

    private URI bankEpayURI;

    private String templateName;

    @Resource(lookup = JNDI_QAZKOM_CONFIG)
    private Properties qazkomConfig;

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @PostConstruct
    public void init() {
	try {
	    String bankEpayURLString = qazkomConfig.getProperty(PROPERTY_BANK_EPAY_URL, DEFAULT_EPAY_URL);
	    bankEpayURI = new URI(bankEpayURLString);
	    templateName = qazkomConfig.getProperty(PROPERTY_BANK_EPAY_TEMPLATE, DEFAULT_EPAY_TEMPLATE_NAME);
	} catch (URISyntaxException e) {
	    String message = String.format("Failed to initialize EJB %1$s", this.getClass().getSimpleName());
	    logger.SEVERE.log(e, message);
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
