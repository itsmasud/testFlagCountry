package com.fieldnation.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.service.data.payment.PaymentClient;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;

import java.util.Calendar;

public class PaymentDetailActivity extends AuthActionBarActivity {
    private static final String TAG = "ui.payment.PaymentDetailActivity";

    public static final String INTENT_KEY_PAYMENT_ID = "com.fieldnation.ui.payment.PaymentDetailActivity:PAYMENT_ID";

    private static final int WEB_GET_PAY = 1;

    // UI
    private TextView _titleTextView;
    private TextView _paymentTextView;
    private TextView _timeTextView;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(_nav_onClick);

        try {
            _paymentId = intent.getLongExtra(INTENT_KEY_PAYMENT_ID, -1);
        } catch (Exception e) {
            // TODO, not a good way to handle this
            Log.v(TAG, e);
            finish();
        }

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);

        _listView = (ListView) findViewById(R.id.items_listview);
        // TODO set loading info
        requestData();
    }

    private final View.OnClickListener _nav_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

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
            return;
        }

        try {
            //_titleTextView.setVisibility(View.VISIBLE);
            if (_paid.getDatePaid() != null) {
                //_titleTextView.setText("PAID");
                String when = "";
                Calendar cal = ISO8601.toCalendar(_paid.getDatePaid());

                when = DateUtils.formatDate(cal);

                _timeTextView.setVisibility(View.VISIBLE);
                _timeTextView.setText(_paid.getPayMethod() + " on " + when);
            } else {
                //_titleTextView.setText("PENDING");
//                _timeTextView.setVisibility(View.INVISIBLE);
                _timeTextView.setVisibility(View.VISIBLE);
                _timeTextView.setText(_paid.getPayMethod() + " Pending");
            }
        } catch (Exception ex) {
            _timeTextView.setVisibility(View.INVISIBLE);
        }

        _adapter = new PaymentDetailAdapter(_paid);
        _listView.setAdapter(_adapter);
        _paymentTextView.setText(misc.toCurrency(_paid.getAmount()));
        _listView.setVisibility(View.VISIBLE);
        _titleTextView.setVisibility(View.GONE);
        setTitle("Payment " + _paid.getPaymentId());
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
}
