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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by mc on 6/7/17.
 */

public class PermissionsClient extends TopicClient {
    private static final String STAG = "PermissionsClient";
    private final String TAG = UniqueTag.makeTag(STAG);

    protected static final int INITIAL_REQUEST = 0;
    protected static final int SECOND_REQUEST = 1;
    protected static final int PARTIAL_REQUEST = 2;

    private static final String TOPIC_ID_REQUESTS = STAG + ":TOPIC_ID_REQUESTS";
    private static final String TOPIC_ID_REQUEST_RESULT = STAG + ":TOPIC_ID_REQUEST_RESULT";
    private static final String TOPIC_ID_COMPLETE = STAG + ":TOPIC_ID_COMPLETE";
    private static final String TOPIC_ID_PROCESS_QUEUE = STAG + ":TOPIC_ID_PROCESS_QUEUE";

    public PermissionsClient(Listener listener) {
        super(listener);
    }

    @Override
    public String getUserTag() {
        return TAG;
    }

    static void processQueue(Context context) {
        TopicService.dispatchEvent(context, TOPIC_ID_PROCESS_QUEUE, null, Sticky.TEMP);
    }

    public static void requestPermissions(Context context, String[] permissions, boolean[] required) {
        Bundle payload = new Bundle();
        payload.putStringArray("permissions", permissions);
        payload.putBooleanArray("required", required);
        payload.putInt("requestCode", INITIAL_REQUEST);

        TopicService.dispatchEvent(context, TOPIC_ID_REQUESTS, payload, Sticky.TEMP);
    }

    static void requestPermissions(Context context, String[] permissions, boolean[] required, int requestCode) {
        Bundle payload = new Bundle();
        payload.putStringArray("permissions", permissions);
        payload.putBooleanArray("required", required);
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

    public static void checkSelfPermissionAndRequest(Context context, String[] permissions, boolean[] required) {
        List<String> requestable = new LinkedList<>();
        List<Boolean> requireds = new LinkedList<>();
        for (int i = 0; i < required.length; i++) {
            if (checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                PermissionsClient.onComplete(context, permissions[i], PackageManager.PERMISSION_GRANTED);
            } else {
                requestable.add(permissions[i]);
                requireds.add(required[i]);
            }
        }

        if (requestable.size() > 0) {
            boolean[] reqs = new boolean[requireds.size()];

            for (int i = 0; i < reqs.length; i++) {
                reqs[i] = requireds.get(i);
            }

            requestPermissions(context, requestable.toArray(new String[requestable.size()]), reqs);
        }
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    public static abstract class RequestListener extends TopicClient.Listener {
        private static final String TAG = "PermissionsClient.RequestListener";

        private final Set<String> requiredPermissions = new HashSet<>();

        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            getClient().register(TOPIC_ID_REQUESTS);
            getClient().register(TOPIC_ID_REQUEST_RESULT);
            getClient().register(TOPIC_ID_PROCESS_QUEUE);
        }

        @Override
        public void onEvent(String topicId, Parcelable payload) {
            Log.v(TAG, "onEvent");
            if (topicId.equals(TOPIC_ID_REQUESTS)) {
                getClient().clearTopicAll(TOPIC_ID_REQUESTS);
                Bundle bundle = (Bundle) payload;
                onRequest(bundle.getStringArray("permissions"),
                        bundle.getBooleanArray("required"),
                        bundle.getInt("requestCode"));

            } else if (topicId.equals(TOPIC_ID_REQUEST_RESULT)) {
                getClient().clearTopicAll(TOPIC_ID_REQUEST_RESULT);
                Bundle bundle = (Bundle) payload;
                onResponse(bundle.getInt("requestCode"),
                        bundle.getStringArray("permissions"),
                        bundle.getIntArray("grantResults"));
            } else if (topicId.equals(TOPIC_ID_PROCESS_QUEUE)) {
                getClient().clearTopicAll(TOPIC_ID_PROCESS_QUEUE);
                processQueue();
            }
        }

        public abstract Activity getActivity();

        public abstract PermissionsClient getClient();

        private void onRequest(String[] permissions, boolean[] required, int requestCode) {
            Log.v(TAG, "onRequest");

            List<String> requestable = new LinkedList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (isPermissionDenied(permissions[i])) {
                    PermissionsClient.onComplete(getActivity(), permissions[i], PackageManager.PERMISSION_DENIED);
                } else {
                    requestable.add(permissions[i]);
                }

                if (required[i]) {
                    requiredPermissions.add(permissions[i]);
                }
            }

            ActivityCompat.requestPermissions(getActivity(), requestable.toArray(new String[requestable.size()]), requestCode);
        }

        private static class PermissionsTuple {
            public String permission;
            public boolean required;
            public boolean shouldShowRationale;
            public boolean secondTry = false;

            public PermissionsTuple(String permission, boolean required, boolean shouldShowRationale, boolean secondTry) {
                this.permission = permission;
                this.required = required;
                this.shouldShowRationale = shouldShowRationale;
                this.secondTry = secondTry;
            }
        }

        private Hashtable<String, PermissionsTuple> QUEUED_PERMS = new Hashtable<>();

        private void onResponse(int requestCode, String[] permissions, int[] grantResults) {
            Log.v(TAG, "onResponse " + requestCode);
            if (requestCode == INITIAL_REQUEST) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        clearPermissionDenied(permissions[i]);
                        PermissionsClient.onComplete(getActivity(), permissions[i], PackageManager.PERMISSION_GRANTED);

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
                            QUEUED_PERMS.put(permissions[i], new PermissionsTuple(permissions[i], requiredPermissions.contains(permissions[i]), true, false));
                        } else {
                            if (requiredPermissions.contains(permissions[i])) {
                                QUEUED_PERMS.put(permissions[i], new PermissionsTuple(permissions[i], true, false, false));
                            } else {
                                PermissionsClient.onComplete(getActivity(), permissions[i], PackageManager.PERMISSION_DENIED);
                                setPermissionDenied(permissions[i]);
                            }
                        }
                    }
                }
            } else if (requestCode == SECOND_REQUEST) {
                for (int i = 0; i < permissions.length; i++) {
                    // Denied and required. show dialog
                    if (requiredPermissions.contains(permissions[i]) && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        QUEUED_PERMS.put(permissions[i], new PermissionsTuple(permissions[i], true, true, true));
                    } else {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                            setPermissionDenied(permissions[i]);
                        else
                            clearPermissionDenied(permissions[i]);

                        PermissionsClient.onComplete(getActivity(), permissions[i], grantResults[i]);
                    }
                }
            }
            processQueue();
        }

        private void processQueue() {
            while (QUEUED_PERMS.size() > 0) {
                PermissionsTuple tuple = QUEUED_PERMS.remove(QUEUED_PERMS.keys().nextElement());

                int grant = checkSelfPermission(getActivity(), tuple.permission);
                if (grant == PackageManager.PERMISSION_DENIED) {
                    if (tuple.shouldShowRationale || tuple.required) {
                        PermissionsDialog.show(getActivity(), tuple.permission, tuple.permission, tuple.required, tuple.secondTry);
                        break;
                    } else {
                        setPermissionDenied(tuple.permission);
                        PermissionsClient.onComplete(getActivity(), tuple.permission, PackageManager.PERMISSION_DENIED);
                    }
                } else {
                    clearPermissionDenied(tuple.permission);
                    PermissionsClient.onComplete(getActivity(), tuple.permission, PackageManager.PERMISSION_GRANTED);
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
