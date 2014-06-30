package com.fieldnation.json;

public class UnsupportedDataTypeException extends Exception {
	public UnsupportedDataTypeException(String msg) {
		super(msg);
	}

	public UnsupportedDataTypeException(Throwable th) {
		super(th);
	}

	public UnsupportedDataTypeException(String msg, Throwable th) {
		super(msg, th);
	}
}
