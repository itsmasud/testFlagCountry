package com.fieldnation.rpc.server;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class PhotoRpc extends RpcInterface implements PhotoServiceConstants {
	private static final String TAG = "rpc.server.PhotoRpc";

	PhotoRpc(HashMap<String, RpcInterface> map) {
		super(map, ACTION_NAME);
	}

	@Override
	public void execute(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		try {
			String url = bundle.getString(KEY_PARAM_URL);

			PhotoCacheNode node = PhotoCacheNode.get(context, url);

			if (node == null) {
				HttpURLConnection conn = null;
				conn = (HttpURLConnection) new URL(url).openConnection();
				conn.setRequestMethod("GET");
				conn.setDoInput(true);

				bundle.putInt(KEY_RESPONSE_CODE, conn.getResponseCode());
				bundle.putString(KEY_RESPONSE_MESSAGE, conn.getResponseMessage());

				InputStream in = conn.getInputStream();
				try {
					int contentLength = conn.getContentLength();
					byte[] results = misc.readAllFromStream(in, 1024, contentLength, 3000);
					bundle.putByteArray(KEY_RESPONSE_DATA, results);
				} finally {
					if (in != null)
						in.close();
				}

				if (bundle.containsKey(KEY_RESULT_RECEIVER)) {
					ResultReceiver rr = bundle.getParcelable(KEY_RESULT_RECEIVER);
					int resultCode = bundle.getInt(KEY_RESULT_CODE);

					rr.send(resultCode, bundle);
				}
			} else if (node != null) {
				ResultReceiver rr = intent.getParcelableExtra(KEY_RESULT_RECEIVER);
				int resultCode = bundle.getInt(KEY_RESULT_CODE);
				byte[] data = node.getPhotoData();
				bundle.putByteArray(KEY_RESPONSE_DATA, data);

				rr.send(resultCode, bundle);
			}
		} catch (Exception ex) {
			// TODO need to send back up
			ex.printStackTrace();
		}
	}
}
