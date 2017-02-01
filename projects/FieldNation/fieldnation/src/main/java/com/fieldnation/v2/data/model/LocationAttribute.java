package com.fieldnation.v2.data.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class LocationAttribute implements Parcelable {
    private static final String TAG = "LocationAttribute";

    @Json(name = "private")
    private Boolean _private;

    @Json(name = "value")
    private String _value;

    @Json(name = "key")
    private String _key;

    public LocationAttribute() {
    }

    public void setPrivate(Boolean privatee) {
        _private = privatee;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public LocationAttribute privatee(Boolean privatee) {
        _private = privatee;
        return this;
    }

    public void setValue(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public LocationAttribute value(String value) {
        _value = value;
        return this;
    }

    public void setKey(String key) {
        _key = key;
    }

    public String getKey() {
        return _key;
    }

    public LocationAttribute key(String key) {
        _key = key;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationAttribute[] fromJsonArray(JsonArray array) {
        LocationAttribute[] list = new LocationAttribute[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static LocationAttribute fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationAttribute.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationAttribute locationAttribute) {
        try {
            return Serializer.serializeObject(locationAttribute);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
