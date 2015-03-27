package com.fieldnation.service.data.payment;

import android.content.Context;

import com.fieldnation.rpc.server.HttpResult;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionHandler;

/**
 * Created by Michael Carver on 3/27/2015.
 */
public class PaymentTransactionHandler extends WebTransactionHandler implements PaymentConstants {

    @Override
    public void handleResult(Context context, Listener listener, WebTransaction transaction, HttpResult resultData) {

    }
}
