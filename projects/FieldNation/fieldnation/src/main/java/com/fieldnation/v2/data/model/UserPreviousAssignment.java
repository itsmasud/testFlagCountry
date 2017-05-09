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

public class UserPreviousAssignment implements Parcelable {
    private static final String TAG = "UserPreviousAssignment";

    @Json(name = "by")
    private User _by;

    @Json(name = "reason")
    private String _reason;

    @Json(name = "removal")
    private Date _removal;

    @Source
    private JsonObject SOURCE;

    public UserPreviousAssignment() {
        SOURCE = new JsonObject();
    }

    public UserPreviousAssignment(JsonObject obj) {
        SOURCE = obj;
    }

    public void setBy(User by) throws ParseException {
        _by = by;
        SOURCE.put("by", by.getJson());
    }

    public User getBy() {
        try {
            if (_by == null && SOURCE.has("by") && SOURCE.get("by") != null)
                _by = User.fromJson(SOURCE.getJsonObject("by"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_by != null && _by.isSet())
            return _by;

        return null;
    }

    public UserPreviousAssignment by(User by) throws ParseException {
        _by = by;
        SOURCE.put("by", by.getJson());
        return this;
    }

    public void setReason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
    }

    public String getReason() {
        try {
            if (_reason == null && SOURCE.has("reason") && SOURCE.get("reason") != null)
                _reason = SOURCE.getString("reason");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _reason;
    }

    public UserPreviousAssignment reason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
        return this;
    }

    public void setRemoval(Date removal) throws ParseException {
        _removal = removal;
        SOURCE.put("removal", removal.getJson());
    }

    public Date getRemoval() {
        try {
            if (_removal == null && SOURCE.has("removal") && SOURCE.get("removal") != null)
                _removal = Date.fromJson(SOURCE.getJsonObject("removal"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_removal != null && _removal.isSet())
            return _removal;

        return null;
    }

    public UserPreviousAssignment removal(Date removal) throws ParseException {
        _removal = removal;
        SOURCE.put("removal", removal.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserPreviousAssignment[] array) {
        JsonArray list = new JsonArray();
        for (UserPreviousAssignment item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserPreviousAssignment[] fromJsonArray(JsonArray array) {
        UserPreviousAssignment[] list = new UserPreviousAssignment[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserPreviousAssignment fromJson(JsonObject obj) {
        try {
            return new UserPreviousAssignment(obj);
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
    public static final Parcelable.Creator<UserPreviousAssignment> CREATOR = new Parcelable.Creator<UserPreviousAssignment>() {

        @Override
        public UserPreviousAssignment createFromParcel(Parcel source) {
            try {
                return UserPreviousAssignment.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserPreviousAssignment[] newArray(int size) {
            return new UserPreviousAssignment[size];
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
