package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomFieldDependency {
    private static final String TAG = "CustomFieldDependency";

    @Json(name = "id")
    private Integer id;

    @Json(name = "value")
    private String value;

    @Json(name = "operator")
    private OperatorEnum operator;

    public CustomFieldDependency() {
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public OperatorEnum getOperator() {
        return operator;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomFieldDependency fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomFieldDependency.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
