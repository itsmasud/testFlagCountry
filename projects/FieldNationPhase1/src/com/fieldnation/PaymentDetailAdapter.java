package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.data.payments.Payment;
import com.fieldnation.data.payments.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PaymentDetailAdapter extends BaseAdapter {
	private static final String TAG = "PaymentDetailAdapter";
	private Payment _payment;
	private Workorder[] _workorders;

	public PaymentDetailAdapter(Payment payment) {
		super();
		_payment = payment;
		_workorders = payment.getWorkorders();
	}

	@Override
	public int getCount() {
		return _workorders.length;
	}

	@Override
	public Object getItem(int position) {
		return _workorders[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PaymentWorkorderView view = null;

		if (convertView == null) {
			view = new PaymentWorkorderView(parent.getContext());
		} else if (convertView instanceof PaymentWorkorderView) {
			view = (PaymentWorkorderView) convertView;
		} else {
			view = new PaymentWorkorderView(parent.getContext());
		}

		view.setWorkorder(_payment, _workorders[position]);

		return view;
	}

}
