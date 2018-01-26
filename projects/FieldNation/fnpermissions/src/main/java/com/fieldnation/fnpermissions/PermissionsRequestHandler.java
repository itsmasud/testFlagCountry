package com.fieldnation.fnpermissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpigeon.Pigeon;
import com.fieldnation.fnpigeon.PigeonRoost;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by mc on 8/10/17.
 */

public abstract class PermissionsRequestHandler extends Pigeon implements Constants {
    private static final String TAG = "PermissionsRequestHandler";

    private static Hashtable<String, PermissionsTuple> QUEUED_PERMS = new Hashtable<>();
    private static final Set<String> requiredPermissions = new HashSet<>();
    protected static boolean requesting = false;

    public void sub() {
        PigeonRoost.sub(this, ADDRESS_REQUESTS);
        PigeonRoost.sub(this, ADDRESS_REQUEST_RESULT);
    }

    public void unsub() {
        PigeonRoost.unsub(this, ADDRESS_REQUESTS);
        PigeonRoost.unsub(this, ADDRESS_REQUEST_RESULT);
    }

    /*-**********************************-*/
    /*-             Listener             -*/
    /*-**********************************-*/
    @Override
    public void onMessage(String address, Object message) {
        Log.v(TAG, "onMessage");
        if (address.equals(ADDRESS_REQUESTS)) {
            PigeonRoost.clearAddressCacheAll(ADDRESS_REQUESTS);
            Bundle bundle = (Bundle) message;
            onRequest(bundle.getStringArray("permissions"),
                    bundle.getBooleanArray("required"));

        } else if (address.equals(ADDRESS_REQUEST_RESULT)) {
            PigeonRoost.clearAddressCacheAll(ADDRESS_REQUEST_RESULT);
            Bundle bundle = (Bundle) message;
            onResponse(bundle.getInt("requestCode"),
                    bundle.getStringArray("permissions"),
                    bundle.getIntArray("grantResults"));
        }
    }

    public abstract Activity getActivity();

    private void onRequest(String[] permissions, boolean[] required) {
        Log.v(TAG, "onRequest");

        List<String> requestable = new LinkedList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (State.isPermissionDenied(getActivity(), permissions[i])) {
                PermissionsClient.onComplete(permissions[i], PackageManager.PERMISSION_DENIED);
            } else {
                requestable.add(permissions[i]);
            }

            if (required[i]) {
                requiredPermissions.add(permissions[i]);
            } else {
                requiredPermissions.remove(permissions[i]);
            }
        }

        // if we have requested... but no response we need to store these and wait
        if (!requesting && requestable.size() > 0) {
            Log.v(TAG, "Requesting permissions" + getActivity().getLocalClassName());
            ActivityCompat.requestPermissions(
                    getActivity(), requestable.toArray(new String[requestable.size()]), 0);
            requesting = true;
        }
    }


    private void onResponse(int requestCode, String[] permissions, int[] grantResults) {
        Log.v(TAG, "onResponse " + requestCode);
        requesting = false;

        for (int i = 0; i < permissions.length; i++) {
            Log.v(TAG, "onResponse for loop " + permissions[i]);
            PermissionsTuple tuple = PermissionsTuple.get(getActivity(), permissions[i]);
            tuple.required(requiredPermissions.contains(permissions[i]));

            if (!tuple.secondTry) {
                Log.v(TAG, "onResponse not second try");
                //tuple.secondTry(true).save(getActivity());

                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "onResponse Granted");
                    State.clearPermissionDenied(getActivity(), permissions[i]);
                    tuple.delete(getActivity());
                    PermissionsClient.onComplete(permissions[i], PackageManager.PERMISSION_GRANTED);

                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Log.v(TAG, "onResponse denied");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
                        Log.v(TAG, "onResponse shouldShowRequestPermissionRationale");
                        QUEUED_PERMS.put(permissions[i],
                                tuple.shouldShowRationale(true).save(getActivity()));
                    } else {
                        Log.v(TAG, "onResponse not shouldShowRequestPermissionRationale");
                        if (requiredPermissions.contains(permissions[i])) {
                            Log.v(TAG, "onResponse not shouldShowRequestPermissionRationale required");
                            QUEUED_PERMS.put(permissions[i],
                                    tuple.shouldShowRationale(true).save(getActivity()));
                        } else {
                            Log.v(TAG, "onResponse not shouldShowRequestPermissionRationale not required");
                            tuple.shouldShowRationale(false).save(getActivity());
                            PermissionsClient.onComplete(permissions[i], PackageManager.PERMISSION_DENIED);
                            State.setPermissionDenied(getActivity(), permissions[i]);
                        }
                    }
                }
            } else if (tuple.secondTry) {
                Log.v(TAG, "onResponse second try");
                // Denied and required. show dialog
                if (tuple.required && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Log.v(TAG, "onResponse second try, denied and required");
                    QUEUED_PERMS.put(permissions[i],
                            tuple.shouldShowRationale(true).save(getActivity()));
                } else {
                    Log.v(TAG, "onResponse second try, not required");
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.v(TAG, "onResponse second try, not required denied");
                        State.setPermissionDenied(getActivity(), permissions[i]);
                    } else {
                        Log.v(TAG, "onResponse second try, not required granted");
                        tuple.delete(getActivity());
                        State.clearPermissionDenied(getActivity(), permissions[i]);
                    }
                    PermissionsClient.onComplete(permissions[i], grantResults[i]);
                }
            }
        }
        processQueue();
    }

    private void processQueue() {
        Log.v(TAG, "processQueue");
        while (QUEUED_PERMS.size() > 0) {
            Log.v(TAG, "processQueue tuple");
            PermissionsTuple tuple = QUEUED_PERMS.remove(QUEUED_PERMS.keys().nextElement());

            int grant = PermissionsClient.checkSelfPermission(getActivity(), tuple.permission);
            if (grant == PackageManager.PERMISSION_DENIED) {
                Log.v(TAG, "processQueue denied");
                if (tuple.shouldShowRationale || tuple.required) {
                    Log.v(TAG, "processQueue shouldShowRationale || required");
                    PermissionsDialog.show(getActivity(), tuple.permission, tuple);
                    requesting = true;
                    break;
                } else {
                    Log.v(TAG, "processQueue !shouldShowRationale && !required");
                    State.setPermissionDenied(getActivity(), tuple.permission);
                    PermissionsClient.onComplete(tuple.permission, PackageManager.PERMISSION_DENIED);
                }
            } else {
                Log.v(TAG, "processQueue granted");
                State.clearPermissionDenied(getActivity(), tuple.permission);
                PermissionsClient.onComplete(tuple.permission, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

}
