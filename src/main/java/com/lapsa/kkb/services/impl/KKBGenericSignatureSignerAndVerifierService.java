package com.lapsa.kkb.services.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

import com.lapsa.kkb.core.KKBSignature;
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
	    byte[] signatureBytes = sig.sign();
	    return (inverted) ? invertedByteArray(signatureBytes) : signatureBytes;
	} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
	    throw new KKBSignatureOperationFailed(e);
	}
    }

    @Override
    @SuppressWarnings("deprecation")
    public void signData(KKBSignature signature) throws KKBSignatureOperationFailed {
	signature.setStatus(KKBSingatureStatus.UNCHECKED);
	signature.setSignature(sign(signature.getData(), signature.isInverted()));
    }

    protected abstract PrivateKey getPrivateKey();

}
