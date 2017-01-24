package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

public enum StatusEnum {
    @Json(name = "Lost")
    LOST("Lost"),
    @Json(name = "new")
    NEW("new"),
    /* TODO need to find source
        @Json(name = "New")
        NEW("New"),
    */
    @Json(name = "approved")
    APPROVED("approved"),
    @Json(name = "En Route")
    EN_ROUTE("En Route"),
    @Json(name = "declined")
    DECLINED("declined"),
    @Json(name = "pending")
    PENDING("pending"),
    @Json(name = "Arrived")
    ARRIVED("Arrived"),
    @Json(name = "Error")
    ERROR("Error"),
    @Json(name = "accepted")
    ACCEPTED("accepted"),
    @Json(name = "denied")
    DENIED("denied"),
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
