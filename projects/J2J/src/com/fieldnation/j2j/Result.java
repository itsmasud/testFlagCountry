package com.fieldnation.j2j;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

/**
 * Represents an HTTP response with some extra parsing.
 * 
 * @author michael.carver
 * 
 */
public class Result {

	private byte[] _baResults = null;
	private String _sResults = null;
	private JsonObject _jsonResults = null;
	private JsonArray _jaResults = null;

	private int _responseCode;
	private String _responseMessage;

	public Result(HttpURLConnection conn) throws IOException {
		_responseCode = conn.getResponseCode();
		_responseMessage = conn.getResponseMessage();
		cacheResults(conn);
	}

	public int getResponseCode() {
		return _responseCode;
	}

	public String getResponseMessage() {
		return _responseMessage;
	}

	private void cacheResults(HttpURLConnection conn) throws IOException {
		try {
			if (conn.getDoInput()) {
				InputStream in = conn.getInputStream();
				_baResults = misc.readAllFromStream(in, 1024, -1, 3000);
				in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			_baResults = null;
			try {
				_baResults = misc.readAllFromStream(conn.getErrorStream(),
						1024, -1, 1000);
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}
		}
	}

	public byte[] getResultsAsByteArray() {
		return _baResults;
	}

	public String getResultsAsString() {
		if (_sResults == null) {
			_sResults = new String(getResultsAsByteArray());
		}
		return _sResults;
	}

	public JsonObject getResultsAsJsonObject() throws ParseException {
		if (_jsonResults == null) {
			_jsonResults = new JsonObject(getResultsAsString());
		}
		return _jsonResults;
	}

	public JsonArray getResultsAsJsonArray() throws ParseException {
		if (_jaResults == null) {
			_jaResults = new JsonArray(getResultsAsString());
		}
		return _jaResults;
	}

}
