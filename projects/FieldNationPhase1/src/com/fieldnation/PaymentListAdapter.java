package com.fieldnation;

import android.content.Context;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.PaymentService;
import com.fieldnation.rpc.client.ProfileService;

public class PaymentListAdapter extends PagingListAdapter {
	private static final String TAG = "PaymentListAdapter";

	private PaymentService _service = null;

	public PaymentListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(JsonObject obj, View convertView, ViewGroup parent) {
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
	public void getWebService(Context context, String username,
			String authToken, ResultReceiver resultReceiver) {
		if (_service == null) {
			_service = new PaymentService(context, username, authToken,
					resultReceiver);
		}
	}

	@Override
	public void rebuildWebService(Context context, String username,
			String authToken, ResultReceiver resultReceiver) {
		_service = new PaymentService(context, username, authToken,
				resultReceiver);
	}

	@Override
	public void executeWebService(int resultCode, int page, boolean allowCache) {
		// TODO replace with getAll when we have the rest call
		// getContext().startService(
		// _service.getPending(resultCode, page + 1, allowCache));
		getContext().startService(
				_service.getPaid(resultCode, page + 1, allowCache));
	}
}
