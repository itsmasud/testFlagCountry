package com.fieldnation.service.transaction.handlers;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by Michael Carver on 3/10/2015.
 */
public class WorkorderDeclineTransactionHandler extends WebTransactionHandler {

    public static byte[] generateParams(long workorderId) {
        try {
            return new JsonObject("workorderId", workorderId).toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, Bundle resultData) {

    }
}
