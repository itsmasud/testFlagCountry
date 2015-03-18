package com.fieldnation.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fieldnation.GlobalState;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.data.profile.ProfileDataClient;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.UpdateDialog;

/**
 * This is the base of all the activities in this project. It provides
 * authentication and sets up the action bars.
 *
 * @author michael.carver
 */
public abstract class AuthActionBarActivity extends ActionBarActivity {
    private static final String TAG_BASE = "ui.AuthActionBarActivity";
    private String TAG = TAG_BASE;

    private final static int AUTH_SERVICE = 1;

    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // UI
    NotificationActionBarView _notificationsView;
    MessagesActionBarView _messagesView;

    private UpdateDialog _updateDialog;
    private OneButtonDialog _notProviderDialog;
    private TwoButtonDialog _acceptTermsDialog;
    private TwoButtonDialog _coiWarningDialog;

    // Services
    private GlobalTopicClient _globalClient;

    // Data
    private Profile _profile;
    private boolean _profileBounceProtect = false;
    protected boolean isPaused = true;

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

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayUseLogoEnabled(true);
        //actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_previous_item);

        _updateDialog = UpdateDialog.getInstance(getSupportFragmentManager(), TAG);
        _acceptTermsDialog = TwoButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":TOS");
        _acceptTermsDialog.setCancelable(false);
        _coiWarningDialog = TwoButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":COI");
        _coiWarningDialog.setCancelable(false);
        _notProviderDialog = OneButtonDialog.getInstance(getSupportFragmentManager(), TAG + ":NOT_SUPPORTED");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        _notificationsView = (NotificationActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.notifications_menuitem));
        _messagesView = (MessagesActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.messages_menuitem));

        return true;
    }

    @Override
    protected void onResume() {
        isPaused = false;
        super.onResume();
        _globalClient = new GlobalTopicClient(_globalListener);
        _globalClient.connect(this);

        _notProviderDialog.setData("User Not Supported",
                "Currently Buyer and Service Company accounts are not supported. Please log in with a provider account.",
                "OK", _notProvider_listener);

        gotProfile();
    }

    @Override
    protected void onPause() {
        isPaused = true;
        _globalClient.disconnect(this);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_TAG, TAG);
        super.onSaveInstanceState(outState);
    }

    private void gotProfile() {
        if (_profileBounceProtect)
            return;

        if (isPaused)
            return;

        if (_profile == null)
            return;

        _profileBounceProtect = true;

        if (!_profile.isProvider()) {
            _notProviderDialog.show();
            return;
        }
        GlobalState gs = GlobalState.getContext();
        if (!_profile.getAcceptedTos() && (gs.canRemindTos() || _profile.isTosRequired())) {
            Log.v(TAG, "Asking Tos");
            if (_profile.isTosRequired()) {
                Log.v(TAG, "Asking Tos, hard");
                _acceptTermsDialog.setData(getString(R.string.dialog_accept_terms_title),
                        getString(R.string.dialog_accept_terms_body_hard, _profile.insurancePercent()),
                        getString(R.string.btn_accept),
                        _acceptTerms_listener);
            } else {
                Log.v(TAG, "Asking Tos, soft");
                _acceptTermsDialog.setData(
                        getString(R.string.dialog_accept_terms_title),
                        getString(R.string.dialog_accept_terms_body_soft, _profile.insurancePercent(), _profile.daysUntilRequired()),
                        getString(R.string.btn_accept),
                        getString(R.string.btn_later), _acceptTerms_listener);
            }
            _acceptTermsDialog.show();
        } else if (!_profile.hasValidCoi() && gs.canRemindCoi()) {
            Log.v(TAG, "Asking coi");
            _coiWarningDialog.setData(
                    getString(R.string.dialog_coi_title),
                    getString(R.string.dialog_coi_body, _profile.insurancePercent()),
                    getString(R.string.btn_later),
                    getString(R.string.btn_no_later),
                    _coi_listener);

            _coiWarningDialog.show();
        } else {
            Log.v(TAG, "tos/coi check done");
            onProfile(_profile);
            _profileBounceProtect = false;
        }
    }

    public void onProfile(Profile profile) {
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private final OneButtonDialog.Listener _notProvider_listener = new OneButtonDialog.Listener() {
        @Override
        public void onButtonClick() {
            AuthTopicClient.dispatchRemoveAuth(AuthActionBarActivity.this);
        }

        @Override
        public void onCancel() {
            AuthTopicClient.dispatchRemoveAuth(AuthActionBarActivity.this);
        }
    };


    private final TwoButtonDialog.Listener _acceptTerms_listener = new TwoButtonDialog.Listener() {
        @Override
        public void onPositive() {
            _profileBounceProtect = false;
            ProfileDataClient.acceptTos(AuthActionBarActivity.this, _profile.getUserId());
        }

        @Override
        public void onNegative() {
            // hide, continue
            _profileBounceProtect = false;
            GlobalState.getContext().setTosReminded();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile();
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
            GlobalState.getContext().setCoiReminded();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile();
                }
            });
        }

        @Override
        public void onNegative() {
            _profileBounceProtect = false;
            GlobalState.getContext().setNeverRemindCoi();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    gotProfile();
                }
            });
        }

        @Override
        public void onCancel() {
        }
    };

    private final GlobalTopicClient.Listener _globalListener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.registerGotProfile();
            _globalClient.registerUpdateApp();
        }

        @Override
        public void onGotProfile(Profile profile) {
            _profile = profile;
            gotProfile();
        }

        @Override
        public void onNeedAppUpdate() {
            _updateDialog.show();
        }
    };

    // Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void onRefresh();
}
