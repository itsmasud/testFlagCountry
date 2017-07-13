package com.fieldnation.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.data.payment.PaymentClient;
import com.fieldnation.ui.AuthSimpleActivity;

public class PaymentDetailActivity extends AuthSimpleActivity {
    private static final String TAG = "PaymentDetailActivity";

    public static final String INTENT_KEY_PAYMENT_ID = "PaymentDetailActivity:PAYMENT_ID";

    private static final int WEB_GET_PAY = 1;

    // UI

    private ListView _listView;

    // Data
    private long _paymentId = -1;
    private PaymentClient _paymentClient;
    private Payment _paid;
    private PaymentDetailAdapter _adapter;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_payments_detail;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        if (intent == null) {
            finish();
            return;
        }

        try {
            _paymentId = intent.getLongExtra(INTENT_KEY_PAYMENT_ID, -1);
        } catch (Exception e) {
            // TODO, not a good way to handle this
            Log.v(TAG, e);
            finish();
        }


        _listView = (ListView) findViewById(R.id.items_listview);
        // TODO set loading info
        requestData();
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _paymentClient = new PaymentClient(_paymentClient_listener);
        _paymentClient.connect(App.get());
    }

    @Override
    protected void onPause() {
        if (_paymentClient != null) _paymentClient.disconnect(App.get());
        super.onPause();
    }

    @Override
    public void onProfile(Profile profile) {
    }

    private void requestData() {
        PaymentClient.get(this, _paymentId);
    }

    private void populateUi() {
        if (_listView == null)
            return;

        if (_paid == null || _paid.getPaymentId() != _paymentId) {
            _listView.setVisibility(View.GONE);
            return;
        }

        try {
            if (_paid.getDatePaid() != null) {
                setTitle("Payment " + _paid.getPaymentId());
            } else {
                setTitle(getResources().getString(R.string.next_payment));
            }
        } catch (Exception ex) {
        }
        _adapter = new PaymentDetailAdapter(_paid);
        _listView.setAdapter(_adapter);
        _listView.setVisibility(View.VISIBLE);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final PaymentClient.Listener _paymentClient_listener = new PaymentClient.Listener() {
        @Override
        public void onConnected() {
            _paymentClient.subGet(-1, false);
        }

        @Override
        public void onGet(long paymentId, Payment payment, boolean failed) {
            _paid = payment;
            populateUi();
        }
    };

    public static void startNew(Context context, long paymentId) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, PaymentDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(INTENT_KEY_PAYMENT_ID, paymentId);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }

}
