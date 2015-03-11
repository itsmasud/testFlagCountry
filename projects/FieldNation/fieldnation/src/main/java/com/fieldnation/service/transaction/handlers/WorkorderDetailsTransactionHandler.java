package com.fieldnation.service.transaction.handlers;

import android.content.Context;
import android.os.Bundle;

import com.fieldnation.service.transaction.WebTransaction;

/**
 * Created by Michael Carver on 3/6/2015.
 */
public class WorkorderDetailsTransactionHandler extends WebTransactionHandler {

    public static byte[] generateParams() {
        return null;
    }

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, Bundle resultData) {

    }
}
