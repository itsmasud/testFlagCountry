package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TaskType {
    private static final String TAG = "TaskType";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "title")
    private String _title;

    @Json(name = "key")
    private String _key;

    public TaskType() {
    }

    public Integer getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public String getKey() {
        return _key;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TaskType fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TaskType.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TaskType taskType) {
        try {
            return Serializer.serializeObject(taskType);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
