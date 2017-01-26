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

public class UserBlockedBy implements Parcelable {
    private static final String TAG = "UserBlockedBy";

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public UserBlockedBy() {
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public UserBlockedBy name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public UserBlockedBy id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserBlockedBy fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserBlockedBy.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserBlockedBy userBlockedBy) {
        try {
            return Serializer.serializeObject(userBlockedBy);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserBlockedBy> CREATOR = new Parcelable.Creator<UserBlockedBy>() {

        @Override
        public UserBlockedBy createFromParcel(Parcel source) {
            try {
                return UserBlockedBy.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserBlockedBy[] newArray(int size) {
            return new UserBlockedBy[size];
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
