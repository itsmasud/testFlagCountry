package com.fieldnation.rpc.webclient;

public enum CancelCategory {
    // TODO, make localizable
    OTHER(1, "Other", false),
    COULD_NOT(7, "Could not arrive on site"),
    UNABLE_TO_COMPLETE(9, "Unable to complete scope of work"),
    PROVIDER_UNABLE_SCHEDULE(10, "Provider unable to adhere to schedule"),
    NOT_AS_DESCRIBED(11, "Scope of work not as described"),
    SCOPE_CHANGE_WITHOUT_PAY(12, "Scope of work changed without sufficient pay increase"),
    WRONG_WORK_TYPE(13, "Work type is not a fit");

    private int _id;
    private String _displayName;
    private boolean _reasonOptional;

    private CancelCategory(int id, String displayName) {
        this(id, displayName, true);
    }

    private CancelCategory(int id, String displayName, boolean reasonOptional) {
        _id = id;
        _displayName = displayName;
        _reasonOptional = reasonOptional;
    }

    public int getId() {
        return _id;
    }

    @Override
    public String toString() {
        return _displayName;
    }

    public boolean isReasonOptional() {
        return _reasonOptional;
    }
}
