package com.fieldnation.v2.data.model;

import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public enum ActionsEnum {
    @Json(name = "accept")
    ACCEPT("accept"),
    @Json(name = "acknowledge")
    ACKNOWLEDGE("acknowledge"),
    @Json(name = "add")
    ADD("add"),
    @Json(name = "charge")
    CHARGE("charge"),
    @Json(name = "check_in")
    CHECK_IN("check_in"),
    @Json(name = "check_out")
    HECK_OUT("check_out"),
    @Json(name = "create")
    CREATE("create"),
    @Json(name = "edit")
    EDIT("edit"),
    @Json(name = "eta")
    ETA("eta"),
    @Json(name = "mark_complete")
    MARK_COMPLETE("mark_complete"),
    @Json(name = "mark_incomplete")
    MARK_INCOMPLETE("mark_incomplete"),
    @Json(name = "messaging")
    MESSAGING("messaging"),
    @Json(name = "remove")
    REMOVE("remove"),
    @Json(name = "report_a_problem")
    REPORT_A_PROBLEM("report_a_problem"),
    @Json(name = "request")
    REQUEST("request"),
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
