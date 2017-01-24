package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class IdResponse {
    private static final String TAG = "IdResponse";

    @Json(name = "id")
    private Integer id;

    public IdResponse() {
    }

    public Integer getId() {
        return id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static IdResponse fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(IdResponse.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(IdResponse idResponse) {
        try {
            return Serializer.serializeObject(idResponse);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
