package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class InlineResponse201 {
    private static final String TAG = "InlineResponse201";

    @Json(name = "id")
    private Integer id = null;

    public InlineResponse201() {
    }

    public Integer getId() {
        return id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static InlineResponse201 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(InlineResponse201.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(InlineResponse201 inlineResponse201) {
        try {
            return Serializer.serializeObject(inlineResponse201);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

