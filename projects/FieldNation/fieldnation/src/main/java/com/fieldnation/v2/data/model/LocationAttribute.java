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

public class LocationAttribute implements Parcelable {
    private static final String TAG = "LocationAttribute";

    @Json(name = "key")
    private String _key;

    @Json(name = "private")
    private Boolean _private;

    @Json(name = "value")
    private String _value;

    @Source
    private JsonObject SOURCE;

    public LocationAttribute() {
        SOURCE = new JsonObject();
    }

    public LocationAttribute(JsonObject obj) {
        SOURCE = obj;
    }

    public void setKey(String key) throws ParseException {
        _key = key;
        SOURCE.put("key", key);
    }

    public String getKey() {
        try {
            if (_key == null && SOURCE.has("key") && SOURCE.get("key") != null)
                _key = SOURCE.getString("key");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _key;
    }

    public LocationAttribute key(String key) throws ParseException {
        _key = key;
        SOURCE.put("key", key);
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

    public LocationAttribute privatee(Boolean privatee) throws ParseException {
        _private = privatee;
        SOURCE.put("private", privatee);
        return this;
    }

    public void setValue(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
    }

    public String getValue() {
        try {
            if (_value == null && SOURCE.has("value") && SOURCE.get("value") != null)
                _value = SOURCE.getString("value");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _value;
    }

    public LocationAttribute value(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(LocationAttribute[] array) {
        JsonArray list = new JsonArray();
        for (LocationAttribute item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static LocationAttribute[] fromJsonArray(JsonArray array) {
        LocationAttribute[] list = new LocationAttribute[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static LocationAttribute fromJson(JsonObject obj) {
        try {
            return new LocationAttribute(obj);
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
    public static final Parcelable.Creator<LocationAttribute> CREATOR = new Parcelable.Creator<LocationAttribute>() {

        @Override
        public LocationAttribute createFromParcel(Parcel source) {
            try {
                return LocationAttribute.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public LocationAttribute[] newArray(int size) {
            return new LocationAttribute[size];
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

}
