package com.fieldnation.webapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

public class Result {
	private HttpURLConnection _conn;

	/*
	 * note, we use cacheing here to prevent multiple interpretations of the
	 * same data
	 */
	private byte[] _baResults = null;
	private String _sResults = null;
	private JsonObject _jsonResults = null;
	private JsonArray _jaResults = null;

	public Result(HttpURLConnection conn) throws IOException {
		_conn = conn;

		getResultsAsByteArray();
	}

	public byte[] getResultsAsByteArray() throws IOException {
		if (_baResults == null) {
			InputStream in = _conn.getInputStream();
			int contentlength = _conn.getContentLength();

			_baResults = misc.readAllFromStream(in, 1024, contentlength, 3000);

			in.close();
		}
		return _baResults;
	}

	public String getResultsAsString() throws IOException {
		if (_sResults == null) {
			_sResults = new String(getResultsAsByteArray());
		}
		return _sResults;
	}

	public JsonObject getResultsAsJsonObject() throws ParseException,
			IOException {
		if (_jsonResults == null) {
			_jsonResults = new JsonObject(getResultsAsString());
		}
		return _jsonResults;
	}

	public JsonArray getResultsAsJsonArray() throws ParseException, IOException {
		if (_jaResults == null) {
			_jaResults = new JsonArray(getResultsAsString());
		}
		return _jaResults;
	}

	public HttpURLConnection getUrlConnection() {
		return _conn;
	}
}
