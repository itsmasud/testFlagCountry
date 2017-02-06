package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger.
 */

public enum TypeEnum {
    @Json(name = "date")
    DATE("date"),
    @Json(name = "discover")
    DISCOVER("discover"),
    @Json(name = "loan")
    LOAN("loan"),
    @Json(name = "american express")
    AMERICAN_EXPRESS("american express"),
    @Json(name = "insurance_fee")
    INSURANCE_FEE("insurance_fee"),
    @Json(name = "document")
    DOCUMENT("document"),
    @Json(name = "service_fee")
    SERVICE_FEE("service_fee"),
    @Json(name = "link")
    LINK("link"),
    @Json(name = "jcb")
    JCB("jcb"),
    @Json(name = "mastercard")
    MASTERCARD("mastercard"),
    @Json(name = "numeric")
    NUMERIC("numeric"),
    @Json(name = "withdrawal")
    WITHDRAWAL("withdrawal"),
    @Json(name = "slot")
    SLOT("slot"),
    @Json(name = "predefined")
    PREDEFINED("predefined"),
    @Json(name = "diners club")
    DINERS_CLUB("diners club"),
    @Json(name = "datetime")
    DATETIME("datetime"),
    @Json(name = "file")
    FILE("file"),
    @Json(name = "phone")
    PHONE("phone"),
    @Json(name = "visa")
    VISA("visa"),
    @Json(name = "ccauth")
    CCAUTH("ccauth"),
    @Json(name = "deposit")
    DEPOSIT("deposit"),
    @Json(name = "payment")
    PAYMENT("payment"),
    @Json(name = "text")
    TEXT("text"),
    @Json(name = "time")
    TIME("time");

    private String value;

    TypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
