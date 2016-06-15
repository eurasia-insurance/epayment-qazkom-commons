package com.lapsa.kkb.services.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

import com.lapsa.kkb.api.KKBServiceError;
import com.lapsa.kkb.api.KKBSignatureVerifierService;
import com.lapsa.kkb.api.KKBWrongSignature;
import com.lapsa.kkb.core.KKBSignature;
import com.lapsa.kkb.core.KKBSingatureStatus;

public abstract class KKBGenericSignatureVerifierService extends KKBGenericCryptographyService
	implements KKBSignatureVerifierService {

    @Override
    public void verify(byte[] data, byte[] signature) throws KKBServiceError, KKBWrongSignature {
	verify(data, signature, true);
    }

    @Override
    public void verify(byte[] data, byte[] signature, boolean inverted) throws KKBServiceError, KKBWrongSignature {
	try {
	    Signature sig = Signature.getInstance(getSignatureAlgorithm());
	    sig.initVerify(getSignatureCertificate());
	    sig.update(data);
	    boolean result = (inverted) ? sig.verify(invertedByteArray(signature)) : sig.verify(signature);
	    if (!result)
		throw new KKBWrongSignature();
	} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
	    throw new KKBServiceError(e);
	}
    }

    @Override
    @SuppressWarnings("deprecation")
    public void verify(KKBSignature signedData) throws KKBServiceError, KKBWrongSignature {
	try {
	    verify(signedData.getData(), signedData.getSignature(), signedData.isInverted());
	    signedData.setStatus(KKBSingatureStatus.CHECKED_VALID);
	} catch (KKBWrongSignature e) {
	    signedData.setStatus(KKBSingatureStatus.CHECKED_INVALID);
	    throw e;
	}
    }
}
