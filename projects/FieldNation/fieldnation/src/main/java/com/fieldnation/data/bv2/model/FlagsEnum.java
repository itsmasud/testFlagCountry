package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public enum FlagsEnum {
    @Json(name = "included_in_alerts")
    INCLUDED_IN_ALERTS("included_in_alerts"),
    @Json(name = "visible_to_clients")
    VISIBLE_TO_CLIENTS("visible_to_clients"),
    @Json(name = "shown_in_header")
    SHOWN_IN_HEADER("shown_in_header"),
    @Json(name = "required_during_checkin")
    REQUIRED_DURING_CHECKIN("required_during_checkin"),
    @Json(name = "seen_by_clients")
    SEEN_BY_CLIENTS("seen_by_clients"),
    @Json(name = "internal_id")
    INTERNAL_ID("internal_id"),
    @Json(name = "client_request_required")
    CLIENT_REQUEST_REQUIRED("client_request_required"),
    @Json(name = "unique")
    UNIQUE("unique"),
    @Json(name = "client_request_use_for")
    CLIENT_REQUEST_USE_FOR("client_request_use_for"),
    @Json(name = "seen_by_provider")
    SEEN_BY_PROVIDER("seen_by_provider"),
    @Json(name = "required")
    REQUIRED("required");

    private String value;

    FlagsEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
