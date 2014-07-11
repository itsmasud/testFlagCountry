package com.fieldnation.webapi;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;

import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.fieldnation.R;
import com.fieldnation.auth.server.AuthCache;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

/**
 * Represents an oauth token. This class should be an immutable class.
 * 
 * @author michael.carver
 * 
 */
public class OAuth {
	private static final String TAG = "webapi.OAuth";

	/* creates a default string to use for all tokens */
	private static String _defaults;

	static {
		try {
			JsonObject o = new JsonObject();
			o.put("access_token", null);
			o.put("token_type", null);
			o.put("scope", null);
			o.put("refresh_token", null);
			o.put("expires_in", -1);
			o.put("expires_on", -1);
			o.put("hostname", null);
			o.put("is_local", false);
			o.put("refresh_token", null);
			_defaults = o.toString();

		} catch (ParseException e) {
		}
	}

	private JsonObject _oauth = new JsonObject(_defaults);

	/*-*********************************-*/
	/*-			Constructors			-*/
	/*-*********************************-*/
	private OAuth(JsonObject json) throws ParseException {
		_oauth.merge(json, true, true);

		if (_oauth.getLong("expires_in") == -1 && _oauth.getLong("expires_on") != -1) {
			_oauth.put("expires_in", (_oauth.getLong("expires_on") - System.currentTimeMillis()) / 1000);
		} else if (_oauth.getLong("expires_in") != -1 && _oauth.getLong("expires_on") == -1) {
			_oauth.put("expires_on", _oauth.getLong("expires_in") * 1000 + System.currentTimeMillis());
		}
	}

	private OAuth(String jsonString) throws ParseException {
		this(new JsonObject(jsonString));
	}

	/*-*********************************-*/
	/*-			Instantiators			-*/
	/*-*********************************-*/
	/**
	 * Create an oauth from a cached oAuthBlob.
	 * 
	 * @see AuthCache
	 * 
	 * @param oauthBlob
	 * @return
	 * @throws ParseException
	 */
	public static OAuth fromCache(String oauthBlob) throws ParseException {
		return new OAuth(oauthBlob);
	}

	/**
	 * Authenticates the user against the Field Nation servers
	 * 
	 * @param hostname
	 * @param grantType
	 * @param clientId
	 * @param clientSecret
	 * @param username
	 * @param password
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static OAuth authServer(String hostname, String grantType, String clientId, String clientSecret,
			String username, String password) throws MalformedURLException, IOException, ParseException {
		return authServer(hostname, "/authentication/api/oauth/token", grantType, clientId, clientSecret, username,
				password);
	}

	/**
	 * Authenticates the user against the Field Nation servers
	 * 
	 * @param hostname
	 * @param path
	 * @param grantType
	 * @param clientId
	 * @param clientSecret
	 * @param username
	 * @param password
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static OAuth authServer(String hostname, String path, String grantType, String clientId,
			String clientSecret, String username, String password) throws MalformedURLException, IOException, ParseException {

		if (!path.startsWith("/"))
			path = "/" + path;

		HttpURLConnection conn = null;
		if (Ws.USE_HTTPS) {
			// only allow if debugging
			if (Ws.DEBUG)
				Ws.trustAllHosts();
			conn = (HttpURLConnection) new URL("https://" + hostname + path).openConnection();

			// only allow if debugging
			if (Ws.DEBUG)
				((HttpsURLConnection) conn).setHostnameVerifier(Ws.DO_NOT_VERIFY);
		} else {
			conn = (HttpURLConnection) new URL("http://" + hostname + path).openConnection();
		}

		conn.setRequestMethod("POST");
		conn.setRequestProperty("ContentType", "application/x-www-form-urlencoded");

		conn.setDoInput(true);
		conn.setDoOutput(true);

		OutputStream out = conn.getOutputStream();

		String payload = "grant_type=" + grantType + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&username=" + misc.escapeForURL(username) + "&password=" + misc.escapeForURL(password);
		Log.v(TAG, payload);
		// payload = misc.escapeForURL(payload);
		out.write(payload.getBytes());

		Result result = new Result(conn);

		conn.disconnect();
		Log.v(TAG, result.getResponseCode() + "");
		JsonObject token = result.getResultsAsJsonObject();
		// TODO, add some timing, learn how to use the refresh token
		token.put("hostname", hostname);

		return new OAuth(token);
	}

	/**
	 * Invalidates the authtoken for this oauth with the {@link AccountManager}
	 * 
	 * @param context
	 */
	public void invalidate(Context context) {
		AccountManager am = AccountManager.get(context);
		am.invalidateAuthToken(context.getString(R.string.accounttype), toString());
	}

	/**
	 * 
	 * @return The TTL on this oauth token in seconds
	 */
	public long getExpiresIn() {
		try {
			return _oauth.getLong("expires_in");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 
	 * @return The UTC time in milliseconds that this expires on
	 */
	public long getExpiresOn() {
		try {
			return _oauth.getLong("expires_on");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 
	 * @return the access token for web requests
	 */
	public String getAccessToken() {
		try {
			return _oauth.getString("access_token");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @return true if the token is expired, false otherwise
	 */
	public boolean isExpired() {
		return System.currentTimeMillis() > getExpiresOn();
	}

	public JsonObject toJson() throws java.text.ParseException {
		return _oauth.copy();
	}

	@Override
	public String toString() {
		try {
			return toJson().toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getHostname() {
		try {
			return _oauth.getString("hostname");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parses a URL option string and adds the access_token to it.
	 * 
	 * @param options
	 * @return
	 * @throws ParseException
	 */
	public String applyToUrlOptions(String options) throws ParseException {
		if (options == null || options.equals("")) {
			return "?access_token=" + getAccessToken();
		} else if (options.startsWith("?")) { // if options already specified
			return "?access_token=" + getAccessToken() + "&" + options.substring(1);
		}
		throw new ParseException("Options must be nothing, or start with '?'. Got: " + options, 0);
	}

}
