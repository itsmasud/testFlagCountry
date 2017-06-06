package com.fieldnation.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.AdditionalOptionsTracker;
import com.fieldnation.analytics.trackers.TestTrackers;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.NavProfileDetailListView;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.v2.ui.dialog.ProfileInformationDialog;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.settings.SettingsActivity;
import com.fieldnation.v2.ui.dialog.WhatsNewDialog;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Michael on 9/1/2016.
 */
public class AdditionalOptionsScreen extends RelativeLayout {
    private static final String TAG = "AdditionalOptionsView";

    // Ui
    private ProfilePicView _profilePicView;
    private TextView _profileNameTextView;
    private IconFontButton _profileExpandButton;
    private NavProfileDetailListView _profileListView = null;

    private View _profileMenu;
    private View _paymentMenu;
    private View _settingsMenu;
    private View _logoutMenu;
    private View _contactMenu;
    private View _debugMenu;
    private View _legalMenu;
    private View _versionMenu;
    private View _rateUsMenu;
    private View _touchMeMenu;
    private TextView _versionTextView;
    private View _linkContainerView;

    // Data
    private Profile _profile = null;
    private WeakReference<Drawable> _profilePic = null;
    private Listener _listener = null;
    private boolean _activated = false;

    // Services
    private PhotoClient _photoClient;
    private ProfileClient _profileClient;

    // Animations
    private Animation _ccw;
    private Animation _cw;


    public AdditionalOptionsScreen(Context context) {
        super(context);
        init();
    }

    public AdditionalOptionsScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdditionalOptionsScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_additional_options, this);

        if (isInEditMode())
            return;

        _profilePicView = (ProfilePicView) findViewById(R.id.pic_view);
        _profilePicView.setProfilePic(R.drawable.missing_circle);

        _profileNameTextView = (TextView) findViewById(R.id.name_textview);
        _profileNameTextView.setVisibility(GONE);

        _profileExpandButton = (IconFontButton) findViewById(R.id.profileexpand_button);
        _profileExpandButton.setOnClickListener(_profileExpandButton_onClick);

        _profileListView = (NavProfileDetailListView) findViewById(R.id.profile_detail_list);
        _profileListView.setListener(_navlistener);

        _linkContainerView = findViewById(R.id.link_container);

        _profileMenu = findViewById(R.id.profile_view);
        _profileMenu.setOnClickListener(_profile_onClick);

        _paymentMenu = findViewById(R.id.payments_menu);
        _paymentMenu.setOnClickListener(_payment_onClick);

        _settingsMenu = findViewById(R.id.settings_menu);
        _settingsMenu.setOnClickListener(_settings_onClick);

        _logoutMenu = findViewById(R.id.logout_menu);
        _logoutMenu.setOnClickListener(_logout_onClick);

        _contactMenu = findViewById(R.id.contact_menu);
        _contactMenu.setOnClickListener(_contact_onClick);

        _debugMenu = findViewById(R.id.debug_menu);
        _debugMenu.setOnClickListener(_debug_onClick);

        _legalMenu = findViewById(R.id.legal_menu);
        _legalMenu.setOnClickListener(_legal_onClick);

        _versionMenu = findViewById(R.id.version_menu);
        _versionMenu.setOnClickListener(_version_onClick);

        _rateUsMenu = findViewById(R.id.rate_us_menu);
        _rateUsMenu.setOnClickListener(_rateUs_onClick);

        _touchMeMenu = findViewById(R.id.touchMe_menu);
        _touchMeMenu.setOnClickListener(_touchMe_onClick);

//        if (BuildConfig.DEBUG)
//            _touchMeMenu.setVisibility(VISIBLE);
//        else
        _touchMeMenu.setVisibility(GONE);

        _versionTextView = (TextView) findViewById(R.id.version_textview);
        try {
            _versionTextView.setText((BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
            _versionTextView.setVisibility(VISIBLE);
        } catch (Exception ex) {
            _versionTextView.setVisibility(GONE);
        }

        _ccw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_ccw);
        _cw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_cw);

        _photoClient = new PhotoClient(_photo_listener);
        _photoClient.connect(App.get());

        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(App.get());

        AdditionalOptionsTracker.onShow(App.get());
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }


    @Override
    protected void onDetachedFromWindow() {
        if (_photoClient != null) _photoClient.disconnect(App.get());
        if (_profileClient != null) _profileClient.disconnect(App.get());


        super.onDetachedFromWindow();
    }

    private void populateUi() {
        if (_profile != null && _profile.getCanViewPayments()) {
            _paymentMenu.setVisibility(VISIBLE);
        } else {
            _paymentMenu.setVisibility(GONE);
        }

        if (_profile != null) {
            _profileNameTextView.setText(_profile.getFirstname() + " " + _profile.getLastname() + ": " + _profile.getUserId());
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
            _profilePicView.setProfilePic(R.drawable.missing_circle);
            return;
        }

        if (_profilePic == null || _profilePic.get() == null) {
            _profilePicView.setProfilePic(R.drawable.missing_circle);
            if (!misc.isEmptyOrNull(_profile.getPhoto().getLarge())) {
                PhotoClient.get(getContext(), _profile.getPhoto().getLarge(), true, false);
            }
        } else {
            _profilePicView.setProfilePic(_profilePic.get());
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

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final NavProfileDetailListView.Listener _navlistener = new NavProfileDetailListView.Listener() {
        @Override
        public void onUserSwitch(long userId) {
            if (_listener != null) {
                _listener.onSwitchUser(userId);
            }
        }
    };

    private final ProfileClient.Listener _profileClient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subGet(false);
        }

        @Override
        public void onGet(Profile profile, boolean failed) {
            if (profile != null) {
                _profilePic = null;
                _profile = profile;

                if (_profilePicView != null) {
                    _profilePicView.setProfilePic(R.drawable.missing_circle);
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

            _profilePic = new WeakReference<>((Drawable) drawable);
            addProfilePhoto();
        }
    };

    private final OnClickListener _profileExpandButton_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _activated = !_activated;
            if (_activated) {
                _linkContainerView.setVisibility(GONE);
                _profileListView.setVisibility(VISIBLE);
                _profileExpandButton.startAnimation(_cw);
            } else {
                _profileListView.setVisibility(GONE);
                _linkContainerView.setVisibility(VISIBLE);
                _profileExpandButton.startAnimation(_ccw);
            }
            populateUi();
        }
    };

    private final View.OnClickListener _profile_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.PROFILE);
            ProfileInformationDialog.show(App.get());
        }
    };

    private final View.OnClickListener _payment_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.PAYMENTS);
            PaymentListActivity.startNew(getContext());
        }
    };

    private final View.OnClickListener _settings_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.SETTINGS);
            SettingsActivity.startNew(getContext());
        }
    };

    private final View.OnClickListener _logout_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.LOG_OUT);
            AuthTopicClient.removeCommand(getContext());
        }
    };

    private final View.OnClickListener _contact_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.CONTACT_US);
            GlobalTopicClient.showContactUsDialog(getContext(), "LeftNavDrawer");
        }
    };

    private final View.OnClickListener _debug_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            File tempfile = DebugUtils.dumpLogcat(getContext(), (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
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

    private final View.OnClickListener _version_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.APP_VERSION);
            WhatsNewDialog.show(App.get());
        }
    };

    private final View.OnClickListener _rateUs_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("market://details?id=" + App.get().getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                App.get().startActivity(intent);
            }

        }
    };

    private final View.OnClickListener _legal_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.LEGAL);
            SettingsActivity.startNewLegal(getContext());
        }
    };

    private final View.OnClickListener _touchMe_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            TestTrackers.runTests(App.get());
        }
    };

    public interface Listener {
        void onSwitchUser(long userId);
    }
}