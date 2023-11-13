package com.innovatechamps.sync.serialization;

import static com.google.gson.stream.JsonToken.END_DOCUMENT;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import com.innovatechamps.sync.execptions.DCJsonParseException;
import com.innovatechamps.sync.network.DCElementsResourcesPage;
import com.innovatechamps.sync.network.DCResource;
import com.innovatechamps.sync.network.DCResourceList;
import com.innovatechamps.sync.network.DCResultsResourcesPage;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializer class, responsible for objects serialization and deserialization.
 */
public class Serializer {

	private static final String DESERIALIZE_DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS[XXX][XX][X]";

	private static final String SERIALIZE_DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(OffsetDateTime.class,
					(JsonDeserializer<OffsetDateTime>) (json, type, context) -> OffsetDateTime.parse(json.getAsString(),
							DateTimeFormatter.ofPattern(DESERIALIZE_DATE_FORMAT_ISO8601)))
			.registerTypeAdapter(OffsetDateTime.class,
					(JsonSerializer<OffsetDateTime>) (offsetDateTime, type, context) -> new JsonPrimitive(
							DateTimeFormatter.ofPattern(SERIALIZE_DATE_FORMAT_ISO8601).format(offsetDateTime)))
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	/**
	 * Method responsible for deserialize objects.
	 *
	 * @param clazz      class.
	 * @param jsonObject json object.
	 * @param <T>        class type.
	 * @return object.
	 * @throws DCJsonParseException if json cannot be deserialized to an MPResource
	 */
	public static <T extends DCResource> T deserializeFromJson(Class<T> clazz, String jsonObject)
			throws DCJsonParseException {
		try {
			if (isJsonValid(jsonObject)) {
				return GSON.fromJson(jsonObject, clazz);
			} else {
				throw new DCJsonParseException(String.format("Could not parse json: %s", jsonObject));
			}
		} catch (IOException e) {
			throw new DCJsonParseException("Could not parse json", e);
		}
	}

	/**
	 * Method responsible for deserialize json to ResultsResources.
	 *
	 * @param type       type
	 * @param jsonObject jsonObject
	 * @param <T>        generic type
	 * @return MPResultsResourcesPage deserialized MPResource
	 * @throws DCJsonParseException if json cannot be parsed to ResultsResourcesPage
	 */
	public static <T extends DCResource> DCResultsResourcesPage<T> deserializeResultsResourcesPageFromJson(Type type,
			String jsonObject) throws DCJsonParseException {
		try {
			if (isJsonValid(jsonObject)) {
				return GSON.fromJson(jsonObject, type);
			} else {
				throw new DCJsonParseException(String.format("Could not parse json: %s", jsonObject));
			}
		} catch (IOException e) {
			throw new DCJsonParseException("Could not parse json", e);
		}
	}

	/**
	 * Method responsible for deserialize json to ElementsResources.
	 *
	 * @param type       type
	 * @param jsonObject jsonObject
	 * @param <T>        generic type
	 * @return MPElementsResourcesPage
	 * @throws DCJsonParseException if json cannot be parsed to
	 *                              MPElementsResourcesPage
	 */
	public static <T extends DCResource> DCElementsResourcesPage<T> deserializeElementsResourcesPageFromJson(Type type,
			String jsonObject) throws DCJsonParseException {
		try {
			if (isJsonValid(jsonObject)) {
				return GSON.fromJson(jsonObject, type);
			} else {
				throw new DCJsonParseException(String.format("Could not parse json: %s", jsonObject));
			}
		} catch (IOException | DCJsonParseException e) {
			throw new DCJsonParseException("Could not parse json", e);
		}
	}

	/**
	 * Method responsible for deserialize objects.
	 *
	 * @param clazz      clazz
	 * @param jsonObject jsonObject
	 * @param <T>        type
	 * @return MPResourceList
	 * @throws DCJsonParseException if json cannot be parsed to ResultsResourcesPage
	 */
	public static <T extends DCResource> DCResourceList<T> deserializeListFromJson(Class<T> clazz, String jsonObject)
			throws DCJsonParseException {
		try {
			if (isJsonValid(jsonObject)) {
				DCResourceList<T> resourceList = new DCResourceList<>();
				List<T> results = new ArrayList<>();
				JsonArray jsonArray = JsonParser.parseString(jsonObject).getAsJsonArray();
				for (int i = 0; i < jsonArray.size(); i++) {
					T resource = GSON.fromJson(jsonArray.get(i), clazz);
					results.add(resource);
				}
				resourceList.setResults(results);

				return resourceList;
			} else {
				throw new DCJsonParseException(String.format("Could not parse json: %s", jsonObject));
			}
		} catch (IOException e) {
			throw new DCJsonParseException("Could not parse json", e);
		}
	}

	/**
	 * Method responsible for serialize objects.
	 *
	 * @param resource resource.
	 * @param <T>      class type.
	 * @return JsonObject.
	 */
	public static <T> JsonObject serializeToJson(T resource) {
		return (JsonObject) GSON.toJsonTree(resource);
	}

	/**
	 * Verify if json is valid.
	 *
	 * @param json json
	 * @return boolean
	 * @throws IOException exception
	 */
	public static boolean isJsonValid(String json) throws IOException {
		try {
			JsonReader jsonReader = new JsonReader(new StringReader(json));
			JsonToken token;
			loop: while ((token = jsonReader.peek()) != END_DOCUMENT && token != null) {
				switch (token) {
				case BEGIN_ARRAY:
					jsonReader.beginArray();
					break;
				case END_ARRAY:
					jsonReader.endArray();
					break;
				case BEGIN_OBJECT:
					jsonReader.beginObject();
					break;
				case END_OBJECT:
					jsonReader.endObject();
					break;
				case NAME:
					jsonReader.nextName();
					break;
				case STRING:
				case NUMBER:
				case BOOLEAN:
				case NULL:
					jsonReader.skipValue();
					break;
				case END_DOCUMENT:
					break loop;
				default:
					throw new AssertionError(token);
				}
			}
			return true;
		} catch (final MalformedJsonException ignored) {
			return false;
		}
	}
}
