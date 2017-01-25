package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomFieldDependency {
    private static final String TAG = "CustomFieldDependency";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "value")
    private String _value;

    @Json(name = "operator")
    private OperatorEnum _operator;

    public CustomFieldDependency() {
    }

    public Integer getId() {
        return _id;
    }

    public String getValue() {
        return _value;
    }

    public OperatorEnum getOperator() {
        return _operator;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomFieldDependency fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomFieldDependency.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomFieldDependency customFieldDependency) {
        try {
            return Serializer.serializeObject(customFieldDependency);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
