package com.lapsa.kkb.services.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

import com.lapsa.kkb.core.KKBSignedData;
import com.lapsa.kkb.core.KKBSingatureStatus;
import com.lapsa.kkb.services.KKBSignatureOperationFailed;
import com.lapsa.kkb.services.KKBSingatureSignerService;

public abstract class KKBGenericSignatureSignerAndVerifierService extends KKBGenericSignatureVerifierService
	implements KKBSingatureSignerService {

    @Override
    public byte[] sign(byte[] data) throws KKBSignatureOperationFailed {
	return sign(data, true);
    }

    @Override
    public byte[] sign(byte[] data, boolean inverted) throws KKBSignatureOperationFailed {
	try {
	    Signature sig = Signature.getInstance(getSignatureAlgorithm());
	    sig.initSign(getPrivateKey());
	    sig.update(data);
	    byte[] digest = sig.sign();
	    return (inverted) ? invertedByteArray(digest) : digest;
	} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
	    throw new KKBSignatureOperationFailed(e);
	}
    }

    @Override
    @SuppressWarnings("deprecation")
    public void signData(KKBSignedData signedData) throws KKBSignatureOperationFailed {
	signedData.setStatus(KKBSingatureStatus.UNCHECKED);
	signedData.setDigest(sign(signedData.getData(), signedData.isInverted()));
    }

    protected abstract PrivateKey getPrivateKey();

}
