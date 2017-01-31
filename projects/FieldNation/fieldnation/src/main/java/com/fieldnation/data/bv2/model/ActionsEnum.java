package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public enum ActionsEnum {
    @Json(name = "add")
    ADD("add"),
    @Json(name = "charge")
    CHARGE("charge"),
    @Json(name = "edit")
    EDIT("edit"),
    @Json(name = "create")
    CREATE("create"),
    @Json(name = "remove")
    REMOVE("remove"),
    @Json(name = "accept")
    ACCEPT("accept");

    private String value;

    ActionsEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
