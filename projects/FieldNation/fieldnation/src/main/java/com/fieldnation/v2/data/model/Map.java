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

public class Map implements Parcelable {
    private static final String TAG = "Map";

    @Json(name = "href")
    private String _href;

    @Json(name = "url")
    private String _url;

    @Source
    private JsonObject SOURCE;

    public Map() {
        SOURCE = new JsonObject();
    }

    public Map(JsonObject obj) {
        SOURCE = obj;
    }

    public void setHref(String href) throws ParseException {
        _href = href;
        SOURCE.put("href", href);
    }

    public String getHref() {
        try {
            if (_href == null && SOURCE.has("href") && SOURCE.get("href") != null)
                _href = SOURCE.getString("href");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _href;
    }

    public Map href(String href) throws ParseException {
        _href = href;
        SOURCE.put("href", href);
        return this;
    }

    public void setUrl(String url) throws ParseException {
        _url = url;
        SOURCE.put("url", url);
    }

    public String getUrl() {
        try {
            if (_url == null && SOURCE.has("url") && SOURCE.get("url") != null)
                _url = SOURCE.getString("url");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _url;
    }

    public Map url(String url) throws ParseException {
        _url = url;
        SOURCE.put("url", url);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Map[] array) {
        JsonArray list = new JsonArray();
        for (Map item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Map[] fromJsonArray(JsonArray array) {
        Map[] list = new Map[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Map fromJson(JsonObject obj) {
        try {
            return new Map(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return !misc.isEmptyOrNull(getHref()) && !misc.isEmptyOrNull(getUrl());
    }
}
