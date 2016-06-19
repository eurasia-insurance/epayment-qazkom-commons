package com.lapsa.kkb.services.impl;

public class Unused {
    // private Unmarshaller jaxbUnmarshaller;
    // jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    // @EJB
    // private KKBBankSignatureService bankSignatureService;

    // private static final String BANK_REGEX = "(\\<bank.*?\\<\\/bank\\>)";
    // private static final int BANK_REGEX_FLAGS = Pattern.CASE_INSENSITIVE |
    // Pattern.UNICODE_CASE | Pattern.MULTILINE;
    // private static final Pattern BANK_REGEX_PATTERN =
    // Pattern.compile(BANK_REGEX, BANK_REGEX_FLAGS);

    // @SuppressWarnings("deprecation")
    // private KKBSignedData compseBankSignature(String responseString, byte[]
    // bankSignatureDigest)
    // throws KKBFormatException {
    // String bankElXML = parseBankElementXML(responseString);
    // byte[] data = bankElXML.getBytes();
    // KKBSignedData res = new KKBSignedData();
    // res.setData(data);
    // res.setSignature(bankSignatureDigest);
    // res.setInverted(true);
    // res.setStatus(KKBSingatureStatus.UNCHECKED);
    // return res;
    // }

    // private KKBDocument parseResponseXmlDocument(String response)
    // throws JAXBException, KKBFormatException {
    // Reader sr = new StringReader(response);
    // KKBDocument document = (KKBDocument)
    // jaxbUnmarshaller.unmarshal(sr);
    // if (document != null)
    // return document;
    // throw new KKBFormatException(
    // String.format("'%1$s' object parsed to null",
    // KKBDocument.class.getSimpleName()));
    // }

    // private static String parseBankElementXML(String response) throws
    // KKBFormatException {
    // Matcher matcher = BANK_REGEX_PATTERN.matcher(response);
    // if (matcher.find())
    // return matcher.group();
    // throw new KKBFormatException(
    // String.format("<bank /> element is null or empty at the response string:
    // '%1$s'", response));
    // }
    // @Override
    // public KKBPaymentResponseDocument parseResponse(KKBOrder order, String
    // responseString)
    // throws KKBServiceError, KKBFormatException {
    // try {
    // KKBDocument xmlDocument = parseResponseXmlDocument(responseString);
    //
    // KKBSignedData bankSignature = compseBankSignature(responseString,
    // xmlDocument.getBankSign().getSignature());
    // KKBCustomer customer =
    // parseKKBCustomer(xmlDocument.getBank().getCustomer());
    //
    // KKBPaymentResponseDocument response = new KKBPaymentResponseDocument();
    // response.setCustomer(customer);
    // response.setReceived(new Date());
    // response.setSignature(bankSignature);
    // response.setXmlContent(responseString);
    //
    // order.addResponse(response);
    // return response;
    // } catch (JAXBException e) {
    // throw new KKBServiceError(e);
    // }
    // }

    // private KKBCustomer parseKKBCustomer(KKBXmlCustomer xmlCustomer) {
    // KKBCustomer customer = new KKBCustomer();
    // customer.setEmail(xmlCustomer.getEmailAddress());
    // customer.setName(xmlCustomer.getName());
    // customer.setPhone(xmlCustomer.getPhone());
    // return customer;
    // }

    // private KKBPaymentResponseDocument buildAuthorization(KKBXmlBank bank)
    // throws KKBFormatException {
    // KKBXmlCustomer customer = bank.getCustomer();
    //
    // KKBPaymentOrder res = new KKBPaymentOrder();
    // res.setCustomer(convertFrom(customer));
    //
    // KKBXmlOrder order = customer.getSourceMerchant().getOrder();
    //
    // res.setOrderId(order.getOrderId());
    // res.setCurrency(order.getFinCurrency());
    //
    // for (KKBXmlDepartment d : order.getDepartments()) {
    // KKBPaymentOperation paymentOperation = new KKBPaymentOperation();
    // paymentOperation.setAmount(d.getAmount());
    // paymentOperation.setMerchantId(d.getMerchantId());
    // res.addOperation(paymentOperation);
    // }
    //
    // KKBXmlResults results = bank.getResults();
    // res.setPaymentsTimestamp(results.getTimestamp());
    // for (KKBXmlPayment p : results.getPayments()) {
    // KKBPaymentOperation payment =
    // res.getOperationToMerchant(p.getMerchantId());
    // if (payment == null)
    // throw new KKBFormatException(
    // String.format(
    // "Unexpected <payment/> element with merchant_id = '%1$s' (is not present
    // as <department/> elements section)",
    // p.getMerchantId()));
    // if (payment.getAmount() != p.getAmount())
    // throw new KKBFormatException(
    // String.format(
    // "<department/> amount = '%1$d' with merchant_id = '%2$s' is not equals to
    // <payment/> amount = '%3$d' with merchant_id = '%4$s'",
    // payment.getAmount(), payment.getMerchantId(), p.getAmount(),
    // p.getMerchantId()));
    // payment.setResult(parsePaymentResult(p));
    // }
    // return res;
    // }

    // protected KKBPaymentOperationResult parsePaymentResult(KKBXmlPayment p) {
    // KKBPaymentOperationResult result = new KKBPaymentOperationResult();
    // result.setApprovalCode(p.getApprovalCode());
    // result.setCardHash(p.getCardHash());
    // result.setCardIssuerConutry(p.getCardCountry());
    // result.setCardNumberMasked(p.getCardNumberMasked());
    // result.setReference(p.getReference());
    // result.setResponseCode(p.getResponseCode());
    // result.setSecured(p.getSecureType().equals(KKBXmlSecureType.SECURED_3D));
    // return result;
    // }

}
