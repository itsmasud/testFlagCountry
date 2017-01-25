package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UpdateModelMetadataUniversalContext {
    private static final String TAG = "UpdateModelMetadataUniversalContext";

    @Json(name = "correlation_id")
    private String _correlationId;

    public UpdateModelMetadataUniversalContext() {
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelMetadataUniversalContext fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelMetadataUniversalContext.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModelMetadataUniversalContext updateModelMetadataUniversalContext) {
        try {
            return Serializer.serializeObject(updateModelMetadataUniversalContext);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
