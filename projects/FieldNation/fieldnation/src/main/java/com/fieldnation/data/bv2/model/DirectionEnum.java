package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public enum DirectionEnum {
    @Json(name = "from_site")
    FROM_SITE("from_site"),
    @Json(name = "to_site")
    TO_SITE("to_site");

    private String value;

    DirectionEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
