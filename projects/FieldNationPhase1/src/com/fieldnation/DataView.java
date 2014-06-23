package com.fieldnation;

import java.util.HashSet;
import java.util.Set;

// available method calls
public enum DataView {
	AVAILABLE("getAvailable", new int[] { 11, 12, 13 }),
	ASSIGNED("getAssigned", new int[] { 17, 18, 16 }),
	IN_PROGRESS("getAssigned", new int[] { 1, 2, 16, 17, 18 }),
	COMPLETED("getCompleted", new int[] { 19, 20, 21 }),
	CANCELLED("getCanceled", new int[] { 0 });

	private String _call;
	private Set<Integer> _labelTable;

	private DataView(String call, int[] labelIds) {
		_call = call;
		_labelTable = new HashSet<Integer>();

		for (int i = 0; i < labelIds.length; i++) {
			_labelTable.add(labelIds[i]);
		}
	}

	public String getCall() {
		return _call;
	}

	public boolean hasLabel(int labelId) {
		return _labelTable.contains(labelId);
	}

	public int getStatusBg() {
		return 1;
	}

	public static DataView fromName(String name) {
		DataView[] vs = values();
		for (int i = 0; i < vs.length; i++) {
			if (vs[i].name().equals(name)) {
				return vs[i];
			}
		}
		return null;
	}

}