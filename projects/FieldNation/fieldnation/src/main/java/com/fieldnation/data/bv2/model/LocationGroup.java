package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class LocationGroup {
    private static final String TAG = "LocationGroup";

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public LocationGroup() {
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static LocationGroup fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(LocationGroup.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(LocationGroup locationGroup) {
        try {
            return Serializer.serializeObject(locationGroup);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
