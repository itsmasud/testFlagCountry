package com.fieldnation.ui.payment;

import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.data.accounting.Payment;
import com.fieldnation.service.data.payment.PaymentDataClient;
import com.fieldnation.ui.ItemListActivity;

import java.util.List;

public class PaymentListActivity extends ItemListActivity<Payment> {
    private static final String TAG = "ui.payment.PaymentListActivity";

    // Data
    private PaymentDataClient _paymentClient;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    protected void onResume() {
        super.onResume();
        _paymentClient = new PaymentDataClient(_payment_listener);
        _paymentClient.connect(this);
    }

    @Override
    protected void onPause() {
        _paymentClient.disconnect(this);
        super.onPause();
    }

    @Override
    public void requestData(int page) {
        PaymentDataClient.requestPage(this, page);
    }

    @Override
    public View getView(Payment object, View convertView, ViewGroup parent) {
        PaymentCardView v = null;
        if (convertView == null) {
            v = new PaymentCardView(parent.getContext());
        } else if (convertView instanceof PaymentCardView) {
            v = (PaymentCardView) convertView;
        } else {
            v = new PaymentCardView(parent.getContext());
        }

        v.setData(object);

        return v;
    }

    private final PaymentDataClient.Listener _payment_listener = new PaymentDataClient.Listener() {
        @Override
        public void onConnected() {
            _paymentClient.registerPage();
        }

        @Override
        public void onGetAll(List<Payment> list, int page) {
            addPage(page, list);
        }
    };
}
