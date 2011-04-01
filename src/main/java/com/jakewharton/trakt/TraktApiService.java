package com.jakewharton.trakt;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.apibuilder.ApiException;
import com.jakewharton.apibuilder.ApiService;
import com.jakewharton.trakt.enumerations.MediaType;
import com.jakewharton.trakt.util.Base64;

/**
 * Trakt-specific API service extension which facilitates provides helper
 * methods for performing remote method calls as well as deserializing the
 * corresponding JSON responses.
 * 
 * @author Jake Wharton <jakewharton@gmail.com>
 */
public abstract class TraktApiService extends ApiService {
	/** Default connection timeout (in milliseconds). */
	private static final int DEFAULT_TIMEOUT_CONNECT = 60 * 1000;
	
	/** Default read timeout (in milliseconds). */
	private static final int DEFAULT_TIMEOUT_READ = 60 * 1000;
	
	/** HTTP header name for authorization. */
	private static final String HEADER_AUTHORIZATION = "Authorization";
	
	/** HTTP authorization type. */
	private static final String HEADER_AUTHORIZATION_TYPE = "Basic";
	
	/** Character set used for encoding and decoding transmitted values. */
	private static final Charset UTF_8_CHAR_SET = Charset.forName(ApiService.CONTENT_ENCODING);
	
	/** HTTP post method name. */
	private static final String HTTP_METHOD_POST = "POST";
	
	/** Format for decoding JSON dates in string format. */
	private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");
	
	
	/** JSON parser for reading the content stream. */
    private final JsonParser parser;
    
    /** API key. */
    private String apiKey;
	
    
    /**
     * Create a new Trakt service with our proper default values.
     */
	public TraktApiService() {
		this.parser = new JsonParser();
		
		this.setConnectTimeout(DEFAULT_TIMEOUT_CONNECT);
		this.setReadTimeout(DEFAULT_TIMEOUT_READ);
	}
	
	
	/**
	 * Execute request using HTTP GET.
	 * 
	 * @param url URL to request.
	 * @return JSON object.
	 */
	public JsonElement get(String url) {
		return this.unmarshall(this.executeGet(url));
	}
	
	/**
	 * Execute request using HTTP POST.
	 * 
	 * @param url URL to request.
	 * @param postBody JSON object to use as the POST body.
	 * @return JSON object.
	 */
	public JsonElement post(String url, JsonObject postBody) {
		return this.unmarshall(this.executeMethod(url, postBody.getAsString(), null, HTTP_METHOD_POST, HttpURLConnection.HTTP_OK));
	}
	
	/**
	 * Set email and password to use for HTTP basic authentication.
	 * 
	 * @param username Username.
	 * @param password_sha Password SHA1.
	 */
	public void setAuthentication(String username, String password_sha) {
		if ((username == null) || (username.length() == 0)) {
			throw new IllegalArgumentException("Username must not be empty.");
		}
		if ((password_sha == null) || (password_sha.length() == 0)) {
			throw new IllegalArgumentException("Password SHA must not be empty.");
		}
		
		String source = username + ":" + password_sha;
		String authentication = HEADER_AUTHORIZATION_TYPE + " " + Base64.encodeBytes(source.getBytes());
		
		this.addRequestHeader(HEADER_AUTHORIZATION, authentication);
	}
	
	/**
	 * Set API key to use for client authentication by Trakt.
	 * 
	 * @param value of API key.
	 */
	public void setApiKey(String value) {
		this.apiKey = value;
	}
	
	/**
	 * Get the API key.
	 * 
	 * @return value of API key.
	 */
	/*package*/ String getApiKey() {
		return this.apiKey;
	}

	/**
	 * Use GSON to deserialize a JSON object to a native class representation.
	 * 
	 * @param <T> Native class type.
	 * @param typeToken Native class type wrapper.
	 * @param response Serialized JSON object.
	 * @return Deserialized native instance.
	 */
	@SuppressWarnings("unchecked")
	protected <T> T unmarshall(TypeToken<T> typeToken, JsonElement response) {
		return (T)TraktApiService.getGsonBuilder().create().fromJson(response, typeToken.getType());
	}
	
	/**
	 * Read the entirety of an input stream and parse to a JSON object.
	 * 
	 * @param jsonContent JSON content input stream.
	 * @return Parsed JSON object.
	 */
	protected JsonElement unmarshall(InputStream jsonContent) {
        try {
        	JsonElement element = this.parser.parse(new InputStreamReader(jsonContent, UTF_8_CHAR_SET));
        	if (element.isJsonObject()) {
        		return element.getAsJsonObject();
        	} else if (element.isJsonArray()) {
        		return element.getAsJsonArray();
        	} else {
        		throw new ApiException("Unknown content found in response." + element);
        	}
        } catch (Exception e) {
            throw new ApiException(e);
        } finally {
	        ApiService.closeStream(jsonContent);
	    }
	}

	/**
	 * Create a {@link GsonBuilder} and register all of the custom types needed
	 * in order to properly deserialize complex Trakt-specific type.
	 * 
	 * @return Assembled GSON builder instance.
	 */
	static GsonBuilder getGsonBuilder() {
		GsonBuilder builder = new GsonBuilder();
		
		//class types
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				//TODO: localize from GMT-8
				try {
					return new Date(json.getAsLong() * TraktApiBuilder.MILLISECONDS_IN_SECOND); //S to MS
				} catch (NumberFormatException outer) {
					try {
						return JSON_STRING_DATE.parse(json.getAsString());
					} catch (ParseException inner) {
						throw outer;
					}
				}
			}
		});
		
		//enum types
		builder.registerTypeAdapter(MediaType.class, new JsonDeserializer<MediaType>() {
			@Override
			public MediaType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
				return MediaType.fromValue(json.getAsString());
			}
		});
		
		return builder;
	}
}
