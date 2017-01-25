package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class AvailableColumn {
    private static final String TAG = "AvailableColumn";

    @Json(name = "items")
    private AvailableColumnItems[] _items;

    @Json(name = "group")
    private String _group;

    public AvailableColumn() {
    }

    public AvailableColumnItems[] getItems() {
        return _items;
    }

    public String getGroup() {
        return _group;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AvailableColumn fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AvailableColumn.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
