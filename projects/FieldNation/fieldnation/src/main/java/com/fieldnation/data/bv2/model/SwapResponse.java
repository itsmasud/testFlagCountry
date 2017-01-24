package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class SwapResponse {
    private static final String TAG = "SwapResponse";

    @Json(name = "success")
    private Boolean success;

    public SwapResponse() {
    }

    public Boolean getSuccess() {
        return success;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static SwapResponse fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SwapResponse.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SwapResponse swapResponse) {
        try {
            return Serializer.serializeObject(swapResponse);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
