package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class UserBlocked implements Parcelable {
    private static final String TAG = "UserBlocked";

    @Json(name = "at")
    private String _at;

    @Json(name = "by")
    private UserBlockedBy _by;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UserBlocked() {
    }

    public void setAt(String at) throws ParseException {
        _at = at;
        SOURCE.put("at", at);
    }

    public String getAt() {
        return _at;
    }

    public UserBlocked at(String at) throws ParseException {
        _at = at;
        SOURCE.put("at", at);
        return this;
    }

    public void setBy(UserBlockedBy by) throws ParseException {
        _by = by;
        SOURCE.put("by", by.getJson());
    }

    public UserBlockedBy getBy() {
        return _by;
    }

    public UserBlocked by(UserBlockedBy by) throws ParseException {
        _by = by;
        SOURCE.put("by", by.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserBlocked[] array) {
        JsonArray list = new JsonArray();
        for (UserBlocked item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserBlocked[] fromJsonArray(JsonArray array) {
        UserBlocked[] list = new UserBlocked[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserBlocked fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserBlocked.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
