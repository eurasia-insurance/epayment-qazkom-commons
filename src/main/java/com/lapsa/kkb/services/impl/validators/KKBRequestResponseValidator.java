package com.lapsa.kkb.services.impl.validators;

import com.lapsa.kkb.services.KKBValidationErrorException;
import com.lapsa.kkb.xml.KKBXmlDocumentRequest;
import com.lapsa.kkb.xml.KKBXmlDocumentResponse;

public interface KKBRequestResponseValidator {
    void validate(KKBXmlDocumentRequest xmlRequestDocument, KKBXmlDocumentResponse xmlResponseDocument) throws KKBValidationErrorException;
}
