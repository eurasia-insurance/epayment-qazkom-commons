package com.lapsa.kkb.services.impl.validators;

import com.lapsa.kkb.services.KKBValidationErrorException;
import com.lapsa.kkb.services.impl.ResponseParserErrorCode;
import com.lapsa.kkb.xml.KKBXmlDocumentRequest;
import com.lapsa.kkb.xml.KKBXmlDocumentResponse;
import com.lapsa.kkb.xml.KKBXmlMerchant;

public class KKBXmlMerchantEqualsValidator implements KKBRequestResponseValidator {

    @Override
    public void validate(KKBXmlDocumentRequest xmlRequestDocument, KKBXmlDocumentResponse xmlResponseDocument)
	    throws KKBValidationErrorException {
	KKBXmlMerchant requestMerchant = xmlRequestDocument.getMerchant();
	KKBXmlMerchant responseMerchant = xmlResponseDocument.getBank().getCustomer().getSourceMerchant();

	if (!requestMerchant.equals(responseMerchant))
	    throw ResponseParserErrorCode.VLDT003.generateValidationException();
    }

}
