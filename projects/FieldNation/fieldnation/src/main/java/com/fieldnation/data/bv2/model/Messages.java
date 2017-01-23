package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Messages {
    private static final String TAG = "Messages";

    @Json(name = "metadata")
    private ListEnvelope metadata = null;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "results")
    private Message results = null;

    @Json(name = "sum")
    private Integer sum = null;

    @Json(name = "problem_reported")
    private Boolean problemReported = null;

    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public Messages() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public Message getResults() {
        return results;
    }

    public Integer getSum() {
        return sum;
    }

    public Boolean getProblemReported() {
        return problemReported;
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

