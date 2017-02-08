package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger.
 */

public enum ActionsEnum {
    // Note, the order matters! This order determines action priority!
    @Json(name = "accept")
    ACCEPT("accept"),
    @Json(name = "request")
    REQUEST("request"),
    @Json(name = "withdraw_request")
    WITHDRAW_REQUEST("withdraw_request"),
    @Json(name = "mark_complete")
    MARK_COMPLETE("mark_complete"),
    @Json(name = "mark_incomplete")
    MARK_INCOMPLETE("mark_incomplete"),
    @Json(name = "eta")
    ETA("eta"),
    @Json(name = "confirm")
    CONFIRM("confirm"),
    @Json(name = "acknowledge")
    ACKNOWLEDGE("acknowledge"),
    @Json(name = "check_in")
    CHECK_IN("check_in"),
    @Json(name = "ready_to_go")
    READY_TO_GO("ready_to_go"),
    @Json(name = "delete")
    DELETE("delete"),
    @Json(name = "unknown")
    UNKNOWN("unknown"),
    @Json(name = "check_out")
    CHECK_OUT("check_out"),
    @Json(name = "report_a_problem")
    REPORT_A_PROBLEM("report_a_problem"),
    @Json(name = "messaging")
    MESSAGING("messaging"),

    @Json(name = "add")
    ADD("add"),
    @Json(name = "charge")
    CHARGE("charge"),
    @Json(name = "create")
    CREATE("create"),
    @Json(name = "edit")
    EDIT("edit"),
    @Json(name = "remove")
    REMOVE("remove"),
    @Json(name = "view_problem")
    VIEW_PROBLEM("view_problem");

    private String value;

    ActionsEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
