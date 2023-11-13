package com.innovatechamps.sync.client.payment;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;
import static com.innovatechamps.sync.serialization.Serializer.deserializeFromJson;

import java.math.BigDecimal;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.client.DataClient;
import com.innovatechamps.sync.core.DCRequestOptions;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DCResourceList;
import com.innovatechamps.sync.network.DCResponse;
import com.innovatechamps.sync.network.HttpMethod;
import com.innovatechamps.sync.resources.payment.PaymentRefund;
import com.innovatechamps.sync.serialization.Serializer;

/** Client that use the Payment Refunds APIs. */
public class PaymentRefundClient extends DataClient {

	private static final Logger LOGGER = Logger.getLogger(PaymentRefundClient.class.getName());

	/** Default constructor. Uses the default http client used by the SDK. */
	public PaymentRefundClient() {
		this(DevChampsConfig.getHttpClient());
	}

	/**
	 * Constructor used for providing a custom http client.
	 *
	 * @param httpClient httpClient
	 */
	public PaymentRefundClient(DCHttpClient httpClient) {
		super(httpClient);
		StreamHandler streamHandler = getStreamHandler();
		streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
		LOGGER.addHandler(streamHandler);
		LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
	}

	/**
	 * Creates a refund for payment.
	 *
	 * @param paymentId payment id
	 * @return PaymentRefund
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/chargebacks/_payments_id_refunds/post">api
	 *      docs</a>
	 */
	public PaymentRefund refund(Long paymentId) throws DCException, DCApiException {
		return this.refund(paymentId, null, null);
	}

	/**
	 * Creates a refund for payment.
	 *
	 * @param paymentId      payment id
	 * @param requestOptions metadata to customize the request
	 * @return PaymentRefund
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/chargebacks/_payments_id_refunds/post">api
	 *      docs</a>
	 */
	public PaymentRefund refund(Long paymentId, DCRequestOptions requestOptions) throws DCException, DCApiException {
		return this.refund(paymentId, null, requestOptions);
	}

	/**
	 * Creates a refund for payment.
	 *
	 * @param paymentId payment id
	 * @param amount    refund amount
	 * @return PaymentRefund
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/chargebacks/_payments_id_refunds/post">api
	 *      docs</a>
	 */
	public PaymentRefund refund(Long paymentId, BigDecimal amount) throws DCException, DCApiException {
		return this.refund(paymentId, amount, null);
	}

	/**
	 * Creates a refund for payment.
	 *
	 * @param paymentId      payment id
	 * @param amount         refund amount
	 * @param requestOptions metadata to customize the request
	 * @return PaymentRefund
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/chargebacks/_payments_id_refunds/post">api
	 *      docs</a>
	 */
	public PaymentRefund refund(Long paymentId, BigDecimal amount, DCRequestOptions requestOptions)
			throws DCException, DCApiException {
		LOGGER.info("Sending refund payment request");
		PaymentRefundCreateRequest request = PaymentRefundCreateRequest.builder().amount(amount).build();

		DCResponse response = send(String.format(URL_PAYMENT_REFUND, paymentId), HttpMethod.POST,
				Serializer.serializeToJson(request), null, requestOptions);
		PaymentRefund result = deserializeFromJson(PaymentRefund.class, response.getContent());
		result.setResponse(response);

		return result;
	}

	/**
	 * Gets refund information by id from the payment.
	 *
	 * @param paymentId payment id
	 * @param refundId  refund id
	 * @return PaymentRefund
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/chargebacks/_payments_id_refunds_refund_id/get">api
	 *      docs</a>
	 */
	public PaymentRefund get(Long paymentId, Long refundId) throws DCException, DCApiException {
		return this.get(paymentId, refundId, null);
	}

	/**
	 * Gets refund information by id from the payment.
	 *
	 * @param paymentId      payment id
	 * @param refundId       refund id
	 * @param requestOptions metadata to customize the request
	 * @return PaymentRefund
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/chargebacks/_payments_id_refunds_refund_id/get">api
	 *      docs</a>
	 */
	public PaymentRefund get(Long paymentId, Long refundId, DCRequestOptions requestOptions)
			throws DCException, DCApiException {
		LOGGER.info("Sending get refund payment request");
		DCResponse response = send(String.format(URL_GET_PAYMENT_REFUND, paymentId, refundId), HttpMethod.GET, null,
				null, requestOptions);
		PaymentRefund result = deserializeFromJson(PaymentRefund.class, response.getContent());
		result.setResponse(response);

		return result;
	}

	/**
	 * Lists the refunds of the payment.
	 *
	 * @param paymentId payment id
	 * @return list of PaymentRefund
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/chargebacks/_payments_id_refunds/get">api
	 *      docs</a>
	 */
	public DCResourceList<PaymentRefund> list(Long paymentId) throws DCException, DCApiException {
		return this.list(paymentId, null);
	}

	/**
	 * Lists the refunds of the payment.
	 *
	 * @param paymentId      payment id
	 * @param requestOptions metadata to customize the request
	 * @return list of PaymentRefund
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/chargebacks/_payments_id_refunds/get">api
	 *      docs</a>
	 */
	public DCResourceList<PaymentRefund> list(Long paymentId, DCRequestOptions requestOptions)
			throws DCException, DCApiException {
		LOGGER.info("Sending list refund payment request");
		DCResponse response = send(String.format(URL_PAYMENT_REFUND, paymentId), HttpMethod.GET, null, null,
				requestOptions);
		DCResourceList<PaymentRefund> result = Serializer.deserializeListFromJson(PaymentRefund.class,
				response.getContent());
		result.setResponse(response);

		return result;
	}
}
