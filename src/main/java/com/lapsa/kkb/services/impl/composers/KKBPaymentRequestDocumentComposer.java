package com.lapsa.kkb.services.impl.composers;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.lapsa.kkb.core.KKBOrder;
import com.lapsa.kkb.core.KKBSignedData;
import com.lapsa.kkb.services.KKBMerchantSignatureService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBSignatureOperationFailed;
import com.lapsa.kkb.xml.KKBXmlDepartment;
import com.lapsa.kkb.xml.KKBXmlDocument;
import com.lapsa.kkb.xml.KKBXmlMerchant;
import com.lapsa.kkb.xml.KKBXmlMerchantSign;
import com.lapsa.kkb.xml.KKBXmlOrder;
import com.lapsa.kkb.xml.KKBXmlSignType;

public class KKBPaymentRequestDocumentComposer implements KKBXmlDocumentComposer {

    private final String merchantId;
    private final String merchantName;
    private final Marshaller marshaller;
    private final KKBMerchantSignatureService merchantSignatureService;

    public KKBPaymentRequestDocumentComposer(String merchantId, String merchantName, Marshaller jaxbMarshaller,
	    KKBMerchantSignatureService merchantSignatureService) {
	this.merchantId = merchantId;
	this.merchantName = merchantName;
	this.marshaller = jaxbMarshaller;
	this.merchantSignatureService = merchantSignatureService;
    }

    @Override
    public KKBXmlDocument composeXmlDocument(KKBOrder order) throws KKBServiceError {
	try {
	    KKBXmlMerchant xmlMerchant = composeXmlMerchant(order);
	    KKBSignedData signature = composeMerchantKKBSignature(xmlMerchant);
	    KKBXmlMerchantSign xmlMerchantSign = composeXmlMerchantSign(signature);
	    KKBXmlDocument xmlDocument = new KKBXmlDocument();
	    xmlDocument.setMerchant(xmlMerchant);
	    xmlDocument.setMerchantSign(xmlMerchantSign);
	    return xmlDocument;
	} catch (JAXBException e) {
	    throw new KKBServiceError(e);
	}
    }

    private KKBXmlMerchantSign composeXmlMerchantSign(KKBSignedData signature) {
	KKBXmlMerchantSign xmlMerchantSign = new KKBXmlMerchantSign();
	xmlMerchantSign.setSignType(KKBXmlSignType.RSA);
	xmlMerchantSign.setSignature(signature.getDigest());
	return xmlMerchantSign;
    }

    private KKBSignedData composeMerchantKKBSignature(KKBXmlMerchant xmlMerchant)
	    throws JAXBException, KKBSignatureOperationFailed {
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	marshaller.marshal(xmlMerchant, output);
	byte[] signatureDta = output.toByteArray();
	byte[] signatureDigest = merchantSignatureService.sign(signatureDta);
	KKBSignedData singature = new KKBSignedData();
	singature.setData(signatureDta);
	singature.setDigest(signatureDigest);
	return singature;
    }

    private KKBXmlMerchant composeXmlMerchant(KKBOrder order) {
	KKBXmlMerchant xmlMerchant = new KKBXmlMerchant();
	BigInteger serialNumber = merchantSignatureService.getSignatureCertificate().getSerialNumber();
	xmlMerchant.setCertificateSerialNumber(serialNumber);
	xmlMerchant.setName(merchantName);

	KKBXmlOrder xmlOrder = new KKBXmlOrder();
	xmlMerchant.setOrder(xmlOrder);
	xmlOrder.setOrderId(order.getId());
	xmlOrder.setAmount(order.getAmount());
	xmlOrder.setFinCurrency(order.getCurrency());
	xmlOrder.setDepartments(new ArrayList<>());

	KKBXmlDepartment xmlDepartment = new KKBXmlDepartment();
	xmlOrder.getDepartments().add(xmlDepartment);
	xmlDepartment.setMerchantId(merchantId);
	xmlDepartment.setAmount(order.getAmount());
	return xmlMerchant;
    }
}
