package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class UserBlocked implements Parcelable {
    private static final String TAG = "UserBlocked";

    @Json(name = "at")
    private String _at;

    @Json(name = "by")
    private UserBlockedBy _by;

    public UserBlocked() {
    }

    public void setAt(String at) {
        _at = at;
    }

    public String getAt() {
        return _at;
    }

    public UserBlocked at(String at) {
        _at = at;
        return this;
    }

    public void setBy(UserBlockedBy by) {
        _by = by;
    }

    public UserBlockedBy getBy() {
        return _by;
    }

    public UserBlocked by(UserBlockedBy by) {
        _by = by;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserBlocked fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserBlocked.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserBlocked userBlocked) {
        try {
            return Serializer.serializeObject(userBlocked);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserBlocked> CREATOR = new Parcelable.Creator<UserBlocked>() {

        @Override
        public UserBlocked createFromParcel(Parcel source) {
            try {
                return UserBlocked.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserBlocked[] newArray(int size) {
            return new UserBlocked[size];
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
