package com.fieldnation.data.workorder;

public enum StatusIntent {
    NORMAL("normal"), // white/0
    WARNING("warning"), // orange/1
    SUCCESS("success"), // green/2
    WAITING("waiting"), // gray/3
    UNKNOWN("unknown");

    private final String _value;

    StatusIntent(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public static StatusIntent fromString(String value) {
        if (value == null) {
            return UNKNOWN;
        }

        StatusIntent[] v = values();
        for (StatusIntent aV : v) {
            if (aV._value.equals(value)) {
                return aV;
            }
        }

        return UNKNOWN;
    }
}
