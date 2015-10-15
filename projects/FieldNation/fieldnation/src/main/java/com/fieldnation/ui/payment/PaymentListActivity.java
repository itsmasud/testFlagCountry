package com.fieldnation.ui.payment;

import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.service.data.payment.PaymentClient;
import com.fieldnation.ui.ItemListActivity;

import java.util.List;

public class PaymentListActivity extends ItemListActivity<Payment> {
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
        _paymentClient.connect(this);
    }

    @Override
    protected void onPause() {
        _paymentClient.disconnect(this);
        super.onPause();
    }

    @Override
    public void requestData(int page) {
        PaymentClient.list(this, page);
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

    private final PaymentClient.Listener _payment_listener = new PaymentClient.Listener() {
        @Override
        public void onConnected() {
            _paymentClient.subList();
        }

        @Override
        public void onList(int page, List<Payment> list, boolean failed, boolean isCached) {
            addPage(page, list); // done
        }
    };
}
