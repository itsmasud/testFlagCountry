package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public enum CalculationEnum {
    @Json(name = "fixed")
    FIXED("fixed"),
    @Json(name = "percent")
    PERCENT("percent");

    private String value;

    CalculationEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
