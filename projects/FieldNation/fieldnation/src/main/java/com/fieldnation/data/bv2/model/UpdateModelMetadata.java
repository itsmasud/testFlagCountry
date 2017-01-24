package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UpdateModelMetadata {
    private static final String TAG = "UpdateModelMetadata";

    @Json(name = "data")
    private UpdateModelMetadataData data;

    @Json(name = "universal_context")
    private UpdateModelMetadataUniversalContext universalContext;

    public UpdateModelMetadata() {
    }

    public UpdateModelMetadataData getData() {
        return data;
    }

    public UpdateModelMetadataUniversalContext getUniversalContext() {
        return universalContext;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelMetadata fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelMetadata.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModelMetadata updateModelMetadata) {
        try {
            return Serializer.serializeObject(updateModelMetadata);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
