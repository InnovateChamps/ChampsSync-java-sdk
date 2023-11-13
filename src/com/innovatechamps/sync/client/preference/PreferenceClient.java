package com.innovatechamps.sync.client.preference;

import com.google.gson.reflect.TypeToken;
import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.client.DataClient;
import com.innovatechamps.sync.core.DCRequestOptions;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.network.DCElementsResourcesPage;
import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DCRequest;
import com.innovatechamps.sync.network.DCResponse;
import com.innovatechamps.sync.network.DCSearchRequest;
import com.innovatechamps.sync.network.HttpMethod;
import com.innovatechamps.sync.resources.preference.Preference;
import com.innovatechamps.sync.resources.preference.PreferenceSearch;
import com.innovatechamps.sync.serialization.Serializer;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;
import static com.innovatechamps.sync.serialization.Serializer.deserializeElementsResourcesPageFromJson;
import static com.innovatechamps.sync.serialization.Serializer.deserializeFromJson;

import java.lang.reflect.Type;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/** Client that use the Preferences APIs. */
public class PreferenceClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(PreferenceClient.class.getName());


  /** Default constructor. Uses the default http client used by the SDK. */
  public PreferenceClient() {
    this(DevChampsConfig.getHttpClient());
  }

  /**
   * Constructor used for providing a custom http client.
   *
   * @param httpClient httpClient
   */
  public PreferenceClient(DCHttpClient httpClient) {
    super(httpClient);
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * Method responsible for getting preference.
   *
   * @param id preference id
   * @return preference information
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/preferences/_checkout_preferences_id/get">api
   *     docs</a>
   */
  public Preference get(String id) throws DCException, DCApiException {
    return this.get(id, null);
  }

  /**
   * Method responsible for getting preference.
   *
   * @param id preference id
   * @param requestOptions metadata to customize the request
   * @return preference information
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/preferences/_checkout_preferences_id/get">api
   *     docs</a>
   */
  public Preference get(String id, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending get preference request");
    DCResponse response =
        send(String.format(URL_PREFERENCE, id), HttpMethod.GET, null, null, requestOptions);

    Preference result = deserializeFromJson(Preference.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Method responsible for creating preference.
   *
   * @param request attributes used to create a preference
   * @return preference information
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/preferences/_checkout_preferences/post">api
   *     docs</a>
   */
  public Preference create(PreferenceRequest request) throws DCException, DCApiException {
    return this.create(request, null);
  }

  /**
   * Method responsible for creating preference with request options.
   *
   * @param request attributes used to create a preference
   * @param requestOptions metadata to customize the request
   * @return preference information
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/preferences/_checkout_preferences/post">api
   *     docs</a>
   */
  public Preference create(PreferenceRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending create preference request");

    DCRequest mpRequest =
        DCRequest.builder()
            .uri(URL_CREATE_PREFERENCE)
            .method(HttpMethod.POST)
            .payload(Serializer.serializeToJson(request))
            .build();

    DCResponse response = send(mpRequest, requestOptions);
    Preference result = deserializeFromJson(Preference.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Method responsible for updating preference.
   *
   * @param id preference id
   * @param request attributes used to create a preference
   * @return preference information
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/preferences/_checkout_preferences_id/put">api
   *     docs</a>
   */
  public Preference update(String id, PreferenceRequest request)
      throws DCException, DCApiException {
    return this.update(id, request, null);
  }

  /**
   * Method responsible for updating preference with request options.
   *
   * @param id preference id
   * @param request attributes used to create a preference
   * @param requestOptions metadata to customize the request
   * @return preference information
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/preferences/_checkout_preferences_id/put">api
   *     docs</a>
   */
  public Preference update(String id, PreferenceRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending update preference request");

    DCRequest mpRequest =
        DCRequest.builder()
            .uri(String.format(URL_PREFERENCE, id))
            .method(HttpMethod.PUT)
            .payload(Serializer.serializeToJson(request))
            .build();

    DCResponse response = send(mpRequest, requestOptions);
    Preference result = deserializeFromJson(Preference.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Method responsible for search preference.
   *
   * @param request attributes used to create a preference
   * @return list of results
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/preferences/_checkout_preferences_search/get">api
   *     docs</a>
   */
  public DCElementsResourcesPage<PreferenceSearch> search(DCSearchRequest request)
      throws DCException, DCApiException {
    return this.search(request, null);
  }

  /**
   * Method responsible for search preference.
   *
   * @param request attributes used to create a preference
   * @param requestOptions metadata to customize the request
   * @return list of results
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/preferences/_checkout_preferences_search/get">api
   *     docs</a>
   */
  public DCElementsResourcesPage<PreferenceSearch> search(
      DCSearchRequest request, DCRequestOptions requestOptions) throws DCException, DCApiException {
    LOGGER.info("Sending search preference request");

    DCResponse response = search(URL_SEARCH_PREFERENCE, request, requestOptions);

    Type responseType = new TypeToken<DCElementsResourcesPage<PreferenceSearch>>() {}.getType();
    DCElementsResourcesPage<PreferenceSearch> result =
        deserializeElementsResourcesPageFromJson(responseType, response.getContent());
    result.setResponse(response);

    return result;
  }
}
