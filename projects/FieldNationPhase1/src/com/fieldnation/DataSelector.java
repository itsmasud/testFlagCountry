package com.fieldnation;

import java.util.HashSet;
import java.util.Set;

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
public enum DataSelector {
	AVAILABLE("getAvailable"),
	ASSIGNED("getAssigned"),
	IN_PROGRESS("getAssigned"),
	COMPLETED("getCompleted"),
	CANCELLED("getCanceled");

	private String _call;

	private DataSelector(String call) {
		_call = call;
	}

	public String getCall() {
		return _call;
	}

	public static DataSelector fromName(String name) {
		DataSelector[] vs = values();
		for (int i = 0; i < vs.length; i++) {
			if (vs[i].name().equals(name)) {
				return vs[i];
			}
		}
		return null;
	}
}