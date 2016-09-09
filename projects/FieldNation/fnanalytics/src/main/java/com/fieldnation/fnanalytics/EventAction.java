package com.fieldnation.fnanalytics;

/**
 * Created by Michael on 9/9/2016.
 */
public enum EventAction {
    CHECK_IN("Check In"),
    CHECK_IN_AGAIN("Check In Again"),
    CHECK_OUT("Check Out"),
    VIEW_COUNTER_OFFER("View Counter Offer"),
    COUNTER_OFFER("Counter Offer"),
    CLOSING_NOTE("Closing Note"),
    CONFIRM("Confirm"),
    ACKNOWLEDGE_HOLD("Acknowledge Hold"),
    MARK_COMPLETE("Mark Complete"),
    MARK_INCOMPLETE("Mark Incomplete"),
    ACCEPT("Accept"),
    REQUEST("Request"),
    NOT_INTERESTED("Not Interested"),
    READY_TO_GO("Ready To Go"),
    REPORT_PROBLEM("Report Problem"),
    WITHDRAW("Withdraw"),
    VIEW_PAYMENT("View Payment"),
    SHOW_MAP("Show Map");

    private String value;

    EventAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
