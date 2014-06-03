package com.fieldnation.wsb;

public class WebResult {
	public int code;
	public String strData;
	public byte[] byteData;


	public WebResult(int code, byte[] data) {
		this.code = code;
		byteData = data;
		strData = new String(data);
	}

}
