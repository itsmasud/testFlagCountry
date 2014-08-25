package com.fieldnation.rpc.client;

import java.text.ParseException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.common.DataServiceConstants;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.rpc.server.DataService;

/**
 * <p>
 * Provides a basic interface for making webservice calls to fieldnation's
 * servers. Other webservices like {@link WorkorderService} inherit from this
 * class.
 * </p>
 * 
 * @author michael.carver
 * 
 */
public class WebService implements WebServiceConstants {
	private Context _context;
	private ResultReceiver _callback;
	private String _authToken;
	private String _username;

	/**
	 * Creates a handle to communicat with the web server
	 * 
	 * @param context
	 * @param username
	 * @param authToken
	 * @param callback
	 *            Recommend using {@link WebServiceResultReceiver}, however any
	 *            {@link ResultReceiver} will do.
	 */
	public WebService(Context context, String username, String authToken, ResultReceiver callback) {
		_context = context.getApplicationContext();
		_username = username;
		_authToken = authToken;
		_callback = callback;
	}

	public String getAuthToken() {
		return _authToken;
	}

	/**
	 * Performs a generic httpRead. (No payload is sent)
	 * 
	 * @param resultCode
	 *            the result code to post back to the {@link ResultReceiver}
	 * @param method
	 *            the HTTP method to use
	 * @param path
	 *            the path on the server
	 * @param options
	 *            and URL options
	 * @param allowCache
	 *            set to true to allow using the cache, false otherwise
	 * @return
	 */
	public Intent httpRead(int resultCode, String method, String path, String options, boolean allowCache) {
		Intent intent = new Intent(_context, DataService.class);
		intent.setAction(DataServiceConstants.ACTION_RPC);
		intent.putExtra(DataServiceConstants.KEY_SERVICE, ACTION_NAME);
		intent.putExtra(KEY_PARAM_AUTH_TOKEN, _authToken);
		intent.putExtra(KEY_PARAM_USERNAME, _username);
		intent.putExtra(KEY_METHOD, METHOD_HTTP_READ);
		intent.putExtra(KEY_ALLOW_CACHE, allowCache);
		intent.putExtra(KEY_PARAM_METHOD, method);
		intent.putExtra(KEY_PARAM_PATH, path);
		intent.putExtra(KEY_PARAM_OPTIONS, options);
		intent.putExtra(KEY_RESULT_CODE, resultCode);

		if (_callback != null) {
			intent.putExtra(KEY_PARAM_CALLBACK, _callback);
		}

		return intent;
	}

	/**
	 * Performs a write action (sends payload data)
	 * 
	 * @param resultCode
	 *            the code to pass to the {@link ResultReceiver}
	 * @param method
	 *            the HTTP method to use
	 * @param path
	 *            the path on the server
	 * @param options
	 *            any url options
	 * @param data
	 *            the payload data to send
	 * @param contentType
	 *            the content type (MIME type) to use
	 * @param allowCache
	 *            set to true to allow using the cache, false otherwise
	 * @return
	 */
	public Intent httpWrite(int resultCode, String method, String path, String options, byte[] data,
			String contentType, boolean allowCache) {
		Intent intent = new Intent(_context, DataService.class);
		intent.setAction(DataServiceConstants.ACTION_RPC);
		intent.putExtra(DataServiceConstants.KEY_SERVICE, ACTION_NAME);
		intent.putExtra(KEY_PARAM_AUTH_TOKEN, _authToken);
		intent.putExtra(KEY_PARAM_USERNAME, _username);
		intent.putExtra(KEY_METHOD, METHOD_HTTP_WRITE);
		intent.putExtra(KEY_ALLOW_CACHE, allowCache);
		intent.putExtra(KEY_PARAM_METHOD, method);
		intent.putExtra(KEY_PARAM_PATH, path);
		intent.putExtra(KEY_PARAM_OPTIONS, options);
		intent.putExtra(KEY_PARAM_CONTENT_TYPE, contentType);
		intent.putExtra(KEY_PARAM_DATA, data);
		intent.putExtra(KEY_RESULT_CODE, resultCode);

		if (_callback != null) {
			intent.putExtra(KEY_PARAM_CALLBACK, _callback);
		}

		return intent;
	}

	public Intent httpPostFile(int resultCode, String path, String options, String fieldName, String filename,
			byte[] data, Map<String, String> fields) {
		Intent intent = new Intent(_context, DataService.class);
		intent.setAction(DataServiceConstants.ACTION_RPC);
		intent.putExtra(DataServiceConstants.KEY_SERVICE, ACTION_NAME);
		intent.putExtra(KEY_PARAM_AUTH_TOKEN, _authToken);
		intent.putExtra(KEY_PARAM_USERNAME, _username);
		intent.putExtra(KEY_METHOD, METHOD_HTTP_WRITE);
		intent.putExtra(KEY_PARAM_PATH, path);
		intent.putExtra(KEY_PARAM_OPTIONS, options);
		intent.putExtra(KEY_PARAM_DATA, data);
		intent.putExtra(KEY_RESULT_CODE, resultCode);
		intent.putExtra(KEY_FILE_FIELD_NAME, fieldName);
		intent.putExtra(KEY_FILE_NAME, filename);

		if (fields != null && fields.size() > 0) {
			JsonObject obj = new JsonObject();

			Iterator<String> iter = fields.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				try {
					obj.put(key, fields.get(key));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			intent.putExtra(KEY_FIELD_MAP, obj.toString());
		}

		if (_callback != null) {
			intent.putExtra(KEY_PARAM_CALLBACK, _callback);
		}

		return intent;
	}

	public Intent httpGet(int resultCode, String path, boolean allowCache) {
		return httpGet(resultCode, path, null, allowCache);
	}

	public Intent httpGet(int resultCode, String path, String options, boolean allowCache) {
		return httpRead(resultCode, "GET", path, options, allowCache);
	}

	public Intent httpDelete(int resultCode, String path, String options, boolean allowCache) {
		return httpRead(resultCode, "DELETE", path, options, allowCache);
	}

	public Intent httpPost(int resultCode, String path, String options, String data, String contentType,
			boolean allowCache) {
		return httpPost(resultCode, path, options, data.getBytes(), contentType, allowCache);
	}

	public Intent httpPost(int resultCode, String path, String options, byte[] data, String contentType,
			boolean allowCache) {
		return httpWrite(resultCode, "POST", path, options, data, contentType, allowCache);
	}

	public Intent httpPut(int resultCode, String path, String options, byte[] data, String contentType,
			boolean allowCache) {
		return httpWrite(resultCode, "PUT", path, options, data, contentType, allowCache);
	}
}