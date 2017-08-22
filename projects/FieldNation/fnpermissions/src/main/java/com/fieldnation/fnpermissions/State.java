package com.fieldnation.fnpermissions;

import android.content.Context;
import android.content.SharedPreferences;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 8/10/17.
 */

public class State {
    static final String TAG = "State";

    static void setPermissionDenied(Context context, String permission) {
        Log.v(TAG, "setPermissionDenied " + permission);
        SharedPreferences sp = context.getSharedPreferences("PermissionsClient", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(permission, System.currentTimeMillis());
        edit.apply();
    }

    static void clearPermissionDenied(Context context, String permission) {
        SharedPreferences sp = context.getSharedPreferences("PermissionsClient", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(permission);
        edit.apply();
    }

    static boolean isPermissionDenied(Context context, String permission) {
        SharedPreferences sp = context.getSharedPreferences("PermissionsClient", 0);
        if (sp.contains(permission)) {
            if (sp.getLong(permission, 0) + 86400000 < System.currentTimeMillis()) { // 1 day
                return true;
            } else {
                clearPermissionDenied(context, permission);
            }
        }
        return false;
    }

}
