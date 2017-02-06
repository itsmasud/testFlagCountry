package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger.
 */

public enum RoleEnum {
    @Json(name = "assigned_provider")
    ASSIGNED_PROVIDER("assigned_provider"),
    @Json(name = "buyer")
    BUYER("buyer");

    private String value;

    RoleEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
