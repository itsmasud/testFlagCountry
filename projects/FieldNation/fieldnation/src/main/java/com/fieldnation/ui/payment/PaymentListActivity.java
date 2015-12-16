package com.fieldnation.ui.payment;

import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.service.data.payment.PaymentClient;
import com.fieldnation.service.objectstore.ObjectStoreConstants;
import com.fieldnation.ui.ItemListActivity;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class PaymentListActivity extends ItemListActivity<Object> {
    private static final String TAG = "PaymentListActivity";

    // Data
    private PaymentClient _paymentClient;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    protected void onResume() {
        super.onResume();
        _paymentClient = new PaymentClient(_payment_listener);
        _paymentClient.connect(App.get());
    }

    @Override
    protected void onPause() {
        if (_paymentClient != null && _paymentClient.isConnected())
            _paymentClient.disconnect(App.get());
        super.onPause();
    }

    @Override
    public void requestData(int page) {
        PaymentClient.list(this, page);
    }

    @Override
    public View getView(Object object, View convertView, ViewGroup parent) {
        if (object instanceof Header) {
            MonthHeaderView v = null;
            if (convertView == null)
                v = new MonthHeaderView(parent.getContext());
            else if (convertView instanceof MonthHeaderView)
                v = (MonthHeaderView) convertView;
            else
                v = new MonthHeaderView(parent.getContext());

            v.setData(((Header) object).date, ((Header) object).amount);
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

    private final PaymentClient.Listener _payment_listener = new PaymentClient.Listener() {
        @Override
        public void onConnected() {
            _paymentClient.subList();
        }

        @Override
        public void onList(int page, List<Payment> list, boolean failed, boolean isCached) {
            if (list == null || list.size() == 0) {
                addPage(page, null); // done
                return;
            }

            List<Object> items = new LinkedList<>();
            int lastHash = 0;
            Header header = null;
            for (int i = 0; i < list.size(); i++) {
                Payment payment = list.get(i);

                if (payment.getDatePaid() == null) {
                    items.add(payment);
                    continue;
                }

                Calendar cal = payment.getDatePaidCalendar();
                int hash = cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) * 10000;

                if (hash != lastHash) {
                    lastHash = hash;
                    header = new Header(payment.getDatePaid(), 0);
                    items.add(header);
                }
                if (header != null)
                    header.amount += payment.getAmount();

                items.add(payment);
            }

            addPage(page, items);
        }
    };

    private static class Header {
        public String date;
        public double amount = 0;

        public Header(String date, double amount) {
            this.date = date;
            this.amount = amount;
        }
    }
}
