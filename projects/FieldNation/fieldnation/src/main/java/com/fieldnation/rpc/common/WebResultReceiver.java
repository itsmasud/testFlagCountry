package com.fieldnation.rpc.common;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.fieldnation.Log;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.utils.Stopwatch;

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
            Stopwatch stopwatch = new Stopwatch(true);
            onSuccess(resultCode, resultData);
            Log.v(TAG, "onSuccess time: " + stopwatch.finish());
        } else {
            if (ERROR_NETWORK_ERROR.equals(errorType)) {
                Topics.dispatchNetworkDown(getContext());
            } else if (ERROR_SESSION_INVALID.equals(errorType)) {
                AuthTopicService.requestAuthInvalid(getContext());
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
        try {
            Log.v(TAG, new String(resultData.getByteArray(KEY_RESPONSE_DATA)));
        } catch (Exception ex) {
        }
    }

    public abstract void onSuccess(int resultCode, Bundle resultData);
}
