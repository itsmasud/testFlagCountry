package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Map {
    private static final String TAG = "Map";

    @Json(name = "href")
    private String _href;

    @Json(name = "url")
    private String _url;

    public Map() {
    }

    public String getHref() {
        return _href;
    }

    public String getUrl() {
        return _url;
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
}
