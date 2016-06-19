package com.lapsa.kkb.services.impl;

import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.xml.KKBXmlDocument;

public interface KKBXmlDocumentComposer {
    KKBXmlDocument composeXmlDocument(KKBOrder order) throws KKBServiceError;
}
