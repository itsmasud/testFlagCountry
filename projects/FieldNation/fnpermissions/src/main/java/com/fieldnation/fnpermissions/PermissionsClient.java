package com.fieldnation.fnpermissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fntools.UniqueTag;

/**
 * Created by mc on 6/7/17.
 */

public class PermissionsClient extends TopicClient {
    private static final String STAG = "PermissionsClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static final String TOPIC_ID_RESULTS = "TOPIC_ID_RESULTS";
    private static final String TOPIC_ID_REQUESTS = "TOPIC_ID_REQUESTS";

    public PermissionsClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static void sendResults(Context context, int requestCode, String permissions, int[] grantResults) {
        Bundle payload = new Bundle();
        payload.putInt("requestCode", requestCode);
        payload.putString("permissions", permissions);
        payload.putIntArray("grantResults", grantResults);

        TopicService.dispatchEvent(context, TOPIC_ID_RESULTS, payload, Sticky.TEMP);
    }

    public static void requestPermissions(Context context, String permissions, int requestCode) {
        Bundle payload = new Bundle();
        payload.putString("permissions", permissions);
        payload.putInt("requestCode", requestCode);

        TopicService.dispatchEvent(context, TOPIC_ID_REQUESTS, payload, Sticky.TEMP);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class RequestListener extends TopicClient.Listener {
        private static final String TAG = "PermissionsClient.RequestListener";

        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            getClient().register(TOPIC_ID_REQUESTS);
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(TAG, "onEvent");
            getClient().clearTopicAll(TOPIC_ID_REQUESTS);

            if (topicId.equals(TOPIC_ID_REQUESTS)) {
                Bundle bundle = (Bundle) payload;
                onRequest(bundle.getString("permissions"), bundle.getInt("requestCode"));
            }
        }

        public abstract Activity getActivity();

        public abstract PermissionsClient getClient();

        public void onRequest(String permissions, int requestCode) {
            Log.v(TAG, "onRequest");
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        permissions)) {

                    // TODO, show a dialog
                    Log.v(TAG, "todo show dialog");

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{permissions}, requestCode);
                }
            }
        }
    }

    public static abstract class ResponseListener extends TopicClient.Listener {
        private static final String TAG = "PermissionsClient.ResponseListener";

        @Override
        public void onConnected() {
            getClient().register(TOPIC_ID_RESULTS);
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.equals(TOPIC_ID_RESULTS)) {
                Bundle bundle = (Bundle) payload;
                onResponse(bundle.getInt("requestCode"),
                        bundle.getStringArray("permissions"),
                        bundle.getIntArray("grantResults"));
            }
        }

        public abstract Activity getActivity();

        public abstract PermissionsClient getClient();

        private void onResponse(int requestCode, String[] permissions, int[] grantResults) {

        }
    }
}
