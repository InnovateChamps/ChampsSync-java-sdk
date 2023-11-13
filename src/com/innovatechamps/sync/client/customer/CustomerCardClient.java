package com.innovatechamps.sync.client.customer;

import com.google.gson.JsonObject;
import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.client.DataClient;
import com.innovatechamps.sync.core.DCRequestOptions;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DCRequest;
import com.innovatechamps.sync.network.DCResourceList;
import com.innovatechamps.sync.network.DCResponse;
import com.innovatechamps.sync.network.HttpMethod;
import com.innovatechamps.sync.resources.customer.CustomerCard;
import com.innovatechamps.sync.serialization.Serializer;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;

import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/** Client responsible for performing customer card actions. */
public class CustomerCardClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(CustomerCardClient.class.getName());

  /** Default constructor. Uses the default http client used by the SDK */
  public CustomerCardClient() {
    this(DevChampsConfig.getHttpClient());
  }

  /**
   * Constructor used for providing a custom http client.
   *
   * @param httpClient http client used for performing requests
   */
  public CustomerCardClient(DCHttpClient httpClient) {
    super(httpClient);
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * Get card of customer.
   *
   * @param customerId id of the customer to which the card belongs
   * @param cardId id of the card being requested
   * @return the requested customer card
   * @throws DCException any error retrieving the customer card
   */
  public CustomerCard get(String customerId, String cardId) throws DCException, DCApiException {
    return this.get(customerId, cardId, null);
  }

  /**
   * Get card of customer.
   *
   * @param customerId id of the customer
   * @param cardId id of the card being retrieved
   * @param requestOptions metadata to customize the request
   * @return customer card retrieved
   * @throws DCException any error retrieving the customer card
   */
  public CustomerCard get(String customerId, String cardId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    DCResponse response =
        send(
            String.format("/v1/customers/%s/cards/%s", customerId, cardId),
            HttpMethod.GET,
            null,
            null,
            requestOptions);

    CustomerCard card = Serializer.deserializeFromJson(CustomerCard.class, response.getContent());
    card.setResponse(response);
    return card;
  }

  /**
   * Add card for customer.
   *
   * @param customerId id of the customer
   * @param request attributes used to perform the request
   * @return the customer card just added
   * @throws DCException any error creating the customer card
   */
  public CustomerCard create(String customerId, CustomerCardCreateRequest request)
      throws DCException, DCApiException {
    return this.create(customerId, request, null);
  }

  /**
   * Add card for customer.
   *
   * @param customerId id of the customer
   * @param request attributes used to perform the request
   * @param requestOptions metadata to customize the request
   * @return the customer card just added
   * @throws DCException any error creating the customer card
   */
  public CustomerCard create(
      String customerId, CustomerCardCreateRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending create customer card request");
    JsonObject payload = Serializer.serializeToJson(request);
    DCRequest mpRequest =
        DCRequest.buildRequest(
            String.format("/v1/customers/%s/cards", customerId),
            HttpMethod.POST,
            payload,
            null,
            requestOptions);
    DCResponse response = send(mpRequest);

    CustomerCard card = Serializer.deserializeFromJson(CustomerCard.class, response.getContent());
    card.setResponse(response);
    return card;
  }

  /**
   * Remove card for customer.
   *
   * @param customerId id of the customer
   * @param cardId id of the card being removed
   * @return the customer card just removed
   * @throws DCException any error removing the customer card
   */
  public CustomerCard delete(String customerId, String cardId) throws DCException, DCApiException {
    return this.delete(customerId, cardId, null);
  }

  /**
   * Remove card for customer.
   *
   * @param customerId id of the customer
   * @param cardId id of the card being retrieved
   * @param requestOptions metadata to customize the request
   * @return the customer card just removed
   * @throws DCException any error removing the customer card
   */
  public CustomerCard delete(String customerId, String cardId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending delete customer card request");

    DCResponse response =
        send(
            String.format("/v1/customers/%s/cards/%s", customerId, cardId),
            HttpMethod.DELETE,
            null,
            null,
            requestOptions);

    CustomerCard card = Serializer.deserializeFromJson(CustomerCard.class, response.getContent());
    card.setResponse(response);
    return card;
  }

  /**
   * List all cards of customer.
   *
   * @param customerId id of the customer
   * @return list of customer cards retrieved
   * @throws DCException any error listing customer cards
   */
  public DCResourceList<CustomerCard> listAll(String customerId)
      throws DCException, DCApiException {
    return this.listAll(customerId, null);
  }

  /**
   * List all cards of customer.
   *
   * @param customerId id of the customer
   * @param requestOptions metadata to customize the request
   * @return list of customer cards retrieved
   * @throws DCException any error listing customer cards
   */
  public DCResourceList<CustomerCard> listAll(String customerId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending list all customer cards request");
    DCResponse response =
        list(
            String.format("/v1/customers/%s/cards", customerId),
            HttpMethod.GET,
            null,
            null,
            requestOptions);

    DCResourceList<CustomerCard> cards =
        Serializer.deserializeListFromJson(CustomerCard.class, response.getContent());
    cards.setResponse(response);
    return cards;
  }
}
