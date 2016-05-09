package com.fieldnation.data.workorder;

public enum WorkorderStatus {
    AVAILABLE("STATUS_AVAILABLE"), // 2 or 9
    ASSIGNED("STATUS_ASSIGNED"), // 3
    INPROGRESS("STATUS_INPROGRESS"), // 3 + checkedin/out -- assigned
    COMPLETED("STATUS_COMPLETED"), // 4 workdone
    APPROVED("STATUS_APPROVED"), // 5 == completed
    PAID("STATUS_PAID"), // 6 == completed
    CANCELED("STATUS_CANCELLED"), // 7
    NA("NA");

    private String _value;

    WorkorderStatus(String value) {
        _value = value;
    }

    public static WorkorderStatus fromValue(String value) {
        if (value == null)
            return NA;

        WorkorderStatus[] stati = values();
        for (int i = 0; i < stati.length; i++) {
            if (stati[i]._value.equals(value)) {
                return stati[i];
            }
        }
        return NA;
    }

}
