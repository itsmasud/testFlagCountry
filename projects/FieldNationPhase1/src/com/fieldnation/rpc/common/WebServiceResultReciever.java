package com.fieldnation.rpc.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

public abstract class WebServiceResultReciever extends ResultReceiver implements WebServiceConstants {

	public WebServiceResultReciever(Handler handler) {
		super(handler);
	}

	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData) {
		String errorType = resultData.getString(KEY_RESPONSE_ERROR_TYPE);

		if (ERROR_NONE.equals(errorType))
			onSuccess(resultCode, resultData);
		else
			onError(resultCode, resultData, errorType);

		super.onReceiveResult(resultCode, resultData);
	}

	public abstract void onError(int resultCode, Bundle resultData,
			String errorType);

	public abstract void onSuccess(int resultCode, Bundle resultData);
}
