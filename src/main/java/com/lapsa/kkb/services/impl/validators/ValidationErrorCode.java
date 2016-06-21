package com.lapsa.kkb.services.impl.validators;

import com.lapsa.kkb.services.KKBValidationErrorException;

public enum ValidationErrorCode {
    VLDT001("VLDT001 Request pay merchantId's set is "
	    + "not equals to response result merchantId's set"),
    VLDT002("VLDT002 Request pay to merchantId = '%1$s' with "
	    + "amount = '%2$f' is not equals to response result amount = '%3$f'"),
    VLDT003("VLDT003 Request values of /document/merchant element "
	    + "is not equals to response values of /document/bank/customer/merchant"),
    //
    ;

    private final String messageFormat;

    ValidationErrorCode(String messageFormat) {
	this.messageFormat = messageFormat;
    }

    public KKBValidationErrorException generateException(Object... args) {
	return new KKBValidationErrorException(String.format(messageFormat, args));
    }

}
