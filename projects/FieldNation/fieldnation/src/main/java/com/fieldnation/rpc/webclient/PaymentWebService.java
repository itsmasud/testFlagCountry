package com.fieldnation.rpc.webclient;

import android.content.Context;
import android.os.ResultReceiver;

import com.fieldnation.service.auth.OAuth;

class PaymentWebService extends WebClientAuth {

    public PaymentWebService(Context context, OAuth auth, ResultReceiver callback) {
        super(context, auth, callback);
    }

//	public Intent getPending(int resultCode, int page, boolean allowCache) {
//		return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/pending", "?page=" + page, allowCache);
//	}
//
//	public Intent getPaid(int resultCode, int page, boolean allowCache) {
//		return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/paid", "?page=" + page, allowCache);
//	}

/*
    public Intent getAll(int resultCode, int page, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/all", "?page=" + page, allowCache);
    }

    public Intent getPayment(int resultCode, long paymentId, boolean allowCache) {
        return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/" + paymentId, allowCache);
    }
*/
}
