package com.fieldnation.rpc.client;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

public class PaymentService extends WebService {

	public PaymentService(Context context, String username, String authToken, ResultReceiver callback) {
		super(context, username, authToken, callback);
	}

	public Intent getPending(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode,
				"/api/rest/v1/accounting/payment-queue/pending",
				"?page=" + page, allowCache);
	}

	public Intent getPaid(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode,
				"/api/rest/v1/accounting/payment-queue/paid", "?page=" + page,
				allowCache);
	}

	public Intent getAll(int resultCode, int page, boolean allowCache) {
		return httpGet(resultCode, "/api/rest/v1/accounting/payment-queue/all",
				"?page=" + page, allowCache);
	}
}
