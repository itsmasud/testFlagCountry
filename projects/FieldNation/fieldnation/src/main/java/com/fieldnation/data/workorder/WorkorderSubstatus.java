package com.fieldnation.data.workorder;

public enum WorkorderSubstatus {
    AVAILABLE("SUBSTATUS_AVAILABLE"),
    ROUTED("SUBSTATUS_ROUTED"),
    REQUESTED("SUBSTATUS_REQUESTED"),
    COUNTEROFFERED("SUBSTATUS_COUNTEROFFERED"),
    UNCONFIRMED("SUBSTATUS_UNCONFIRMED"),
    CONFIRMED("SUBSTATUS_CONFIRMED"),
    ONHOLD_UNACKNOWLEDGED("SUBSTATUS_ONHOLD_UNACKNOWLEDGED"),
    ONHOLD_ACKNOWLEDGED("SUBSTATUS_ONHOLD_ACKNOWLEDGED"),
    CHECKEDIN("SUBSTATUS_CHECKEDIN"),
    CHECKEDOUT("SUBSTATUS_CHECKEDOUT"),
    PENDINGREVIEWED("SUBSTATUS_PENDINGREVIEW"),
    INREVIEW("SUBSTATUS_INREVIEW"),
    APPROVED_PROCESSINGPAYMENT("SUBSTATUS_APPROVED_PROCESSINGPAYMENT"),
    PAID("SUBSTATUS_PAID"),
    CANCELLED("SUBSTATUS_CANCELLED"),
    CANCELLED_LATEFEEPROCESSING("SUBSTATUS_CANCELLED_LATEFEEPROCESSING"),
    CANCELLED_LATEFEEPAID("SUBSTATUS_CANCELLED_LATEFEEPAID"),
    NA("NA");

    private String _value;

    private WorkorderSubstatus(String value) {
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
