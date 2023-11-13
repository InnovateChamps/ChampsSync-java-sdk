package com.innovatechamps.sync.client.paymentmethod;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;
import static com.innovatechamps.sync.serialization.Serializer.deserializeListFromJson;

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
import com.innovatechamps.sync.resources.paymentmethod.PaymentMethod;

/** Client with methods of Payment Method APIs. */
public class PaymentMethodClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(PaymentMethodClient.class.getName());

  /** Default constructor. Uses the default http client used by the SDK. */
  public PaymentMethodClient() {
    this(DevChampsConfig.getHttpClient());
  }

  /**
   * Constructor used for providing a custom http client.
   *
   * @param httpClient httpClient
   */
  public PaymentMethodClient(DCHttpClient httpClient) {
    super(httpClient);
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * List all payment methods.
   *
   * @return list of payment methods
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/payment_methods/_payment_methods/get">api
   *     docs</a>
   */
  public DCResourceList<PaymentMethod> list() throws DCException, DCApiException {
    return this.list(null);
  }

  /**
   * List all payment methods.
   *
   * @param requestOptions metadata to customize the request
   * @return list of payment methods
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/payment_methods/_payment_methods/get">api
   *     docs</a>
   */
  public DCResourceList<PaymentMethod> list(DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending list payment method");

    DCResponse response = list(URL_PAYMENT_METHODS, HttpMethod.GET, null, null, requestOptions);

    DCResourceList<PaymentMethod> paymentMethods =
        deserializeListFromJson(PaymentMethod.class, response.getContent());
    paymentMethods.setResponse(response);

    return paymentMethods;
  }
}
