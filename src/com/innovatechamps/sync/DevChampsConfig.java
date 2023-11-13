package com.innovatechamps.sync;

import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.StreamHandler;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;

import com.innovatechamps.sync.network.DCHttpClient;
import com.innovatechamps.sync.network.DefaultHttpClient;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

public class DevChampsConfig {

	public static final String CURRENT_VERSION = "2.2.3";

	public static final String PRODUCT_ID = "6S8M2VRYS89XQ7WEGS";

	public static final String TRACKING_ID = String.format("platform:%s,type:SDK%s,so;",
			DevChampsConfig.getJavaVersion(), DevChampsConfig.CURRENT_VERSION);

	public static final String BASE_URL = "http://localhost:8080";
//	public static final String BASE_URL = "https://api.devchamps.com.br";

	private static final int DEFAULT_MAX_CONNECTIONS = 10;

	private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 20000;

	private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS = 20000;

	private static final int DEFAULT_SOCKET_TIMEOUT_MS = 20000;

	private static final String DEFAULT_METRICS_SCOPE = "production";

	private static final Level DEFAULT_LOGGING_LEVEL = Level.OFF;

	@Getter @Setter private static volatile String accessToken;

	@Getter @Setter   private static volatile StreamHandler loggingHandler;

	@Getter @Setter private static volatile String metricsScope = DEFAULT_METRICS_SCOPE;

	@Getter @Setter private static volatile Level loggingLevel = DEFAULT_LOGGING_LEVEL;

	@Getter @Setter private static volatile int maxConnections = DEFAULT_MAX_CONNECTIONS;

	@Getter @Setter private static volatile int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_MS;

	@Getter @Setter private static volatile int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT_MS;

	@Getter @Setter private static volatile int socketTimeout = DEFAULT_SOCKET_TIMEOUT_MS;

	@Setter private static volatile DCHttpClient httpClient;

	@NonNull
	@Getter(onMethod_ = {@Synchronized})
	@Setter(onMethod_ = {@Synchronized})
	private static HttpHost proxy;
	
	@Getter @Setter private static HttpRequestRetryHandler retryHandler;

	  /**
	   * Verifies which http client use.
	   *
	   * @return DCHttpClient
	   */
	  public static synchronized DCHttpClient getHttpClient() {
	    if (Objects.isNull(httpClient)) {
	      httpClient = new DefaultHttpClient();
	    }
	    return httpClient;
	  }

	/**
	 * Method responsible for return Java version.
	 *
	 * @return java version
	 */
	public static synchronized String getJavaVersion() {
		String version = System.getProperty("java.runtime.version");
		if (Objects.isNull(version)) {
			return null;
		}

		String major = version.replaceAll("^1\\.", "");
		int dotIndex = major.indexOf('.');
		if (dotIndex != -1) {
			major = major.substring(0, dotIndex);
		}

		return major + "|" + version;
	}

	/**
	 * Method responsible for return StreamHandler.
	 *
	 * @return StreamHandler
	 */
	public static StreamHandler getStreamHandler() {
		return Objects.nonNull(loggingHandler) ? loggingHandler : new ConsoleHandler();
	}
}
