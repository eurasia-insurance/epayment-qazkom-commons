package com.lapsa.kkb.services.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

import com.lapsa.kkb.core.KKBSignedData;
import com.lapsa.kkb.core.KKBSingatureStatus;
import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBSignatureVerifierService;
import com.lapsa.kkb.services.KKBWrongSignature;

public abstract class KKBGenericSignatureVerifierService extends KKBGenericCryptographyService
	implements KKBSignatureVerifierService {

    @Override
    public void verify(byte[] data, byte[] signature) throws KKBServiceError, KKBWrongSignature, KKBFormatException {
	verify(data, signature, true);
    }

    @Override
    public void verify(byte[] data, byte[] signature, boolean inverted)
	    throws KKBServiceError, KKBWrongSignature, KKBFormatException {
	try {
	    Signature sig = Signature.getInstance(getSignatureAlgorithm());
	    sig.initVerify(getSignatureCertificate());
	    try {
		sig.update(data);
	    } catch (SignatureException e) {
		throw new KKBServiceError(e);
	    }
	    byte[] digest = (inverted) ? invertedByteArray(signature) : signature;
	    try {
		if (!sig.verify(digest))
		    throw new KKBWrongSignature(String.format("Signature is wrong for certificate '%1$s'",
			    getSignatureCertificate().getSubjectX500Principal().getName("RFC2253")));
	    } catch (SignatureException e) {
		throw new KKBFormatException(e);
	    }
	} catch (InvalidKeyException | NoSuchAlgorithmException e) {
	    throw new KKBServiceError(e);
	}
    }

    @Override
    @SuppressWarnings("deprecation")
    public void verify(KKBSignedData signedData) throws KKBServiceError, KKBWrongSignature, KKBFormatException {
	try {
	    verify(signedData.getData(), signedData.getDigest(), signedData.isInverted());
	    signedData.setStatus(KKBSingatureStatus.CHECKED_VALID);
	} catch (KKBWrongSignature e) {
	    signedData.setStatus(KKBSingatureStatus.CHECKED_INVALID);
	    throw e;
	}
    }
}
