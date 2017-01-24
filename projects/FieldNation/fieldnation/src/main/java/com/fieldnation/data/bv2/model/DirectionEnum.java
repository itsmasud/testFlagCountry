package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

public enum DirectionEnum {
    @Json(name = "to site")
    TO_SITE("to site"),
    @Json(name = "from site")
    FROM_SITE("from site");

    private String value;

    DirectionEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
