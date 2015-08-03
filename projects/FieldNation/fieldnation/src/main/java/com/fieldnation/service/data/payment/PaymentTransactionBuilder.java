package com.fieldnation.service.data.payment;

import android.content.Context;

import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class PaymentTransactionBuilder implements PaymentConstants {

    public static void list(Context context, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(PaymentTransactionHandler.class)
                    .handlerParams(
                            PaymentTransactionHandler.pList(page)
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
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void get(Context context, long paymentId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(PaymentTransactionHandler.class)
                    .handlerParams(PaymentTransactionHandler.pGet(paymentId))
                    .key((isSync ? "Sync/" : "") + "Payment/" + paymentId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .protocol("https")
                                    .method("GET")
                                    .path("/api/rest/v1/accounting/payment-queue/" + paymentId)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
