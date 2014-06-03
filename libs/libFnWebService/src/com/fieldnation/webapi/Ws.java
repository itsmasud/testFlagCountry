package com.fieldnation.webapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.apache.http.HttpConnection;

import android.net.Uri;

public class Ws {
	public static boolean USE_HTTPS = true;

	private AccessToken _accessToken = null;

	public Ws(AccessToken accessToken) {
		_accessToken = accessToken;
	}

	protected Ws() {
	}

	public Result httpRead(String method, String path, String options,
			String contentType) throws MalformedURLException, IOException,
			ParseException {
		if (!path.startsWith("/"))
			path = "/" + path;

		if (_accessToken != null)
			options = _accessToken.addToken(options);

		HttpURLConnection conn = null;
		if (USE_HTTPS) {
			conn = (HttpURLConnection) new URL("https://"
					+ _accessToken.getHostname() + path + options)
					.openConnection();
		} else {
			conn = (HttpURLConnection) new URL("http://"
					+ _accessToken.getHostname() + path + options)
					.openConnection();
		}

		conn.setRequestMethod(method);
		if (contentType != null)
			conn.setRequestProperty("ContentType", contentType);

		conn.setDoInput(true);

		return new Result(conn);
	}

	public Result httpGet(String path) throws MalformedURLException,
			IOException, ParseException {
		return httpGet(path, null, null);
	}

	public Result httpGet(String path, String options, String contentType)
			throws MalformedURLException, IOException, ParseException {
		return httpRead("GET", path, options, contentType);
	}

	public Result httpDelete(String path, String options, String contentType)
			throws MalformedURLException, IOException, ParseException {
		return httpRead("DELETE", path, options, contentType);
	}

	public Result httpWrite(String method, String path, String options,
			byte[] data, String contentType) throws MalformedURLException,
			IOException, ParseException {

		if (!path.startsWith("/"))
			path = "/" + path;

		if (_accessToken != null)
			options = _accessToken.addToken(options);

		HttpURLConnection conn = null;
		if (USE_HTTPS) {
			conn = (HttpURLConnection) new URL("https://"
					+ _accessToken.getHostname() + path + options)
					.openConnection();
		} else {
			conn = (HttpURLConnection) new URL("http://"
					+ _accessToken.getHostname() + path + options)
					.openConnection();
		}

		conn.setRequestMethod(method);
		conn.setRequestProperty("ContentType", contentType);

		conn.setDoInput(true);
		conn.setDoOutput(true);

		OutputStream out = conn.getOutputStream();

		out.write(data);

		return new Result(conn);
	}

	public Result httpPost(String path, String options, byte[] data,
			String contentType) throws MalformedURLException, IOException,
			ParseException {
		return httpWrite("POST", path, options, data, contentType);
	}

	public Result httpPut(String path, String options, byte[] data,
			String contentType) throws MalformedURLException, IOException,
			ParseException {
		return httpWrite("PUT", path, options, data, contentType);
	}
}
