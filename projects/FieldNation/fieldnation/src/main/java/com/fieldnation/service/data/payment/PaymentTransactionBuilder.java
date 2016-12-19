package com.fieldnation.service.data.payment;

import android.content.Context;

import com.fieldnation.fnlog.Log;
import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionService;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class PaymentTransactionBuilder implements PaymentConstants {
    private static final String TAG = "PaymentTransactionBuilder";

    public static void list(Context context, int page, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/accounting/payment-queue/all")
                    .priority(Priority.HIGH)
                    .listener(PaymentTransactionListener.class)
                    .listenerParams(
                            PaymentTransactionListener.pList(page)
                    )
                    .key((isSync ? "Sync/" : "") + "PaymentPage" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/accounting/payment-queue/all")
                                    .urlParams("?page=" + page)
                    ).build();
            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    public static void get(Context context, long paymentId, boolean isSync) {
        try {
            WebTransaction transaction = new WebTransaction.Builder()
                    .timingKey("GET/api/rest/v1/accounting/payment-queue/[paymentId]")
                    .priority(Priority.HIGH)
                    .listener(PaymentTransactionListener.class)
                    .listenerParams(PaymentTransactionListener.pGet(paymentId))
                    .key((isSync ? "Sync/" : "") + "Payment/" + paymentId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/accounting/payment-queue/" + paymentId)
                    ).build();
            WebTransactionService.queueTransaction(context, transaction);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

    }
}
