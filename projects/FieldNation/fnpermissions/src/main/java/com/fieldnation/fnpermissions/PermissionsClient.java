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

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 6/7/17.
 */

public class PermissionsClient extends TopicClient {
    private static final String STAG = "PermissionsClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    private static final int INITIAL_REQUEST = 0;
    private static final int SECOND_REQUEST = 1;

    private static final String TOPIC_ID_REQUESTS = "TOPIC_ID_REQUESTS";
    private static final String TOPIC_ID_REQUEST_RESULT = "TOPIC_ID_REQUEST_RESULT";

    public PermissionsClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    public static void requestPermissions(Context context, String[] permissions) {
        Bundle payload = new Bundle();
        payload.putStringArray("permissions", permissions);

        TopicService.dispatchEvent(context, TOPIC_ID_REQUESTS, payload, Sticky.TEMP);
    }

    public static void onRequestPermissionsResult(Context context, int requestCode, String[] permissions, int[] grantResults) {
        Bundle payload = new Bundle();
        payload.putInt("requestCode", requestCode);
        payload.putStringArray("permissions", permissions);
        payload.putIntArray("grantResults", grantResults);

        TopicService.dispatchEvent(context, TOPIC_ID_REQUEST_RESULT, payload, Sticky.TEMP);
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
            getClient().register(TOPIC_ID_REQUEST_RESULT);
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(TAG, "onEvent");
            getClient().clearTopicAll(TOPIC_ID_REQUESTS);

            if (topicId.equals(TOPIC_ID_REQUESTS)) {
                Bundle bundle = (Bundle) payload;
                onRequest(bundle.getStringArray("permissions"));
            } else if (topicId.equals(TOPIC_ID_REQUEST_RESULT)) {
                Bundle bundle = (Bundle) payload;
                onResponse(bundle.getInt("requestCode"),
                        bundle.getStringArray("permissions"),
                        bundle.getIntArray("grantResults"));
            }
        }

        public abstract Activity getActivity();

        public abstract PermissionsClient getClient();

        private void onRequest(String[] permissions) {
            Log.v(TAG, "onRequest");
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                List<String> requiresRational = new LinkedList<>();
                List<String> requestAgain = new LinkedList<>();

                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                        requiresRational.add(permission);
                    } else {
                        requestAgain.add(permission);
                    }
                }

                // TODO show dialogs
                if (requiresRational.size() > 0) {
                    Log.v(TAG, "requiresRational");
                    for (String permission : requiresRational) {
                        PermissionsDialog.show(getActivity(), permission, permission);
                    }
                }

                if (requestAgain.size() > 0) {
                    Log.v(TAG, "requestAgain");
                    ActivityCompat.requestPermissions(getActivity(),
                            requestAgain.toArray(new String[requestAgain.size()]), INITIAL_REQUEST);
                }
            }
        }

        private void onResponse(int requestCode, String[] permissions, int[] grantResults) {
            if (requestCode == INITIAL_REQUEST) {

                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        // send results to client
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
                            // show dialogs
                        } else {
                            // send results
                        }
                    }
                }
            } else if (requestCode == SECOND_REQUEST) {
                // send results to client
            }
        }
    }

    public static abstract class ResponseListener extends TopicClient.Listener {
        private static final String TAG = "PermissionsClient.ResponseListener";

        @Override
        public void onConnected() {
            getClient().register(TOPIC_ID_REQUEST_RESULT);
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.equals(TOPIC_ID_REQUEST_RESULT)) {
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
