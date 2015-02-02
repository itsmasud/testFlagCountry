package com.fieldnation.ui.workorder;

/**
 * <P>
 * An enum that provides data selection abilities. Used primarily in
 * {@link WorkorderListAdapter}
 * </p>
 *
 * @author michael.carver
 * @see WorkorderListAdapter
 */
public enum WorkorderDataSelector {
    AVAILABLE("available"),
    REQUESTED("requested"),
    ASSIGNED("assigned", true),
    IN_PROGRESS("assigned", true),
    COMPLETED("completed"),
    CANCELED("canceled");

    private String _call;
    private boolean _allowCache = false;

    private WorkorderDataSelector(String call) {
        _call = call;
    }

    private WorkorderDataSelector(String call, boolean allowCache) {
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
        return this == ASSIGNED || this == IN_PROGRESS || this == COMPLETED || this == CANCELED;
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