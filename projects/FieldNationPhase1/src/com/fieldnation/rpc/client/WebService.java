package com.fieldnation.rpc.client;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.rpc.common.DataServiceConstants;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.server.DataService;

public class WebService implements WebServiceConstants {
	private Context _context;
	private ResultReceiver _callback;
	private String _authToken;
	private String _username;

	public WebService(Context context, String username, String authToken, ResultReceiver callback) {
		_context = context;
		_authToken = authToken;
		_username = username;
		_callback = callback;
	}

	public Intent httpRead(int resultCode, String method, String path,
			String options, boolean allowCache) {

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

	public Intent httpWrite(int resultCode, String method, String path,
			String options, byte[] data, String contentType, boolean allowCache) {

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

	public Intent httpGet(int resultCode, String path, boolean allowCache) {
		return httpGet(resultCode, path, null, allowCache);
	}

	public Intent httpGet(int resultCode, String path, String options,
			boolean allowCache) {
		return httpRead(resultCode, "GET", path, options, allowCache);
	}

	public Intent httpDelete(int resultCode, String path, String options,
			boolean allowCache) {
		return httpRead(resultCode, "DELETE", path, options, allowCache);
	}

	public Intent httpPost(int resultCode, String path, String options,
			String data, String contentType, boolean allowCache) {
		return httpPost(resultCode, path, options, data.getBytes(),
				contentType, allowCache);
	}

	public Intent httpPost(int resultCode, String path, String options,
			byte[] data, String contentType, boolean allowCache) {
		return httpWrite(resultCode, "POST", path, options, data, contentType,
				allowCache);
	}

	public Intent httpPut(int resultCode, String path, String options,
			byte[] data, String contentType, boolean allowCache) {
		return httpWrite(resultCode, "PUT", path, options, data, contentType,
				allowCache);
	}
}