package com.fieldnation.ui;

import android.accounts.AccountManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.dialog.FeedbackDialog;
import com.fieldnation.ui.dialog.HelpDialog;
import com.fieldnation.ui.dialog.OneButtonDialog;
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
    NotificationActionBarView _notificationsView;
    MessagesActionBarView _messagesView;
    private ActionBarDrawerView _actionBarView;

    private UpdateDialog _updateDialog;
    private OneButtonDialog _notProviderDialog;
    private TwoButtonDialog _acceptTermsDialog;
    private TwoButtonDialog _coiWarningDialog;
    private FeedbackDialog _feedbackDialog;
    private HelpDialog _helpDialog;

    // Services
    private GlobalTopicClient _globalClient;
    private ToastClient _toastClient;
    private AuthTopicClient _authTopicClient;

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

        _acceptTermsDialog = TwoButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":TOS");
        _acceptTermsDialog.setCancelable(false);

        _coiWarningDialog = TwoButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":COI");
        _coiWarningDialog.setCancelable(false);

        _notProviderDialog = OneButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":NOT_SUPPORTED");
        onFinishCreate(savedInstanceState);

        _feedbackDialog = FeedbackDialog.getInstance(getSupportFragmentManager(), TAG);

        _helpDialog = HelpDialog.getInstance(getSupportFragmentManager(), TAG);
    }

    public abstract int getLayoutResource();

    public abstract void onFinishCreate(Bundle savedInstanceState);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        _notificationsView = (NotificationActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.notifications_menuitem));
        _notificationsView.setOnClickListener(_notifications_onClick);

        _messagesView = (MessagesActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.messages_menuitem));
        _messagesView.setOnClickListener(_messages_onClick);

        return true;
    }

    private final View.OnClickListener _notifications_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _actionBarView.showNotificationNav();
        }
    };

    private final View.OnClickListener _messages_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _actionBarView.showMessageNav();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        _toastClient = new ToastClient(_toastListener);
        _toastClient.connect(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _globalClient = new GlobalTopicClient(_globalListener);
        _globalClient.connect(this);
        _authTopicClient = new AuthTopicClient(_authTopicClient_listener);
        _authTopicClient.connect(this);

        _notProviderDialog.setData("User Not Supported",
                "Currently Buyer and Service Company accounts are not supported. Please log in with a provider account.",
                "OK", _notProvider_listener);
    }

    @Override
    protected void onPause() {
        if (_globalClient != null && _globalClient.isConnected())
            _globalClient.disconnect(this);

        if (_authTopicClient != null && _authTopicClient.isConnected()) {
            _authTopicClient.disconnect(this);
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        _toastClient.disconnect(this);
        super.onStop();
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

        if (!_profile.isProvider()) {
//            _notProviderDialog.show();
//            return;
        }
        App gs = App.get();
        if (!profile.getAcceptedTos() && (gs.canRemindTos() || profile.isTosRequired())) {
            Log.v(TAG, "Asking Tos");
            if (profile.isTosRequired()) {
                Log.v(TAG, "Asking Tos, hard");
                _acceptTermsDialog.setData(getString(R.string.dialog_accept_terms_title),
                        getString(R.string.dialog_accept_terms_body_hard, profile.insurancePercent()),
                        getString(R.string.btn_accept),
                        _acceptTerms_listener);
            } else {
                Log.v(TAG, "Asking Tos, soft");
                _acceptTermsDialog.setData(
                        getString(R.string.dialog_accept_terms_title),
                        getString(R.string.dialog_accept_terms_body_soft, profile.insurancePercent(), profile.daysUntilRequired()),
                        getString(R.string.btn_accept),
                        getString(R.string.btn_later), _acceptTerms_listener);
            }
            _acceptTermsDialog.show();
        } else if (!profile.hasValidCoi() && gs.canRemindCoi() && _profile.getCanViewPayments()) {
            Log.v(TAG, "Asking coi");
            _coiWarningDialog.setData(
                    getString(R.string.dialog_coi_title),
                    getString(R.string.dialog_coi_body, profile.insurancePercent()),
                    getString(R.string.btn_later),
                    getString(R.string.btn_no_later),
                    _coi_listener);

            _coiWarningDialog.show();
        } else {
            Log.v(TAG, "tos/coi check done");
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
        if (!_actionBarView.onBackPressed())
            super.onBackPressed();
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

    private final GlobalTopicClient.Listener _globalListener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.registerGotProfile();
            _globalClient.registerUpdateApp();
            _globalClient.registerAppShutdown();
            _globalClient.registerShowFeedbackDialog();
            _globalClient.registerShowHelpDialog();
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
            finish();
        }

        @Override
        public void onShowFeedbackDialog() {
            _feedbackDialog.show();
        }

        @Override
        public void onShowHelpDialog() {
            _helpDialog.show();
        }
    };

    private final ToastClient.Listener _toastListener = new ToastClient.Listener() {
        @Override
        public void onConnected() {
            _toastClient.subSnackbar();
            _toastClient.subToast();
        }

        @Override
        public void showSnackBar(String title, String buttonText, final PendingIntent buttonIntent, int duration) {
            if (findViewById(android.R.id.content) == null) {
                Log.v(TAG, "showSnackBar.findViewById() == null");
                return;
            }

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), title, duration);
            TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(getResources().getColor(R.color.fn_white_text));
            snackbar.setActionTextColor(getResources().getColor(R.color.fn_clickable_text));

            if (buttonText != null) {
                snackbar.setAction(buttonText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (buttonIntent != null) {
                            try {
                                buttonIntent.send(AuthActionBarActivity.this, 0, new Intent());
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            snackbar.show();
        }

        @Override
        public void showToast(String title, int duration) {
            Toast.makeText(AuthActionBarActivity.this, title, duration).show();
        }
    };
}

