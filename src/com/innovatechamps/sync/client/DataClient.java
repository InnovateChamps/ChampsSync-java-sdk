package com.innovatechamps.sync.client;

import com.google.gson.JsonObject;
import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.core.DCRequestOptions;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DCRequest;
import com.innovatechamps.sync.network.DCResponse;
import com.innovatechamps.sync.network.DCSearchRequest;
import com.innovatechamps.sync.network.Headers;
import com.innovatechamps.sync.network.HttpMethod;
import com.innovatechamps.sync.network.UrlFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Mercado Pago client class. */
public abstract class DataClient {
	/*
	 * GET O método GET solicita a representação de um recurso específico.
	 * Requisições utilizando o método GET devem retornar apenas dados.
	 * 
	 * HEAD O método HEAD solicita uma resposta de forma idêntica ao método GET,
	 * porém sem conter o corpo da resposta.
	 * 
	 * POST O método POST é utilizado para submeter uma entidade a um recurso
	 * específico, frequentemente causando uma mudança no estado do recurso ou
	 * efeitos colaterais no servidor.
	 * 
	 * PUT O método PUT substitui todas as atuais representações do recurso de
	 * destino pela carga de dados da requisição.
	 * 
	 * DELETE O método DELETE remove um recurso específico.
	 */

	protected static final String AUTH_BASE_URL = "https://auth.devchamps.com.br";

	public static final String URL_SERVICE = "/v1/service/%s";// Get
	public static final String URL_GET_USER = "/users/me";// Get

	public static final String URL_PREFERENCE = "/checkout/preferences/%s";// Get
	public static final String URL_SEARCH_PREFERENCE = "/checkout/preferences/search";// Get
	public static final String URL_CREATE_PREFERENCE = "/checkout/preferences";// Post

	public static final String URL_GET_PREAPPROVAL = "/preapproval/%s";// Get
	public static final String URL_SEARCH_PREAPPROVAL = "/preapproval/search";// Get
	public static final String URL_PREAPPROVAL = "/preapproval";// Post,Put

	public static final String URL_PAYMENT_METHODS = "/v1/payment_methods";// Get

	public static final String URL_PAYMENT = "/v1/payments/%s";// Get,Put
	public static final String URL_CREATE_PAYMENT = "/v1/payments";// Post
	public static final String URL_SEARCH_PAYMENT = "/v1/payments/search";// Get
	public static final String URL_PAYMENT_REFUND = "/v1/payments/%s/refunds";// Post,Get
	public static final String URL_GET_PAYMENT_REFUND = "/v1/payments/%s/refunds/%s";// Get,Put

	public static final String URL_OAUTH = "/oauth/token";// Post

	public static final String URL_MERCHANT = "/merchant_orders/%s";// Get, Put
	public static final String URL_CREATE_MERCHANT = "/merchant_orders";// Post
	public static final String URL_SEARCH_MERCHANT = "/merchant_orders/search";// Get

	public static final String URL_IDENTIFICATION_TYPES = "/v1/identification_types";// Get

	public static final String URL_CUSTOMER = "/v1/customers/%s";// Get, Put, Delete
	public static final String URL_SEARCH_CUSTOMER = "/v1/customers/search";// Get
	public static final String URL_CREATE_CUSTOMER = "/v1/customers";// Post

	public static final String URL_CARD_TOKEN = "/v1/card_tokens/%s";// Get
	public static final String URL_CREATE_CARD_TOKEN = "/v1/card_tokens";// Post

	private static final String ACCEPT_HEADER_VALUE = "application/json";

	private static final String CONTENT_TYPE_HEADER_VALUE = "application/json; charset=UTF-8";

	protected final DCHttpClient httpClient;

	protected Map<String, String> defaultHeaders;

	/**
	 * DataClient constructor.
	 *
	 * @param httpClient http client
	 */
	public DataClient(DCHttpClient httpClient) {
		this.httpClient = httpClient;
		this.defaultHeaders = new HashMap<>();
		defaultHeaders.put(Headers.ACCEPT, ACCEPT_HEADER_VALUE);
		defaultHeaders.put(Headers.PRODUCT_ID, DevChampsConfig.PRODUCT_ID);
		defaultHeaders.put(Headers.USER_AGENT, String.format("DevChamps Java SDK/%s", DevChampsConfig.CURRENT_VERSION));
		defaultHeaders.put(Headers.TRACKING_ID, DevChampsConfig.TRACKING_ID);
		defaultHeaders.put(Headers.CONTENT_TYPE, CONTENT_TYPE_HEADER_VALUE);
	}

	/**
	 * Method used directly or by other methods to make requests.
	 *
	 * @param request request data
	 * @return DCResponse response object
	 * @throws DCException exception
	 */
	protected DCResponse send(DCRequest request) throws DCException, DCApiException {
		String uri = UrlFormatter.format(request.getUri(), request.getQueryParams());

		return httpClient.send(DCRequest.builder().uri(uri).method(request.getMethod())
				.headers(addDefaultHeaders(request)).payload(request.getPayload())
				.connectionRequestTimeout(addConnectionRequestTimeout(request, null))
				.connectionTimeout(addConnectionTimeout(request, null)).socketTimeout(addSocketTimeout(request, null))
				.build());
	}

	/**
	 * Method used directly or by other methods to make requests with request
	 * options.
	 *
	 * @param request        request
	 * @param requestOptions requestOptions
	 * @return DCResponse response
	 * @throws DCException exception
	 */
	protected DCResponse send(DCRequest request, DCRequestOptions requestOptions) throws DCException, DCApiException {
		return this.send(this.buildRequest(request.getUri(), request.getMethod(), request.getPayload(),
				request.getQueryParams(), requestOptions));
	}

	/**
	 * Method used directly or by other methods to make requests.
	 *
	 * @param path        path of request url
	 * @param method      http method used in the request
	 * @param payload     request body
	 * @param queryParams query string params
	 * @return DCResponse response data
	 * @throws DCException exception
	 */
	protected DCResponse send(String path, HttpMethod method, JsonObject payload, Map<String, Object> queryParams)
			throws DCException, DCApiException {
		return this.send(path, method, payload, queryParams, null);
	}

	/**
	 * Method used directly or by other methods to make requests.
	 *
	 * @param path           path of request url
	 * @param method         http method used in the request
	 * @param payload        request body
	 * @param queryParams    query string params
	 * @param requestOptions extra data used to override configuration passed to
	 *                       DevChampsConfig for a single request
	 * @return response data
	 * @throws DCException exception
	 */
	protected DCResponse send(String path, HttpMethod method, JsonObject payload, Map<String, Object> queryParams,
			DCRequestOptions requestOptions) throws DCException, DCApiException {
		DCRequest mpRequest = buildRequest(path, method, payload, queryParams, requestOptions);
		return this.send(mpRequest);
	}

	/**
	 * Convenience method to perform searches.
	 *
	 * @param path    path of request url
	 * @param request parameters for performing search request
	 * @return response data
	 * @throws DCException exception
	 */
	protected DCResponse search(String path, DCSearchRequest request) throws DCException, DCApiException {
		return this.search(path, request, null);
	}

	/**
	 * Convenience method to perform searches.
	 *
	 * @param path           path of searchRequest url
	 * @param searchRequest  parameters for performing search searchRequest
	 * @param requestOptions extra data used to override configuration passed to
	 *                       DevChampsConfig for a single searchRequest
	 * @return response data
	 * @throws DCException exception
	 */
	protected DCResponse search(String path, DCSearchRequest searchRequest, DCRequestOptions requestOptions)
			throws DCException, DCApiException {
		Map<String, Object> queryParams = Objects.nonNull(searchRequest) ? searchRequest.getParameters() : null;

		return this.send(path, HttpMethod.GET, null, queryParams, requestOptions);
	}

	/**
	 * Convenience method to perform requests that returns lists of results.
	 *
	 * @param path           path of request url
	 * @param requestOptions extra data used to override configuration passed to
	 *                       DevChampsConfig for a single request
	 * @return response data
	 * @throws DCException exception
	 */
	protected DCResponse list(String path, DCRequestOptions requestOptions) throws DCException, DCApiException {
		return this.list(path, HttpMethod.GET, null, null, requestOptions);
	}

	/**
	 * Convenience method to perform requests that returns lists of results.
	 *
	 * @param path           path of request url
	 * @param method         http method used in the request
	 * @param payload        request body
	 * @param queryParams    query string params
	 * @param requestOptions extra data used to override configuration passed to
	 *                       DevChampsConfig for a single request
	 * @return response data
	 * @throws DCException exception
	 */
	protected DCResponse list(String path, HttpMethod method, JsonObject payload, Map<String, Object> queryParams,
			DCRequestOptions requestOptions) throws DCException, DCApiException {
		return this.send(path, method, payload, queryParams, requestOptions);
	}

	private DCRequest buildRequest(String path, HttpMethod method, JsonObject payload, Map<String, Object> queryParams,
			DCRequestOptions requestOptions) {

		return DCRequest.builder().uri(path).accessToken(getAccessToken(requestOptions)).payload(payload).method(method)
				.queryParams(queryParams).headers(addCustomHeaders(path, requestOptions))
				.connectionRequestTimeout(addConnectionRequestTimeout(null, requestOptions))
				.connectionTimeout(addConnectionTimeout(null, requestOptions))
				.socketTimeout(addSocketTimeout(null, requestOptions)).build();
	}

	private int addSocketTimeout(DCRequest request, DCRequestOptions requestOptions) {
		if (Objects.nonNull(requestOptions) && requestOptions.getSocketTimeout() > 0) {
			return requestOptions.getSocketTimeout();
		}

		if (Objects.nonNull(request) && request.getSocketTimeout() > 0) {
			return request.getSocketTimeout();
		}

		return DevChampsConfig.getSocketTimeout();
	}

	private int addConnectionTimeout(DCRequest request, DCRequestOptions requestOptions) {
		if (Objects.nonNull(requestOptions) && requestOptions.getConnectionTimeout() > 0) {
			return requestOptions.getConnectionTimeout();
		}

		if (Objects.nonNull(request) && request.getConnectionTimeout() > 0) {
			return request.getConnectionTimeout();
		}

		return DevChampsConfig.getConnectionTimeout();
	}

	private int addConnectionRequestTimeout(DCRequest request, DCRequestOptions requestOptions) {
		if (Objects.nonNull(requestOptions) && requestOptions.getConnectionRequestTimeout() > 0) {
			return requestOptions.getConnectionRequestTimeout();
		}

		if (Objects.nonNull(request) && request.getConnectionRequestTimeout() > 0) {
			return request.getConnectionRequestTimeout();
		}

		return DevChampsConfig.getConnectionRequestTimeout();
	}

	private Map<String, String> addCustomHeaders(String uri, DCRequestOptions requestOptions) {
		Map<String, String> headers = new HashMap<>();

		if (Objects.nonNull(requestOptions) && Objects.nonNull(requestOptions.getCustomHeaders())) {
			for (Map.Entry<String, String> entry : requestOptions.getCustomHeaders().entrySet()) {
				headers.put(entry.getKey(), entry.getValue());
			}
		}

		if (!uri.contains("/oauth/token")) {
			headers.put("Authorization", String.format("Bearer %s", getAccessToken(requestOptions)));
		}
		return headers;
	}

	private Map<String, String> addDefaultHeaders(DCRequest request) {
		Map<String, String> headers = Objects.nonNull(request.getHeaders()) ? request.getHeaders() : new HashMap<>();

		for (Map.Entry<String, String> entry : defaultHeaders.entrySet()) {
			headers.put(entry.getKey(), entry.getValue());
		}

		if (shouldAddIdempotencyKey(request)) {
			headers.put(Headers.IDEMPOTENCY_KEY, request.createIdempotencyKey());
		}

		if (!request.getUri().contains("/oauth/token") && !headers.containsKey("Authorization")) {
			headers.put("Authorization", String.format("Bearer %s", getAccessToken(null)));
		}

		return headers;
	}

	private boolean shouldAddIdempotencyKey(DCRequest request) {
		return request.getMethod() == HttpMethod.POST;
	}

	private String getAccessToken(DCRequestOptions requestOptions) {
		return Objects.nonNull(requestOptions) && Objects.nonNull(requestOptions.getAccessToken())
				&& !requestOptions.getAccessToken().isEmpty() ? requestOptions.getAccessToken()
						: DevChampsConfig.getAccessToken();
	}
}
