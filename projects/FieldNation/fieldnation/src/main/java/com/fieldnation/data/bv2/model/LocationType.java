package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LocationType {
    private static final String TAG = "LocationType";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    public LocationType() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationType fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationType.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationType locationType) {
        try {
            return Serializer.serializeObject(locationType);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}