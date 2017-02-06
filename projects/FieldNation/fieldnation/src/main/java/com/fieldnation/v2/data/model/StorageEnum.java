package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger.
 */

public enum StorageEnum {
    @Json(name = "s3")
    S3("s3");

    private String value;

    StorageEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
