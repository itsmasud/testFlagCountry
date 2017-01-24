package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

public enum ViewEnum {
    @Json(name = "schedule")
    SCHEDULE("schedule"),
    @Json(name = "list")
    LIST("list"),
    @Json(name = "map")
    MAP("map"),
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
