package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Status {
    private static final String TAG = "Status";

    @Json(name = "display")
    private String display;

    @Json(name = "name")
    private String name;

    @Json(name = "id")
    private Integer id;

    @Json(name = "publish_stats")
    private StatusPublishStats publishStats;

    public Status() {
    }

    public String getDisplay() {
        return display;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public StatusPublishStats getPublishStats() {
        return publishStats;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Status fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Status.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Status status) {
        try {
            return Serializer.serializeObject(status);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
