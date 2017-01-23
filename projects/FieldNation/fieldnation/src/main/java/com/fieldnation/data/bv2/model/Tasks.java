package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Tasks {
    private static final String TAG = "Tasks";

    @Json(name = "metadata")
    private ListEnvelope metadata = null;

    @Json(name = "results")
    private Task[] results;

    public Tasks() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public Task[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Tasks fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Tasks.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Tasks tasks) {
        try {
            return Serializer.serializeObject(tasks);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}