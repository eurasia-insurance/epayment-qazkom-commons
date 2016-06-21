package com.lapsa.kkb.services.impl.composers;

import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.services.KKBServiceError;

public interface KKBXmlDocumentComposer {
    String composeXmlDocument(KKBOrder order) throws KKBServiceError;
}
