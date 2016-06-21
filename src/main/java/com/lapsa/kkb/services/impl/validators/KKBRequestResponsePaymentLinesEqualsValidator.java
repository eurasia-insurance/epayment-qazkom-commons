package com.lapsa.kkb.services.impl.validators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.lapsa.kkb.services.KKBValidationErrorException;
import com.lapsa.kkb.xml.KKBXmlDepartment;
import com.lapsa.kkb.xml.KKBXmlDocumentRequest;
import com.lapsa.kkb.xml.KKBXmlDocumentResponse;
import com.lapsa.kkb.xml.KKBXmlPayment;

public class KKBRequestResponsePaymentLinesEqualsValidator implements KKBRequestResponseValidator {

    @Override
    public void validate(KKBXmlDocumentRequest xmlRequestDocument, KKBXmlDocumentResponse xmlResponseDocument)
	    throws KKBValidationErrorException {
	Map<String, KKBXmlDepartment> reqLines = new HashMap<>();
	Map<String, KKBXmlPayment> resLines = new HashMap<>();

	for (KKBXmlDepartment e : xmlRequestDocument.getMerchant().getOrder().getDepartments())
	    reqLines.put(e.getMerchantId(), e);
	for (KKBXmlPayment e : xmlResponseDocument.getBank().getResults().getPayments())
	    resLines.put(e.getMerchantId(), e);

	Set<String> reqMerchants = reqLines.keySet();
	Set<String> resMerchants = resLines.keySet();

	if (!reqMerchants.containsAll(resMerchants) || resMerchants.containsAll(reqMerchants))
	    throw new KKBValidationErrorException(
		    "Request pay merchantId's set is not equals to response result merchantId's set");

	for (String reqMerchantId : reqMerchants) {
	    if (!resLines.containsKey(reqMerchantId))
		throw new KKBValidationErrorException(String.format(
			"Request pay to merchantId = '%1$s' is not present at the response result", reqMerchantId));
	    KKBXmlDepartment req = reqLines.get(reqMerchantId);
	    KKBXmlPayment res = resLines.get(reqMerchantId);
	    if (req.getAmount() != res.getAmount())
		throw new KKBValidationErrorException(String.format(
			"Request pay to merchantId = '%1$s' with amount = '%2$d' is not equals to response result amount = '%3$d'",
			reqMerchantId, req.getAmount(), res.getAmount()));
	}
    }

}
