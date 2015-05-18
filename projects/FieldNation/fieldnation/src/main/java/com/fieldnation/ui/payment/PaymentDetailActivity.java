package com.fieldnation.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.service.data.payment.PaymentDataClient;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.util.Calendar;

public class PaymentDetailActivity extends AuthActionBarActivity {
    private static final String TAG = "ui.payment.PaymentDetailActivity";

    public static final String INTENT_KEY_PAYMENT_ID = "com.fieldnation.ui.payment.PaymentDetailActivity:PAYMENT_ID";

    private static final int WEB_GET_PAY = 1;

    // UI
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
    private PaymentDataClient _paymentClient;
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
            e.printStackTrace();
            finish();
        }

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
        _paymentClient = new PaymentDataClient(_paymentClient_listener);
        _paymentClient.connect(this);
    }

    @Override
    protected void onPause() {
        _paymentClient.disconnect(this);
        super.onPause();
    }

    private void requestData() {
        PaymentDataClient.requestPayment(this, _paymentId);
    }

    private void populateUi() {
        if (_paid == null)
            return;

        try {
            if (_paid.getDatePaid() != null) {
                String when = "";
                Calendar cal = ISO8601.toCalendar(_paid.getDatePaid());

                when = misc.formatDate(cal);

                _dateTextView.setVisibility(View.VISIBLE);
                _dateTextView.setText(this.getString(R.string.estimated) + " " + when);
            } else {
                _dateTextView.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            _dateTextView.setVisibility(View.GONE);
        }

        _workorderCountTextView.setText(_paid.getWorkorders().length + " " + this.getString(R.string.work_orders));

        if (_paid.getFees() != null && _paid.getFees().length > 0) {
            _feesCountTextView.setText(_paid.getFees().length + " Fees");
        } else {
            _feesCountTextView.setText("0 Fees");
        }

        _adapter = new PaymentDetailAdapter(_paid);
        _listView.setAdapter(_adapter);
        _idTextView.setText("Payment Id " + _paid.getPaymentId());
        _paymentTextView.setText(misc.toCurrency(_paid.getAmount()));
        String paymethod = misc.capitalize(_paid.getPayMethod().replaceAll("_", " "));
        _paymentTypeTextView.setText(paymethod);
        _stateTextView.setText(misc.capitalize(_paid.getStatus() + " "));
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private PaymentDataClient.Listener _paymentClient_listener = new PaymentDataClient.Listener() {
        @Override
        public void onConnected() {
            _paymentClient.registerPayment(-1, false);
        }

        @Override
        public void onPayment(Payment payment) {
            _paid = payment;
            populateUi();
        }
    };
}
