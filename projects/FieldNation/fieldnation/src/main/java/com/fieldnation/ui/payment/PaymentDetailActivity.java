package com.fieldnation.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.PaymentService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
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
    private PaymentService _service;
    private Payment _paid;
    private PaymentDetailAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_detail);

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
    }

    private void requestData() {
        if (_service == null)
            return;

        startService(_service.getPayment(WEB_GET_PAY, _paymentId, false));
    }

    private void populateUi() {
        if (_service == null)
            return;

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

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        if (_service == null || isNew) {
            _service = new PaymentService(PaymentDetailActivity.this, username, authToken, _resultReceiver);
            requestData();
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private WebResultReceiver _resultReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_GET_PAY) {
                byte[] data = resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA);

                Log.v(TAG, new String(data));

                try {
                    _paid = Payment.fromJson(new JsonObject(new String(data)));
                    populateUi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.v(TAG, "BP");
            }
        }

        @Override
        public Context getContext() {
            return PaymentDetailActivity.this;
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            AuthTopicService.requestAuthInvalid(PaymentDetailActivity.this);
        }
    };

    @Override
    public void onRefresh() {
        // TODO Method Stub: onRefresh()
        Log.v(TAG, "Method Stub: onRefresh()");
    }
}
