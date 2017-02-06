package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 2/06/17.
 */

public enum OperationEnum {
    @Json(name = "not_equal_to")
    NOT_EQUAL_TO("not_equal_to"),
    @Json(name = "equal_to")
    EQUAL_TO("equal_to"),
    @Json(name = "greater_than")
    GREATER_THAN("greater_than"),
    @Json(name = "less_than")
    LESS_THAN("less_than");

    private String value;

    OperationEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
