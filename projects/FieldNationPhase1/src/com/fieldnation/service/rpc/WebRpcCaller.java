package com.fieldnation.service.rpc;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.fieldnation.service.DataService;
import com.fieldnation.webapi.OAuth;

public class WebRpcCaller {
	private Context context;
	private OAuth _at;
	private String atString;
	private ResultReceiver callback;

	public WebRpcCaller(Context conetxt, OAuth at, ResultReceiver callback) {
		this.context = conetxt;
		_at = at;
		atString = _at.toString();
		this.callback = callback;
	}

	public Intent httpRead(int resultCode, String method, String path,
			String options) {

		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "web_request");
		intent.putExtra("PARAM_ACCESS_TOKEN", atString);
		intent.putExtra("METHOD", "httpread");
		intent.putExtra("PARAM_METHOD", method);
		intent.putExtra("PARAM_PATH", path);
		intent.putExtra("PARAM_OPTIONS", options);
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		return intent;
	}

	public Intent httpWrite(int resultCode, String method, String path,
			String options, byte[] data, String contentType) {

		Intent intent = new Intent(context, DataService.class);
		intent.setAction("RPC");
		intent.putExtra("SERVICE", "web_request");
		intent.putExtra("PARAM_ACCESS_TOKEN", atString);
		intent.putExtra("METHOD", "httpwrite");
		intent.putExtra("PARAM_METHOD", method);
		intent.putExtra("PARAM_PATH", path);
		intent.putExtra("PARAM_OPTIONS", options);
		intent.putExtra("PARAM_CONTENT_TYPE", contentType);
		intent.putExtra("PARAM_DATA", data);
		intent.putExtra("RESULT_CODE", resultCode);

		if (callback != null) {
			intent.putExtra("PARAM_CALLBACK", callback);
		}

		return intent;
	}

	public Intent httpGet(int resultCode, String path) {
		return httpGet(resultCode, path, null);
	}

	public Intent httpGet(int resultCode, String path, String options) {
		return httpRead(resultCode, "GET", path, options);
	}

	public Intent httpDelete(int resultCode, String path, String options) {
		return httpRead(resultCode, "DELETE", path, options);
	}

	public Intent httpPost(int resultCode, String path, String options,
			String data, String contentType) {
		return httpPost(resultCode, path, options, data.getBytes(), contentType);
	}

	public Intent httpPost(int resultCode, String path, String options,
			byte[] data, String contentType) {
		return httpWrite(resultCode, "POST", path, options, data, contentType);
	}

	public Intent httpPut(int resultCode, String path, String options,
			byte[] data, String contentType) {
		return httpWrite(resultCode, "PUT", path, options, data, contentType);
	}
}