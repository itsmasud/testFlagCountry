package com.fieldnation.webapi;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.ParseException;

import com.fieldnation.json.JsonObject;

public class AccessToken {

	private String _hostname;

	private int _expiresIn = 0;
	private String _accessToken = null;
	private String _tokenType = null;
	private String _scope = null;
	private String _refreshToken = null;

	public AccessToken(String hostname, String grantType, String clientId,
			String clientSecret, String username, String password)
			throws MalformedURLException, IOException, java.text.ParseException {
		this(hostname, "/authentication/api/oauth/token", grantType, clientId,
				clientSecret, username, password);
	}

	public AccessToken(String hostname, String path, String grantType,
			String clientId, String clientSecret, String username,
			String password) throws MalformedURLException, IOException,
			java.text.ParseException {

		_hostname = hostname;

		if (!path.startsWith("/"))
			path = "/" + path;

		HttpURLConnection conn = null;
		if (Ws.USE_HTTPS) {
			conn = (HttpURLConnection) new URL("https://" + hostname + path)
					.openConnection();
		} else {
			conn = (HttpURLConnection) new URL("http://" + hostname + path)
					.openConnection();
		}

		conn.setRequestMethod("POST");
		conn.setRequestProperty("ContentType",
				"application/x-www-form-urlencoded");

		conn.setDoInput(true);
		conn.setDoOutput(true);

		OutputStream out = conn.getOutputStream();

		out.write(("grant_type=" + grantType + "&client_id=" + clientId
				+ "&client_secret=" + clientSecret + "&username=" + username
				+ "&password=" + password).getBytes());

		Result result = new Result(conn);

		JsonObject token = result.getResultsAsJsonObject();

		// TODO, add some timing, learn how to use the refresh token
		_accessToken = token.getString("access_token");
		_tokenType = token.getString("token_type");
		_scope = token.getString("scope");
		_refreshToken = token.getString("refresh_token");
		_expiresIn = token.getInt("expires_in");
	}

	public String getHostname() {
		return _hostname;
	}

	public String addToken(String options) throws ParseException {
		if (options == null || options.equals("")) {
			return "?access_token=" + _accessToken;
		} else if (options.startsWith("?")) { // if options already specified
			return "?access_token=" + _accessToken + "&" + options.substring(1);
		}
		throw new ParseException(
				"Options must be nothing, or start with '?'. Got: " + options);
	}

}
