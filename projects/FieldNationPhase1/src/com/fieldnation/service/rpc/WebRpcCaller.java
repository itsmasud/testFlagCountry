package com.fieldnation.service.rpc;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.service.DataService;
import com.fieldnation.webapi.OAuth;

public class WebRpcCaller {
	private Context context;
	private ResultReceiver callback;
	private String _accessToken;
	private String _username;

	public WebRpcCaller(Context conetxt, String username, String accessToken, ResultReceiver callback) {
		this.context = conetxt;
		_accessToken = accessToken;
		_username = username;
		this.callback = callback;
	}

	public Intent httpRead(int resultCode, String method, String path,
			String options, boolean allowCache) {

		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", WebRpc.ACTION_NAME);
		intent.putExtra(WebRpc.KEY_PARAM_ACCESS_TOKEN, _accessToken);
		intent.putExtra(WebRpc.KEY_PARAM_USERNAME, _username);
		intent.putExtra(WebRpc.KEY_METHOD, WebRpc.METHOD_HTTP_READ);
		intent.putExtra(WebRpc.KEY_ALLOW_CACHE, allowCache);
		intent.putExtra(WebRpc.KEY_PARAM_METHOD, method);
		intent.putExtra(WebRpc.KEY_PARAM_PATH, path);
		intent.putExtra(WebRpc.KEY_PARAM_OPTIONS, options);
		intent.putExtra(WebRpc.KEY_RESULT_CODE, resultCode);

		if (callback != null) {
			intent.putExtra(WebRpc.KEY_PARAM_CALLBACK, callback);
		}

		return intent;
	}

	public Intent httpWrite(int resultCode, String method, String path,
			String options, byte[] data, String contentType, boolean allowCache) {

		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", WebRpc.ACTION_NAME);
		intent.putExtra(WebRpc.KEY_PARAM_ACCESS_TOKEN, _accessToken);
		intent.putExtra(WebRpc.KEY_PARAM_USERNAME, _username);
		intent.putExtra(WebRpc.KEY_METHOD, WebRpc.METHOD_HTTP_WRITE);
		intent.putExtra(WebRpc.KEY_ALLOW_CACHE, allowCache);
		intent.putExtra(WebRpc.KEY_PARAM_METHOD, method);
		intent.putExtra(WebRpc.KEY_PARAM_PATH, path);
		intent.putExtra(WebRpc.KEY_PARAM_OPTIONS, options);
		intent.putExtra(WebRpc.KEY_PARAM_CONTENT_TYPE, contentType);
		intent.putExtra(WebRpc.KEY_PARAM_DATA, data);
		intent.putExtra(WebRpc.KEY_RESULT_CODE, resultCode);

		if (callback != null) {
			intent.putExtra(WebRpc.KEY_PARAM_CALLBACK, callback);
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