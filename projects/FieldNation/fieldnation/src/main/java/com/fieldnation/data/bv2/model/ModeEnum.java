package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public enum ModeEnum {
    @Json(name = "hours")
    HOURS("hours"),
    @Json(name = "custom")
    CUSTOM("custom"),
    @Json(name = "exact")
    EXACT("exact"),
    @Json(name = "location")
    LOCATION("location"),
    @Json(name = "remote")
    REMOTE("remote"),
    @Json(name = "between")
    BETWEEN("between");

    private String value;

    ModeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
