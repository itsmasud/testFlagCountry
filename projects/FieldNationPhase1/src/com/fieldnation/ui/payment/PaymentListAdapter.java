package com.fieldnation.ui.payment;

import android.app.Activity;
import android.content.Context;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.data.accounting.Payment;
import com.fieldnation.rpc.client.PaymentService;
import com.fieldnation.ui.PagingListAdapter;

public class PaymentListAdapter extends PagingListAdapter<Payment> {
	private static final String TAG = "ui.payment.PaymentListAdapter";

	private PaymentService _service = null;

	public PaymentListAdapter(FragmentActivity activity) {
		super(activity, Payment.class);
	}

	@Override
	public View getView(int position, Payment obj, View convertView, ViewGroup parent) {
		PaymentCardView view = null;

		if (convertView == null) {
			view = new PaymentCardView(getContext());
		} else if (convertView instanceof PaymentCardView) {
			view = (PaymentCardView) convertView;
		} else {
			view = new PaymentCardView(getContext());
		}

		view.setData(obj);

		return view;
	}

	@Override
	public void invalidateWebService() {
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
