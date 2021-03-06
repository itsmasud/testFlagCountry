package com.fieldnation.ui.workorder;

/**
 * <P>
 * An enum that provides data selection abilities. Used primarily in
 * </p>
 *
 * @author michael.carver
 */
public enum WorkorderDataSelector {
    AVAILABLE("available"),
    REQUESTED("requested"),
    ASSIGNED("assigned", true),
    COMPLETED("completed"),
    CANCELED("canceled"),
    ROUTED("available");

    private final String _call;
    private boolean _allowCache = false;

    WorkorderDataSelector(String call) {
        _call = call;
    }

    WorkorderDataSelector(String call, boolean allowCache) {
        _call = call;
        _allowCache = allowCache;
    }

    public String getCall() {
        return _call;
    }

    public boolean allowCache() {
        return _allowCache;
    }

    public boolean shouldShowGoToMarketplace() {
        return this == ASSIGNED || this == COMPLETED || this == CANCELED;
    }

    public static WorkorderDataSelector fromName(String name) {
        WorkorderDataSelector[] vs = values();
        for (int i = 0; i < vs.length; i++) {
            if (vs[i].name().equals(name)) {
                return vs[i];
            }
        }
        return null;
    }
}