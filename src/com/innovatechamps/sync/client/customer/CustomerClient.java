package com.innovatechamps.sync.client.customer;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;
import static com.innovatechamps.sync.serialization.Serializer.deserializeResultsResourcesPageFromJson;

import java.lang.reflect.Type;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.google.gson.JsonObject;
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
import com.innovatechamps.sync.resources.customer.Customer;
import com.innovatechamps.sync.resources.customer.CustomerCard;
import com.innovatechamps.sync.serialization.Serializer;

/** Client responsible for performing customer actions. */
public class CustomerClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(CustomerClient.class.getName());

  private final CustomerCardClient cardClient;

  /** Default constructor. Uses the default http client used by the SDK */
  public CustomerClient() {
    this(DevChampsConfig.getHttpClient());
  }

  /**
   * Constructor used for providing a custom http client.
   *
   * @param httpClient http client used for performing requests
   */
  public CustomerClient(DCHttpClient httpClient) {
    super(httpClient);
    cardClient = new CustomerCardClient(httpClient);
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * Get customer.
   *
   * @param customerId id of the customer to which the card belongs
   * @return the requested customer card
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com/developers/en/reference/customers/_customers_id/get/">api
   *     docs</a>
   */
  public Customer get(String customerId) throws DCException, DCApiException {
    return this.get(customerId, null);
  }

  /**
   * Get customer.
   *
   * @param customerId id of the customer
   * @param requestOptions metadata to customize the request
   * @return the requested customer card
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com/developers/en/reference/customers/_customers_id/get/">api
   *     docs</a>
   */
  public Customer get(String customerId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending get customer request");

    DCResponse response =
        send(
            String.format(URL_CUSTOMER, customerId),
            HttpMethod.GET,
            null,
            null,
            requestOptions);

    Customer customer = Serializer.deserializeFromJson(Customer.class, response.getContent());
    customer.setResponse(response);
    return customer;
  }

  /**
   * Add new customer.
   *
   * @param request attributes used to perform the request
   * @return the customer just added
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com/developers/en/reference/customers/_customers/post/">api
   *     docs</a>
   */
  public Customer create(CustomerRequest request) throws DCException, DCApiException {
    return this.create(request, null);
  }

  /**
   * Add new customer.
   *
   * @param request attributes used to perform the request
   * @param requestOptions metadata to customize the request
   * @return the customer just added
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com/developers/en/reference/customers/_customers/post/">api
   *     docs</a>
   */
  public Customer create(CustomerRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending create customer request");

    JsonObject payload = Serializer.serializeToJson(request);
    DCRequest mpRequest =
        DCRequest.buildRequest(URL_CREATE_CUSTOMER, HttpMethod.POST, payload, null, requestOptions);
    DCResponse response = send(mpRequest);

    Customer customer = Serializer.deserializeFromJson(Customer.class, response.getContent());
    customer.setResponse(response);
    return customer;
  }

  /**
   * Update customer.
   *
   * @param customerId id of the customer
   * @param request attributes used to perform the request
   * @return the customer just updated
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com/developers/en/reference/customers/_customers_id/put/">api
   *     docs</a>
   */
  public Customer update(String customerId, CustomerRequest request)
      throws DCException, DCApiException {
    return this.update(customerId, request, null);
  }

  /**
   * Update customer.
   *
   * @param customerId id of the customer
   * @param request attributes used to perform the request
   * @param requestOptions metadata to customize the request
   * @return the customer just updated
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com/developers/en/reference/customers/_customers_id/put/">api
   *     docs</a>
   */
  public Customer update(
      String customerId, CustomerRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending update customer request");

    JsonObject payload = Serializer.serializeToJson(request);
    DCRequest mpRequest =
        DCRequest.buildRequest(
            String.format(URL_CUSTOMER, customerId),
            HttpMethod.PUT,
            payload,
            null,
            requestOptions);
    DCResponse response = send(mpRequest);

    Customer customer = Serializer.deserializeFromJson(Customer.class, response.getContent());
    customer.setResponse(response);
    return customer;
  }

  /**
   * Delete customer.
   *
   * @param customerId id of the customer
   * @return the customer just deleted
   * @throws DCException an error if the request fails
   */
  public Customer delete(String customerId) throws DCException, DCApiException {
    return this.delete(customerId, null);
  }

  /**
   * Delete customer.
   *
   * @param customerId id of the customer
   * @param requestOptions metadata to customize the request
   * @return the customer just deleted
   * @throws DCException an error if the request fails
   */
  public Customer delete(String customerId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending delete customer request");

    DCRequest mpRequest =
        DCRequest.buildRequest(
            String.format(URL_CUSTOMER, customerId),
            HttpMethod.DELETE,
            null,
            null,
            requestOptions);
    DCResponse response = send(mpRequest);

    Customer customer = Serializer.deserializeFromJson(Customer.class, response.getContent());
    customer.setResponse(response);
    return customer;
  }

  /**
   * Search customer.
   *
   * @param request attributes used to perform the request
   * @return search result
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/customers/_customers_search/get">api
   *     docs</a>
   */
  public DCResultsResourcesPage<Customer> search(DCSearchRequest request)
      throws DCException, DCApiException {
    return this.search(request, null);
  }

  /**
   * Search customer.
   *
   * @param request attributes used to search for customer
   * @param requestOptions metadata to customize the request
   * @return search result
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/customers/_customers_search/get">api
   *     docs</a>
   */
  public DCResultsResourcesPage<Customer> search(
      DCSearchRequest request, DCRequestOptions requestOptions) throws DCException, DCApiException {
    LOGGER.info("Sending search customer request");

    DCResponse response = search(URL_SEARCH_CUSTOMER, request, requestOptions);

    Type responseType = new TypeToken<DCResultsResourcesPage<Customer>>() {}.getType();
    DCResultsResourcesPage<Customer> result =
        deserializeResultsResourcesPageFromJson(responseType, response.getContent());
    result.setResponse(response);
    return result;
  }

  /**
   * Get customer card by id.
   *
   * @param customerId id of the customer
   * @param cardId id of the card
   * @return the requested card
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/cards/_customers_customer_id_cards_id/get">api
   *     docs</a>
   */
  public CustomerCard getCard(String customerId, String cardId) throws DCException, DCApiException {
    return this.getCard(customerId, cardId, null);
  }

  /**
   * Get customer card by id.
   *
   * @param customerId id of the customer
   * @param cardId id of the card
   * @param requestOptions metadata to customize the request
   * @return the requested card
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/cards/_customers_customer_id_cards_id/get">api
   *     docs</a>
   */
  public CustomerCard getCard(String customerId, String cardId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return cardClient.get(customerId, cardId, requestOptions);
  }

  /**
   * Associate new card with customer.
   *
   * @param customerId id of the customer
   * @param request attributes used to associate a new card with customer
   * @return the added card
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/cards/_customers_customer_id_cards/post">api
   *     docs</a>
   */
  public CustomerCard createCard(String customerId, CustomerCardCreateRequest request)
      throws DCException, DCApiException {
    return this.createCard(customerId, request, null);
  }

  /**
   * Associate new card with customer.
   *
   * @param customerId id of the customer
   * @param request attributes used to associate a new card with customer
   * @param requestOptions metadata to customize the request
   * @return the added card
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/cards/_customers_customer_id_cards/post">api
   *     docs</a>
   */
  public CustomerCard createCard(
      String customerId, CustomerCardCreateRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return cardClient.create(customerId, request, requestOptions);
  }

  /**
   * Delete card.
   *
   * @param customerId id of the customer
   * @param cardId id of the card being removed
   * @return the deleted card
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/cards/_customers_customer_id_cards_id/delete">api
   *     docs</a>
   */
  public CustomerCard deleteCard(String customerId, String cardId)
      throws DCException, DCApiException {
    return this.deleteCard(customerId, cardId, null);
  }

  /**
   * Delete card.
   *
   * @param customerId id of the customer
   * @param cardId id of the card being removed
   * @param requestOptions metadata to customize the request
   * @return the deleted card
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/cards/_customers_customer_id_cards_id/delete">api
   *     docs</a>
   */
  public CustomerCard deleteCard(String customerId, String cardId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return cardClient.delete(customerId, cardId, requestOptions);
  }

  /**
   * List customer cards.
   *
   * @param customerId id of the customer
   * @return list of customer cards
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/cards/_customers_customer_id_cards/get">api
   *     docs</a>
   */
  public DCResourceList<CustomerCard> listCards(String customerId)
      throws DCException, DCApiException {
    return this.listCards(customerId, null);
  }

  /**
   * List customer cards.
   *
   * @param customerId id of the customer
   * @param requestOptions metadata to customize the request
   * @return list of customer cards
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/cards/_customers_customer_id_cards/get">api
   *     docs</a>
   */
  public DCResourceList<CustomerCard> listCards(String customerId, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    return cardClient.listAll(customerId, requestOptions);
  }
}
