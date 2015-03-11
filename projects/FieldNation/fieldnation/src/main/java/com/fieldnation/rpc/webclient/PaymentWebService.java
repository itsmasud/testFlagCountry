package com.fieldnation.rpc.webclient;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

public class PaymentWebService extends WebClient {

    public PaymentWebService(Context context, String username, String authToken, ResultReceiver callback) {
        super(context, username, authToken, callback);
    }

//	public Intent getPending(int resultCode, int page, boolean allowCache) {
//		return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/pending", "?page=" + page, allowCache);
//	}
//
//	public Intent getPaid(int resultCode, int page, boolean allowCache) {
//		return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/paid", "?page=" + page, allowCache);
//	}

    public Intent getAll(int resultCode, int page, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/all", "?page=" + page, allowCache);
    }

    public Intent getPayment(int resultCode, long paymentId, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/" + paymentId, allowCache);
    }
}
