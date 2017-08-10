package com.fieldnation.fnpermissions;

import android.content.SharedPreferences;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ContextProvider;

/**
 * Created by mc on 8/10/17.
 */

public class State {
    static final String TAG = "State";

    static void setPermissionDenied(String permission) {
        Log.v(TAG, "setPermissionDenied " + permission);
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

    static boolean isPermissionDenied(String permission) {
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

}
