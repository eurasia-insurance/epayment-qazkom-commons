package test.com.lapsa.kkb.services.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.kkb.core.KKBPaymentRequestDocument;
import com.lapsa.kkb.core.KKBPaymentResponseDocument;
import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBResponseService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBValidationErrorException;
import com.lapsa.kkb.services.KKBWrongSignature;
import com.lapsa.kkb.services.impl.ResponseParserErrorCode;

public class KKBResponseParser_ValidatorTestCase extends ArquillianBaseTestCase {

    @EJB
    private KKBResponseService responseParserService;

    private static final String VALIDATE_SIGNATURE_RESPONSE = ""
	    + "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARDHOLDER\" mail=\"vadim.isaev@me.com\" "
	    + "phone=\"87019377979\"><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"600982718179637\""
	    + " amount=\"5212.39\" currency=\"398\"><department merchant_id=\"92061103\" amount=\"5212.39\"/></order>"
	    + "</merchant><merchant_sign type=\"RSA\"/></customer><customer_sign type=\"RSA\"/><results "
	    + "timestamp=\"2016-06-21 15:20:15\"><payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" "
	    + "amount=\"5212.39\" reference=\"160621152015\" approval_code=\"152015\" response_code=\"00\" Secure=\"No\" "
	    + "card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results></bank><bank_sign cert_id=\"00c183d690\" "
	    + "type=\"SHA/RSA\">4ZKQA/tYgGju6XZuQV61/PE1j72bvp9OXV/4S8GgyCvK4WX+wT8oPtQS4q7PhK2+PgJtON/MM8QczdzVYYxrWG10"
	    + "zDNfaK3MzgDcO+RqTe5E10fYr31Dlm4bvda6AGWJJ08vcjSbWmXt92fZTVQJ/u9zDYzwS0QHpexH4Pa0AGE=</bank_sign></document>";

    @Test
    public void testValidateSignature() throws KKBServiceError, KKBFormatException, KKBWrongSignature {
	responseParserService.validateSignature(VALIDATE_SIGNATURE_RESPONSE);
    }

    private static final String VALIDATE_ORDER_RESPONSE_OK_REQUEST = ""
	    + "<document><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"600982718179637\" "
	    + "currency=\"398\" amount=\"5212.39\"><department merchant_id=\"92061103\" amount=\"5212.39\"/>"
	    + "</order></merchant><merchant_sign type=\"RSA\">BfOXCuQ0e4e9hnRSiVa9hib7dTIc6YBN8pB3VgX/pa"
	    + "QO3X2M5590fSLFvJeMkDylHHhZKVl30qRC0y18ojkUZ5QKZmnCKzKh2naKaS90knwC2U/5yX61QWhfBZkaCpwyXZ2wrhGI"
	    + "9yQourLLFcfHSiygQriEUUcETF4zEFS+zVo=</merchant_sign></document>";

    private static final String VALIDATE_ORDER_RESPONSE_OK_RESPONSE = ""
	    + "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARDHOLDER\" "
	    + "mail=\"vadim.isaev@me.com\" phone=\"87019377979\"><merchant cert_id=\"c183d70b\" "
	    + "name=\"Test shop 3\"><order order_id=\"600982718179637\" amount=\"5212.39\" "
	    + "currency=\"398\"><department merchant_id=\"92061103\" amount=\"5212.39\"/>"
	    + "</order></merchant><merchant_sign type=\"RSA\"/></customer><customer_sign "
	    + "type=\"RSA\"/><results timestamp=\"2016-06-21 15:20:15\"><payment merchant_id=\"92061103\" "
	    + "card=\"440564-XX-XXXX-6150\" amount=\"5212.39\" reference=\"160621152015\" "
	    + "approval_code=\"152015\" response_code=\"00\" Secure=\"No\" card_bin=\"\" "
	    + "c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results></bank><bank_sign "
	    + "cert_id=\"00c183d690\" type=\"SHA/RSA\">4ZKQA/tYgGju6XZuQV61/PE1j72bvp9OXV/4S8GgyCvK4W"
	    + "X+wT8oPtQS4q7PhK2+PgJtON/MM8QczdzVYYxrWG10zDNfaK3MzgDcO+RqTe5E10fYr31Dlm4bvda6AGWJJ08"
	    + "vcjSbWmXt92fZTVQJ/u9zDYzwS0QHpexH4Pa0AGE=</bank_sign></document>";

    @Test
    public void testValidateOrderResponse_OK() throws KKBValidationErrorException, KKBFormatException {
	KKBPaymentRequestDocument request = new KKBPaymentRequestDocument(VALIDATE_ORDER_RESPONSE_OK_REQUEST);
	KKBPaymentResponseDocument response = genRespDoc(VALIDATE_ORDER_RESPONSE_OK_RESPONSE);
	responseParserService.validateResponse(request, response);
    }

    protected KKBPaymentResponseDocument genRespDoc(String content) {
	KKBPaymentResponseDocument doc = new KKBPaymentResponseDocument();
	doc.setContent(content);
	return doc;
    }

    private static final String VALIDATE_ORDER_RESPONSE_FAIL_MCINE_REQUEST = ""
	    + "<document><merchant cert_id=\"c183d70b32b\" name=\"Test shop 3\"><order order_id=\"600982718179637\" "
	    + "currency=\"398\" amount=\"5212.39\"><department merchant_id=\"92061103\" amount=\"5212.39\"/></order>"
	    + "</merchant><merchant_sign type=\"RSA\">BfOXCuQ0e4e9hnRSiVa9hib7dTIc6YBN8pB3VgX/paQO3X2M5590fSLFvJeMkD"
	    + "ylHHhZKVl30qRC0y18ojkUZ5QKZmnCKzKh2naKaS90knwC2U/5yX61QWhfBZkaCpwyXZ2wrhGI9yQourLLFcfHSiygQriEUUcETF4"
	    + "zEFS+zVo=</merchant_sign></document>";

    private static final String VALIDATE_ORDER_RESPONSE_FAIL_MCINE_RESPONSE = ""
	    + "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARDHOLDER\" mail=\"vadim.isaev@me.com\" "
	    + "phone=\"87019377979\"><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"600982718179637\" "
	    + "amount=\"5212.39\" currency=\"398\"><department merchant_id=\"92061103\" amount=\"5212.39\"/></order>"
	    + "</merchant><merchant_sign type=\"RSA\"/></customer><customer_sign type=\"RSA\"/><results timestamp=\"2016-06-21 15:20:15\">"
	    + "<payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"5212.39\" reference=\"160621152015\" "
	    + "approval_code=\"152015\" response_code=\"00\" Secure=\"No\" card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/>"
	    + "</results></bank><bank_sign cert_id=\"00c183d690\" type=\"SHA/RSA\">4ZKQA/tYgGju6XZuQV61/PE1j72bvp9OXV/4S8Gg"
	    + "yCvK4WX+wT8oPtQS4q7PhK2+PgJtON/MM8QczdzVYYxrWG10zDNfaK3MzgDcO+RqTe5E10fYr31Dlm4bvda6AGWJJ08vcjSbWmXt92fZTVQJ"
	    + "/u9zDYzwS0QHpexH4Pa0AGE=</bank_sign></document>";

    @Test
    public void testValidateOrderResponse_Fail_MerchantCertIdNotEquals() throws KKBFormatException {
	KKBPaymentRequestDocument request = new KKBPaymentRequestDocument(VALIDATE_ORDER_RESPONSE_FAIL_MCINE_REQUEST);
	KKBPaymentResponseDocument response = genRespDoc(VALIDATE_ORDER_RESPONSE_FAIL_MCINE_RESPONSE);
	expectValidationException(ResponseParserErrorCode.VLDT003, request, response);
    }

    private static final String VALIDATE_ORDER_RESPONSE_FAIL_PLMIDNTS_REQUEST = ""
	    + "<document><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"600982718179637\" currency=\"398\" "
	    + "amount=\"5212.39\"><department merchant_id=\"6692061103\" amount=\"5212.39\"/></order></merchant><merchant_sign "
	    + "type=\"RSA\">BfOXCuQ0e4e9hnRSiVa9hib7dTIc6YBN8pB3VgX/paQO3X2M5590fSLFvJeMkDylHHhZKVl30qRC0y18ojkUZ5QKZmnCKzKh2naK"
	    + "aS90knwC2U/5yX61QWhfBZkaCpwyXZ2wrhGI9yQourLLFcfHSiygQriEUUcETF4zEFS+zVo=</merchant_sign></document>";

    private static final String VALIDATE_ORDER_RESPONSE_FAIL_PLMIDNTS_RESPONSE = ""
	    + "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARDHOLDER\" mail=\"vadim.isaev@me.com\" "
	    + "phone=\"87019377979\"><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"600982718179637\" "
	    + "amount=\"5212.39\" currency=\"398\"><department merchant_id=\"6692061103\" amount=\"5212.39\"/></order></merchant>"
	    + "<merchant_sign type=\"RSA\"/></customer><customer_sign type=\"RSA\"/><results timestamp=\"2016-06-21 15:20:15\">"
	    + "<payment merchant_id=\"92061103\" card=\"440564-XX-XXXX-6150\" amount=\"5212.39\" reference=\"160621152015\" "
	    + "approval_code=\"152015\" response_code=\"00\" Secure=\"No\" card_bin=\"\" c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/>"
	    + "</results></bank><bank_sign cert_id=\"00c183d690\" type=\"SHA/RSA\">4ZKQA/tYgGju6XZuQV61/PE1j72bvp9OXV/4S8G"
	    + "gyCvK4WX+wT8oPtQS4q7PhK2+PgJtON/MM8QczdzVYYxrWG10zDNfaK3MzgDcO+RqTe5E10fYr31Dlm4bvda6AGWJJ08vcjSbWmXt92fZTV"
	    + "QJ/u9zDYzwS0QHpexH4Pa0AGE=</bank_sign></document>";

    @Test
    public void testValidateOrderResponse_Fail_PaymentLinesMerchantIdsIsNotTheSame() throws KKBFormatException {
	KKBPaymentRequestDocument request = new KKBPaymentRequestDocument(
		VALIDATE_ORDER_RESPONSE_FAIL_PLMIDNTS_REQUEST);
	KKBPaymentResponseDocument response = genRespDoc(VALIDATE_ORDER_RESPONSE_FAIL_PLMIDNTS_RESPONSE);
	expectValidationException(ResponseParserErrorCode.VLDT001, request, response);
    }

    private static final String VALIDATE_ORDER_RESPONSE_FAIL_PLAMNTS_REQUEST = ""
	    + "<document><merchant cert_id=\"c183d70b\" name=\"Test shop 3\"><order order_id=\"600982718179637\" "
	    + "currency=\"398\" amount=\"5212.39\"><department merchant_id=\"92061103\" amount=\"5212.39\"/>"
	    + "</order></merchant><merchant_sign type=\"RSA\">BfOXCuQ0e4e9hnRSiVa9hib7dTIc6YBN8pB3VgX/pa"
	    + "QO3X2M5590fSLFvJeMkDylHHhZKVl30qRC0y18ojkUZ5QKZmnCKzKh2naKaS90knwC2U/5yX61QWhfBZkaCpwyXZ2wrhGI"
	    + "9yQourLLFcfHSiygQriEUUcETF4zEFS+zVo=</merchant_sign></document>";

    private static final String VALIDATE_ORDER_RESPONSE_FAIL_PLAMNTS_RESPONSE = ""
	    + "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARDHOLDER\" "
	    + "mail=\"vadim.isaev@me.com\" phone=\"87019377979\"><merchant cert_id=\"c183d70b\" "
	    + "name=\"Test shop 3\"><order order_id=\"600982718179637\" amount=\"5212.39\" "
	    + "currency=\"398\"><department merchant_id=\"92061103\" amount=\"5212.39\"/>"
	    + "</order></merchant><merchant_sign type=\"RSA\"/></customer><customer_sign "
	    + "type=\"RSA\"/><results timestamp=\"2016-06-21 15:20:15\"><payment merchant_id=\"92061103\" "
	    // 5212.39 -> 5212.40
	    + "card=\"440564-XX-XXXX-6150\" amount=\"5212.40\" reference=\"160621152015\" "
	    + "approval_code=\"152015\" response_code=\"00\" Secure=\"No\" card_bin=\"\" "
	    + "c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results></bank><bank_sign "
	    + "cert_id=\"00c183d690\" type=\"SHA/RSA\">4ZKQA/tYgGju6XZuQV61/PE1j72bvp9OXV/4S8GgyCvK4W"
	    + "X+wT8oPtQS4q7PhK2+PgJtON/MM8QczdzVYYxrWG10zDNfaK3MzgDcO+RqTe5E10fYr31Dlm4bvda6AGWJJ08"
	    + "vcjSbWmXt92fZTVQJ/u9zDYzwS0QHpexH4Pa0AGE=</bank_sign></document>";

    @Test
    public void testValidateOrderResponse_Fail_PaymentLinesAmountsIsNotTheSame()
	    throws KKBValidationErrorException, KKBFormatException {
	KKBPaymentRequestDocument request = new KKBPaymentRequestDocument(VALIDATE_ORDER_RESPONSE_FAIL_PLAMNTS_REQUEST);
	KKBPaymentResponseDocument response = genRespDoc(VALIDATE_ORDER_RESPONSE_FAIL_PLAMNTS_RESPONSE);
	expectValidationException(ResponseParserErrorCode.VLDT002, request, response);
    }

    private static final String PARSE_REFERENCE_OK = ""
	    + "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARDHOLDER\" "
	    + "mail=\"vadim.isaev@me.com\" phone=\"87019377979\"><merchant cert_id=\"c183d70b\" "
	    + "name=\"Test shop 3\"><order order_id=\"600982718179637\" amount=\"5212.39\" "
	    + "currency=\"398\"><department merchant_id=\"92061103\" amount=\"5212.39\"/>"
	    + "</order></merchant><merchant_sign type=\"RSA\"/></customer><customer_sign "
	    + "type=\"RSA\"/><results timestamp=\"2016-06-21 15:20:15\"><payment merchant_id=\"92061103\" "
	    + "card=\"440564-XX-XXXX-6150\" amount=\"5212.39\" reference=\"160621152015\" "
	    + "approval_code=\"152015\" response_code=\"00\" Secure=\"No\" card_bin=\"\" "
	    + "c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results></bank><bank_sign "
	    + "cert_id=\"00c183d690\" type=\"SHA/RSA\">4ZKQA/tYgGju6XZuQV61/PE1j72bvp9OXV/4S8GgyCvK4W"
	    + "X+wT8oPtQS4q7PhK2+PgJtON/MM8QczdzVYYxrWG10zDNfaK3MzgDcO+RqTe5E10fYr31Dlm4bvda6AGWJJ08"
	    + "vcjSbWmXt92fZTVQJ/u9zDYzwS0QHpexH4Pa0AGE=</bank_sign></document>";

    @Test
    public void testParseReference_OK() throws KKBServiceError, KKBFormatException {
	KKBPaymentResponseDocument response = genRespDoc(PARSE_REFERENCE_OK);
	String ref = responseParserService.parsePaymentReferences(response);
	assertThat(ref, allOf(notNullValue(), equalTo("160621152015")));
    }

    private static final String PARSE_TIMESTAMP_OK = ""
	    + "<document><bank name=\"Kazkommertsbank JSC\"><customer name=\"MR CARDHOLDER\" "
	    + "mail=\"vadim.isaev@me.com\" phone=\"87019377979\"><merchant cert_id=\"c183d70b\" "
	    + "name=\"Test shop 3\"><order order_id=\"600982718179637\" amount=\"5212.39\" "
	    + "currency=\"398\"><department merchant_id=\"92061103\" amount=\"5212.39\"/>"
	    + "</order></merchant><merchant_sign type=\"RSA\"/></customer><customer_sign "
	    + "type=\"RSA\"/><results timestamp=\"2016-06-21 15:20:15\"><payment merchant_id=\"92061103\" "
	    + "card=\"440564-XX-XXXX-6150\" amount=\"5212.39\" reference=\"160621152015\" "
	    + "approval_code=\"152015\" response_code=\"00\" Secure=\"No\" card_bin=\"\" "
	    + "c_hash=\"13988BBF7C6649F799F36A4808490A3E\"/></results></bank><bank_sign "
	    + "cert_id=\"00c183d690\" type=\"SHA/RSA\">4ZKQA/tYgGju6XZuQV61/PE1j72bvp9OXV/4S8GgyCvK4W"
	    + "X+wT8oPtQS4q7PhK2+PgJtON/MM8QczdzVYYxrWG10zDNfaK3MzgDcO+RqTe5E10fYr31Dlm4bvda6AGWJJ08"
	    + "vcjSbWmXt92fZTVQJ/u9zDYzwS0QHpexH4Pa0AGE=</bank_sign></document>";

    @Test
    public void testParseTimestamp_OK() throws KKBServiceError, KKBFormatException {
	KKBPaymentResponseDocument response = genRespDoc(PARSE_TIMESTAMP_OK);
	Date ref = responseParserService.parsePaymentTimestamp(response);
	assertThat(ref, allOf(notNullValue(), equalTo(Date.from(Instant.parse("2016-06-21T15:20:15Z")))));
	assertThat(ref.toInstant(),
		equalTo(LocalDateTime.parse("2016-06-21T15:20:15").atZone(ZoneId.systemDefault()).toInstant()));
    }

    // PRIVATE

    private void expectValidationException(ResponseParserErrorCode code, KKBPaymentRequestDocument request,
	    KKBPaymentResponseDocument response) throws KKBFormatException {
	try {
	    responseParserService.validateResponse(request, response);
	    fail(String.format("%1$s with %2$s code must be thrown",
		    KKBValidationErrorException.class.getSimpleName(), code.name()));
	} catch (KKBValidationErrorException e) {
	    assertThat(e.getMessage(), containsString(code.name()));
	}
    }

}
