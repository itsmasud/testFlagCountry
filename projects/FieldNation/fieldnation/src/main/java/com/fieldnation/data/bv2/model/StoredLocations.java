package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class StoredLocations {
    private static final String TAG = "StoredLocations";

    @Json(name = "mode")
    private ModeEnum _mode;

    @Json(name = "role")
    private String _role;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "actions")
    private ActionsEnum _actions;

    @Json(name = "results")
    private Location[] _results;

    public StoredLocations() {
    }

    public ModeEnum getMode() {
        return _mode;
    }

    public String getRole() {
        return _role;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public ActionsEnum getActions() {
        return _actions;
    }

    public Location[] getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static StoredLocations fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(StoredLocations.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(StoredLocations storedLocations) {
        try {
            return Serializer.serializeObject(storedLocations);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
