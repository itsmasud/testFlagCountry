package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TaskGroup {
    private static final String TAG = "TaskGroup";

    @Json(name = "id")
    private Integer id;

    @Json(name = "label")
    private String label;

    public TaskGroup() {
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TaskGroup fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TaskGroup.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TaskGroup taskGroup) {
        try {
            return Serializer.serializeObject(taskGroup);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
