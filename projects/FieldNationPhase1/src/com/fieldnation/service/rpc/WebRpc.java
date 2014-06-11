package com.fieldnation.service.rpc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.HashMap;

import com.fieldnation.webapi.AccessToken;
import com.fieldnation.webapi.Result;
import com.fieldnation.webapi.Ws;

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
			AccessToken at = new AccessToken(intent.getStringExtra("PARAM_ACCESS_TOKEN"));

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

	private void doHttpRead(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			String method = bundle.getString("PARAM_METHOD");
			String path = bundle.getString("PARAM_PATH");
			String options = bundle.getString("PARAM_OPTIONS");
			String contentType = bundle.getString("PARAM_CONTENT_TYPE");
			
			// TODO ANDR-16 implement a cache here

			Ws ws = new Ws(at);

			Result result = ws.httpRead(method, path, options, contentType);
			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");

				bundle.putByteArray("RESPONSE_DATA",
						result.getResultsAsByteArray());
				bundle.putInt("RESPONSE_CODE",
						result.getUrlConnection().getResponseCode());

				rr.send(bundle.getInt("RESULT_CODE"), bundle);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doHttpWrite(Context context, Intent intent, AccessToken at) {
		try {
			Bundle bundle = intent.getExtras();
			String method = bundle.getString("PARAM_METHOD");
			String path = bundle.getString("PARAM_PATH");
			String options = bundle.getString("PARAM_OPTIONS");
			String contentType = bundle.getString("PARAM_CONTENT_TYPE");
			byte[] data = bundle.getByteArray("PARAM_DATA");

			// TODO ANDR-16 implement a cache here
			
			Ws ws = new Ws(at);

			Result result = ws.httpWrite(method, path, options, data,
					contentType);
			if (bundle.containsKey("PARAM_CALLBACK")) {
				ResultReceiver rr = bundle.getParcelable("PARAM_CALLBACK");

				bundle.putByteArray("RESPONSE_DATA",
						result.getResultsAsByteArray());
				bundle.putInt("RESPONSE_CODE",
						result.getUrlConnection().getResponseCode());

				rr.send(bundle.getInt("RESULT_CODE"), bundle);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
