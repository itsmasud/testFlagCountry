package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger.
 */

public enum StatusEnum {
    @Json(name = "new")
    NEW("new"),
    @Json(name = "match")
    MATCH("match"),
    @Json(name = "no_match_optional")
    NO_MATCH_OPTIONAL("no_match_optional"),
    @Json(name = "completed")
    COMPLETED("completed"),
    @Json(name = "failed")
    FAILED("failed"),
    @Json(name = "no_match_required")
    NO_MATCH_REQUIRED("no_match_required"),
    @Json(name = "holding")
    HOLDING("holding"),
    @Json(name = "arrived")
    ARRIVED("arrived"),
    @Json(name = "approved")
    APPROVED("approved"),
    @Json(name = "declined")
    DECLINED("declined"),
    @Json(name = "checked_in")
    CHECKED_IN("checked_in"),
    @Json(name = "checked_out")
    CHECKED_OUT("checked_out"),
    @Json(name = "lost")
    LOST("lost"),
    @Json(name = "pending")
    PENDING("pending"),
    @Json(name = "en_route")
    EN_ROUTE("en_route"),
    @Json(name = "accepted")
    ACCEPTED("accepted"),
    @Json(name = "denied")
    DENIED("denied"),
    @Json(name = "error")
    ERROR("error"),
    @Json(name = "disapproved")
    DISAPPROVED("disapproved");

    private String value;

    StatusEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
