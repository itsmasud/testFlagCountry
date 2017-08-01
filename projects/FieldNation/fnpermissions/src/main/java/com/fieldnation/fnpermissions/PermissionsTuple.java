package com.fieldnation.fnpermissions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 7/25/17.
 */

public class PermissionsTuple implements Parcelable {
    private static final String TAG = "PermissionsTuple";
    private static final String PREFERENCES_NAME = "com.fieldnation.fnpermissions";

    @Json
    public String permission;
    @Json
    public boolean required = false;
    @Json
    public boolean shouldShowRationale = false;
    @Json
    public boolean secondTry = false;

    public PermissionsTuple() {
    }

/*
    private PermissionsTuple(String permission, boolean required, boolean shouldShowRationale, boolean secondTry) {
        this.permission = permission;
        this.required = required;
        this.shouldShowRationale = shouldShowRationale;
        this.secondTry = secondTry;
    }
*/

    private PermissionsTuple(String permission) {
        this.permission = permission;
    }

    public PermissionsTuple required(boolean required) {
        this.required = required;
        return this;
    }

    public PermissionsTuple shouldShowRationale(boolean shouldShowRationale) {
        this.shouldShowRationale = shouldShowRationale;
        return this;
    }

    public PermissionsTuple secondTry(boolean secondTry) {
        this.secondTry = secondTry;
        return this;
    }

    public static PermissionsTuple get(Context context, String permission) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
        try {
            if (sharedPreferences.contains(permission)) {
                return fromJson(new JsonObject(sharedPreferences.getString(permission, "")));
            } else {
                return new PermissionsTuple(permission);
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public PermissionsTuple save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(permission, toJson(this).toString());
        editor.apply();

        return this;
    }

    public void delete(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(permission);
        editor.apply();
    }

    public static PermissionsTuple fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(PermissionsTuple.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static JsonObject toJson(PermissionsTuple permissionsTuple) {
        try {
            return Serializer.serializeObject(permissionsTuple);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PermissionsTuple> CREATOR = new Parcelable.Creator<PermissionsTuple>() {

        @Override
        public PermissionsTuple createFromParcel(Parcel source) {
            try {
                return PermissionsTuple.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PermissionsTuple[] newArray(int size) {
            return new PermissionsTuple[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }

}
