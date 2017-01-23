package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class SelectionRule {
    private static final String TAG = "SelectionRule";

    @Json(name = "name")
    private String name = null;

    @Json(name = "id")
    private Integer id = null;

    public SelectionRule() {
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static SelectionRule fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SelectionRule.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SelectionRule selectionRule) {
        try {
            return Serializer.serializeObject(selectionRule);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}