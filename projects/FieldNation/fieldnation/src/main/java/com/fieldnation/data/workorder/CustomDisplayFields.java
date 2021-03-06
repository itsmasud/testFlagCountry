package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomDisplayFields {
    private static final String TAG = "CustomDisplayFields";

    @Json(name = "label")
    private String _label;
    @Json(name = "value")
    private String _value;

    public CustomDisplayFields() {
    }

    public String getLabel() {
        return _label;
    }

    public String getValue() {
        return _value;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomDisplayFields customDisplayFields) {
        try {
            return Serializer.serializeObject(customDisplayFields);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static CustomDisplayFields fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(CustomDisplayFields.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
