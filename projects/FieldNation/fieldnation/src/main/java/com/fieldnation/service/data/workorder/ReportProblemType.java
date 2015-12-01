package com.fieldnation.service.data.workorder;

/**
 * Created by Michael Carver on 11/25/2015.
 */
public enum ReportProblemType {
    CANNOT_MAKE_ASSIGNMENT("CANNOT_MAKE_ASSIGNMENT"),
    WILL_BE_LATE("WILL_BE_LATE"),
    BUYER_UNRESPONSIVE("BUYER_UNRESPONSIVE"),
    SCOPE_OF_WORK("SCOPE_OF_WORK"),
    OTHER("OTHER"),
    DO_NOT_HAVE_SHIPMENT("DO_NOT_HAVE_SHIPMENT"),
    DO_NOT_HAVE_INFO("DO_NOT_HAVE_INFO"),
    DO_NOT_HAVE_RESPONSE("DO_NOT_HAVE_RESPONSE"),
    DO_NOT_HAVE_OTHER("DO_NOT_HAVE_OTHER"),
    APPROVAL_DISAGREEMENT("APPROVAL_DISAGREEMENT"),
    APPROVAL_NOT_YET("APPROVAL_NOT_YET"),
    PAYMENT_NOT_ACCURATE("PAYMENT_NOT_ACCURATE"),
    PAYMENT_NOT_RECEIVED("PAYMENT_NOT_RECEIVED"),
    SITE_NOT_READY_CONTACT("SITE_NOT_READY_CONTACT"),
    SITE_NOT_READY_PRIOR_WORK("SITE_NOT_READY_PRIOR_WORK"),
    SITE_NOT_READY_ACCESS("SITE_NOT_READY_ACCESS"),
    SITE_NOT_READY_OTHER("SITE_NOT_READY_OTHER");

    public String value;

    ReportProblemType(String value) {
        this.value = value;
    }
}
