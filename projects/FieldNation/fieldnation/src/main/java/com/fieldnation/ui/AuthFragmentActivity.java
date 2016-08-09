package com.fieldnation.ui;

import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.fnlog.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.ToSDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.UpdateDialog;

/**
 * Created by michael.carver on 12/5/2014.
 */
public abstract class AuthFragmentActivity extends FragmentActivity {
    private static final String TAG_BASE = "AuthFragmentActivity";
    private String TAG = TAG_BASE;

    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // UI
    private UpdateDialog _updateDialog;
    private OneButtonDialog _notProviderDialog;
    private TwoButtonDialog _coiWarningDialog;
    private ToSDialog _tosDialog;


    // Services
    private GlobalTopicClient _globalTopicClient;
    private AuthTopicClient _authTopicClient;
    private ToastClient _toastClient;
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
        _tosDialog = ToSDialog.getInstance(getSupportFragmentManager(), TAG);
        _coiWarningDialog = TwoButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":COI");
        _coiWarningDialog.setCancelable(false);
        _notProviderDialog = OneButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":NOT_SUPPORTED");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        _toastClient = new ToastClient(_toastListener);
        _toastClient.connect(App.get());
        _authTopicClient = new AuthTopicClient(_authTopicClient_listener);
        _authTopicClient.connect(App.get());
        _globalTopicClient = new GlobalTopicClient(_globalTopicClient_listener);
        _globalTopicClient.connect(App.get());
        _activityResultClient = new ActivityResultClient(_activityResultClient_listener);
        _activityResultClient.connect(App.get());

        _notProviderDialog.setData("User Not Supported",
                "Currently Buyer accounts are not supported. Please log in with a provider or service company account.",
                "OK", _notProvider_listener);
    }

    @Override
    protected void onPause() {
        if (_globalTopicClient != null && _globalTopicClient.isConnected())
            _globalTopicClient.disconnect(App.get());

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
        if (_profileBounceProtect)
            return;

        if (_profile == null)
            return;

        if (isFinishing())
            return;

        _profileBounceProtect = true;

        if (App.get().shouldShowToSDialog()) {
            _tosDialog.show();
        }

        if (_profile != null && !App.get().hasReleaseNoteShownForThisVersion()) {
            App.get().setReleaseNoteShownReminded();
            Intent intent = new Intent(App.get(), NewFeatureActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            App.get().startActivity(intent);
        }

        if (!_profile.isProvider()) {
            _notProviderDialog.show();
            return;
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
            Log.v(TAG, "tos/coi check done");
            onProfile(profile);
            _profileBounceProtect = false;
        }
    }

    public void onProfile(Profile profile) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultClient.onActivityResult(App.get(), requestCode, resultCode, data);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final ActivityResultClient.Listener _activityResultClient_listener = new ActivityResultClient.Listener() {
        @Override
        public void onConnected() {
            _activityResultClient.subStartActivityForResult();
            _activityResultClient.subStartActivity();
        }

        @Override
        public void startActivity(Intent intent) {
            Log.v(TAG, "startActivity");
            AuthFragmentActivity.this.startActivity(intent);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            Log.v(TAG, "startActivityForResult");
            AuthFragmentActivity.this.startActivityForResult(intent, requestCode);
        }
    };

    private final OneButtonDialog.Listener _notProvider_listener = new OneButtonDialog.Listener() {
        @Override
        public void onButtonClick() {
            AuthTopicClient.removeCommand(AuthFragmentActivity.this);
        }

        @Override
        public void onCancel() {
            AuthTopicClient.removeCommand(AuthFragmentActivity.this);
        }
    };


    private final TwoButtonDialog.Listener _acceptTerms_listener = new TwoButtonDialog.Listener() {
        @Override
        public void onPositive() {
            _profileBounceProtect = false;
            ProfileClient.actionAcceptTos(AuthFragmentActivity.this, _profile.getUserId());
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

    private final AuthTopicClient.Listener _authTopicClient_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            _authTopicClient.subNeedUsernameAndPassword();
        }

        @Override
        public void onNeedUsernameAndPassword(Parcelable authenticatorResponse) {
            Intent intent = new Intent(AuthFragmentActivity.this, AuthActivity.class);

            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, authenticatorResponse);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    private final GlobalTopicClient.Listener _globalTopicClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.subGotProfile();
            _globalTopicClient.subUpdateApp();
            _globalTopicClient.subAppShutdown();
            //_globalTopicClient.subNetworkState();
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            gotProfile(profile);
        }

        @Override
        public void onNeedAppUpdate() {
            _updateDialog.show();
        }

        @Override
        public void onShutdown() {
        }

        @Override
        public void onNetworkConnected() {
        }

        @Override
        public void onNetworkDisconnected() {
            //Intent intent = GlobalTopicClient.networkConnectIntent(App.get());
            //if (intent != null) {
            //    PendingIntent pi = PendingIntent.getService(App.get(), 0, intent, 0);
            //    ToastClient.snackbar(App.get(), "Can't connect to servers.", "RETRY", pi, Snackbar.LENGTH_INDEFINITE);
            //}
        }

        @Override
        public void onNetworkConnecting() {
        }

    };

    private final ToastClient.Listener _toastListener = new ToastClient.Listener() {
        private Snackbar _snackbar = null;
        private long _lastId = 0;

        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            _toastClient.subSnackbar();
            _toastClient.subToast();
        }

        @Override
        public void showSnackBar(long id, String title, String buttonText, final PendingIntent buttonIntent, int duration) {
            Log.v(TAG, "showSnackBar(" + title + ")");

            if (id > 0 && id == _lastId)
                return;

            if (findViewById(android.R.id.content) == null) {
                Log.v(TAG, "showSnackBar.findViewById() == null");
                return;
            }

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), title, duration);
            TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(getResources().getColor(R.color.fn_white_text));
            snackbar.setActionTextColor(getResources().getColor(R.color.fn_clickable_text));

            if (buttonText == null)
                buttonText = "DISMISS";

            snackbar.setAction(buttonText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_snackbar != null) {
                        _snackbar.dismiss();
                        _snackbar = null;
                        _lastId = 0;
                    }

                    if (buttonIntent != null) {
                        try {
                            buttonIntent.send(AuthFragmentActivity.this, 0, new Intent());
                        } catch (PendingIntent.CanceledException e) {
                            Log.v(TAG, e);
                        }
                    }
                }
            });

            snackbar.show();
            _snackbar = snackbar;
            _lastId = id;
            Log.v(TAG, "snackbar.show()");
        }

        @Override
        public void showToast(String title, int duration) {
            Log.v(TAG, "onConnected");
            Toast.makeText(AuthFragmentActivity.this, title, duration).show();
        }

        @Override
        public void dismissSnackBar(long id) {
            if (_snackbar == null)
                return;

            if (_lastId != id)
                return;

            try {
                _snackbar.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            _snackbar = null;
            _lastId = 0;
        }
    };
}
