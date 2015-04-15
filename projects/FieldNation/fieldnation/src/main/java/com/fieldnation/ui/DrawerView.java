package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.BuildConfig;
import com.fieldnation.GlobalState;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.payment.PaymentDataClient;
import com.fieldnation.service.data.photo.PhotoDataClient;
import com.fieldnation.ui.market.MarketActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.MyWorkActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;

/**
 * This view defines what is in the pull out drawer, and what the buttons do.
 *
 * @author michael.carver
 */
public class DrawerView extends RelativeLayout {
    private static final String TAG = "ui.DrawerView";

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
    private Payment _paidPayment = null;
    private Payment _estPayment = null;
    //    private int _nextPage = 0;

    private Profile _profile = null;
    private WeakReference<Drawable> _profilePic = null;

    private PaymentDataClient _paymentClient;
    private PhotoDataClient _photoClient;
    private GlobalTopicClient _globalTopicClient;

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

        _globalTopicClient = new GlobalTopicClient(_globalTopicClient_listener);
        _globalTopicClient.connect(getContext());

        _photoClient = new PhotoDataClient(_photo_listener);
        _photoClient.connect(getContext());

        _paymentClient = new PaymentDataClient(_payment_listener);
        _paymentClient.connect(getContext());

        PaymentDataClient.requestGetAll(getContext(), 0);
    }

    @Override
    protected void onDetachedFromWindow() {
        _globalTopicClient.disconnect(getContext());
        _photoClient.disconnect(getContext());
        _paymentClient.disconnect(getContext());
        super.onDetachedFromWindow();
    }

    private void populateUi() {
        if (_estPayment != null) {
            _estimatedLayout.setVisibility(View.VISIBLE);
            _estimatedAmountTextView.setText(misc.toCurrency(_estPayment.getAmount()));
            _estimatedDateTextView.setText("Pending");
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
            addProfilePhoto();
        }
    }

    private void addProfilePhoto() {
        if (_profile == null || _photoClient == null || !_photoClient.isConnected()) {
            _profileImageView.setImageResource(R.drawable.missing_circle);
            return;
        }

        if (_profilePic == null || _profilePic.get() == null) {
            _profileImageView.setImageResource(R.drawable.missing_circle);
            if (!misc.isEmptyOrNull(_profile.getPhoto().getLarge())) {
                _photoClient.getPhoto(getContext(), _profile.getPhoto().getLarge(), true);
            }
        } else {
            _profileImageView.setImageDrawable(_profilePic.get());
        }
    }

    private void attachAnimations() {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in, 0);
        }

    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
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
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            attachAnimations();
//            AuthTopicClient.dispatchInvalidateCommand(getContext());
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
            AuthTopicClient.dispatchRemoveCommand(getContext());

            Log.v(TAG, "SplashActivity");
            SplashActivity.startNew(getContext());
            attachAnimations();
        }
    };

    private final OnClickListener _call_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18775734353"));
            getContext().startActivity(intent);
        }
    };

    /*-*********************************************-*/
    /*-				System/web Events				-*/
    /*-*********************************************-*/
    private final GlobalTopicClient.Listener _globalTopicClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.registerGotProfile();
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            populateUi();
        }
    };

    private final PhotoDataClient.Listener _photo_listener = new PhotoDataClient.Listener() {
        @Override
        public void onConnected() {
            addProfilePhoto();
        }

        @Override
        public void onPhoto(String url, File file, boolean isCircle) {
            if (file == null || url == null)
                return;

            Drawable pic = new BitmapDrawable(GlobalState.getContext().getResources(), file.getAbsolutePath());
            _profilePic = new WeakReference<>(pic);
            addProfilePhoto();
        }
    };

    private final PaymentDataClient.Listener _payment_listener = new PaymentDataClient.Listener() {
        @Override
        public void onConnected() {
            _paymentClient.registerGetAll();
        }

        @Override
        public void onGetAll(List<Payment> list, int page) {
            if (list == null || list.size() == 0) {
                return;
            }
            PaymentDataClient.requestGetAll(getContext(), page + 1);

            for (int i = 0; i < list.size(); i++) {
                try {
                    Payment payment = list.get(i);

                    if (payment == null)
                        continue;
                    ;

                    if (payment.getDatePaid() == null)
                        continue;

                    long date = ISO8601.toUtc(payment.getDatePaid());

                    if ("paid".equals(payment.getStatus())) {
                        if (_paidPayment == null || date >= ISO8601.toUtc(_paidPayment.getDatePaid())) {
                            _paidPayment = payment;
                        }
                    } else {
                        if (_estPayment == null || date >= ISO8601.toUtc(_estPayment.getDatePaid())) {
                            _estPayment = payment;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            populateUi();
        }
    };
}

