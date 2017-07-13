package com.fieldnation.ui.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.data.accounting.Fee;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.accounting.Workorder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PaymentDetailAdapter extends BaseAdapter {
    private static final String TAG = "PaymentDetailAdapter";
    private static final String HEADER = "HEADER";
    private final Payment _payment;
    private List<Object> _objects = new LinkedList<>();


    public PaymentDetailAdapter(Payment payment) {
        super();
        _payment = payment;
        _objects.add(HEADER);
        Collections.addAll(_objects, payment.getWorkorders());
        Collections.addAll(_objects, payment.getFees());
    }

    @Override
    public int getCount() {
        return _objects.size();
    }

    @Override
    public Object getItem(int position) {
        return _objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object item = getItem(position);

        if (item == HEADER) {
            PaymentDetailHeader header;

            if (convertView == null) {
                header = new PaymentDetailHeader(parent.getContext());
            } else if (convertView instanceof PaymentDetailHeader) {
                header = (PaymentDetailHeader) convertView;
            } else {
                header = new PaymentDetailHeader(parent.getContext());
            }

            header.setPayment(_payment);

            return header;
        } else if (item instanceof Fee) {
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
        } else if (item instanceof Workorder) {
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
        return null;
    }
}