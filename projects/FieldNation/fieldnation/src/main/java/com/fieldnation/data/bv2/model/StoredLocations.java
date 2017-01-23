package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class StoredLocations {
    private static final String TAG = "StoredLocations";

    @Json(name = "work_order_id")
    private Integer workOrderId = null;

    @Json(name = "mode")
    private ModeEnum mode = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "actions")
    private ActionsEnum actions = null;

    @Json(name = "results")
    private Location[] results;

    public enum ModeEnum {
        @Json(name = "custom")
        CUSTOM("custom"),
        @Json(name = "location")
        LOCATION("location"),
        @Json(name = "remote")
        REMOTE("remote");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public StoredLocations() {
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public ModeEnum getMode() {
        return mode;
    }

    public String getRole() {
        return role;
    }

    public ActionsEnum getActions() {
        return actions;
    }

    public Location[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static StoredLocations fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(StoredLocations.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}