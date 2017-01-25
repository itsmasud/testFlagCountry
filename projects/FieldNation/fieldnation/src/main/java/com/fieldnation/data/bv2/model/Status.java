package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Status {
    private static final String TAG = "Status";

    @Json(name = "display")
    private String _display;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "publish_stats")
    private StatusPublishStats _publishStats;

    public Status() {
    }

    public String getDisplay() {
        return _display;
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    public StatusPublishStats getPublishStats() {
        return _publishStats;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Status fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Status.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
