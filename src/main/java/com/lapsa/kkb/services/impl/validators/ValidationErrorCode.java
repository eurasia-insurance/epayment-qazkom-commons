package com.lapsa.kkb.services.impl.validators;

import com.lapsa.kkb.services.KKBValidationErrorException;

public enum ValidationErrorCode {
    VLDT001("Request pay merchantId's set is not equals to response result merchantId's set"),
    VLDT002("Request pay to merchantId = '%1$s' with amount = '%2$f' is not equals to response result amount = '%3$f'"),
    VLDT003("Request element /document/merchant is not equals to response element /document/bank/customer/merchant"),
    //
    ;

    private final String messageFormat;

    ValidationErrorCode(String messageFormat) {
	this.messageFormat = messageFormat;
    }

    public KKBValidationErrorException generateException(Object... args) {
	return new KKBValidationErrorException(name() + " " + String.format(messageFormat, args));
    }

}
