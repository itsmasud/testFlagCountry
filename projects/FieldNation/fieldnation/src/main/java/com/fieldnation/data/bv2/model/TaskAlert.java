package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TaskAlert {
    private static final String TAG = "TaskAlert";

    @Json(name = "id")
    private Integer id;

    @Json(name = "sent")
    private Long sent;

    @Json(name = "email")
    private String email;

    public TaskAlert() {
    }

    public Integer getId() {
        return id;
    }

    public Long getSent() {
        return sent;
    }

    public String getEmail() {
        return email;
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
