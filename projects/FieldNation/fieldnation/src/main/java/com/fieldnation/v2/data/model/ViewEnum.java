package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger.
 */

public enum ViewEnum {
    @Json(name = "schedule")
    SCHEDULE("schedule"),
    @Json(name = "list")
    LIST("list"),
    @Json(name = "map")
    MAP("map"),
    @Json(name = "model")
    MODEL("model"),
    @Json(name = "card")
    CARD("card");

    private String value;

    ViewEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
