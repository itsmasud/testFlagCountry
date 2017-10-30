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

public class LocationNotes implements Parcelable {
    private static final String TAG = "LocationNotes";

    @Json(name = "created")
    private String _created;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "private")
    private Boolean _private;

    @Json(name = "text")
    private String _text;

    @Json(name = "user")
    private Boolean _user;

    @Source
    private JsonObject SOURCE;

    public LocationNotes() {
        SOURCE = new JsonObject();
    }

    public LocationNotes(JsonObject obj) {
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

    public LocationNotes created(String created) throws ParseException {
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

    public LocationNotes id(Integer id) throws ParseException {
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

    public LocationNotes privatee(Boolean privatee) throws ParseException {
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

    public LocationNotes text(String text) throws ParseException {
        _text = text;
        SOURCE.put("text", text);
        return this;
    }

    public void setUser(Boolean user) throws ParseException {
        _user = user;
        SOURCE.put("user", user);
    }

    public Boolean getUser() {
        try {
            if (_user == null && SOURCE.has("user") && SOURCE.get("user") != null)
                _user = SOURCE.getBoolean("user");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _user;
    }

    public LocationNotes user(Boolean user) throws ParseException {
        _user = user;
        SOURCE.put("user", user);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(LocationNotes[] array) {
        JsonArray list = new JsonArray();
        for (LocationNotes item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static LocationNotes[] fromJsonArray(JsonArray array) {
        LocationNotes[] list = new LocationNotes[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static LocationNotes fromJson(JsonObject obj) {
        try {
            return new LocationNotes(obj);
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
    public static final Parcelable.Creator<LocationNotes> CREATOR = new Parcelable.Creator<LocationNotes>() {

        @Override
        public LocationNotes createFromParcel(Parcel source) {
            try {
                return LocationNotes.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public LocationNotes[] newArray(int size) {
            return new LocationNotes[size];
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
