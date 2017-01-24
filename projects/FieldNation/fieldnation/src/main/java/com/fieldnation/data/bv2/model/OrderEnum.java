package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

public enum OrderEnum {
    @Json(name = "asc")
    ASC("asc"),
    @Json(name = "desc")
    DESC("desc");

    private String value;

    OrderEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
