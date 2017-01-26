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

public class BackgroundCheck implements Parcelable {
    private static final String TAG = "BackgroundCheck";

    @Json(name = "expires")
    private String _expires;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public BackgroundCheck() {
    }

    public void setExpires(String expires) {
        _expires = expires;
    }

    public String getExpires() {
        return _expires;
    }

    public BackgroundCheck expires(String expires) {
        _expires = expires;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public BackgroundCheck name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public BackgroundCheck id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static BackgroundCheck fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(BackgroundCheck.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(BackgroundCheck backgroundCheck) {
        try {
            return Serializer.serializeObject(backgroundCheck);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<BackgroundCheck> CREATOR = new Parcelable.Creator<BackgroundCheck>() {

        @Override
        public BackgroundCheck createFromParcel(Parcel source) {
            try {
                return BackgroundCheck.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public BackgroundCheck[] newArray(int size) {
            return new BackgroundCheck[size];
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
