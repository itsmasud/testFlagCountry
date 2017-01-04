package com.fieldnation.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.BuildConfig;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.analytics.ElementAction;
import com.fieldnation.analytics.ElementIdentity;
import com.fieldnation.analytics.ElementType;
import com.fieldnation.analytics.ScreenName;
import com.fieldnation.analytics.SpUIContext;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.NavProfileDetailListView;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.ui.dialog.v2.NewFeaturesDialog;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.ui.dialog.v2.ProfileInformationDialog;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.settings.SettingsActivity;

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
    private ViewStub _stubProfileListView;
    private NavProfileDetailListView _profileListView = null;

    private View _profileMenu;
    private View _paymentMenu;
    private View _settingsMenu;
    private View _logoutMenu;
    private View _contactMenu;
    private View _debugMenu;
    private View _legalMenu;
    private View _versionMenu;
    private TextView _versionTextView;
    private View _linkContainerView;

    // Data
    private Profile _profile = null;
    private WeakReference<Drawable> _profilePic = null;
    private Listener _listener = null;

    // Services
    private PhotoClient _photoClient;
    private ProfileClient _profileClient;


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

        _stubProfileListView = (ViewStub) findViewById(R.id.stub_profile_detail_list);

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

        _versionTextView = (TextView) findViewById(R.id.version_textview);
        try {
            _versionTextView.setText((BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
            _versionTextView.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            _versionTextView.setVisibility(View.GONE);
        }

        _photoClient = new PhotoClient(_photo_listener);
        _photoClient.connect(App.get());

        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(App.get());

    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_photoClient != null && _photoClient.isConnected())
            _photoClient.disconnect(App.get());
        if (_profileClient != null && _profileClient.isConnected())
            _profileClient.disconnect(App.get());


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

    private void setProfileListViewVisibility(int visibility) {
        if (_stubProfileListView == null)
            return;

        if (visibility == GONE) {
            if (_profileListView != null) {
                _profileListView.setVisibility(GONE);
            }
        } else if (visibility == VISIBLE) {
            if (_profileListView == null) {
                _profileListView = (NavProfileDetailListView) _stubProfileListView.inflate().findViewById(R.id.profile_detail_list);
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

    private final View.OnClickListener _profile_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracker.event(App.get(), new Event.Builder()
                    .addContext(new SpUIContext.Builder()
                            .page(ScreenName.additionalOptions().name)
                            .elementIdentity("Profile")
                            .elementAction(ElementAction.CLICK)
                            .elementType(ElementType.LIST_ITEM)
                            .build())
                    .action(ElementAction.CLICK)
                    .build());
            ProfileInformationDialog.Controller.show(App.get());
        }
    };

    private final View.OnClickListener _payment_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracker.event(App.get(), new Event.Builder()
                    .addContext(new SpUIContext.Builder()
                            .page(ScreenName.additionalOptions().name)
                            .elementIdentity("Payments")
                            .elementAction(ElementAction.CLICK)
                            .elementType(ElementType.LIST_ITEM)
                            .build())
                    .action(ElementAction.CLICK)
                    .build());
            PaymentListActivity.startNew(getContext());
        }
    };

    private final View.OnClickListener _settings_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracker.event(App.get(), new Event.Builder()
                    .addContext(new SpUIContext.Builder()
                            .page(ScreenName.additionalOptions().name)
                            .elementIdentity("Settings")
                            .elementAction(ElementAction.CLICK)
                            .elementType(ElementType.LIST_ITEM)
                            .build())
                    .action(ElementAction.CLICK)
                    .build());
            SettingsActivity.startNew(getContext());
        }
    };

    private final View.OnClickListener _logout_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracker.event(App.get(), new Event.Builder()
                    .addContext(new SpUIContext.Builder()
                            .page(ScreenName.additionalOptions().name)
                            .elementIdentity("Log Out")
                            .elementAction(ElementAction.CLICK)
                            .elementType(ElementType.LIST_ITEM)
                            .build())
                    .action(ElementAction.CLICK)
                    .build());
            AuthTopicClient.removeCommand(getContext());
        }
    };

    private final View.OnClickListener _contact_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracker.event(App.get(), new Event.Builder()
                    .addContext(new SpUIContext.Builder()
                            .page(ScreenName.additionalOptions().name)
                            .elementIdentity("Contact Us")
                            .elementAction(ElementAction.CLICK)
                            .elementType(ElementType.LIST_ITEM)
                            .build())
                    .action(ElementAction.CLICK)
                    .build());
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
            Tracker.event(App.get(), new Event.Builder()
                    .addContext(new SpUIContext.Builder()
                            .page(ScreenName.additionalOptions().name)
                            .elementIdentity("App Version")
                            .elementAction(ElementAction.CLICK)
                            .elementType(ElementType.LIST_ITEM)
                            .build())
                    .action(ElementAction.CLICK)
                    .build());
            NewFeaturesDialog.Controller.show(App.get());
        }
    };

    private final View.OnClickListener _legal_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracker.event(App.get(), new Event.Builder()
                    .addContext(new SpUIContext.Builder()
                            .page(ScreenName.additionalOptions().name)
                            .elementIdentity("Legal")
                            .elementAction(ElementAction.CLICK)
                            .elementType(ElementType.LIST_ITEM)
                            .build())
                    .action(ElementAction.CLICK)
                    .build());
            SettingsActivity.startNewLegal(getContext());
        }
    };

    public interface Listener {
        void onSwitchUser(long userId);
    }
}