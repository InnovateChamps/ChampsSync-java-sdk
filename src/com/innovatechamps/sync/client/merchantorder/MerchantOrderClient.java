package com.innovatechamps.sync.client.merchantorder;

import static com.innovatechamps.sync.DevChampsConfig.getStreamHandler;
import static com.innovatechamps.sync.serialization.Serializer.deserializeElementsResourcesPageFromJson;
import static com.innovatechamps.sync.serialization.Serializer.deserializeFromJson;

import java.lang.reflect.Type;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

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
import com.innovatechamps.sync.resources.merchantorder.MerchantOrder;
import com.innovatechamps.sync.serialization.Serializer;

/** MerchantOrderClient class. */
public class MerchantOrderClient extends DataClient {
	private static final Logger LOGGER = Logger.getLogger(MerchantOrderClient.class.getName());

	/** Default constructor. Uses the default http client used by the SDK. */
	public MerchantOrderClient() {
		this(DevChampsConfig.getHttpClient());
	}

	/**
	 * Constructor used for providing a custom http client.
	 *
	 * @param httpClient httpClient
	 */
	public MerchantOrderClient(DCHttpClient httpClient) {
		super(httpClient);
		StreamHandler streamHandler = getStreamHandler();
		streamHandler.setLevel(DevChampsConfig.getLoggingLevel());
		LOGGER.addHandler(streamHandler);
		LOGGER.setLevel(DevChampsConfig.getLoggingLevel());
	}

	/**
	 * Method responsible for getting merchant order.
	 *
	 * @param id merchant order id
	 * @return merchant order information
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/merchant_orders/_merchant_orders_id/get">api
	 *      docs</a>
	 */
	public MerchantOrder get(Long id) throws DCException, DCApiException {
		return this.get(id, null);
	}

	/**
	 * Method responsible for getting merchant order.
	 *
	 * @param id             merchant order id
	 * @param requestOptions metadata to customize the request
	 * @return merchant order information
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/merchant_orders/_merchant_orders_id/get">api
	 *      docs</a>
	 */
	public MerchantOrder get(Long id, DCRequestOptions requestOptions) throws DCException, DCApiException {
		LOGGER.info("Sending get merchant order request");

		DCRequest mpRequest = DCRequest.builder().uri(String.format(URL_MERCHANT, id.toString())).method(HttpMethod.GET)
				.build();

		DCResponse response = send(mpRequest, requestOptions);
		MerchantOrder result = deserializeFromJson(MerchantOrder.class, response.getContent());
		result.setResponse(response);

		return result;
	}

	/**
	 * Method responsible for creating merchant order.
	 *
	 * @param request attributes used to create merchant order
	 * @return merchant order information
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/merchant_orders/_merchant_orders/post">api
	 *      docs</a>
	 */
	public MerchantOrder create(MerchantOrderCreateRequest request) throws DCException, DCApiException {
		return this.create(request, null);
	}

	/**
	 * Method responsible for creating merchant order with request options.
	 *
	 * @param request        attributes used to create merchant order
	 * @param requestOptions metadata to customize the request
	 * @return merchant order information
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/merchant_orders/_merchant_orders/post">api
	 *      docs</a>
	 */
	public MerchantOrder create(MerchantOrderCreateRequest request, DCRequestOptions requestOptions)
			throws DCException, DCApiException {
		LOGGER.info("Sending create merchant order request");

		DCRequest mpRequest = DCRequest.builder().uri(URL_CREATE_MERCHANT).method(HttpMethod.POST)
				.payload(Serializer.serializeToJson(request)).build();

		DCResponse response = send(mpRequest, requestOptions);
		MerchantOrder result = deserializeFromJson(MerchantOrder.class, response.getContent());
		result.setResponse(response);

		return result;
	}

	/**
	 * Method responsible for creating merchant order.
	 *
	 * @param request attributes used to update merchant order
	 * @param id      merchant order id
	 * @return merchant order information
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/merchant_orders/_merchant_orders_id/put">api
	 *      docs</a>
	 */
	public MerchantOrder update(Long id, MerchantOrderUpdateRequest request) throws DCException, DCApiException {
		return this.update(id, request, null);
	}

	/**
	 * Method responsible for creating merchant order with request options.
	 *
	 * @param request        attributes used to update merchant order
	 * @param id             merchant order id
	 * @param requestOptions metadata to customize the request
	 * @return merchant order response
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/merchant_orders/_merchant_orders_id/put">api
	 *      docs</a>
	 */
	public MerchantOrder update(Long id, MerchantOrderUpdateRequest request, DCRequestOptions requestOptions)
			throws DCException, DCApiException {
		LOGGER.info("Sending update merchant order request");

		DCRequest mpRequest = DCRequest.builder().uri(String.format(URL_MERCHANT, id.toString())).method(HttpMethod.PUT)
				.payload(Serializer.serializeToJson(request)).build();

		DCResponse response = send(mpRequest, requestOptions);
		MerchantOrder result = deserializeFromJson(MerchantOrder.class, response.getContent());
		result.setResponse(response);

		return result;
	}

	/**
	 * Method responsible for search merchant order.
	 *
	 * @param request attributes used to search merchant order
	 * @return list of results
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/merchant_orders/_merchant_orders_search/get">api
	 *      docs</a>
	 */
	public DCElementsResourcesPage<MerchantOrder> search(DCSearchRequest request) throws DCException, DCApiException {
		return this.search(request, null);
	}

	/**
	 * Method responsible for search merchant order.
	 *
	 * @param request        attributes used to search merchant order
	 * @param requestOptions metadata to customize the request
	 * @return list of results
	 * @throws DCException an error if the request fails
	 * @see <a href=
	 *      "https://www.mercadopago.com.br/developers/en/reference/merchant_orders/_merchant_orders_search/get">api
	 *      docs</a>
	 */
	public DCElementsResourcesPage<MerchantOrder> search(DCSearchRequest request, DCRequestOptions requestOptions)
			throws DCException, DCApiException {
		LOGGER.info("Sending search merchant order request");

		DCResponse response = search(URL_SEARCH_MERCHANT, request, requestOptions);

		Type responseType = new TypeToken<DCElementsResourcesPage<MerchantOrder>>() {
		}.getType();
		DCElementsResourcesPage<MerchantOrder> result = deserializeElementsResourcesPageFromJson(responseType,
				response.getContent());
		result.setResponse(response);

		return result;
	}
}
