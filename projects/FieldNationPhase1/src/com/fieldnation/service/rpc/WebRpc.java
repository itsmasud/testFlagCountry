package com.fieldnation.service.rpc;

import java.text.ParseException;
import java.util.HashMap;

import com.fieldnation.service.DataCache;
import com.fieldnation.webapi.OAuth;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.Ws;

import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class WebRpc extends RpcInterface {

	public WebRpc(HashMap<String, RpcInterface> map) {
		super(map, "web_request");
	}

	@Override
	public void execute(Context context, Intent intent) {
		try {
			String method = intent.getStringExtra("METHOD");
			OAuth at = OAuth.fromString(intent.getStringExtra("PARAM_ACCESS_TOKEN"));

			if ("httpread".equals(method)) {
				doHttpRead(context, intent, at);
			} else if ("httpwrite".equals(method)) {
				doHttpWrite(context, intent, at);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doHttpRead(Context context, Intent intent, OAuth at) {
		try {
			Bundle bundle = intent.getExtras();
			String method = bundle.getString("PARAM_METHOD");
			String path = bundle.getString("PARAM_PATH");
			String options = bundle.getString("PARAM_OPTIONS");
			String contentType = bundle.getString("PARAM_CONTENT_TYPE");
			boolean allowCache = bundle.getBoolean("ALLOW_CACHE");

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");

				Bundle cachedData = null;

				if (allowCache)
					cachedData = DataCache.query(at, bundle);

				if (cachedData != null) {
					bundle.putByteArray("RESPONSE_DATA",
							cachedData.getByteArray("RESPONSE_DATA"));
					bundle.putInt("RESPONSE_CODE",
							cachedData.getInt("RESPONSE_CODE"));
					bundle.putBoolean("RESPONSE_CACHED", true);
				} else {
					Ws ws = new Ws(at);
					Result result = ws.httpRead(method, path, options,
							contentType);

					bundle.putByteArray("RESPONSE_DATA",
							result.getResultsAsByteArray());
					bundle.putInt("RESPONSE_CODE",
							result.getUrlConnection().getResponseCode());
					bundle.putBoolean("RESPONSE_CACHED", false);

					DataCache.store(at, bundle, bundle);
				}

				rr.send(bundle.getInt("RESULT_CODE"), bundle);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doHttpWrite(Context context, Intent intent, OAuth at) {
		try {
			Bundle bundle = intent.getExtras();
			String method = bundle.getString("PARAM_METHOD");
			String path = bundle.getString("PARAM_PATH");
			String options = bundle.getString("PARAM_OPTIONS");
			String contentType = bundle.getString("PARAM_CONTENT_TYPE");
			byte[] data = bundle.getByteArray("PARAM_DATA");
			boolean allowCache = bundle.getBoolean("ALLOW_CACHE");

			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");

				Bundle cachedData = null;

				if (allowCache)
					cachedData = DataCache.query(at, bundle);

				if (cachedData != null) {
					bundle.putByteArray("RESPONSE_DATA",
							cachedData.getByteArray("RESPONSE_DATA"));
					bundle.putInt("RESPONSE_CODE",
							cachedData.getInt("RESPONSE_CODE"));
					bundle.putBoolean("RESPONSE_CACHED", true);
				} else {
					Ws ws = new Ws(at);
					Result result = ws.httpWrite(method, path, options, data,
							contentType);

					bundle.putByteArray("RESPONSE_DATA",
							result.getResultsAsByteArray());
					bundle.putInt("RESPONSE_CODE",
							result.getUrlConnection().getResponseCode());
					bundle.putBoolean("RESPONSE_CACHED", false);

					DataCache.store(at, bundle, bundle);
				}

				rr.send(bundle.getInt("RESULT_CODE"), bundle);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
