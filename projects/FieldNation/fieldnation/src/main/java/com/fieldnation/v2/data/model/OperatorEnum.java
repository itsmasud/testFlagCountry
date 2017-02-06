package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger.
 */

public enum OperatorEnum {
    @Json(name = "less_than_equals")
    LESS_THAN_EQUALS("less_than_equals"),
    @Json(name = "greater_than")
    GREATER_THAN("greater_than"),
    @Json(name = "greater_than_equals")
    GREATER_THAN_EQUALS("greater_than_equals"),
    @Json(name = "less_than")
    LESS_THAN("less_than"),
    @Json(name = "equals")
    EQUALS("equals");

    private String value;

    OperatorEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
