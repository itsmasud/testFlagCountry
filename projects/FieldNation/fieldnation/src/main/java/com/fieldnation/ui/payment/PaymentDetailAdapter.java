package com.fieldnation.ui.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.data.accounting.Fee;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.accounting.Workorder;

public class PaymentDetailAdapter extends BaseAdapter {
    private static final String TAG = "PaymentDetailAdapter";
    private final Payment _payment;
    private final Workorder[] _workorders;
    private final Fee[] _fees;

    public PaymentDetailAdapter(Payment payment) {
        super();
        _payment = payment;
        _workorders = payment.getWorkorders();
        _fees = payment.getFees();
    }

    @Override
    public int getCount() {
        return _workorders.length + _fees.length;
    }

    @Override
    public Object getItem(int position) {
        if (position < _workorders.length) {
            return _workorders[position];
        }
        return _fees[position - _workorders.length];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = getItem(position);

        if (item instanceof Fee) {
            PaymentFeeView view = null;
            Fee fee = (Fee) item;

            if (convertView == null) {
                view = new PaymentFeeView(parent.getContext());
            } else if (convertView instanceof PaymentFeeView) {
                view = (PaymentFeeView) convertView;
            } else {
                view = new PaymentFeeView(parent.getContext());
            }

            view.setWorkorder(_payment, fee);

            return view;
        } else {
            PaymentWorkorderView view = null;
            Workorder wo = (Workorder) item;

            if (convertView == null) {
                view = new PaymentWorkorderView(parent.getContext());
            } else if (convertView instanceof PaymentWorkorderView) {
                view = (PaymentWorkorderView) convertView;
            } else {
                view = new PaymentWorkorderView(parent.getContext());
            }

            view.setWorkorder(_payment, wo);

            return view;
        }

    }

}
