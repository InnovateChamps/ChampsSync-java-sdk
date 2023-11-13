package com.innovatechamps.sync.client.preapproval;

import com.google.gson.reflect.TypeToken;
import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.client.DataClient;
import com.innovatechamps.sync.core.DCRequestOptions;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DCResponse;
import com.innovatechamps.sync.network.DCResultsResourcesPage;
import com.innovatechamps.sync.network.DCSearchRequest;
import com.innovatechamps.sync.network.HttpMethod;
import com.innovatechamps.sync.resources.preapproval.Preapproval;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;
import static com.innovatechamps.sync.serialization.Serializer.deserializeFromJson;
import static com.innovatechamps.sync.serialization.Serializer.deserializeResultsResourcesPageFromJson;
import static com.innovatechamps.sync.serialization.Serializer.serializeToJson;

import java.lang.reflect.Type;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/** Client that use the Preapproval APIs. */
public class PreapprovalClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(PreapprovalClient.class.getName());


  /** Default constructor. Uses the default http client used by the SDK. */
  public PreapprovalClient() {
    this(DevChampsConfig.getHttpClient());
  }

  /**
   * Constructor used for providing a custom http client.
   *
   * @param httpClient httpClient
   */
  public PreapprovalClient(DCHttpClient httpClient) {
    super(httpClient);
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * Get a Preapproval by your ID.
   *
   * @param id preapproval id.
   * @return Preapproval pre approval information
   * @throws DCException an error if the request fails
   */
  public Preapproval get(String id) throws DCException, DCApiException {
    return this.get(id, null);
  }

  /**
   * Get a Preapproval by your ID.
   *
   * @param id preapprovalId.
   * @param requestOptions metadata to customize the request
   * @return Preapproval pre approval information
   * @throws DCException an error if the request fails
   */
  public Preapproval get(String id, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending get preapproval request");
    DCResponse response =
        send(String.format(URL_GET_PREAPPROVAL, id), HttpMethod.GET, null, null, requestOptions);

    Preapproval result = deserializeFromJson(Preapproval.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Creates a Preapproval.
   *
   * @param request attributes used to create a preapproval
   * @return Preapproval pre approval information
   * @throws DCException an error if the request fails
   */
  public Preapproval create(PreapprovalCreateRequest request) throws DCException, DCApiException {
    return this.create(request, null);
  }

  /**
   * Creates a Preapproval.
   *
   * @param request attributes used to create a preapproval
   * @param requestOptions metadata to customize the request
   * @return Preapproval pre approval information
   * @throws DCException an error if the request fails
   */
  public Preapproval create(PreapprovalCreateRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending create preapproval request");
    DCResponse response =
        send(URL_PREAPPROVAL, HttpMethod.POST, serializeToJson(request), null, requestOptions);

    Preapproval result = deserializeFromJson(Preapproval.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Updates a Preapproval.
   *
   * @param id preapprovalId
   * @param request attributes used for the update
   * @return Preapproval pre approval information
   * @throws DCException an error if the request fails
   */
  public Preapproval update(String id, PreapprovalUpdateRequest request)
      throws DCException, DCApiException {
    return this.update(id, request, null);
  }

  /**
   * Updates a Preapproval.
   *
   * @param id preapprovalId
   * @param request attributes used for the update
   * @param requestOptions metadata to customize the request
   * @return Preapproval pre approval information
   * @throws DCException an error if the request fails
   */
  public Preapproval update(
      String id, PreapprovalUpdateRequest request, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending update preapproval request");
    DCResponse response =
        send(
            String.format(URL_PREAPPROVAL, id),
            HttpMethod.PUT,
            serializeToJson(request),
            null,
            requestOptions);

    Preapproval result = deserializeFromJson(Preapproval.class, response.getContent());
    result.setResponse(response);

    return result;
  }

  /**
   * Method responsible for search preapprovals.
   *
   * @param request attributes used for the search
   * @return list of results
   * @throws DCException an error if the request fails
   */
  public DCResultsResourcesPage<Preapproval> search(DCSearchRequest request)
      throws DCException, DCApiException {
    return this.search(request, null);
  }

  /**
   * Method responsible for search preapprovals.
   *
   * @param request attributes used for the search
   * @param requestOptions metadata to customize the request
   * @return list of results
   * @throws DCException an error if the request fails
   */
  public DCResultsResourcesPage<Preapproval> search(
      DCSearchRequest request, DCRequestOptions requestOptions) throws DCException, DCApiException {
    LOGGER.info("Sending search preapproval request");
    DCResponse response = search(URL_SEARCH_PREAPPROVAL, request, requestOptions);

    Type responseType = new TypeToken<DCResultsResourcesPage<Preapproval>>() {}.getType();
    DCResultsResourcesPage<Preapproval> result =
        deserializeResultsResourcesPageFromJson(responseType, response.getContent());
    result.setResponse(response);

    return result;
  }
}
