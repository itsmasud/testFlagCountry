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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class UserPreviousRequest implements Parcelable {
    private static final String TAG = "UserPreviousRequest";

    @Json(name = "at")
    private Date _at;

    @Source
    private JsonObject SOURCE;

    public UserPreviousRequest() {
        SOURCE = new JsonObject();
    }

    public UserPreviousRequest(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAt(Date at) throws ParseException {
        _at = at;
        SOURCE.put("at", at.getJson());
    }

    public Date getAt() {
        try {
            if (_at == null && SOURCE.has("at") && SOURCE.get("at") != null)
                _at = Date.fromJson(SOURCE.getJsonObject("at"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_at != null && _at.isSet())
            return _at;

        return null;
    }

    public UserPreviousRequest at(Date at) throws ParseException {
        _at = at;
        SOURCE.put("at", at.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserPreviousRequest[] array) {
        JsonArray list = new JsonArray();
        for (UserPreviousRequest item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserPreviousRequest[] fromJsonArray(JsonArray array) {
        UserPreviousRequest[] list = new UserPreviousRequest[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserPreviousRequest fromJson(JsonObject obj) {
        try {
            return new UserPreviousRequest(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserPreviousRequest> CREATOR = new Parcelable.Creator<UserPreviousRequest>() {

        @Override
        public UserPreviousRequest createFromParcel(Parcel source) {
            try {
                return UserPreviousRequest.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserPreviousRequest[] newArray(int size) {
            return new UserPreviousRequest[size];
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
