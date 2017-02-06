package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger.
 */

public enum NameEnum {
    @Json(name = "insurance")
    INSURANCE("insurance"),
    @Json(name = "unconfirmed")
    UNCONFIRMED("unconfirmed"),
    @Json(name = "ratings_positive")
    RATINGS_POSITIVE("ratings_positive"),
    @Json(name = "skillset")
    SKILLSET("skillset"),
    @Json(name = "assignment_history")
    ASSIGNMENT_HISTORY("assignment_history"),
    @Json(name = "distance")
    DISTANCE("distance"),
    @Json(name = "onmyway")
    ONMYWAY("onmyway"),
    @Json(name = "requests")
    REQUESTS("requests"),
    @Json(name = "confirmed")
    CONFIRMED("confirmed"),
    @Json(name = "background_check")
    BACKGROUND_CHECK("background_check"),
    @Json(name = "cancel_rate")
    CANCEL_RATE("cancel_rate"),
    @Json(name = "assignment_nearby")
    ASSIGNMENT_NEARBY("assignment_nearby"),
    @Json(name = "readytogo")
    READYTOGO("readytogo"),
    @Json(name = "completed_wo_marketplace")
    COMPLETED_WO_MARKETPLACE("completed_wo_marketplace"),
    @Json(name = "block")
    BLOCK("block"),
    @Json(name = "has_verified")
    HAS_VERIFIED("has_verified"),
    @Json(name = "protec")
    PROTEC("protec"),
    @Json(name = "custom_provider_field")
    CUSTOM_PROVIDER_FIELD("custom_provider_field"),
    @Json(name = "assignment_less_then")
    ASSIGNMENT_LESS_THEN("assignment_less_then"),
    @Json(name = "ratings_all")
    RATINGS_ALL("ratings_all"),
    @Json(name = "custom_field_match")
    CUSTOM_FIELD_MATCH("custom_field_match"),
    @Json(name = "completed_wo_company")
    COMPLETED_WO_COMPANY("completed_wo_company"),
    @Json(name = "preferred_provider")
    PREFERRED_PROVIDER("preferred_provider"),
    @Json(name = "custom_buyer_field")
    CUSTOM_BUYER_FIELD("custom_buyer_field"),
    @Json(name = "drug_test")
    DRUG_TEST("drug_test");

    private String value;

    NameEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
