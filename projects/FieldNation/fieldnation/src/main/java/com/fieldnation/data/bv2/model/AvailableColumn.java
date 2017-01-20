package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class AvailableColumn {
    private static final String TAG = "AvailableColumn";

    @Json(name = "group")
    private String group = null;

    @Json(name = "items")
    private AvailableColumnItems[] items;

    private AvailableColumn() {
    }

    public String getGroup() {
        return group;
    }

    public AvailableColumnItems[] getItems() {
        return items;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AvailableColumn fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AvailableColumn.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(AvailableColumn availableColumn) {
        try {
            return Serializer.serializeObject(availableColumn);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}