package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Map {
    private static final String TAG = "Map";

    @Json(name = "href")
    private String href;

    @Json(name = "url")
    private String url;

    public Map() {
    }

    public String getHref() {
        return href;
    }

    public String getUrl() {
        return url;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Map fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Map.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
