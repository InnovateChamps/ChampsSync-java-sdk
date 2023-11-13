package com.innovatechamps.sync.client.identificationtype;

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
import com.innovatechamps.sync.resources.identificationtype.IdentificationType;

/** Client with methods of Identification Type APIs. */
public class IdentificationTypeClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(IdentificationTypeClient.class.getName());

  /** Default constructor. Uses the default http client used by the SDK. */
  public IdentificationTypeClient() {
    this(DevChampsConfig.getHttpClient());
  }

  /**
   * Constructor used for providing a custom http client.
   *
   * @param httpClient httpClient
   */
  public IdentificationTypeClient(DCHttpClient httpClient) {
    super(httpClient);
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * List all identification types.
   *
   * @return list of identification types
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/identification_types/_identification_types/get">api
   *     docs</a>
   */
  public DCResourceList<IdentificationType> list() throws DCException, DCApiException {
    return this.list(null);
  }

  /**
   * List all identification types.
   *
   * @param requestOptions metadata to customize the request
   * @return list of identification types
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/identification_types/_identification_types/get">api
   *     docs</a>
   */
  public DCResourceList<IdentificationType> list(DCRequestOptions requestOptions)
      throws DCException, DCApiException
  {
    LOGGER.info("Sending list identification types");

    DCResponse response =
        list(URL_IDENTIFICATION_TYPES, HttpMethod.GET, null, null, requestOptions);

    DCResourceList<IdentificationType> identificationTypes =
        deserializeListFromJson(IdentificationType.class, response.getContent());
    identificationTypes.setResponse(response);

    return identificationTypes;
  }
}
