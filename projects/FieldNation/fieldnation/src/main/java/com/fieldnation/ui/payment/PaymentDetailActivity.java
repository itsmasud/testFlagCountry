package com.fieldnation.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.service.data.payment.PaymentClient;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Calendar;

public class PaymentDetailActivity extends AuthActionBarActivity {
    private static final String TAG = "ui.payment.PaymentDetailActivity";

    public static final String INTENT_KEY_PAYMENT_ID = "com.fieldnation.ui.payment.PaymentDetailActivity:PAYMENT_ID";

    private static final int WEB_GET_PAY = 1;

    // UI
    private View _paymentHeaderLayout;
    private TextView _idTextView;
    private TextView _paymentTextView;
    private TextView _paymentTypeTextView;
    private TextView _dateTextView;
    private TextView _workorderCountTextView;
    private TextView _feesCountTextView;
    private ListView _listView;
    private TextView _stateTextView;

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

        _paymentHeaderLayout = findViewById(R.id.paymentheader_layout);
        _idTextView = (TextView) findViewById(R.id.id_textview);
        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
        _paymentTypeTextView = (TextView) findViewById(R.id.paymenttype_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _workorderCountTextView = (TextView) findViewById(R.id.workordercount_textview);
        _feesCountTextView = (TextView) findViewById(R.id.feescount_textview);
        _stateTextView = (TextView) findViewById(R.id.state_textview);

        _listView = (ListView) findViewById(R.id.items_listview);
        // TODO set loading info
        requestData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _paymentClient = new PaymentClient(_paymentClient_listener);
        _paymentClient.connect(App.get());
    }

    @Override
    protected void onPause() {
        if (_paymentClient != null && _paymentClient.isConnected())
            _paymentClient.disconnect(App.get());
        super.onPause();
    }

    private void requestData() {
        PaymentClient.get(this, _paymentId);
    }

    private void populateUi() {
        if (_listView == null)
            return;

        if (_paid == null || _paid.getPaymentId() != _paymentId) {
            _listView.setVisibility(View.GONE);
            _paymentHeaderLayout.setVisibility(View.GONE);
            return;
        }

        try {
            if (_paid.getDatePaid() != null) {
                String when = "";
                Calendar cal = ISO8601.toCalendar(_paid.getDatePaid());

                when = misc.formatDate(cal);

                _dateTextView.setVisibility(View.VISIBLE);
                _dateTextView.setText("Paid On " + when);
            } else {
                _dateTextView.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            _dateTextView.setVisibility(View.GONE);
        }

        if (_paid.getWorkorders().length == 1) {
            _workorderCountTextView.setText(_paid.getWorkorders().length + " Work Order");
        } else if (_paid.getWorkorders().length > 1) {
            _workorderCountTextView.setText(_paid.getWorkorders().length + " Work Orders");
        }

        if (_paid.getFees() != null && _paid.getFees().length == 1) {
            _feesCountTextView.setText(_paid.getFees().length + " Fee");
        } else if (_paid.getFees() != null && _paid.getFees().length > 1) {
            _feesCountTextView.setText(_paid.getFees().length + " Fees");
        } else {
            _feesCountTextView.setText("0 Fees");
        }

        _adapter = new PaymentDetailAdapter(_paid);
        _listView.setAdapter(_adapter);
        _idTextView.setText("Payment ID: " + _paid.getPaymentId());
        _paymentTextView.setText(misc.toCurrency(_paid.getAmount()));
        try {
            String paymethod = misc.capitalize(_paid.getPayMethod().replaceAll("_", " "));
            _paymentTypeTextView.setText(paymethod);
        } catch (Exception ex) {
            _paymentTypeTextView.setText("No Pay Method");
        }
        _stateTextView.setText(misc.capitalize(_paid.getStatus() + " "));

        _listView.setVisibility(View.VISIBLE);
        _paymentHeaderLayout.setVisibility(View.VISIBLE);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private PaymentClient.Listener _paymentClient_listener = new PaymentClient.Listener() {
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
}
