package com.fieldnation.data.workorder;

public enum WorkorderStatus {
	AVAILABLE("STATUS_AVAILABLE"),
	ASSIGNED("STATUS_ASSIGNED"),
	INPROGRESS("STATUS_INPROGRESS"),
	COMPLETED("STATUS_COMPLETED"),
	CANCELLED("STATUS_CANCELLED"),
	NA(null);

	private String _value;

	private WorkorderStatus(String value) {
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
