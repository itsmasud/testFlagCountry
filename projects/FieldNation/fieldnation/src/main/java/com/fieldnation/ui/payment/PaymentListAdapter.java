package com.fieldnation.ui.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.Log;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.utils.Stopwatch;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 2/17/2016.
 */
class PaymentListAdapter extends BaseAdapter {
    private final static String TAG = "PaymentListAdapter";

    private List<Object> _items = new LinkedList<>();
    private Listener _listener;

    public PaymentListAdapter(List<Object> items, Listener listener) {
        _items = items;
        _listener = listener;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public Object getItem(int position) {
        if (position == (getCount() * 2) / 3 && _listener != null) {
            _listener.onNextPage();
        }
        return _items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = getItem(position);
        if (object instanceof Placeholder)
            return 0;
        else if (object instanceof MonthHeaderView.Header)
            return 1;
        else if (object instanceof Payment)
            return 2;

        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object object = getItem(position);

        if (object instanceof Placeholder) {
            NoPaymentsTileView v = null;

            if (convertView == null)
                v = new NoPaymentsTileView(parent.getContext());
            else if (convertView instanceof NoPaymentsTileView)
                v = (NoPaymentsTileView) convertView;
            else
                v = new NoPaymentsTileView(parent.getContext());

            return v;
        } else if (object instanceof MonthHeaderView.Header) {
            MonthHeaderView v = null;

            if (convertView == null)
                v = new MonthHeaderView(parent.getContext());
            else if (convertView instanceof MonthHeaderView)
                v = (MonthHeaderView) convertView;
            else
                v = new MonthHeaderView(parent.getContext());

            v.setData((MonthHeaderView.Header) object);
            return v;
        } else if (object instanceof Payment) {
            PaymentCardView v = null;

            if (convertView == null)
                v = new PaymentCardView(parent.getContext());
            else if (convertView instanceof PaymentCardView)
                v = (PaymentCardView) convertView;
            else
                v = new PaymentCardView(parent.getContext());

            v.setData((Payment) object);
            return v;
        }
        return null;
    }

    public interface Listener {
        void onNextPage();
    }
}
