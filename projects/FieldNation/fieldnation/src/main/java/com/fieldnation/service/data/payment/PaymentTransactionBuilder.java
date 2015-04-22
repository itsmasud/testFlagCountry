package com.fieldnation.service.data.payment;

import android.content.Context;

import com.fieldnation.rpc.server.HttpJsonBuilder;
import com.fieldnation.service.transaction.Priority;
import com.fieldnation.service.transaction.WebTransactionBuilder;

/**
 * Created by Michael Carver on 4/22/2015.
 */
public class PaymentTransactionBuilder implements PaymentConstants {

    public static void getAll(Context context, int page, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(PaymentTransactionHandler.class)
                    .handlerParams(
                            PaymentTransactionHandler.pGetAll(page)
                    )
                    .key((isSync ? "Sync/" : "") + "PaymentGetAll" + page)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path("/api/rest/v1/accounting/payment-queue/all")
                                    .urlParams("?page=" + page)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getPayment(Context context, long paymentId, boolean isSync) {
        try {
            WebTransactionBuilder.builder(context)
                    .priority(Priority.HIGH)
                    .handler(PaymentTransactionHandler.class)
                    .handlerParams(PaymentTransactionHandler.pPayment(paymentId))
                    .key((isSync ? "Sync/" : "") + "GetPayment" + paymentId)
                    .useAuth(true)
                    .isSyncCall(isSync)
                    .request(
                            new HttpJsonBuilder()
                                    .method("GET")
                                    .path("/api/rest/v1/accounting/payment-queue/" + paymentId)
                    ).send();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
