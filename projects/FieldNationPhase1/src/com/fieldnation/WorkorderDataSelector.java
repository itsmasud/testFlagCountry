package com.fieldnation;

/**
 * <P>
 * An enum that provides data selection abilities. Used primarily in
 * {@link WorkorderListAdapter}
 * </p>
 * 
 * @see WorkorderListAdapter
 * @author michael.carver
 * 
 */
public enum WorkorderDataSelector {
	AVAILABLE("getAvailable"), ASSIGNED("getAssigned", true), IN_PROGRESS("getAssigned", true), COMPLETED(
			"getCompleted"), CANCELLED("getCanceled");

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