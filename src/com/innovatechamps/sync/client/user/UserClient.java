package com.innovatechamps.sync.client.user;

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
import com.innovatechamps.sync.resources.user.User;
import com.innovatechamps.sync.serialization.Serializer;

/** Client to get user information. */
public class UserClient extends DataClient {
	private static final Logger LOGGER = Logger.getLogger(UserClient.class.getName());

	/** Default constructor. Uses the default http client used by the SDK */
	public UserClient() {
		this(DevChampsConfig.getHttpClient());
	}

	/**
	 * Constructor used for providing a custom http client.
	 *
	 * @param httpClient Http Client
	 */
	public UserClient(DCHttpClient httpClient) {
		super(httpClient);
		StreamHandler streamHandler = getStreamHandler();
		streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
		LOGGER.addHandler(streamHandler);
		LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
	}

	/**
	 * Get user information.
	 *
	 * @return user information
	 * @throws DCException an error if the request fails
	 */
	public User get() throws DCException, DCApiException {
		return this.get(null);
	}

	/**
	 * Get user information with custom attributes on request.
	 *
	 * @param requestOptions metadata to customize the request
	 * @return user information
	 * @throws DCException an error if the request fails
	 */
	public User get(DCRequestOptions requestOptions) throws DCException, DCApiException {
		LOGGER.info("Sending get user request");
		DCResponse response = send(URL_GET_USER, HttpMethod.GET, null, null, requestOptions);
		User user = Serializer.deserializeFromJson(User.class, response.getContent());
		user.setResponse(response);
		return user;
	}
}
