package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.BuildConfig;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.PaymentService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.market.MarketActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.MyWorkActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.util.Calendar;

/**
 * This view defines what is in the pull out drawer, and what the buttons do.
 *
 * @author michael.carver
 */
public class DrawerView extends RelativeLayout {
    private static final String TAG = "ui.DrawerView";

    // UI
    private RelativeLayout _myworkView;
    private RelativeLayout _marketView;
    private RelativeLayout _paymentView;
    //    private RelativeLayout _settingsView;
    private RelativeLayout _logoutView;
    private TextView _paidAmountTextView;
    private TextView _paidDateTextView;
    private TextView _estimatedAmountTextView;
    private TextView _estimatedDateTextView;
    private RelativeLayout _estimatedLayout;
    private RelativeLayout _paidLayout;
    private TextView _versionTextView;
    private LinearLayout _feedbackLayout;
    private Button _feedbackButton;
    private LinearLayout _reviewLayout;
    private Button _reviewButton;

    private LinearLayout _sendLogLayout;
    private Button _sendLogButton;

    // Data
    private PaymentService _dataService;
    private Payment _paidPayment = null;
    private Payment _estPayment = null;
    private int _nextPage = 0;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public DrawerView(Context context) {
        super(context);
        init();
    }

    public DrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_drawer, this);

        if (isInEditMode())
            return;

        AuthTopicService.subscribeAuthState(getContext(), 0, TAG, _authReceiver);

        _myworkView = (RelativeLayout) findViewById(R.id.mywork_view);
        _myworkView.setOnClickListener(_myworkView_onClick);

        _marketView = (RelativeLayout) findViewById(R.id.market_view);
        _marketView.setOnClickListener(_marketView_onClick);

        _paymentView = (RelativeLayout) findViewById(R.id.payment_view);
        _paymentView.setOnClickListener(_paymentView_onClick);

//        _settingsView = (RelativeLayout) findViewById(R.id.settings_view);
//        _settingsView.setOnClickListener(_settingsView_onClick);

        _logoutView = (RelativeLayout) findViewById(R.id.logout_view);
        _logoutView.setOnClickListener(_logoutView_onClick);

        _paidLayout = (RelativeLayout) findViewById(R.id.paid_layout);
        _paidAmountTextView = (TextView) findViewById(R.id.paidamount_textview);
        _paidDateTextView = (TextView) findViewById(R.id.paiddate_textview);

        _estimatedLayout = (RelativeLayout) findViewById(R.id.estimated_layout);
        _estimatedAmountTextView = (TextView) findViewById(R.id.estimatedamount_textview);
        _estimatedDateTextView = (TextView) findViewById(R.id.estimateddate_textview);

        _versionTextView = (TextView) findViewById(R.id.version_textview);
        try {
            _versionTextView.setText("v" + BuildConfig.VERSION_NAME);
            _versionTextView.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            _versionTextView.setVisibility(View.GONE);
        }

        _feedbackLayout = (LinearLayout) findViewById(R.id.feedback_layout);
        _feedbackButton = (Button) findViewById(R.id.feedback_button);
        _feedbackButton.setOnClickListener(_feedback_onClick);

        _sendLogLayout = (LinearLayout) findViewById(R.id.sendlog_layout);
        _sendLogButton = (Button) findViewById(R.id.sendlog_button);
        _sendLogButton.setOnClickListener(_sendlog_onClick);

        _reviewLayout = (LinearLayout) findViewById(R.id.review_layout);
        _reviewButton = (Button) findViewById(R.id.review_button);
        _reviewButton.setOnClickListener(_review_onClick);

        _sendLogButton.setVisibility(View.VISIBLE);
        if (BuildConfig.FLAVOR.equals("prod")) {
            _feedbackLayout.setVisibility(View.GONE);
            if (((GlobalState) getContext().getApplicationContext()).shouldShowReviewDialog()) {
                _reviewLayout.setVisibility(View.VISIBLE);
            } else {
                _reviewLayout.setVisibility(View.GONE);
            }
        } else {
            _feedbackLayout.setVisibility(View.VISIBLE);
            _reviewLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        TopicService.delete(getContext(), TAG);
        super.finalize();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final OnClickListener _review_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri marketUri = Uri.parse("market://details?id=com.fieldnation.android");
            getContext().startActivity(new Intent(Intent.ACTION_VIEW).setData(marketUri));
        }
    };

    private final OnClickListener _sendlog_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            File tempfile = misc.dumpLogcat(getContext());
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"apps@fieldnation.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Logcat log");
            intent.putExtra(Intent.EXTRA_TEXT, "Tell me about the problem you are having.");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempfile));
            getContext().startActivity(intent);
        }
    };

    private final OnClickListener _feedback_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1ImIpsrdzWdVUytIjEcKfGpbNFHm0cZP0q_ZHI2FUb48/viewform?usp=send_form"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private final View.OnClickListener _myworkView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyWorkActivity.startNew(getContext());
            attachAnimations();
        }
    };
    private final View.OnClickListener _marketView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), MarketActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            attachAnimations();
        }
    };
    private final View.OnClickListener _paymentView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), PaymentListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            attachAnimations();
        }
    };
    private final View.OnClickListener _settingsView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(getContext(), SettingsActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            getContext().startActivity(intent);
//            attachAnimations();
            AuthTopicService.requestAuthInvalid(getContext());
        }
    };
    private final View.OnClickListener _logoutView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AuthTopicService.requestAuthRemove(getContext());

            Log.v(TAG, "SplashActivity");
            SplashActivity.startNew(getContext());
            attachAnimations();
        }
    };

    private final AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_dataService == null || isNew) {
                _dataService = new PaymentService(getContext(), username, authToken, _resultReciever);

                getContext().startService(_dataService.getAll(1, 0, true));
                _nextPage = 1;
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _dataService = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _dataService = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(getContext());
        }
    };

    private class PaymentParseAsyncTask extends AsyncTaskEx<Bundle, Object, Payment[]> {

        @Override
        protected Payment[] doInBackground(Bundle... params) {
            Bundle resultData = params[0];

            Payment selPaid = _paidPayment;
            Payment selEst = _estPayment;
            try {
                JsonArray ja = new JsonArray(new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));

                for (int i = 0; i < ja.size(); i++) {
                    try {
                        Payment payment = Payment.fromJson(ja.getJsonObject(i));

                        if (payment == null) {
                            continue;
                        }

                        if (payment.getDatePaid() == null)
                            continue;

                        Log.v(TAG, payment.getDatePaid());
                        long date = ISO8601.toUtc(payment.getDatePaid());

                        if ("paid".equals(payment.getStatus())) {
                            if (selPaid == null || date >= ISO8601.toUtc(selPaid.getDatePaid())) {
                                selPaid = payment;
                            }
                        } else {
                            if (selEst == null || date >= ISO8601.toUtc(selEst.getDatePaid())) {
                                selEst = payment;
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (ja.size() == 0) {
                    return new Payment[]{selPaid, selEst};
                } else {
                    getContext().startService(_dataService.getAll(1, _nextPage, true));
                    _nextPage++;
                    return new Payment[]{selPaid, selEst};
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Payment[] payments) {
            super.onPostExecute(payments);
            if (payments != null) {
                _paidPayment = payments[0];
                _estPayment = payments[1];
                populateUi();
            }
        }
    }

    private void populateUi() {
        if (_estPayment != null) {
            _estimatedLayout.setVisibility(View.VISIBLE);
            _estimatedAmountTextView.setText(misc.toCurrency(_estPayment.getAmount()));
            try {
                Calendar cal = ISO8601.toCalendar(_estPayment.getDatePaid());
                _estimatedDateTextView.setText("Estimated " + misc.formatDate(cal));
            } catch (Exception ex) {
                ex.printStackTrace();
                _estimatedDateTextView.setText("");
            }
        } else {
            _estimatedLayout.setVisibility(View.GONE);
        }

        if (_paidPayment != null) {
            _paidLayout.setVisibility(View.VISIBLE);
            _paidAmountTextView.setText(misc.toCurrency(_paidPayment.getAmount()));
            try {
                Calendar cal = ISO8601.toCalendar(_paidPayment.getDatePaid());
                _paidDateTextView.setText("Estimated " + misc.formatDate(cal));
            } catch (Exception ex) {
                ex.printStackTrace();
                _paidDateTextView.setText("");
            }
        } else {
            _paidLayout.setVisibility(View.GONE);
        }
    }

    private final WebResultReceiver _resultReciever = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            new PaymentParseAsyncTask().executeEx(resultData);
        }

        @Override
        public Context getContext() {
            return DrawerView.this.getContext();
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);

            _dataService = null;
            AuthTopicService.requestAuthInvalid(getContext());
        }
    };

    private void attachAnimations() {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in, 0);
        }

    }
}
