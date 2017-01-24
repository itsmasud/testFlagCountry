package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomFields {
    private static final String TAG = "CustomFields";

    @Json(name = "metadata")
    private ListEnvelope metadata;

    @Json(name = "results")
    private CustomFieldCategory[] results;

    public CustomFields() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public CustomFieldCategory[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomFields fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomFields.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomFields customFields) {
        try {
            return Serializer.serializeObject(customFields);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
