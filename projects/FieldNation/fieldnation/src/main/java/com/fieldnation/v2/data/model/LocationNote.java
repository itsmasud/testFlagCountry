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

public class LocationNote implements Parcelable {
    private static final String TAG = "LocationNote";

    @Json(name = "created")
    private String _created;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "private")
    private Boolean _private;

    @Json(name = "text")
    private String _text;

    @Json(name = "user_id")
    private Integer _userId;

    @Source
    private JsonObject SOURCE;

    public LocationNote() {
        SOURCE = new JsonObject();
    }

    public LocationNote(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCreated(String created) throws ParseException {
        _created = created;
        SOURCE.put("created", created);
    }

    public String getCreated() {
        try {
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = SOURCE.getString("created");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _created;
    }

    public LocationNote created(String created) throws ParseException {
        _created = created;
        SOURCE.put("created", created);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public LocationNote id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setPrivate(Boolean privatee) throws ParseException {
        _private = privatee;
        SOURCE.put("private", privatee);
    }

    public Boolean getPrivate() {
        try {
            if (_private == null && SOURCE.has("private") && SOURCE.get("private") != null)
                _private = SOURCE.getBoolean("private");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _private;
    }

    public LocationNote privatee(Boolean privatee) throws ParseException {
        _private = privatee;
        SOURCE.put("private", privatee);
        return this;
    }

    public void setText(String text) throws ParseException {
        _text = text;
        SOURCE.put("text", text);
    }

    public String getText() {
        try {
            if (_text == null && SOURCE.has("text") && SOURCE.get("text") != null)
                _text = SOURCE.getString("text");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _text;
    }

    public LocationNote text(String text) throws ParseException {
        _text = text;
        SOURCE.put("text", text);
        return this;
    }

    public void setUserId(Integer userId) throws ParseException {
        _userId = userId;
        SOURCE.put("user_id", userId);
    }

    public Integer getUserId() {
        try {
            if (_userId == null && SOURCE.has("user_id") && SOURCE.get("user_id") != null)
                _userId = SOURCE.getInt("user_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _userId;
    }

    public LocationNote userId(Integer userId) throws ParseException {
        _userId = userId;
        SOURCE.put("user_id", userId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(LocationNote[] array) {
        JsonArray list = new JsonArray();
        for (LocationNote item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static LocationNote[] fromJsonArray(JsonArray array) {
        LocationNote[] list = new LocationNote[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static LocationNote fromJson(JsonObject obj) {
        try {
            return new LocationNote(obj);
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
    public static final Parcelable.Creator<LocationNote> CREATOR = new Parcelable.Creator<LocationNote>() {

        @Override
        public LocationNote createFromParcel(Parcel source) {
            try {
                return LocationNote.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public LocationNote[] newArray(int size) {
            return new LocationNote[size];
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
        return getId() != null && getId() != 0;
    }
}
