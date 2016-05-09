package com.fieldnation.data.workorder;

public enum WorkorderSubstatus {
    AVAILABLE("SUBSTATUS_AVAILABLE"), // 2
    ROUTED("SUBSTATUS_ROUTED"), // 2 + routed or 9 + routed
    REQUESTED("SUBSTATUS_REQUESTED"), // not routed, not in available
    COUNTEROFFERED("SUBSTATUS_COUNTEROFFERED"), // SUBSTATUS_REQUESTED + countered
    UNCONFIRMED("SUBSTATUS_UNCONFIRMED"), // 3
    CONFIRMED("SUBSTATUS_CONFIRMED"), // 3 + confirmed
    ONHOLD_UNACKNOWLEDGED("SUBSTATUS_ONHOLD_UNACKNOWLEDGED"), // 3
    ONHOLD_ACKNOWLEDGED("SUBSTATUS_ONHOLD_ACKNOWLEDGED"), // 3
    CHECKEDIN("SUBSTATUS_CHECKEDIN"), // 3 + checked in
    CHECKEDOUT("SUBSTATUS_CHECKEDOUT"), // 3 + checked out

    PENDINGREVIEW("SUBSTATUS_PENDINGREVIEW"), // 4
    INREVIEW("SUBSTATUS_INREVIEW"), // not a thing?
    APPROVED_PROCESSINGPAYMENT("SUBSTATUS_APPROVED_PROCESSINGPAYMENT"), // 5
    PAID("SUBSTATUS_PAID"), // 6
    CANCELED("SUBSTATUS_CANCELLED"), // 7
    CANCELED_LATEFEEPROCESSING("SUBSTATUS_CANCELLED_LATEFEEPROCESSING"), // 7 + has fees not paid
    CANCELED_LATEFEEPAID("SUBSTATUS_CANCELLED_LATEFEEPAID"), // 7 + has feed, paid
    NA("NA");

    private String _value;

    WorkorderSubstatus(String value) {
        _value = value;

    }

    public static WorkorderSubstatus fromValue(String value) {
        if (value == null)
            return NA;

        WorkorderSubstatus[] stati = values();
        for (int i = 0; i < stati.length; i++) {
            if (stati[i]._value.equals(value)) {
                return stati[i];
            }
        }
        return NA;
    }

}
