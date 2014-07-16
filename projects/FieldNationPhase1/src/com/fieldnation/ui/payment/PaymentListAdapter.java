package com.fieldnation.ui.payment;

import android.app.Activity;
import android.content.Context;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.data.payments.Payment;
import com.fieldnation.rpc.client.PaymentService;
import com.fieldnation.ui.PagingListAdapter;

public class PaymentListAdapter extends PagingListAdapter<Payment> {
	private static final String TAG = "PaymentListAdapter";

	private PaymentService _service = null;

	public PaymentListAdapter(Activity activity) {
		super(activity, Payment.class);
	}

	@Override
	public View getView(Payment obj, View convertView, ViewGroup parent) {
		PaymentSummaryView view = null;

		if (convertView == null) {
			view = new PaymentSummaryView(getContext());
		} else if (convertView instanceof PaymentSummaryView) {
			view = (PaymentSummaryView) convertView;
		} else {
			view = new PaymentSummaryView(getContext());
		}

		view.setData(obj);

		return view;
	}

	@Override
	public void invalidateWebervice() {
		_service = null;
	}

	@Override
	public void getWebService(Context context, String username, String authToken, ResultReceiver resultReceiver) {
		if (_service == null) {
			_service = new PaymentService(context, username, authToken, resultReceiver);
		}
	}

	@Override
	public void rebuildWebService(Context context, String username, String authToken, ResultReceiver resultReceiver) {
		_service = new PaymentService(context, username, authToken, resultReceiver);
	}

	@Override
	public void executeWebService(int resultCode, int page, boolean allowCache) {
		getContext().startService(_service.getAll(resultCode, page, allowCache));
	}
}
