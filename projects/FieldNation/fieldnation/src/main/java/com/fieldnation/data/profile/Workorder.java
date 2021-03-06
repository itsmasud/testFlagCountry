package com.fieldnation.data.profile;

import com.fieldnation.data.workorder.Status;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Workorder {
    private static final String TAG = "Workorder";

    @Json(name = "status")
    private Status _status;
    @Json(name = "title")
    private String _title;
    @Json(name = "workorderId")
    private Long _workorderId;

    public Workorder() {
    }

    public Status getStatus() {
        return _status;
    }

    public String getTitle() {
        return _title;
    }

    public Long getWorkorderId() {
        return _workorderId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Workorder workorder) {
        try {
            return Serializer.serializeObject(workorder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Workorder fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Workorder.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
