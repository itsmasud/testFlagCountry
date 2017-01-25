package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Messages {
    private static final String TAG = "Messages";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "problem_reported")
    private Boolean _problemReported;

    @Json(name = "sum")
    private Integer _sum;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "results")
    private Message _results;

    public Messages() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Boolean getProblemReported() {
        return _problemReported;
    }

    public Integer getSum() {
        return _sum;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Message getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Messages fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Messages.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
