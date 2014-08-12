package com.fieldnation.data.workorder;

public enum StatusIntent {
	NORMAL("normal"), // white/0
	WARNING("warning"), // orange/1
	SUCCESS("success"), // green/2
	WAITING("waiting"), // gray/3
	UNKNOWN("unknown");

	private String _value;

	private StatusIntent(String value) {
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
		for (int i = 0; i < v.length; i++) {
			if (v[i]._value.equals(value)) {
				return v[i];
			}
		}

		return UNKNOWN;
	}
}
