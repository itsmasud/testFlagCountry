package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.BuildConfig;
import com.fieldnation.GlobalState;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.PaymentService;
import com.fieldnation.rpc.client.PhotoService;
import com.fieldnation.rpc.common.PhotoServiceConstants;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.Topics;
import com.fieldnation.ui.market.MarketActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.MyWorkActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

/**
 * This view defines what is in the pull out drawer, and what the buttons do.
 *
 * @author michael.carver
 */
public class DrawerView extends RelativeLayout {
    private static final String TAG = "ui.DrawerView";

    private int WEB_GET_PHOTO = 1;

    // UI
    private LinearLayout _profileContainerLayout;
    private ImageView _profileImageView;
    private TextView _profileNameTextView;
    private TextView _profileCompanyTextView;
    private ImageButton _profileExpandButton;
    private TextView _providerIdTextView;

    // items
    private RelativeLayout _myworkView;
    private RelativeLayout _marketView;
    private RelativeLayout _paymentView;
    private TextView _paidAmountTextView;
    private TextView _paidDateTextView;
    private TextView _estimatedAmountTextView;
    private TextView _estimatedDateTextView;
    private RelativeLayout _estimatedLayout;
    private RelativeLayout _paidLayout;

    // sub items
    private LinearLayout _settingsView;
    private LinearLayout _feedbackView;
    private LinearLayout _helpView;
    private LinearLayout _logoutView;

    // misc
    private TextView _versionTextView;

    // Data
    private PaymentService _dataService;
    private Payment _paidPayment = null;
    private Payment _estPayment = null;
    private int _nextPage = 0;
    private Profile _profile = null;
    private Drawable _profilePic = null;
    private PhotoService _photoService;
    private Random _rand = new Random();


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

        // profile
        _profileContainerLayout = (LinearLayout) findViewById(R.id.profile_container);
        _profileContainerLayout.setOnClickListener(_profileContainerLayout_onClick);

        _profileImageView = (ImageView) findViewById(R.id.profile_imageview);
        _profileNameTextView = (TextView) findViewById(R.id.profile_name_textview);
        _profileNameTextView.setVisibility(View.GONE);
        _profileCompanyTextView = (TextView) findViewById(R.id.profile_company_textview);
        _profileCompanyTextView.setVisibility(View.GONE);
        _providerIdTextView = (TextView) findViewById(R.id.providerid_textview);
        _providerIdTextView.setVisibility(View.GONE);
        _profileExpandButton = (ImageButton) findViewById(R.id.profileexpand_button);
        _profileExpandButton.setOnClickListener(_profileExpandButton_onClick);

        // items
        _myworkView = (RelativeLayout) findViewById(R.id.mywork_view);
        _myworkView.setOnClickListener(_myworkView_onClick);

        _marketView = (RelativeLayout) findViewById(R.id.market_view);
        _marketView.setOnClickListener(_marketView_onClick);

        _paymentView = (RelativeLayout) findViewById(R.id.payment_view);
        _paymentView.setOnClickListener(_paymentView_onClick);

        _paidLayout = (RelativeLayout) findViewById(R.id.paid_layout);
        _paidAmountTextView = (TextView) findViewById(R.id.paidamount_textview);
        _paidDateTextView = (TextView) findViewById(R.id.paiddate_textview);

        _estimatedLayout = (RelativeLayout) findViewById(R.id.estimated_layout);
        _estimatedAmountTextView = (TextView) findViewById(R.id.estimatedamount_textview);
        _estimatedDateTextView = (TextView) findViewById(R.id.estimateddate_textview);

        // sub items
        _settingsView = (LinearLayout) findViewById(R.id.settings_view);
        _settingsView.setOnClickListener(_settingsView_onClick);

        _feedbackView = (LinearLayout) findViewById(R.id.feedback_view);
        _feedbackView.setOnClickListener(_feedback_onClick);

        _helpView = (LinearLayout) findViewById(R.id.help_view);
        _helpView.setOnClickListener(_help_onClick);

        _logoutView = (LinearLayout) findViewById(R.id.logout_view);
        _logoutView.setOnClickListener(_logoutView_onClick);

        // other status
        _versionTextView = (TextView) findViewById(R.id.version_textview);
        try {
            _versionTextView.setText("v" + BuildConfig.VERSION_NAME);
            _versionTextView.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            _versionTextView.setVisibility(View.GONE);
        }

        if (BuildConfig.FLAVOR.equals("prod")) {
            if (((GlobalState) getContext().getApplicationContext()).shouldShowReviewDialog()) {
            } else {
            }
        } else {
        }

        _photoService = new PhotoService(GlobalState.getContext(), _photoReceiver);
        Topics.subscrubeProfileUpdated(getContext(), TAG + ":PROFILE", _topicReceiver);
    }

    @Override
    protected void finalize() throws Throwable {
        TopicService.delete(getContext(), TAG);
        TopicService.delete(getContext(), TAG + ":PROFILE");
        super.finalize();
    }

    private void populateUi() {
        if (_estPayment != null) {
            _estimatedLayout.setVisibility(View.VISIBLE);
            _estimatedAmountTextView.setText(misc.toCurrency(_estPayment.getAmount()));
            try {
//                Calendar cal = ISO8601.toCalendar(_estPayment.getDatePaid());
//                _estimatedDateTextView.setText("Estimated " + misc.formatDate(cal));
                _estimatedDateTextView.setText("Pending");
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
                _paidDateTextView.setText("Paid " + misc.formatDate(cal));
            } catch (Exception ex) {
                ex.printStackTrace();
                _paidDateTextView.setText("");
            }
        } else {
            _paidLayout.setVisibility(View.GONE);
        }

        if (_profile != null) {
            _providerIdTextView.setVisibility(View.VISIBLE);
            _providerIdTextView.setText("Provider Id: " + _profile.getUserId());

            _profileNameTextView.setText(_profile.getFirstname() + " " + _profile.getLastname());
            _profileNameTextView.setVisibility(View.VISIBLE);

            // TODO add service company name

            if (_profilePic == null) {
//                _profileImageView.setBackgroundResource(R.drawable.missing_circle);
                _profileImageView.setImageResource(R.drawable.missing_circle);
                if (_profile.getPhoto().getThumb() != null) {
                    WEB_GET_PHOTO = _rand.nextInt();
                    getContext().startService(_photoService.getPhoto(WEB_GET_PHOTO, _profile.getPhoto().getThumb(), true));
                }
            } else {
                _profileImageView.setImageDrawable(_profilePic);
//                _profileImageView.setBackgroundDrawable(_profilePic);
            }
        }

    }

    /*-*****************************************-*/
    /*-				Profile Events				-*/
    /*-*****************************************-*/
    private final OnClickListener _profileContainerLayout_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
    private final OnClickListener _profileExpandButton_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _profileExpandButton.setActivated(!_profileExpandButton.isActivated());
        }
    };

    /*-*************************************-*/
    /*-				Item Events				-*/
    /*-*************************************-*/
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

    /*-*****************************************-*/
    /*-				Subitem Events				-*/
    /*-*****************************************-*/
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

    private final OnClickListener _feedback_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1ImIpsrdzWdVUytIjEcKfGpbNFHm0cZP0q_ZHI2FUb48/viewform?usp=send_form"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };

    private final OnClickListener _help_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

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

    /*-*********************************************-*/
    /*-				System/web Events				-*/
    /*-*********************************************-*/

    private ResultReceiver _photoReceiver = new ResultReceiver(new Handler()) {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == WEB_GET_PHOTO) {
                Bitmap photo = resultData.getParcelable(PhotoServiceConstants.KEY_RESPONSE_DATA);
                _profilePic = new BitmapDrawable(getContext().getResources(), photo);
//                _profileImageView.setBackgroundDrawable(_profilePic);
                _profileImageView.setImageDrawable(_profilePic);
            }
            super.onReceiveResult(resultCode, resultData);
        }
    };

    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (Topics.TOPIC_PROFILE_UPDATE.equals(topicId)) {
                Log.v(TAG, "TOPIC_PROFILE_UPDATE");
                parcel.setClassLoader(getContext().getClassLoader());
                _profile = parcel.getParcelable(Topics.TOPIC_PROFILE_PARAM_PROFILE);
                populateUi();
            }
        }
    };

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


    private final OnClickListener _call_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18775734353"));
            getContext().startActivity(intent);
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
