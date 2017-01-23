package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TaskAlert {
    private static final String TAG = "TaskAlert";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "email")
    private String email = null;

    @Json(name = "sent")
    private Long sent = null;

    public TaskAlert() {
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getSent() {
        return sent;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TaskAlert fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TaskAlert.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TaskAlert taskAlert) {
        try {
            return Serializer.serializeObject(taskAlert);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}