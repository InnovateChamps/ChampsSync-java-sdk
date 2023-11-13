package com.innovatechamps.sync.client.payment;

import com.google.gson.reflect.TypeToken;
import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.client.DataClient;
import com.innovatechamps.sync.core.DCRequestOptions;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DCRequest;
import com.innovatechamps.sync.network.DCResourceList;
import com.innovatechamps.sync.network.DCResponse;
import com.innovatechamps.sync.network.DCResultsResourcesPage;
import com.innovatechamps.sync.network.DCSearchRequest;
import com.innovatechamps.sync.network.HttpMethod;
import com.innovatechamps.sync.resources.payment.Payment;
import com.innovatechamps.sync.resources.payment.PaymentRefund;
import com.innovatechamps.sync.serialization.Serializer;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;
import static com.innovatechamps.sync.serialization.Serializer.deserializeFromJson;
import static com.innovatechamps.sync.serialization.Serializer.deserializeResultsResourcesPageFromJson;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/** Client responsible for performing payment actions. */
public class PaymentClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(PaymentClient.class.getName());


  private final PaymentRefundClient refundClient;

  /** Default constructor. Uses the default http client used by the SDK. */
  public PaymentClient() {
    this(DevChampsConfig.getHttpClient());
  }

  /**
   * Constructor used for providing a custom http client.
   *
   * @param httpClient httpClient
   */
  public PaymentClient(DCHttpClient httpClient) {
    super(httpClient);
    refundClient = new PaymentRefundClient(httpClient);
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * Method responsible for getting payment.
   *
   * @param id paymentId
   * @return payment
   * @throws DCException an error if the request fails
   */
  public Payment get(Long id) throws DCException, DCApiException {
    return this.get(id, null);
  }

  /**
   * Method responsible for getting payment.
   *
   * @param id paymentId
   * @param requestOptions metadata to customize the request
   * @return payment
   * @throws DCException an error if the request fails
   */
  public Payment get(Long id, DCRequestOptions requestOptions) throws DCException, DCApiException {
    LOGGER.info("Sending get payment request");
    DCResponse response =
        send(String.format(URL_PAYMENT, id.toString()), HttpMethod.GET, null, null, requestOptions);

    Payment result = deserializeFromJson(Payment.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Method responsible for creating payment.
   *
   * @param request request
   * @return payment response
   * @throws DCException an error if the request fails
   */
  public Payment create(PaymentCreateRequest request) throws DCException, DCApiException {
    return this.create(request, null);
  }

  /**
   * Method responsible for creating payment with request options.
   *
   * @param request request
   * @param requestOptions metadata to customize the request
   * @return payment response
   * @throws DCException an error if the request fails
   */
  public Payment create(PaymentCreateRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending create payment request");

    DCRequest mpRequest =
        DCRequest.builder()
            .uri(URL_CREATE_PAYMENT)
            .method(HttpMethod.POST)
            .payload(Serializer.serializeToJson(request))
            .build();

    DCResponse response = send(mpRequest, requestOptions);
    Payment result = deserializeFromJson(Payment.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Method responsible for cancel payment.
   *
   * @param id id
   * @return Payment payment that was cancelled
   * @throws DCException an error if the request fails
   */
  public Payment cancel(Long id) throws DCException, DCApiException {
    return this.cancel(id, null);
  }

  /**
   * Method responsible for cancel payment with request options.
   *
   * @param id payment id
   * @param requestOptions metadata to customize the request
   * @return Payment payment that was cancelled
   * @throws DCException an error if the request fails
   */
  public Payment cancel(Long id, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending cancel payment request");
    PaymentCancelRequest payload = new PaymentCancelRequest();
    DCResponse response =
        send(
            String.format(URL_PAYMENT, id.toString()),
            HttpMethod.PUT,
            Serializer.serializeToJson(payload),
            new HashMap<>(),
            requestOptions);

    Payment result = deserializeFromJson(Payment.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Method responsible for capture payment.
   *
   * @param id id
   * @return Payment payment that was captured
   * @throws DCException an error if the request fails
   */
  public Payment capture(Long id) throws DCException, DCApiException {
    return this.capture(id, null, null);
  }

  /**
   * Method responsible for capture payment.
   *
   * @param id id
   * @param requestOptions metadata to customize the request
   * @return Payment payment that was captured
   * @throws DCException an error if the request fails
   */
  public Payment capture(Long id, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return this.capture(id, null, requestOptions);
  }

  /**
   * Method responsible for capture payment.
   *
   * @param id id
   * @param amount amount to be captured
   * @return Payment payment that was captured
   * @throws DCException an error if the request fails
   */
  public Payment capture(Long id, BigDecimal amount) throws DCException, DCApiException {
    return this.capture(id, amount, null);
  }

  /**
   * Method responsible for capture payment.
   *
   * @param id paymentId
   * @param amount amount to be captured
   * @param requestOptions metadata to customize the request
   * @return Payment payment that was captured
   * @throws DCException an error if the request fails
   */
  public Payment capture(Long id, BigDecimal amount, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending capture payment request");
    PaymentCaptureRequest payload =
        PaymentCaptureRequest.builder().transactionAmount(amount).build();

    DCResponse response =
        send(
            String.format(URL_PAYMENT, id.toString()),
            HttpMethod.PUT,
            Serializer.serializeToJson(payload),
            new HashMap<>(),
            requestOptions);

    Payment result = deserializeFromJson(Payment.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Method responsible for search payments.
   *
   * @param request search request information
   * @return list of results
   * @throws DCException an error if the request fails
   */
  public DCResultsResourcesPage<Payment> search(DCSearchRequest request)
      throws DCException, DCApiException {
    return this.search(request, null);
  }

  /**
   * Method responsible for search payments.
   *
   * @param request search request information
   * @param requestOptions metadata to customize the request
   * @return list of results
   * @throws DCException an error if the request fails
   */
  public DCResultsResourcesPage<Payment> search(
      DCSearchRequest request, DCRequestOptions requestOptions) throws DCException, DCApiException {
    LOGGER.info("Sending search payment request");
    DCResponse response = search(URL_SEARCH_PAYMENT, request, requestOptions);

    Type responseType = new TypeToken<DCResultsResourcesPage<Payment>>() {}.getType();
    DCResultsResourcesPage<Payment> result =
        deserializeResultsResourcesPageFromJson(responseType, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Creates a total refund for payment.
   *
   * @param paymentId payment id
   * @return PaymentRefund refund information
   * @throws DCException an error if the request fails
   */
  public PaymentRefund refund(Long paymentId) throws DCException, DCApiException {
    return this.refund(paymentId, null, null);
  }

  /**
   * Creates a total refund for payment.
   *
   * @param paymentId payment id
   * @param requestOptions metadata to customize the request
   * @return PaymentRefund refund information
   * @throws DCException an error if the request fails
   */
  public PaymentRefund refund(Long paymentId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return this.refund(paymentId, null, requestOptions);
  }

  /**
   * Creates a refund for payment.
   *
   * @param paymentId payment id
   * @param amount refund amount
   * @return PaymentRefund refund information
   * @throws DCException an error if the request fails
   */
  public PaymentRefund refund(Long paymentId, BigDecimal amount)
      throws DCException, DCApiException {
    return this.refund(paymentId, amount, null);
  }

  /**
   * Creates a refund for payment.
   *
   * @param paymentId payment id
   * @param amount refund amount
   * @param requestOptions metadata to customize the request
   * @return PaymentRefund refund information
   * @throws DCException an error if the request fails
   */
  public PaymentRefund refund(Long paymentId, BigDecimal amount, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return refundClient.refund(paymentId, amount, requestOptions);
  }

  /**
   * Gets a refund by id from the payment.
   *
   * @param paymentId payment id
   * @param refundId refund id
   * @return PaymentRefund refund information
   * @throws DCException an error if the request fails
   */
  public PaymentRefund getRefund(Long paymentId, Long refundId) throws DCException, DCApiException {
    return this.getRefund(paymentId, refundId, null);
  }

  /**
   * Gets a refund by id from the payment.
   *
   * @param paymentId payment id
   * @param refundId refund id
   * @param requestOptions metadata to customize the request
   * @return PaymentRefund refund information
   * @throws DCException an error if the request fails
   */
  public PaymentRefund getRefund(Long paymentId, Long refundId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return refundClient.get(paymentId, refundId, requestOptions);
  }

  /**
   * Lists the refunds of the payment.
   *
   * @param paymentId payment id
   * @return list of PaymentRefund
   * @throws DCException an error if the request fails
   */
  public DCResourceList<PaymentRefund> listRefunds(Long paymentId)
      throws DCException, DCApiException {
    return this.listRefunds(paymentId, null);
  }

  /**
   * Lists the refunds of the payment.
   *
   * @param paymentId payment id
   * @param requestOptions metadata to customize the request
   * @return list of PaymentRefund
   * @throws DCException an error if the request fails
   */
  public DCResourceList<PaymentRefund> listRefunds(Long paymentId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return refundClient.list(paymentId, requestOptions);
  }
}
