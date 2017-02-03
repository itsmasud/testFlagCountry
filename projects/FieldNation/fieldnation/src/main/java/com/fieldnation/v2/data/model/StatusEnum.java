package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public enum StatusEnum {
    @Json(name = "new")
    NEW("new"),
    @Json(name = "arrived")
    ARRIVED("arrived"),
    @Json(name = "approved")
    APPROVED("approved"),
    @Json(name = "checked_in")
    CHECKED_IN("checked_in"),
    @Json(name = "checked_out")
    CHECKED_OUT("checked_out"),
    @Json(name = "declined")
    DECLINED("declined"),
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
