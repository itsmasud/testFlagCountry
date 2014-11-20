package com.fieldnation.rpc.server;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.utils.misc;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

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
            Log.v(TAG, "Getting image: " + url);

            PhotoCacheNode node = PhotoCache.query(context, url);

            boolean doGetCircle = bundle.getBoolean(KEY_GET_CIRCLE);

            if (node == null) {
                HttpURLConnection conn = null;
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                bundle.putInt(KEY_RESPONSE_CODE, conn.getResponseCode());
                bundle.putString(KEY_RESPONSE_MESSAGE, conn.getResponseMessage());
                bundle.putString(KEY_RESPONSE_ERROR, ERROR_NONE);

                InputStream in = conn.getInputStream();
                try {
                    byte[] results = misc.readAllFromStream(in, 1024, -1, 3000);

                    // generate the circle
                    Bitmap photo = BitmapFactory.decodeByteArray(results, 0, results.length);
                    Bitmap circle = misc.extractCircle(photo);

                    if (doGetCircle)
                        bundle.putParcelable(KEY_RESPONSE_DATA, circle);
                    else
                        bundle.putParcelable(KEY_RESPONSE_DATA, photo);

                    PhotoCache.store(context, url, photo, circle);
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

                if (doGetCircle)
                    bundle.putParcelable(KEY_RESPONSE_DATA, node.getCircleData());
                else
                    bundle.putParcelable(KEY_RESPONSE_DATA, node.getPhotoData());

                // todo doGetCircle flag
                bundle.putString(KEY_RESPONSE_ERROR, ERROR_NONE);

                rr.send(resultCode, bundle);
            }
        } catch (Exception ex) {
            doError(intent);
            ex.printStackTrace();
        }
    }

    private void doError(Intent intent) {
        Bundle bundle = intent.getExtras();
        ResultReceiver rr = intent.getParcelableExtra(KEY_RESULT_RECEIVER);
        bundle.putString(KEY_RESPONSE_ERROR, ERROR_UNKNOWN);
        rr.send(bundle.getInt(KEY_RESULT_CODE), bundle);
    }
}
