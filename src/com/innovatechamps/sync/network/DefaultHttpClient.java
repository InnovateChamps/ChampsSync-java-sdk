package com.innovatechamps.sync.network;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;
import static com.innovatechamps.sync.network.HttpStatus.BAD_REQUEST;
import static com.innovatechamps.sync.network.HttpStatus.FORBIDDEN;
import static com.innovatechamps.sync.network.HttpStatus.INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.innovatechamps.sync.DevChampsConfig;
import com.innovatechamps.sync.execptions.DCApiException;
import com.innovatechamps.sync.execptions.DCException;
import com.innovatechamps.sync.execptions.DCMalformedRequestException;

/** default Http Client class. */
public class DefaultHttpClient implements DCHttpClient {
	private static final int VALIDATE_INACTIVITY_INTERVAL_MS = 30000;

	private static final int DEFAULT_RETRIES = 3;

	private static final String UTF_8 = "UTF-8";

	private static final String PAYLOAD_NOT_SUPPORTED_MESSAGE = "Payload not supported for this method.";

	private static final String HEADER_LOG_FORMAT = "%s: %s%s";

	private static final Logger LOGGER = Logger.getLogger(DefaultHttpClient.class.getName());

	private final HttpClient httpClient;

	/** DefaultHttpClient constructor. */
	public DefaultHttpClient() {
		this(null);
	}

	/** DefaultHttpClient constructor for testing only. */
	protected DefaultHttpClient(HttpClient httpClient) {
		StreamHandler streamHandler = getStreamHandler();
		streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
		LOGGER.addHandler(streamHandler);
		LOGGER.setLevel(DevChampsConfig.getLoggingLevel());

		if (Objects.isNull(httpClient)) {
			this.httpClient = createHttpClient();
		} else {
			this.httpClient = httpClient;
		}
	}

	private HttpClient createHttpClient() {
		SSLContext sslContext = SSLContexts.createDefault();
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
				new String[] { "TLSv1.2" }, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslConnectionSocketFactory).build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(DevChampsConfig.getMaxConnections());
		connectionManager.setDefaultMaxPerRoute(DevChampsConfig.getMaxConnections());
		connectionManager.setValidateAfterInactivity(VALIDATE_INACTIVITY_INTERVAL_MS);

		HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager)
				.setKeepAliveStrategy(new KeepAliveStrategy()).disableCookieManagement().disableRedirectHandling();

		if (Objects.isNull(DevChampsConfig.getProxy())) {
			httpClientBuilder.setProxy(DevChampsConfig.getProxy());
		}

		if (Objects.isNull(DevChampsConfig.getRetryHandler())) {
			httpClientBuilder.setRetryHandler(DevChampsConfig.getRetryHandler());
		} else {
			DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(DEFAULT_RETRIES, false);
			httpClientBuilder.setRetryHandler(retryHandler);
		}

		return httpClientBuilder.build();
	}

	@Override
	public DCResponse send(DCRequest mpRequest) throws DCException, DCApiException {
		try {
			HttpRequestBase completeRequest = createHttpRequest(mpRequest);
			HttpClientContext context = HttpClientContext.create();

			HttpResponse response = executeHttpRequest(mpRequest, completeRequest, context);

			String responseBody = EntityUtils.toString(response.getEntity(), UTF_8);
			Map<String, List<String>> headers = getHeaders(response);
			int statusCode = response.getStatusLine().getStatusCode();
			DCResponse mpResponse = new DCResponse(statusCode, headers, responseBody);

			if (statusCode > 299) {
				throw new DCApiException("Api error. Check response for details", mpResponse);
			}

			StringBuilder responseHeaders = new StringBuilder(
					String.format("Response headers:%s", System.lineSeparator()));
			for (Header header : response.getAllHeaders()) {
				responseHeaders.append(
						String.format(HEADER_LOG_FORMAT, header.getName(), header.getValue(), System.lineSeparator()));
			}
			LOGGER.fine(responseHeaders.toString());
			LOGGER.fine(String.format("Response status code: %s", response.getStatusLine().getStatusCode()));
			LOGGER.fine(String.format("Response body: %s", responseBody));

			return mpResponse;

		} catch (DCMalformedRequestException | DCApiException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new DCException(ex);
		}
	}

	private HttpRequestBase createHttpRequest(DCRequest mpRequest) throws DCMalformedRequestException {
		HttpEntity entity = normalizePayload(mpRequest.getPayload());
		HttpRequestBase request = getRequestBase(mpRequest.getMethod(), mpRequest.getUri(), entity);
		Map<String, String> headers = new HashMap<>(mpRequest.getHeaders());

		for (Map.Entry<String, String> header : headers.entrySet()) {
			request.addHeader(new BasicHeader(header.getKey(), header.getValue()));
		}

		int socketTimeout = mpRequest.getSocketTimeout() != 0 ? mpRequest.getSocketTimeout()
				: DevChampsConfig.getSocketTimeout();
		int connectionTimeout = mpRequest.getConnectionTimeout() != 0 ? mpRequest.getConnectionTimeout()
				: DevChampsConfig.getConnectionTimeout();
		int connectionRequestTimeout = mpRequest.getConnectionRequestTimeout() != 0
				? mpRequest.getConnectionRequestTimeout()
				: DevChampsConfig.getConnectionRequestTimeout();
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom().setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectionTimeout).setConnectionRequestTimeout(connectionRequestTimeout);

		request.setConfig(requestConfigBuilder.build());
		return request;
	}

	private HttpResponse executeHttpRequest(DCRequest mpRequest, HttpRequestBase completeRequest,
			HttpClientContext context) {
		try {
			if (Objects.nonNull(mpRequest.getPayload())) {
				LOGGER.fine(String.format("Request body: %s", mpRequest.getPayload().toString()));
			}

			StringBuilder headersMessage = new StringBuilder(
					String.format("Request Headers:%s", System.lineSeparator()));
			for (Map.Entry<String, String> entry : mpRequest.getHeaders().entrySet()) {
				headersMessage.append(
						String.format(HEADER_LOG_FORMAT, entry.getKey(), entry.getValue(), System.lineSeparator()));
			}
			LOGGER.fine(headersMessage.toString());

			return httpClient.execute(completeRequest, context);
		} catch (ClientProtocolException e) {
			LOGGER.fine(String.format("ClientProtocolException: %s", e.getMessage()));
			return new BasicHttpResponse(new BasicStatusLine(completeRequest.getProtocolVersion(), BAD_REQUEST, null));
		} catch (SSLPeerUnverifiedException e) {
			LOGGER.fine(String.format("SSLException: %s", e.getMessage()));
			return new BasicHttpResponse(new BasicStatusLine(completeRequest.getProtocolVersion(), FORBIDDEN, null));
		} catch (IOException e) {
			LOGGER.fine(String.format("IOException: %s", e.getMessage()));
			return new BasicHttpResponse(
					new BasicStatusLine(completeRequest.getProtocolVersion(), INTERNAL_SERVER_ERROR, null));
		}
	}

	private Map<String, List<String>> getHeaders(HttpResponse response) {
		Map<String, List<String>> headers = new HashMap<>();
		for (Header header : response.getAllHeaders()) {
			if (!headers.containsKey(header.getName())) {
				headers.put(header.getName(), new ArrayList<>());
			}
			headers.get(header.getName()).add(header.getValue());
		}
		return headers;
	}

	private HttpRequestBase getRequestBase(HttpMethod method, String uri, HttpEntity entity)
			throws DCMalformedRequestException {
		if (Objects.isNull(method)) {
			throw new DCMalformedRequestException(
					"HttpMethod must be either \"GET\", \"POST\", \"PUT\" or \"DELETE\".");
		}

		if (uri == null || uri.length() == 0 || uri.isEmpty()) {
			throw new DCMalformedRequestException("Uri can not be an empty String.");
		}

		if ((method.equals(HttpMethod.GET) || method.equals(HttpMethod.DELETE)) && Objects.nonNull(entity)) {
			throw new DCMalformedRequestException(PAYLOAD_NOT_SUPPORTED_MESSAGE);
		}

		return getHttpRequestBase(method, uri, entity);
	}

	private HttpRequestBase getHttpRequestBase(HttpMethod method, String uri, HttpEntity entity) {
		if (method.equals(HttpMethod.GET)) {
			return new HttpGet(uri);
		}

		if (method.equals(HttpMethod.POST)) {
			HttpPost post = new HttpPost(uri);
			post.setEntity(entity);
			return post;
		}

		if (method.equals(HttpMethod.PUT)) {
			HttpPut put = new HttpPut(uri);
			put.setEntity(entity);
			return put;
		}

		if (method.equals(HttpMethod.DELETE)) {
			return new HttpDelete(uri);
		}

		return null;
	}

	private HttpEntity normalizePayload(JsonObject payload) throws DCMalformedRequestException {
		if (payload != null && payload.size() != 0) {
			try {
				return new StringEntity(payload.toString(), UTF_8);
			} catch (Exception ex) {
				throw new DCMalformedRequestException(ex);
			}
		}
		return null;
	}
}
