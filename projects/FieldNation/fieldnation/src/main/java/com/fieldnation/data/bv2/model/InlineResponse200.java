package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class InlineResponse200 {
    private static final String TAG = "InlineResponse200";

    @Json(name = "success")
    private Boolean success = null;

    @Json(name = "error")
    private Boolean error = null;

    public InlineResponse200() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public Boolean getError() {
        return error;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static InlineResponse200 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(InlineResponse200.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(InlineResponse200 inlineResponse200) {
        try {
            return Serializer.serializeObject(inlineResponse200);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}

