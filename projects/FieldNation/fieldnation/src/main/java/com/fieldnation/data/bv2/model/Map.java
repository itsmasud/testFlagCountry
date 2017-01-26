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

public class Map implements Parcelable {
    private static final String TAG = "Map";

    @Json(name = "href")
    private String _href;

    @Json(name = "url")
    private String _url;

    public Map() {
    }

    public void setHref(String href) {
        _href = href;
    }

    public String getHref() {
        return _href;
    }

    public Map href(String href) {
        _href = href;
        return this;
    }

    public void setUrl(String url) {
        _url = url;
    }

    public String getUrl() {
        return _url;
    }

    public Map url(String url) {
        _url = url;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Map fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Map.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Map map) {
        try {
            return Serializer.serializeObject(map);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Map> CREATOR = new Parcelable.Creator<Map>() {

        @Override
        public Map createFromParcel(Parcel source) {
            try {
                return Map.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Map[] newArray(int size) {
            return new Map[size];
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
