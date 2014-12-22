package com.fieldnation.rpc.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.fieldnation.topics.Topics;

/**
 * Performs some basic parsing on the response and provides some convenience
 * methods for interacting with the response
 *
 * @author michael.carver
 */
public abstract class WebResultReceiver extends ResultReceiver implements WebServiceConstants {
    private static final String TAG = "rpc.common.WebServiceResultReceiver";

    public WebResultReceiver(Handler handler) {
        super(handler);
    }

    public abstract Context getContext();

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        String errorType = resultData.getString(KEY_RESPONSE_ERROR_TYPE);

        if (ERROR_NONE.equals(errorType)) {
            onSuccess(resultCode, resultData);
        } else {
            if (ERROR_NETWORK_ERROR.equals(errorType)) {
                Topics.dispatchNetworkDown(getContext());
            }
            onError(resultCode, resultData, errorType);
        }
        super.onReceiveResult(resultCode, resultData);
    }

    /**
     * @param resultCode
     * @param resultData
     * @param errorType  Will be one of the following WebServiceConstants.ERROR_*
     */
    public void onError(int resultCode, Bundle resultData, String errorType) {
        Log.v(TAG, "onError[" + resultCode + "] " + errorType);
        Log.v(TAG, resultData.toString());
    }

    public abstract void onSuccess(int resultCode, Bundle resultData);
}
