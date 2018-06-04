package com.fieldnation.fnpermissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.PigeonRoost;
import com.fieldnation.fnpigeon.Sticky;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/10/17.
 */

public class PermissionsClient implements Constants {
    private static final String TAG = "PermissionsClient";

    public static void requestPermissions(String[] permissions, boolean[] required) {
        requestPermissions(permissions, required, null);
    }

    public static void requestPermissions(String[] permissions, boolean[] required, Parcelable extraData) {
        Log.v(TAG, "requestPermissions");
        Bundle payload = new Bundle();
        payload.putStringArray("permissions", permissions);
        payload.putBooleanArray("required", required);
        payload.putParcelable("extraData", extraData);

        PigeonRoost.sendMessage(ADDRESS_REQUESTS, payload, Sticky.TEMP);
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults, null);
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Parcelable extraData) {
        Log.v(TAG, "onRequestPermissionsResult");
        Bundle payload = new Bundle();
        payload.putInt("requestCode", requestCode);
        payload.putStringArray("permissions", permissions);
        payload.putIntArray("grantResults", grantResults);
        payload.putParcelable("extraData", extraData);

        PigeonRoost.sendMessage(ADDRESS_REQUEST_RESULT, payload, Sticky.TEMP);
    }

    protected static void onComplete(String permission, int grantResult, Parcelable extraData) {
        Log.v(TAG, "onComplete");
        Bundle payload = new Bundle();
        payload.putString("permission", permission);
        payload.putInt("grantResult", grantResult);
        payload.putParcelable("extraData", extraData);

        PigeonRoost.sendMessage(ADDRESS_COMPLETE, payload, Sticky.FOREVER);
    }

    public static int checkSelfPermission(Context context, String permission) {
        Log.v(TAG, "checkSelfPermission");
        int grant = ContextCompat.checkSelfPermission(context, permission);

        if (grant == PackageManager.PERMISSION_GRANTED)
            State.clearPermissionDenied(context, permission);

        return grant;
    }

    public static void checkSelfPermissionAndRequest(Context context, String[] permissions, boolean[] required) {
        checkSelfPermissionAndRequest(context, permissions, required, null);
    }

    public static void checkSelfPermissionAndRequest(Context context, String[] permissions, boolean[] required, Parcelable extraData) {
        Log.v(TAG, "checkSelfPermissionAndRequest");
        List<String> requestable = new LinkedList<>();
        List<Boolean> requireds = new LinkedList<>();
        for (int i = 0; i < required.length; i++) {
            if (checkSelfPermission(context, permissions[i]) == PackageManager.PERMISSION_GRANTED) {
                onComplete(permissions[i], PackageManager.PERMISSION_GRANTED, extraData);
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

            requestPermissions(requestable.toArray(new String[requestable.size()]), reqs, extraData);
        }
    }
}
