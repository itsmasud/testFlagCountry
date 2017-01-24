package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Messages {
    private static final String TAG = "Messages";

    @Json(name = "metadata")
    private ListEnvelope metadata;

    @Json(name = "problem_reported")
    private Boolean problemReported;

    @Json(name = "sum")
    private Integer sum;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "results")
    private Message results;

    public Messages() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public Boolean getProblemReported() {
        return problemReported;
    }

    public Integer getSum() {
        return sum;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public Message getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Messages fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Messages.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Messages messages) {
        try {
            return Serializer.serializeObject(messages);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
