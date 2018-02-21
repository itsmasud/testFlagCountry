package com.fieldnation.ui.nav;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.AppMessagingClient;
import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.AdditionalOptionsTracker;
import com.fieldnation.analytics.trackers.TestTrackers;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fntools.DebugUtils;
import com.fieldnation.service.auth.AuthClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.profileimage.ProfilePhotoClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.NavProfileDetailListView;
import com.fieldnation.ui.ProfilePicView;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.settings.SettingsActivity;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.ui.UnsyncedActivity;
import com.fieldnation.v2.ui.dialog.ContactUsDialog;
import com.fieldnation.v2.ui.dialog.ProfileInformationDialog;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;
import com.fieldnation.v2.ui.dialog.WhatsNewDialog;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Michael on 9/1/2016.
 */
public class AdditionalOptionsScreen extends RelativeLayout {
    private static final String TAG = "AdditionalOptionsScreen";

    private static final String DIALOG_DOWNLOAD_WARNING = TAG + ".DIALOG_DOWNLOAD_WARNING";
    private static final String DIALOG_SYNC_WARNING = TAG + ".DIALOG_SYNC_WARNING";

    // Ui
    private ProfilePicView _profilePicView;
    private TextView _profileNameTextView;
    private IconFontButton _profileExpandButton;
    private NavProfileDetailListView _profileListView = null;

    private View _offlineMenu;
    private Switch _offlineSwitch;
    private View _unsyncedMenu;
    private TextView _unsyncedCoungTextView;
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
    private Uri _profileImageUri = null;
    private WeakReference<Drawable> _profilePic = null;
    private int _listSize = 0;

    private Listener _listener = null;
    private boolean _activated = false;

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
        LayoutInflater.from(getContext()).inflate(R.layout.screen_additional_options, this);

        if (isInEditMode())
            return;

        setSaveEnabled(true);

        _offlineMenu = findViewById(R.id.offline_menu);
        _offlineMenu.setOnClickListener(_switch_onClick);

        _offlineSwitch = findViewById(R.id.offline_switch);
        _offlineSwitch.setClickable(false);

        _unsyncedMenu = findViewById(R.id.unsynced_menu);
        _unsyncedMenu.setOnClickListener(_unsynced_onClick);

        _unsyncedCoungTextView = findViewById(R.id.unsyncedCount_textview);

        _profilePicView = findViewById(R.id.pic_view);
        _profilePicView.setProfilePic(R.drawable.missing_circle);

        _profileNameTextView = findViewById(R.id.name_textview);
        _profileNameTextView.setVisibility(GONE);

        _profileExpandButton = findViewById(R.id.profileexpand_button);
        _profileExpandButton.setOnClickListener(_profileExpandButton_onClick);

        _profileListView = findViewById(R.id.profile_detail_list);
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

        _versionTextView = findViewById(R.id.version_textview);
        try {
            _versionTextView.setText((BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
            _versionTextView.setVisibility(VISIBLE);
        } catch (Exception ex) {
            _versionTextView.setVisibility(GONE);
        }

        _ccw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_ccw);
        _cw = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_180_cw);

        _profilePhotoClient.sub();
        _profileClient.subGet(false);
        _authClient.subRemoveCommand();
        ProfileClient.get(App.get(), false);

        AdditionalOptionsTracker.onShow(App.get());

    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        _workOrdersApi.sub();
        _appClient.subOfflineMode();
        WorkordersWebApi.getWorkOrderLists(App.get(), true, WebTransaction.Type.NORMAL);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_DOWNLOAD_WARNING, _downloadWarning_onPrimary);
        TwoButtonDialog.addOnSecondaryListener(DIALOG_DOWNLOAD_WARNING, _downloadWarning_onSecondary);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_SYNC_WARNING, _sync_onPrimary);
        TwoButtonDialog.addOnSecondaryListener(DIALOG_SYNC_WARNING, _sync_onSecondary);
        TwoButtonDialog.addOnCanceledListener(DIALOG_SYNC_WARNING, _sync_onCancel);
    }

    @Override
    protected void onDetachedFromWindow() {
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DOWNLOAD_WARNING, _downloadWarning_onPrimary);
        TwoButtonDialog.removeOnSecondaryListener(DIALOG_DOWNLOAD_WARNING, _downloadWarning_onSecondary);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_SYNC_WARNING, _sync_onPrimary);
        TwoButtonDialog.removeOnSecondaryListener(DIALOG_SYNC_WARNING, _sync_onSecondary);
        TwoButtonDialog.removeOnCanceledListener(DIALOG_SYNC_WARNING, _sync_onCancel);
        _authClient.unsubRemoveCommand();
        _profilePhotoClient.unsub();
        _profileClient.unsubGet(false);
        _workOrdersApi.unsub();
        _appClient.unsubOfflineMode();

        super.onDetachedFromWindow();
    }

    private void populateUi() {
        if (_profile != null && _profile.getCanViewPayments() && App.get().getOfflineState() == App.OfflineState.NORMAL) {
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
        } else {
            _profileNameTextView.setVisibility(GONE);
            _profileExpandButton.setVisibility(GONE);
            _profileListView.setProfile(null);
        }


        int size = WebTransaction.getSyncing().size();
        if (size == 0 && (App.get().getOfflineState() == App.OfflineState.NORMAL || App.get().getOfflineState() == App.OfflineState.DOWNLOADING)) {
            _unsyncedMenu.setVisibility(GONE);
        } else {
            _unsyncedMenu.setVisibility(VISIBLE);
            _unsyncedCoungTextView.setText(size + "");
        }

        _offlineSwitch.setChecked(App.get().getOfflineState() != App.OfflineState.NORMAL && App.get().getOfflineState() != App.OfflineState.SYNC);

        addProfilePhoto();
    }

    private void addProfilePhoto() {
        if (_profile == null || _profilePhotoClient == null) {
            _profilePicView.setProfilePic(R.drawable.missing_circle);
            return;
        }

        if (_profilePic == null || _profilePic.get() == null) {
            _profilePicView.setProfilePic(R.drawable.missing_circle);
            ProfilePhotoClient.get(App.get());
        } else {
            _profilePicView.setProfilePic(_profilePic.get());
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final ApatheticOnClickListener _switch_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            if (App.get().getOfflineState() == App.OfflineState.NORMAL) {
                TwoButtonDialog.show(App.get(), DIALOG_DOWNLOAD_WARNING, "Download Size",
                        "You are about to download " + _listSize + " assigned work orders including all attachments. Data rates may apply.",
                        "CONTINUE", "CANCEL", true, null);

            } else if (App.get().getOfflineState() == App.OfflineState.OFFLINE) {
                // TODO will need new dialog from adam
                TwoButtonDialog.show(App.get(), DIALOG_SYNC_WARNING, "Sync?", "Do you want to sync?", "SYNC", "CANCEL", true, null);
            } else if (App.get().getOfflineState() == App.OfflineState.SYNC) {
                // TODO  should we go back into download mode?
                AppMessagingClient.setOfflineMode(App.OfflineState.OFFLINE);
            }
            _offlineSwitch.setChecked(App.get().getOfflineState() != App.OfflineState.NORMAL && App.get().getOfflineState() != App.OfflineState.SYNC);
        }
    };

    private final ApatheticOnClickListener _unsynced_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            UnsyncedActivity.startNew(App.get());
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _downloadWarning_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setOfflineMode(App.OfflineState.DOWNLOADING);
        }
    };

    private final TwoButtonDialog.OnSecondaryListener _downloadWarning_onSecondary = new TwoButtonDialog.OnSecondaryListener() {
        @Override
        public void onSecondary(Parcelable extraData) {
            TwoButtonDialog.dismiss(App.get(), DIALOG_DOWNLOAD_WARNING);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _sync_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setOfflineMode(App.OfflineState.DOWNLOADING);
        }
    };

    private final TwoButtonDialog.OnSecondaryListener _sync_onSecondary = new TwoButtonDialog.OnSecondaryListener() {
        @Override
        public void onSecondary(Parcelable extraData) {
            AppMessagingClient.setOfflineMode(App.OfflineState.SYNC);
        }
    };

    private final TwoButtonDialog.OnCanceledListener _sync_onCancel = new TwoButtonDialog.OnCanceledListener() {
        @Override
        public void onCanceled(Parcelable extraData) {
            AppMessagingClient.setOfflineMode(App.OfflineState.SYNC);
        }
    };

    private final NavProfileDetailListView.Listener _navlistener = new NavProfileDetailListView.Listener() {
        @Override
        public void onUserSwitch(long userId) {
            _profileImageUri = null;
            if (_listener != null) {
                _listener.onSwitchUser(userId);
            }
        }
    };

    private final ProfileClient _profileClient = new ProfileClient() {
        @Override
        public void onGet(Profile profile, boolean failed) {
            if (profile != null) {
                Log.v(TAG, "onGetProfile " + profile.getUserId());

                _profile = profile;
                populateUi();
            }
        }
    };

    private final AuthClient _authClient = new AuthClient() {
        @Override
        public void onCommandRemove() {
            _profilePic = null;
            _profile = null;
            _profileImageUri = null;
            populateUi();
        }
    };

    private final ProfilePhotoClient _profilePhotoClient = new ProfilePhotoClient() {
        @Override
        public boolean getProfileImage(Uri uri) {
            Log.v(TAG, "getProfileImage " + uri);
            if (_profileImageUri == null
                    || !_profileImageUri.toString().equals(uri.toString())) {
                _profileImageUri = uri;
                return true;
            }
            return false;
        }

        @Override
        public void onProfileImage(BitmapDrawable drawable) {
            Log.v(TAG, "onProfileImage");
            _profilePic = new WeakReference<>((Drawable) drawable);
            addProfilePhoto();
        }
    };

    private final View.OnClickListener _profileExpandButton_onClick = new View.OnClickListener() {
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
            App.get().clearPrefKey(App.PREF_LAST_VISITED_WOL);
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.LOG_OUT);

            App.logout();

            AppMessagingClient.finishActivity();
        }
    };

    private final View.OnClickListener _contact_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AdditionalOptionsTracker.onClick(App.get(), AdditionalOptionsTracker.Item.CONTACT_US);
            ContactUsDialog.show(getContext(), null, "LeftNavDrawer");
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
            intent.putExtra(Intent.EXTRA_STREAM, App.getUriFromFile(tempfile));
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
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                ActivityClient.startActivity(intent);
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

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuid, TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrderLists");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && methodName.equals("getWorkOrderLists")) {
                SavedList[] savedList = (SavedList[]) successObject;
                for (SavedList sl : savedList) {
                    if (sl.getId().equals("workorders_assignments")) {
                        Log.v(TAG, "List size: " + sl.getCount());
                        _listSize = sl.getCount();
                        break;
                    }
                }
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);

        }
    };

    private final AppMessagingClient _appClient = new AppMessagingClient() {
        @Override
        public void onOfflineMode(App.OfflineState state) {
            populateUi();
        }
    };


    public interface Listener {
        void onSwitchUser(long userId);
    }
}
