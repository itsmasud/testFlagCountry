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
 * Created by dmgen from swagger.
 */

public class LocationNote implements Parcelable {
    private static final String TAG = "LocationNote";

    @Json(name = "private")
    private Boolean _private;

    @Json(name = "user_id")
    private Integer _userId;

    @Json(name = "created")
    private String _created;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "text")
    private String _text;

    public LocationNote() {
    }

    public void setPrivate(Boolean privatee) {
        _private = privatee;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public LocationNote privatee(Boolean privatee) {
        _private = privatee;
        return this;
    }

    public void setUserId(Integer userId) {
        _userId = userId;
    }

    public Integer getUserId() {
        return _userId;
    }

    public LocationNote userId(Integer userId) {
        _userId = userId;
        return this;
    }

    public void setCreated(String created) {
        _created = created;
    }

    public String getCreated() {
        return _created;
    }

    public LocationNote created(String created) {
        _created = created;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public LocationNote id(Integer id) {
        _id = id;
        return this;
    }

    public void setText(String text) {
        _text = text;
    }

    public String getText() {
        return _text;
    }

    public LocationNote text(String text) {
        _text = text;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationNote[] fromJsonArray(JsonArray array) {
        LocationNote[] list = new LocationNote[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static LocationNote fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationNote.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationNote locationNote) {
        try {
            return Serializer.serializeObject(locationNote);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
