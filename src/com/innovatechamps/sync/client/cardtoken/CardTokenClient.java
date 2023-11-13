package com.innovatechamps.sync.client.cardtoken;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;

import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.client.DataClient;
import com.innovatechamps.sync.core.DCRequestOptions;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DCResponse;
import com.innovatechamps.sync.network.HttpMethod;
import com.innovatechamps.sync.resources.CardToken;
import com.innovatechamps.sync.serialization.Serializer;

/** Client for retrieving the token for a card. */
public class CardTokenClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(CardTokenClient.class.getName());

  /** Default constructor. Uses http client provided by DevChampsConfig. */
  public CardTokenClient() {
    this(DevChampsConfig.getHttpClient());
  }

  /**
   * CardTokenClient constructor.
   *
   * @param httpClient http client
   */
  public CardTokenClient(DCHttpClient httpClient) {
    super(httpClient);
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * Get card token.
   *
   * @param id card id
   * @return card token information
   */
  public CardToken get(String id) throws DCException, DCApiException {
    return this.get(id, null);
  }

  /**
   * Get card token.
   *
   * @param id card id
   * @param requestOptions metadata to customize the request
   * @return card token information
   */
  public CardToken get(String id, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    DCResponse response =
        send(String.format(URL_CARD_TOKEN, id), HttpMethod.GET, null, null, requestOptions);
    CardToken cardToken = Serializer.deserializeFromJson(CardToken.class, response.getContent());
    cardToken.setResponse(response);
    return cardToken;
  }

  /**
   * Create token associated with a card.
   *
   * @param request attributes used to perform the request
   * @return card token information
   * @throws DCException an error if the request fails
   */
  public CardToken create(CardTokenRequest request) throws DCException, DCApiException {
    return this.create(request, null);
  }

  /**
   * Create token associated with a card.
   *
   * @param request attributes used to perform the request
   * @param requestOptions metadata to customize the request
   * @return card token information
   * @throws DCException an error if the request fails
   */
  public CardToken create(CardTokenRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    DCResponse response =
        send(
            URL_CREATE_CARD_TOKEN,
            HttpMethod.POST,
            Serializer.serializeToJson(request),
            null,
            requestOptions);
    CardToken cardToken = Serializer.deserializeFromJson(CardToken.class, response.getContent());
    cardToken.setResponse(response);
    return cardToken;
  }
}
