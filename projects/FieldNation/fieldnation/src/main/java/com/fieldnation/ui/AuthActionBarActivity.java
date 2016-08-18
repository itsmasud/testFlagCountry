package com.fieldnation.ui;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.dialog.ContactUsDialog;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.TermsAndConditionsDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.UpdateDialog;

/**
 * This is the base of all the activities in this project. It provides
 * authentication and sets up the action bars.
 *
 * @author michael.carver
 */
public abstract class AuthActionBarActivity extends AppCompatActivity {
    private static final String TAG_BASE = "AuthActionBarActivity";
    private String TAG = TAG_BASE;

    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // UI
    private ActionBarDrawerView _actionBarView;

    private UpdateDialog _updateDialog;
    private OneButtonDialog _notProviderDialog;
    private TwoButtonDialog _coiWarningDialog;
    private ContactUsDialog _contactUsDialog;
    private TermsAndConditionsDialog _termsAndConditionsDialog;

    // Services
    private GlobalTopicClient _globalClient;
    private ToastClient _toastClient;
    private AuthTopicClient _authTopicClient;
    private ActivityResultClient _activityResultClient;

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

        _actionBarView = (ActionBarDrawerView) findViewById(R.id.actionbardrawerview);

        Toolbar toolbar = _actionBarView.getToolbar();

        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.nav_icon);
        ab.setDisplayHomeAsUpEnabled(true);


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

        _updateDialog = UpdateDialog.getInstance(getSupportFragmentManager(), TAG);

        _termsAndConditionsDialog = TermsAndConditionsDialog.getInstance(getSupportFragmentManager(), TAG);

        _coiWarningDialog = TwoButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":COI");
        _coiWarningDialog.setCancelable(false);

        _notProviderDialog = OneButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":NOT_SUPPORTED");
        _contactUsDialog = ContactUsDialog.getInstance(getSupportFragmentManager(), TAG);

        onFinishCreate(savedInstanceState);
    }

    public abstract int getLayoutResource();

    public abstract void onFinishCreate(Bundle savedInstanceState);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        _notProviderDialog.setData(getString(R.string.user_not_supported),
                getString(R.string.buyer_not_supported),
                getString(R.string.btn_ok), _notProvider_listener);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        if (_globalClient != null && _globalClient.isConnected())
            _globalClient.disconnect(App.get());

        if (_authTopicClient != null && _authTopicClient.isConnected())
            _authTopicClient.disconnect(App.get());

        if (_toastClient != null && _toastClient.isConnected())
            _toastClient.disconnect(App.get());

        if (_activityResultClient != null && _activityResultClient.isConnected())
            _activityResultClient.disconnect(App.get());

        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_TAG, TAG);
        super.onSaveInstanceState(outState);
    }

    private void gotProfile(Profile profile) {
        if (_profile == null)
            return;

        if (_profileBounceProtect)
            return;

        _profileBounceProtect = true;

        if (App.get().shouldShowTermsAndConditionsDialog()) {
            _termsAndConditionsDialog.show();
        }

        if (_profile != null && !App.get().hasReleaseNoteShownForThisVersion()) {
            App.get().setReleaseNoteShownReminded();
            Intent intent = new Intent(App.get(), NewFeatureActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            App.get().startActivity(intent);
        }

        if (!_profile.isProvider()) {
//            _notProviderDialog.show();
//            return;
        }
        App gs = App.get();
        if (!profile.hasValidCoi() && gs.canRemindCoi() && _profile.getCanViewPayments()) {
            Log.v(TAG, "Asking coi");
            _coiWarningDialog.setData(
                    getString(R.string.dialog_coi_title),
                    getString(R.string.dialog_coi_body, profile.insurancePercent()),
                    getString(R.string.btn_later),
                    getString(R.string.btn_no_later),
                    _coi_listener);
            try {
                _coiWarningDialog.show();
            } catch (Exception ex) {
                Debug.logException(ex);
            }
        } else {
            Log.v(TAG, "toc/coi check done");
            onProfile(profile);
            _profileBounceProtect = false;
        }
    }

    public void onProfile(Profile profile) {
    }

    // Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                _actionBarView.showLeftNav();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (_actionBarView == null || !_actionBarView.onBackPressed())
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultClient.onActivityResult(App.get(), requestCode, resultCode, data);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private final OneButtonDialog.Listener _notProvider_listener = new OneButtonDialog.Listener() {
        @Override
        public void onButtonClick() {
            AuthTopicClient.removeCommand(AuthActionBarActivity.this);
        }

        @Override
        public void onCancel() {
            AuthTopicClient.removeCommand(AuthActionBarActivity.this);
        }
    };


    private final TwoButtonDialog.Listener _acceptTerms_listener = new TwoButtonDialog.Listener() {
        @Override
        public void onPositive() {
            _profileBounceProtect = false;
            ProfileClient.actionAcceptTos(AuthActionBarActivity.this, _profile.getUserId());
        }

        @Override
        public void onNegative() {
            // hide, continue
            _profileBounceProtect = false;
            App.get().setTosReminded();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile(_profile);
                }
            });
        }

        @Override
        public void onCancel() {
        }
    };

    private final TwoButtonDialog.Listener _coi_listener = new TwoButtonDialog.Listener() {
        @Override
        public void onPositive() {
            _profileBounceProtect = false;
            App.get().setCoiReminded();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile(_profile);
                }
            });
        }

        @Override
        public void onNegative() {
            _profileBounceProtect = false;
            App.get().setNeverRemindCoi();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile(_profile);
                }
            });
        }

        @Override
        public void onCancel() {
        }
    };

    private final ActivityResultClient.Listener _activityResultClient_listener = new ActivityResultClient.Listener() {
        @Override
        public void onConnected() {
            _activityResultClient.subStartActivity();
            _activityResultClient.subStartActivityForResult();
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            Log.v(TAG, "startActivityForResult");
            AuthActionBarActivity.this.startActivityForResult(intent, requestCode);
        }

        @Override
        public void startActivity(Intent intent) {
            Log.v(TAG, "startActivity");
            AuthActionBarActivity.this.startActivity(intent);
        }
    };

    private final AuthTopicClient.Listener _authTopicClient_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            _authTopicClient.subNeedUsernameAndPassword();
        }

        @Override
        public void onNeedUsernameAndPassword(Parcelable authenticatorResponse) {
            Intent intent = new Intent(AuthActionBarActivity.this, AuthActivity.class);

            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, authenticatorResponse);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    private final GlobalTopicClient.Listener _globalClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.subGotProfile();
            _globalClient.subUpdateApp();
            _globalClient.subAppShutdown();
            _globalClient.subShowContactUsDialog();
            //_globalClient.subNetworkState();
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            gotProfile(profile);
        }

        @Override
        public void onNeedAppUpdate() {
            try {
                _updateDialog.show();
            } catch (Exception ex) {
                Debug.logException(ex);
            }
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
                Debug.logException(ex);
            }
        }

        @Override
        public void onNetworkDisconnected() {
            //Intent intent = GlobalTopicClient.networkConnectIntent(App.get());
            //if (intent != null) {
            //    PendingIntent pi = PendingIntent.getService(App.get(), 0, intent, 0);
            //    ToastClient.snackbar(App.get(), "Can't connect to servers.", "RETRY", pi, Snackbar.LENGTH_INDEFINITE);
            //}
        }
    };

    private final ToastClient.Listener _toastClient_listener = new ToastClient.Listener() {
        @Override
        public Activity getActivity() {
            return AuthActionBarActivity.this;
        }

        @Override
        public ToastClient getToastClient() {
            return _toastClient;
        }
    };
}