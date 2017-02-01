package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public enum TypeEnum {
    @Json(name = "date")
    DATE("date"),
    @Json(name = "datetime")
    DATETIME("datetime"),
    @Json(name = "file")
    FILE("file"),
    @Json(name = "phone")
    PHONE("phone"),
    @Json(name = "document")
    DOCUMENT("document"),
    @Json(name = "link")
    LINK("link"),
    @Json(name = "numeric")
    NUMERIC("numeric"),
    @Json(name = "slot")
    SLOT("slot"),
    @Json(name = "text")
    TEXT("text"),
    @Json(name = "time")
    TIME("time"),
    @Json(name = "predefined")
    PREDEFINED("predefined");

    private String value;

    TypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
