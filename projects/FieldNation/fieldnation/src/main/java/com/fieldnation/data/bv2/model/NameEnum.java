package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

public enum NameEnum {
    @Json(name = "unconfirmed")
    UNCONFIRMED("unconfirmed"),
    @Json(name = "onmyway")
    ONMYWAY("onmyway"),
    @Json(name = "readytogo")
    READYTOGO("readytogo"),
    @Json(name = "confirmed")
    CONFIRMED("confirmed");

    private String value;

    NameEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
