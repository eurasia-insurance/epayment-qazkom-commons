package com.lapsa.kkb.services.impl;

import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBValidationErrorException;

public enum ResponseParserErrorCode {
    VLDT001("Request pay merchantId's set is not equals to response result merchantId's set"),
    VLDT002("Request pay to merchantId = '%1$s' with amount = '%2$f' is not equals to response result amount = '%3$f'"),
    VLDT003("Request element /document/merchant is not equals to response element /document/bank/customer/merchant"),
    //
    FRMT001("<bank /> element is null or empty at the response string: '%1$s'"),
    //
    ;

    private final String messageFormat;

    ResponseParserErrorCode(String messageFormat) {
	this.messageFormat = messageFormat;
    }

    public KKBValidationErrorException generateValidationException(Object... args) {
	return new KKBValidationErrorException(name() + " " + String.format(messageFormat, args));
    }

    public KKBFormatException generateFormatException(Object... args) {
	return new KKBFormatException(name() + " " + String.format(messageFormat, args));
    }

}
