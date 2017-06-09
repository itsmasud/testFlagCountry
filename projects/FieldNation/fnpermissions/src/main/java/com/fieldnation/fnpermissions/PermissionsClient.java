package com.fieldnation.fnpermissions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Sticky;
import com.fieldnation.fnpigeon.TopicClient;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fntools.ContextProvider;
import com.fieldnation.fntools.UniqueTag;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 6/7/17.
 */

public class PermissionsClient extends TopicClient {
    private static final String STAG = "PermissionsClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    protected static final int INITIAL_REQUEST = 0;
    protected static final int SECOND_REQUEST = 1;

    private static final String TOPIC_ID_REQUESTS = "TOPIC_ID_REQUESTS";
    private static final String TOPIC_ID_REQUEST_RESULT = "TOPIC_ID_REQUEST_RESULT";
    private static final String TOPIC_ID_COMPLETE = "TOPIC_ID_COMPLETE";

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
        payload.putInt("requestCode", INITIAL_REQUEST);

        TopicService.dispatchEvent(context, TOPIC_ID_REQUESTS, payload, Sticky.TEMP);
    }

    static void requestPermissions(Context context, String[] permissions, int requestCode) {
        Bundle payload = new Bundle();
        payload.putStringArray("permissions", permissions);
        payload.putInt("requestCode", requestCode);

        TopicService.dispatchEvent(context, TOPIC_ID_REQUESTS, payload, Sticky.TEMP);
    }

    public static void onRequestPermissionsResult(Context context, int requestCode, String[] permissions, int[] grantResults) {
        Bundle payload = new Bundle();
        payload.putInt("requestCode", requestCode);
        payload.putStringArray("permissions", permissions);
        payload.putIntArray("grantResults", grantResults);

        TopicService.dispatchEvent(context, TOPIC_ID_REQUEST_RESULT, payload, Sticky.TEMP);
    }

    protected static void onComplete(Context context, String permission, int grantResult) {
        Bundle payload = new Bundle();
        payload.putString("permission", permission);
        payload.putInt("grantResult", grantResult);

        TopicService.dispatchEvent(context, TOPIC_ID_COMPLETE, payload, Sticky.FOREVER);
    }

    static void setPermissionDenied(String permission) {
        SharedPreferences sp = ContextProvider.get().getSharedPreferences("PermissionsClient", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(permission, System.currentTimeMillis());
        edit.apply();
    }

    static void clearPermissionDenied(String permission) {
        SharedPreferences sp = ContextProvider.get().getSharedPreferences("PermissionsClient", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(permission);
        edit.apply();
    }

    private static boolean isPermissionDenied(String permission) {
        SharedPreferences sp = ContextProvider.get().getSharedPreferences("PermissionsClient", 0);
        if (sp.contains(permission)) {
            if (sp.getLong(permission, 0) + 86400000 < System.currentTimeMillis()) { // 1 day
                return true;
            } else {
                clearPermissionDenied(permission);
            }
        }
        return false;
    }

    public static int checkSelfPermission(Context context, String permission) {
        int grant = ContextCompat.checkSelfPermission(context, permission);

        if (grant == PackageManager.PERMISSION_GRANTED)
            clearPermissionDenied(permission);

        return grant;
    }

    public static void checkSelfPermissionAndRequest(Context context, String[] permissions){

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
            if (topicId.equals(TOPIC_ID_REQUESTS)) {
                getClient().clearTopicAll(TOPIC_ID_REQUESTS);
                Bundle bundle = (Bundle) payload;
                onRequest(bundle.getStringArray("permissions"), bundle.getInt("requestCode"));
            } else if (topicId.equals(TOPIC_ID_REQUEST_RESULT)) {
                getClient().clearTopicAll(TOPIC_ID_REQUEST_RESULT);
                Bundle bundle = (Bundle) payload;
                onResponse(bundle.getInt("requestCode"),
                        bundle.getStringArray("permissions"),
                        bundle.getIntArray("grantResults"));
            }
        }

        public abstract Activity getActivity();

        public abstract PermissionsClient getClient();

        private void onRequest(String[] permissions, int requestCode) {
            Log.v(TAG, "onRequest");
            List<String> requestable = new LinkedList<>();

            for (int i = 0; i < permissions.length; i++) {
                if (isPermissionDenied(permissions[i])) {
                    PermissionsClient.onComplete(getActivity(), permissions[i], PackageManager.PERMISSION_DENIED);
                } else {
                    requestable.add(permissions[i]);
                }
            }

            ActivityCompat.requestPermissions(getActivity(), requestable.toArray(new String[requestable.size()]), requestCode);
        }

        private void onResponse(int requestCode, String[] permissions, int[] grantResults) {
            Log.v(TAG, "onResponse " + requestCode);
            if (requestCode == INITIAL_REQUEST) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        clearPermissionDenied(permissions[i]);
                        PermissionsClient.onComplete(getActivity(), permissions[i], PackageManager.PERMISSION_GRANTED);

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
                            PermissionsDialog.show(getActivity(), permissions[i], permissions[i]);
                        } else {
                            PermissionsClient.onComplete(getActivity(), permissions[i], PackageManager.PERMISSION_DENIED);
                            setPermissionDenied(permissions[i]);
                        }
                    }
                }
            } else if (requestCode == SECOND_REQUEST) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                        setPermissionDenied(permissions[i]);
                    else
                        clearPermissionDenied(permissions[i]);

                    PermissionsClient.onComplete(getActivity(), permissions[i], grantResults[i]);
                }
            }
        }
    }

    public static abstract class ResponseListener extends TopicClient.Listener {
        private static final String TAG = "PermissionsClient.ResponseListener";

        @Override
        public void onConnected() {
            getClient().register(TOPIC_ID_COMPLETE);
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            if (topicId.equals(TOPIC_ID_COMPLETE)) {
                Bundle bundle = (Bundle) payload;
                onComplete(bundle.getString("permission"),
                        bundle.getInt("grantResult"));
            }
        }

        public abstract PermissionsClient getClient();

        public abstract void onComplete(String permission, int grantResult);
    }
}
