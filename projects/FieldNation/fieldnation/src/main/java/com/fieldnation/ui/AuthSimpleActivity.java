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
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityRequestHandler;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fnpermissions.PermissionsClient;
import com.fieldnation.fnpermissions.PermissionsRequestHandler;
import com.fieldnation.fnpigeon.TopicService;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.auth.AuthClient;
import com.fieldnation.service.auth.AuthSystem;
import com.fieldnation.service.crawler.WebCrawlerService;
import com.fieldnation.service.data.profile.ProfileClient;
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

    // Data
    private boolean _profileBounceProtect = false;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthSystem.start();
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

        onFinishCreate(savedInstanceState);
    }

    public abstract int getLayoutResource();

    public abstract void onFinishCreate(Bundle savedInstanceState);

    public abstract int getToolbarId();

    public abstract DialogManager getDialogManager();

    public boolean doPermissionsChecks() {
        return true;
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        DialogManager dialogManager = getDialogManager();
        if (dialogManager != null) dialogManager.onStart();

        TermsAndConditionsDialog.addOnOkListener(DIALOG_TOC, _termsAndConditionsDialog_onOk);
        WhatsNewDialog.addOnClosedListener(DIALOG_WHATS_NEW_DIALOG, _whatsNewDialog_onClosed);
        OneButtonDialog.addOnPrimaryListener(DIALOG_NOT_PROVIDER, _notProvider_onOk);
        OneButtonDialog.addOnCanceledListener(DIALOG_NOT_PROVIDER, _notProvider_onCancel);
        TwoButtonDialog.addOnPrimaryListener(DIALOG_COI, _coiDialog_onPrimary);
        TwoButtonDialog.addOnSecondaryListener(DIALOG_COI, _coiDialog_onSecondary);

        if (doPermissionsChecks()) {
            _permissionsListener.sub();
            PermissionsClient.checkSelfPermissionAndRequest(this, App.getPermissions(), App.getPermissionsRequired());
        }
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        _toastClient.subSnackbar();
        _toastClient.subToast();

        startService(new Intent(this, TopicService.class));
        startService(new Intent(this, WebCrawlerService.class));

        _authClient.subNeedUsernameAndPassword();

        _appMessagingClient.subGotProfile();
        _appMessagingClient.subUpdateApp();
        _appMessagingClient.subShutdownUI();
        _appMessagingClient.subProfileInvalid();
        _appMessagingClient.subFinishActivity();
        ProfileClient.get(App.get());

        _activityRequestHandler.sub();

        DialogManager dialogManager = getDialogManager();
        if (dialogManager != null) dialogManager.onResume();

        if (doPermissionsChecks()) {
            _permissionsListener.sub();
            PermissionsClient.checkSelfPermissionAndRequest(this, App.getPermissions(), App.getPermissionsRequired());
        }
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        _appMessagingClient.unsubGotProfile();
        _appMessagingClient.unsubUpdateApp();
        _appMessagingClient.unsubShutdownUI();
        _appMessagingClient.unsubProfileInvalid();
        _appMessagingClient.unsubFinishActivity();

        _authClient.unsubNeedUsernameAndPassword();
        _toastClient.unSubToast();
        _toastClient.unSubSnackbar();
        _activityRequestHandler.unsub();

        DialogManager dialogManager = getDialogManager();
        if (dialogManager != null) dialogManager.onPause();

        if (doPermissionsChecks()) {
            _permissionsListener.unsub();
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop");
        TermsAndConditionsDialog.removeOnOkListener(DIALOG_TOC, _termsAndConditionsDialog_onOk);
        WhatsNewDialog.removeOnClosedListener(DIALOG_WHATS_NEW_DIALOG, _whatsNewDialog_onClosed);
        OneButtonDialog.removeOnPrimaryListener(DIALOG_NOT_PROVIDER, _notProvider_onOk);
        OneButtonDialog.removeOnCanceledListener(DIALOG_NOT_PROVIDER, _notProvider_onCancel);
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_COI, _coiDialog_onPrimary);
        TwoButtonDialog.removeOnSecondaryListener(DIALOG_COI, _coiDialog_onSecondary);

        if (doPermissionsChecks()) {
            _permissionsListener.unsub();
        }

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
        if (App.getProfile() == null)
            return;

        if (_profileBounceProtect)
            return;

        if (!App.getProfile().isProvider()) {
            OneButtonDialog.show(App.get(), DIALOG_NOT_PROVIDER, R.string.user_not_supported,
                    R.string.buyer_not_supported, R.string.btn_ok, true);
            return;
        }

        _profileBounceProtect = true;

        if (App.getProfile() != null && !App.get().hasReleaseNoteShownForThisVersion() && getDialogManager() != null) {
            Log.v(TAG, "show release notes");
            WhatsNewDialog.show(App.get(), DIALOG_WHATS_NEW_DIALOG);
            return;
        }

        if (App.get().shouldShowTermsAndConditionsDialog()) {
            Log.v(TAG, "_termsAndConditionsDialog");
            TermsAndConditionsDialog.show(this, DIALOG_TOC);
            return;
        }

        App gs = App.get();
        if (!App.getProfile().hasValidCoi() && gs.canRemindCoi() && App.getProfile().getCanViewPayments()) {
            Log.v(TAG, "Asking coi");
            TwoButtonDialog.show(App.get(), DIALOG_COI, getString(R.string.dialog_coi_title),
                    getString(R.string.dialog_coi_body, App.getProfile().insurancePercent()),
                    getString(R.string.btn_later), getString(R.string.btn_no_later), false, null);
        } else {
            Log.v(TAG, "toc/coi check done");
            onProfile(App.getProfile());
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
        ActivityRequestHandler.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.v(TAG, "onRequestPermissionsResult");
        if (doPermissionsChecks()) {
            PermissionsClient.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final TermsAndConditionsDialog.OnOkListener _termsAndConditionsDialog_onOk = new TermsAndConditionsDialog.OnOkListener() {
        @Override
        public void onOk() {
            _profileBounceProtect = false;
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
            _profileBounceProtect = false;
            App.get().setReleaseNoteShownReminded();
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
            AuthClient.removeCommand();
        }
    };

    private final OneButtonDialog.OnCanceledListener _notProvider_onCancel = new OneButtonDialog.OnCanceledListener() {
        @Override
        public void onCanceled() {
            AuthClient.removeCommand();
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _coiDialog_onPrimary = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
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
        public void onSecondary(Parcelable extraData) {
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

    private final ActivityRequestHandler _activityRequestHandler = new ActivityRequestHandler() {
        @Override
        public Activity getActivity() {
            return AuthSimpleActivity.this;
        }
    };

    private final AuthClient _authClient = new AuthClient() {
        @Override
        public void onNeedUsernameAndPassword(Parcelable authenticatorResponse) {
            Log.v(TAG, "AuthActivity.startNewWithResponse");
            AuthActivity.startNewWithResponse(App.get(), authenticatorResponse);
        }
    };

    private final AppMessagingClient _appMessagingClient = new AppMessagingClient() {
        @Override
        public void onGotProfile(Profile profile) {
            AuthSimpleActivity.this.gotProfile();
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
        public void onShutdownUI() {
            finish();
        }

        @Override
        public void onNetworkDisconnected() {
        }

        @Override
        public void onFinish() {
            AuthSimpleActivity.this.finish();
        }
    };

    private final ToastClient _toastClient = new ToastClient() {
        @Override
        public Activity getActivity() {
            return AuthSimpleActivity.this;
        }

        @Override
        public int getSnackbarTextId() {
            return android.support.design.R.id.snackbar_text;
        }
    };

    private final PermissionsRequestHandler _permissionsListener = new PermissionsRequestHandler() {
        @Override
        public Activity getActivity() {
            return AuthSimpleActivity.this;
        }
    };
}
