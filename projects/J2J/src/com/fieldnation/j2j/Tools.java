package com.fieldnation.j2j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;

import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

public class Tools {
	public static String authServer(String hostname, String path, String grantType, String clientId,
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
		// payload = misc.escapeForURL(payload);
		out.write(payload.getBytes());

		Result result = new Result(conn);

		conn.disconnect();
		JsonObject token = result.getResultsAsJsonObject();

		return token.getString("access_token");
	}
}
