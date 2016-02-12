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
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.ui.market.MarketActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.MyWorkActivity;
import com.fieldnation.utils.misc;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * This view defines what is in the pull out drawer, and what the buttons do.
 *
 * @author michael.carver
 */
public class DrawerView extends RelativeLayout {
    private static final String STAG = "DrawerView";
    private final String TAG = UniqueTag.makeTag(STAG);

    // UI
    private ProfilePicView _picView;
    private TextView _profileNameTextView;
    private TextView _profileCompanyTextView;
    private ImageButton _profileExpandButton;
    private TextView _providerIdTextView;
    private ViewStub _stubProfielListView;
    private NavProfileDetailListView _profileListView = null;
    private LinearLayout _linkContainerView;

    // items
    private RelativeLayout _myworkView;
    private RelativeLayout _marketView;
    private RelativeLayout _paymentView;
    // private RelativeLayout _paidLayout;
    // private TextView _paidAmountTextView;
    // private TextView _paidDateTextView;
    // private TextView _estimatedAmountTextView;
    // private TextView _estimatedDateTextView;
    // private RelativeLayout _estimatedLayout;

    // sub items
    private LinearLayout _settingsView;
    private LinearLayout _feedbackView;
    private LinearLayout _debugView;
    private LinearLayout _helpView;
    private LinearLayout _logoutView;

    // misc
    private TextView _versionTextView;

    // Data
    private Profile _profile = null;
    private WeakReference<Drawable> _profilePic = null;

    private PhotoClient _photoClient;
    private GlobalTopicClient _globalTopicClient;

    private Listener _listener;

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
        _picView = (ProfilePicView) findViewById(R.id.pic_view);
        _picView.setProfilePic(R.drawable.missing_circle);
        _profileNameTextView = (TextView) findViewById(R.id.profile_name_textview);
        _profileNameTextView.setVisibility(View.GONE);
        _profileCompanyTextView = (TextView) findViewById(R.id.profile_company_textview);
        _profileCompanyTextView.setVisibility(View.GONE);
        _providerIdTextView = (TextView) findViewById(R.id.providerid_textview);
        _providerIdTextView.setVisibility(View.GONE);
        _profileExpandButton = (ImageButton) findViewById(R.id.profileexpand_button);
        _profileExpandButton.setOnClickListener(_profileExpandButton_onClick);

        _stubProfielListView = (ViewStub) findViewById(R.id.stub_profile_detail_list);

        // Items
        _linkContainerView = (LinearLayout) findViewById(R.id.link_container);
        _myworkView = (RelativeLayout) findViewById(R.id.mywork_view);
        _myworkView.setOnClickListener(_myworkView_onClick);

        _marketView = (RelativeLayout) findViewById(R.id.market_view);
        _marketView.setOnClickListener(_marketView_onClick);

        _paymentView = (RelativeLayout) findViewById(R.id.payment_view);
        _paymentView.setOnClickListener(_paymentView_onClick);

//        _paidLayout = (RelativeLayout) findViewById(R.id.paid_layout);
//        _paidAmountTextView = (TextView) findViewById(R.id.paidamount_textview);
//        _paidDateTextView = (TextView) findViewById(R.id.paiddate_textview);
//
//        _estimatedLayout = (RelativeLayout) findViewById(R.id.estimated_layout);
//        _estimatedAmountTextView = (TextView) findViewById(R.id.estimatedamount_textview);
//        _estimatedDateTextView = (TextView) findViewById(R.id.estimateddate_textview);

        // Sub items
        _settingsView = (LinearLayout) findViewById(R.id.settings_view);
        _settingsView.setOnClickListener(_settingsView_onClick);

        _debugView = (LinearLayout) findViewById(R.id.debug_view);
        _debugView.setOnClickListener(_debugView_onClick);

        _feedbackView = (LinearLayout) findViewById(R.id.feedback_view);
        _feedbackView.setOnClickListener(_feedback_onClick);

        _helpView = (LinearLayout) findViewById(R.id.help_view);
        _helpView.setOnClickListener(_help_onClick);

        _logoutView = (LinearLayout) findViewById(R.id.logout_view);
        _logoutView.setOnClickListener(_logoutView_onClick);

        // other status
        _versionTextView = (TextView) findViewById(R.id.version_textview);
        try {
            _versionTextView.setText("Version " +
                    (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
            _versionTextView.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            _versionTextView.setVisibility(View.GONE);
        }

        if (BuildConfig.FLAVOR.equals("prod")) {
        } else {
        }

        _globalTopicClient = new GlobalTopicClient(_globalTopicClient_listener);
        _globalTopicClient.connect(App.get());

        _photoClient = new PhotoClient(_photo_listener);
        _photoClient.connect(App.get());
    }

    private final NavProfileDetailListView.Listener _navlistener = new NavProfileDetailListView.Listener() {
        @Override
        public void onUserSwitch(long userId) {
            if (_listener != null)
                _listener.onSwitchUser(userId);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (_globalTopicClient != null && _globalTopicClient.isConnected())
            _globalTopicClient.disconnect(App.get());
        if (_photoClient != null && _photoClient.isConnected())
            _photoClient.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private void populateUi() {
//        if (_estPayment != null) {
//            _estimatedLayout.setVisibility(VISIBLE);
//            _estimatedAmountTextView.setText(misc.toCurrency(_estPayment.getAmount()));
//            _estimatedDateTextView.setText("Pending");
//        } else {
//            _estimatedLayout.setVisibility(GONE);
//        }

        if (_profile != null && _profile.getCanViewPayments()) {
            _paymentView.setVisibility(VISIBLE);
//            if (_paidPayment != null) {
//                _paidLayout.setVisibility(VISIBLE);
//                _paidAmountTextView.setText(misc.toCurrency(_paidPayment.getAmount()));
//                try {
//                    Calendar cal = ISO8601.toCalendar(_paidPayment.getDatePaid());
//                    _paidDateTextView.setText("Paid " + misc.formatDate(cal));
//                } catch (Exception ex) {
//                    Log.v(TAG, ex);
//                    _paidDateTextView.setText("");
//                }
//            } else {
//                _paidLayout.setVisibility(GONE);
//            }
        } else {
            _paymentView.setVisibility(GONE);
        }

        if (_profile != null) {
            _providerIdTextView.setVisibility(VISIBLE);
            _providerIdTextView.setText("Provider Id: " + _profile.getUserId());

            _profileNameTextView.setText(_profile.getFirstname() + " " + _profile.getLastname());
            _profileNameTextView.setVisibility(VISIBLE);

            if (_profile.getManagedProviders() != null && _profile.getManagedProviders().length > 1) {
                _profileExpandButton.setVisibility(VISIBLE);
            } else {
                _profileExpandButton.setVisibility(GONE);
            }

            if (_profileListView != null)
                _profileListView.setProfile(_profile);

            subPhoto();
            addProfilePhoto();
        }
    }

    private void addProfilePhoto() {
        if (_profile == null || _photoClient == null || !_photoClient.isConnected()) {
            _picView.setProfilePic(R.drawable.missing_circle);
            return;
        }

        if (_profilePic == null || _profilePic.get() == null) {
            _picView.setProfilePic(R.drawable.missing_circle);
            if (!misc.isEmptyOrNull(_profile.getPhoto().getLarge())) {
                PhotoClient.get(getContext(), _profile.getPhoto().getLarge(), true, false);
            }
        } else {
            _picView.setProfilePic(_profilePic.get());
        }
    }

    private void attachAnimations() {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in, 0);
        }

    }

    private void subPhoto() {
        if (_profile == null)
            return;

        if (_profile.getPhoto() == null)
            return;

        if (misc.isEmptyOrNull(_profile.getPhoto().getLarge()))
            return;

        _photoClient.subGet(_profile.getPhoto().getLarge(), true, false);
    }

    private void setProfileListViewVisibility(int visibility) {
        if (_stubProfielListView == null)
            return;

        if (visibility == GONE) {
            if (_profileListView != null) {
                _profileListView.setVisibility(GONE);
            }
        } else if (visibility == VISIBLE) {
            if (_profileListView == null) {
                _profileListView = (NavProfileDetailListView) _stubProfielListView.inflate().findViewById(R.id.profile_detail_list);
                _profileListView.setListener(_navlistener);

                if (_profile != null)
                    _profileListView.setProfile(_profile);
            }

            _profileListView.setVisibility(VISIBLE);
        } else if (visibility == INVISIBLE) {
            // Not used
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final OnClickListener _profileExpandButton_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _profileExpandButton.setActivated(!_profileExpandButton.isActivated());
            if (_profileExpandButton.isActivated()) {
                setProfileListViewVisibility(VISIBLE);
                _linkContainerView.setVisibility(View.GONE);
            } else {
                setProfileListViewVisibility(GONE);
                _linkContainerView.setVisibility(View.VISIBLE);
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
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
            attachAnimations();
        }
    };

    private final View.OnClickListener _paymentView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), PaymentListActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            getContext().startActivity(intent);
            attachAnimations();
        }
    };

    private final View.OnClickListener _debugView_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            File tempfile = misc.dumpLogcat(getContext(), (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"apps@fieldnation.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Android Log " + (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
            intent.putExtra(Intent.EXTRA_TEXT, "Tell me about the problem you are having.");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempfile));
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                getContext().startActivity(intent);
            } else {
                Toast.makeText(getContext(), R.string.no_email_app_is_available, Toast.LENGTH_LONG).show();
            }
        }
    };

    private final OnClickListener _feedback_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
/*
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1ImIpsrdzWdVUytIjEcKfGpbNFHm0cZP0q_ZHI2FUb48/viewform?usp=send_form"));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
*/
/*
            File tempfile = misc.dumpLogcat(getContext());
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"apps@fieldnation.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Logcat log");
            intent.putExtra(Intent.EXTRA_TEXT, "Tell me about the problem you are having.");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempfile));
            getContext().startActivity(intent);
*/

            // getContext().startService(new Intent(getContext(), WebCrawlerService.class));

            // Feedback Dialog
            GlobalTopicClient.showFeedbackDialog(getContext(), "LeftNavDrawer");
        }
    };

    private final OnClickListener _help_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            GlobalTopicClient.showHelpDialog(getContext());
        }
    };

    private final View.OnClickListener _logoutView_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AuthTopicClient.removeCommand(getContext());
            Log.v(TAG, "SplashActivity");
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
            _globalTopicClient.subGotProfile();
        }

        @Override
        public void onGotProfile(Profile profile) {
            if (_profile == null || profile.getUserId() != _profile.getUserId()) {
                _profilePic = null;
                _profile = profile;

                if (_picView != null) {
                    _picView.setProfilePic(R.drawable.missing_circle);
                }

                populateUi();
            }
        }
    };

    private final PhotoClient.Listener _photo_listener = new PhotoClient.Listener() {
        @Override
        public void onConnected() {
            subPhoto();
        }

        @Override
        public void onGet(String url, BitmapDrawable drawable, boolean isCircle, boolean failed) {
            if (drawable == null || url == null || failed)
                return;

            if (_profile.getPhoto() == null
                    || misc.isEmptyOrNull(_profile.getPhoto().getLarge())
                    || !url.equals(_profile.getPhoto().getLarge()))
                return;

            Drawable pic = drawable;
            _profilePic = new WeakReference<>(pic);
            addProfilePhoto();
        }
    };

    public interface Listener {
        void onSwitchUser(long userId);
    }
}

