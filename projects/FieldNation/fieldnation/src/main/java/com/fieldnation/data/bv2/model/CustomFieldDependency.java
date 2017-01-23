package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomFieldDependency {
    private static final String TAG = "CustomFieldDependency";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "value")
    private String value = null;

    @Json(name = "operator")
    private OperatorEnum operator = null;

    public enum OperatorEnum {
        @Json(name = "less_than")
        LESS_THAN("less_than"),
        @Json(name = "greater_than")
        GREATER_THAN("greater_than"),
        @Json(name = "equals")
        EQUALS("equals"),
        @Json(name = "less_than_equals")
        LESS_THAN_EQUALS("less_than_equals"),
        @Json(name = "greater_than_equals")
        GREATER_THAN_EQUALS("greater_than_equals");

        private String value;

        OperatorEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }


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

