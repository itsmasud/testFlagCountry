package com.fieldnation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityResultClient;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.dialog.ContactUsDialog;
import com.fieldnation.v2.ui.dialog.OneButtonDialog;
import com.fieldnation.v2.ui.dialog.TermsAndConditionsDialog;
import com.fieldnation.v2.ui.dialog.TwoButtonDialog;
import com.fieldnation.v2.ui.dialog.UpdateDialog;
import com.fieldnation.v2.ui.dialog.WhatsNewDialog;

/**
 * Created by Michael on 8/19/2016.
 */
public abstract class AuthSimpleActivity extends AppCompatActivity {
    private static final String TAG_BASE = "AuthSimpleActivity";
    private String TAG = TAG_BASE;

    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";
    private static final String DIALOG_TOC = TAG_BASE + ".termsAndConditionsDialog";
    private static final String DIALOG_WHATS_NEW_DIALOG = TAG_BASE + ".whatsNewDialog";
    private static final String DIALOG_NOT_PROVIDER = TAG_BASE + ".notProviderDialog";
    private static final String DIALOG_COI = TAG_BASE + ".certOfInsuranceDialog";

    // UI
    private ContactUsDialog _contactUsDialog;

    // Services
    private GlobalTopicClient _globalClient;
    private ToastClient _toastClient;
    private AuthTopicClient _authTopicClient;
    private ActivityResultClient _activityResultClient;
    private PermissionsClient _permissionsClient;

    // Data
    private Profile _profile;
    private boolean _profileBounceProtect = false;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (getToolbarId() != 0) {
            Toolbar toolbar = (Toolbar) findViewById(getToolbarId());
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.back_arrow);
            toolbar.setNavigationOnClickListener(_toolbarNavication_listener);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TAG)) {
                TAG = savedInstanceState.getString(STATE_TAG);
            } else {
                TAG = UniqueTag.makeTag(TAG_BASE);
            }
        }

        if (TAG.equals(TAG_BASE)) {
            TAG = UniqueTag.makeTag(TAG_BASE);
        }

        _contactUsDialog = ContactUsDialog.getInstance(getSupportFragmentManager(), TAG);

        onFinishCreate(savedInstanceState);
    }

    public abstract int getLayoutResource();

    public abstract void onFinishCreate(Bundle savedInstanceState);

    public abstract int getToolbarId();

    public abstract DialogManager getDialogManager();

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        DialogManager dialogManager = getDialogManager();
        if (dialogManager != null) dialogManager.onStart();

        _permissionsClient = new PermissionsClient(_permissionsListener);
        _permissionsClient.connect(App.get());
        PermissionsClient.checkSelfPermissionAndRequest(this, App.getPermissions(), App.getPermissionsRequired());
        TermsAndConditionsDialog.addOnOkListener(DIALOG_TOC, _termsAndConditionsDialog_onOk);
        WhatsNewDialog.addOnClosedListener(DIALOG_WHATS_NEW_DIALOG, _whatsNewDialog_onClosed);
        OneButtonDialog.addOnPrimaryListener(DIALOG_NOT_PROVIDER, _notProvider_onOk);
        OneButtonDialog.addOnCanceledListener(DIALOG_NOT_PROVIDER, _notProvider_onCancel);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_COI, _coiDialog_onPrimary);
        TwoButtonDialog.addOnSecondaryListener(DIALOG_COI, _coiDialog_onSecondary);
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        _toastClient = new ToastClient(_toastClient_listener);
        _toastClient.connect(App.get());
        _authTopicClient = new AuthTopicClient(_authTopicClient_listener);
        _authTopicClient.connect(App.get());
        _globalClient = new GlobalTopicClient(_globalClient_listener);
        _globalClient.connect(App.get());
        _activityResultClient = new ActivityResultClient(_activityResultClient_listener);
        _activityResultClient.connect(App.get());

        DialogManager dialogManager = getDialogManager();
        if (dialogManager != null) dialogManager.onResume();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        if (_globalClient != null) _globalClient.disconnect(App.get());
        if (_authTopicClient != null) _authTopicClient.disconnect(App.get());
        if (_toastClient != null) _toastClient.disconnect(App.get());
        if (_activityResultClient != null) _activityResultClient.disconnect(App.get());

        DialogManager dialogManager = getDialogManager();
        if (dialogManager != null) dialogManager.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        if (_permissionsClient != null) _permissionsClient.disconnect(App.get());
        TermsAndConditionsDialog.removeOnOkListener(DIALOG_TOC, _termsAndConditionsDialog_onOk);
        WhatsNewDialog.removeOnClosedListener(DIALOG_WHATS_NEW_DIALOG, _whatsNewDialog_onClosed);
        OneButtonDialog.removeOnPrimaryListener(DIALOG_NOT_PROVIDER, _notProvider_onOk);
        OneButtonDialog.removeOnCanceledListener(DIALOG_NOT_PROVIDER, _notProvider_onCancel);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_COI, _coiDialog_onPrimary);
        TwoButtonDialog.removeOnSecondaryListener(DIALOG_COI, _coiDialog_onSecondary);

        super.onStop();
        DialogManager dialogManager = getDialogManager();
        if (dialogManager != null) dialogManager.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_TAG, TAG);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.slide_out_right);
    }

    private void gotProfile() {
        if (_profile == null)
            return;

        if (_profileBounceProtect)
            return;

        if (!_profile.isProvider()) {
            OneButtonDialog.show(App.get(), DIALOG_NOT_PROVIDER, R.string.user_not_supported,
                    R.string.buyer_not_supported, R.string.btn_ok, true);
            return;
        }

        _profileBounceProtect = true;

        if (_profile != null && !App.get().hasReleaseNoteShownForThisVersion() && getDialogManager() != null) {
            Log.v(TAG, "show release notes");
            _profileBounceProtect = false;
            App.get().setReleaseNoteShownReminded();
            WhatsNewDialog.show(App.get(), DIALOG_WHATS_NEW_DIALOG);
            return;
        }

        if (App.get().shouldShowTermsAndConditionsDialog()) {
            Log.v(TAG, "_termsAndConditionsDialog");
            _profileBounceProtect = false;
            TermsAndConditionsDialog.show(this, DIALOG_TOC);
            return;
        }

        App gs = App.get();
        if (!_profile.hasValidCoi() && gs.canRemindCoi() && _profile.getCanViewPayments()) {
            Log.v(TAG, "Asking coi");
            TwoButtonDialog.show(App.get(), DIALOG_COI, getString(R.string.dialog_coi_title),
                    getString(R.string.dialog_coi_body, _profile.insurancePercent()),
                    getString(R.string.btn_later), getString(R.string.btn_no_later), false, null);
        } else {
            Log.v(TAG, "toc/coi check done");
            onProfile(_profile);
            _profileBounceProtect = false;
        }
    }

    public abstract void onProfile(Profile profile);

    @Override
    public void onBackPressed() {
        DialogManager dialogManager = getDialogManager();
        if (dialogManager != null) {
            if (dialogManager.onBackPressed())
                return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult " + requestCode + ", " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultClient.onActivityResult(App.get(), requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsClient.onRequestPermissionsResult(App.get(), requestCode, permissions, grantResults);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final TermsAndConditionsDialog.OnOkListener _termsAndConditionsDialog_onOk = new TermsAndConditionsDialog.OnOkListener() {
        @Override
        public void onOk() {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile();
                }
            });
        }
    };

    private final WhatsNewDialog.OnClosedListener _whatsNewDialog_onClosed = new WhatsNewDialog.OnClosedListener() {
        @Override
        public void onClosed() {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile();
                }
            });
        }
    };

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private final OneButtonDialog.OnPrimaryListener _notProvider_onOk = new OneButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary() {
            AuthTopicClient.removeCommand(AuthSimpleActivity.this);
        }
    };

    private final OneButtonDialog.OnCanceledListener _notProvider_onCancel = new OneButtonDialog.OnCanceledListener() {
        @Override
        public void onCanceled() {
            AuthTopicClient.removeCommand(AuthSimpleActivity.this);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _coiDialog_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary() {
            _profileBounceProtect = false;
            App.get().setCoiReminded();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile();
                }
            });
        }
    };

    private final TwoButtonDialog.OnSecondaryListener _coiDialog_onSecondary = new TwoButtonDialog.OnSecondaryListener() {
        @Override
        public void onSecondary() {
            _profileBounceProtect = false;
            App.get().setNeverRemindCoi();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile();
                }
            });
        }
    };

    private final ActivityResultClient.Listener _activityResultClient_listener = new ActivityResultClient.RequestListener() {
        @Override
        public Activity getActivity() {
            return AuthSimpleActivity.this;
        }

        @Override
        public ActivityResultClient getClient() {
            return _activityResultClient;
        }
    };

    private final AuthTopicClient.Listener _authTopicClient_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            _authTopicClient.subNeedUsernameAndPassword();
        }

        @Override
        public void onNeedUsernameAndPassword(Parcelable authenticatorResponse) {
            AuthActivity.startNewWithResponse(App.get(), authenticatorResponse);
        }
    };

    private final GlobalTopicClient.Listener _globalClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.subGotProfile();
            _globalClient.subUpdateApp();
            _globalClient.subAppShutdown();
            _globalClient.subShowContactUsDialog();
            _globalClient.subProfileInvalid(App.get());
            ProfileClient.get(App.get());
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            gotProfile();
        }

        @Override
        public void onProfileInvalid() {
            ProfileClient.get(App.get());
        }

        @Override
        public void onNeedAppUpdate() {
            UpdateDialog.show(App.get());
        }

        @Override
        public void onShutdown() {
            finish();
        }

        @Override
        public void onShowContactUsDialog(String source) {
            try {
                _contactUsDialog.show(source);
            } catch (Exception ex) {
                Log.logException(ex);
            }
        }

        @Override
        public void onNetworkDisconnected() {
        }
    };

    private final ToastClient.Listener _toastClient_listener = new ToastClient.Listener() {
        @Override
        public Activity getActivity() {
            return AuthSimpleActivity.this;
        }

        @Override
        public ToastClient getToastClient() {
            return _toastClient;
        }

        @Override
        public int getSnackbarTextId() {
            return android.support.design.R.id.snackbar_text;
        }
    };

    private final PermissionsClient.Listener _permissionsListener = new PermissionsClient.RequestListener() {
        @Override
        public Activity getActivity() {
            return AuthSimpleActivity.this;
        }

        @Override
        public PermissionsClient getClient() {
            return _permissionsClient;
        }
    };
}
