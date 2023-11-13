package com.innovatechamps.sync.client.oauth;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;

import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.client.DataClient;
import com.innovatechamps.sync.client.user.UserClient;
import com.innovatechamps.sync.core.DCRequestOptions;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DCRequest;
import com.innovatechamps.sync.network.DCResponse;
import com.innovatechamps.sync.network.HttpMethod;
import com.innovatechamps.sync.network.UrlFormatter;
import com.innovatechamps.sync.resources.oauth.CreateOauthCredential;
import com.innovatechamps.sync.resources.oauth.RefreshOauthCredential;
import com.innovatechamps.sync.resources.user.User;
import com.innovatechamps.sync.serialization.Serializer;

/** Client responsible for performing oauth authorization. */
public class OauthClient extends DataClient {
  private static final Logger LOGGER = Logger.getLogger(OauthClient.class.getName());

  private final UserClient userClient;



  /** Default constructor. Uses the default http client used by the SDK. */
  public OauthClient() {
    this(DevChampsConfig.getHttpClient());
    StreamHandler streamHandler = getStreamHandler();
    streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
    LOGGER.addHandler(streamHandler);
    LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
  }

  /**
   * Constructor used for providing a custom http client.
   *
   * @param httpClient http client
   */
  public OauthClient(DCHttpClient httpClient) {
    super(httpClient);
    userClient = new UserClient(httpClient);
  }

  /**
   * Get URL for Oauth authorization.
   *
   * @param appId Id of the app
   * @param redirectUri URL for redirection after authorization
   * @return URL to perform authorization
   * @throws DCException an error if the request fails
   */
  public String getAuthorizationURL(String appId, String redirectUri)
      throws DCException, DCApiException {
    return this.getAuthorizationURL(appId, redirectUri, null);
  }

  /**
   * Get URL for Oauth authorization.
   *
   * @param appId Id of the app
   * @param redirectUri URL for redirection after authorization
   * @param requestOptions metadata to customize the request
   * @return URL to perform authorization
   * @throws DCException an error if the request fails
   */
  public String getAuthorizationURL(
      String appId, String redirectUri, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending get oauth authorization url request");

    User user = userClient.get(requestOptions);

    if (Objects.isNull(user) || user.getCountryId().isEmpty()) {
      return null;
    }

    HashMap<String, Object> queryParams = new HashMap<>();
    queryParams.put("client_id", appId);
    queryParams.put("response_type", "code");
    queryParams.put("platform_id", "mp");
    queryParams.put("redirect_uri", redirectUri);

    return UrlFormatter.format(
        String.format("%s.%s/authorization", AUTH_BASE_URL, user.getCountryId().toLowerCase()),
        queryParams);
  }

  /**
   * Create Oauth credentials to operate on behalf of a seller. Go <a
   * href=https://www.mercadopago.com.br/developers/en/guides/security/oauth>here</a> to learn more.
   *
   * @param authorizationCode authorization code received from calling getAuthorizationURL
   * @param redirectUri the redirectUri received from calling getAuthorizationURL
   * @return the Oauth credentials
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/oauth/_oauth_token/post">api
   *     docs</a>
   */
  public CreateOauthCredential createCredential(String authorizationCode, String redirectUri)
      throws DCException, DCApiException {
    return this.createCredential(authorizationCode, redirectUri, null);
  }

  /**
   * Create Oauth credentials to operate on behalf of a seller. Go <a
   * href=https://www.mercadopago.com.br/developers/en/guides/security/oauth>here</a> to learn more.
   *
   * @param authorizationCode authorization code received from calling getAuthorizationURL
   * @param redirectUri the redirectUri received from calling getAuthorizationURL
   * @param requestOptions metadata to customize the request
   * @return the Oauth credentials
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/oauth/_oauth_token/post">api
   *     docs</a>
   */
  public CreateOauthCredential createCredential(
      String authorizationCode, String redirectUri, DCRequestOptions requestOptions)
      throws DCException, DCApiException {
    LOGGER.info("Sending create oauth credential request");
    CreateOauthCredentialRequest request =
        CreateOauthCredentialRequest.builder()
            .clientSecret(getAccessToken(requestOptions))
            .code(authorizationCode)
            .redirectUri(redirectUri)
            .build();
    DCRequest mpRequest =
        DCRequest.buildRequest(
            URL_OAUTH, HttpMethod.POST, Serializer.serializeToJson(request), null, requestOptions);
    DCResponse response = send(mpRequest);

    CreateOauthCredential credential =
        Serializer.deserializeFromJson(CreateOauthCredential.class, response.getContent());
    credential.setResponse(response);

    return credential;
  }

  /**
   * Refresh Oauth credentials.
   *
   * @param refreshToken refresh token received when you create credentials
   * @return new Oauth credentials
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/oauth/_oauth_token/post">api
   *     docs</a>
   */
  public RefreshOauthCredential refreshCredential(String refreshToken)
      throws DCException, DCApiException {
    return this.refreshCredential(refreshToken, null);
  }

  /**
   * Refresh Oauth credentials.
   *
   * @param refreshToken refresh token received when you create credentials
   * @param requestOptions metadata to customize the request
   * @return new Oauth credentials
   * @throws DCException an error if the request fails
   * @see <a
   *     href="https://www.mercadopago.com.br/developers/en/reference/oauth/_oauth_token/post">api
   *     docs</a>
   */
  public RefreshOauthCredential refreshCredential(
      String refreshToken, DCRequestOptions requestOptions) throws DCException, DCApiException {
    LOGGER.info("Sending refresh oauth credential request");
    RefreshOauthCredentialRequest request =
        RefreshOauthCredentialRequest.builder()
            .clientSecret(getAccessToken(requestOptions))
            .refreshToken(refreshToken)
            .build();

    DCRequest mpRequest =
        DCRequest.buildRequest(
            URL_OAUTH, HttpMethod.POST, Serializer.serializeToJson(request), null, requestOptions);
    DCResponse response = send(mpRequest);
    RefreshOauthCredential credential =
        Serializer.deserializeFromJson(RefreshOauthCredential.class, response.getContent());
    credential.setResponse(response);

    return credential;
  }

  private String getAccessToken(DCRequestOptions requestOptions) {
    return Objects.isNull(requestOptions)
        ? DevChampsConfig.getAccessToken()
        : requestOptions.getAccessToken();
  }
}
