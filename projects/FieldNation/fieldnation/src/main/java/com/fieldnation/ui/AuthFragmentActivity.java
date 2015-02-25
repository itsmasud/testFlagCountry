package com.fieldnation.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.fieldnation.GlobalState;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.rpc.webclient.ProfileWebService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.TopicShutdownReciever;
import com.fieldnation.topics.Topics;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.dialog.UpdateDialog;

/**
 * Created by michael.carver on 12/5/2014.
 */
public abstract class AuthFragmentActivity extends FragmentActivity {
    private static final String TAG_BASE = "ui.AuthFragmentActivity";
    private String TAG = TAG_BASE;

    private static final int AUTH_SERVICE = 1;

    private static final String STATE_TAG = TAG_BASE + ".STATE_TAG";

    // UI
    NotificationActionBarView _notificationsView;
    MessagesActionBarView _messagesView;

    private UpdateDialog _updateDialog;
    private OneButtonDialog _notProviderDialog;
    private TwoButtonDialog _acceptTermsDialog;
    private TwoButtonDialog _coiWarningDialog;

    // Services
    private ProfileWebService _service;
    private TopicShutdownReciever _shutdownListener;

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
        AuthTopicService.subscribeAuthState(this, AUTH_SERVICE, TAG, _authReceiver);
        _shutdownListener = new TopicShutdownReciever(this, new Handler(), TAG + ":SHUTDOWN");
        TopicService.registerListener(this, 0, TAG + ":NEED_UPDATE", Topics.TOPIC_NEED_UPDATE, _topicReceiver);
        Topics.subscrubeProfileUpdated(this, TAG + ":PROFILE", _topicReceiver);

        _notProviderDialog.setData("User Not Supported",
                "Currently Buyer and Service Company accounts are not supported. Please log in with a provider account.",
                "OK", _notProvider_listener);

        gotProfile();
    }

    @Override
    protected void onPause() {
        isPaused = true;
        TopicService.delete(this, TAG);
        TopicService.unRegisterListener(this, 0, TAG + ":NEED_UPDATE", Topics.TOPIC_NEED_UPDATE);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_TAG, TAG);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (_shutdownListener != null)
            _shutdownListener.onPause();
        TopicService.unRegisterListener(this, 0, TAG + ":PROFILE", Topics.TOPIC_PROFILE_UPDATE);
        super.onDestroy();
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
            AuthTopicService.requestAuthRemove(AuthFragmentActivity.this);
        }

        @Override
        public void onCancel() {
            AuthTopicService.requestAuthRemove(AuthFragmentActivity.this);
        }
    };


    private final TwoButtonDialog.Listener _acceptTerms_listener = new TwoButtonDialog.Listener() {
        @Override
        public void onPositive() {
            _profileBounceProtect = false;
            startService(_service.acceptTos(0, _profile.getUserId()));
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

    private final AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(AuthFragmentActivity.this);
        }

        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_service == null || isNew) {
                _service = new ProfileWebService(AuthFragmentActivity.this, username, authToken, _webReceiver);
            }
            AuthFragmentActivity.this.onAuthentication(username, authToken, isNew);
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            AuthFragmentActivity.this.onAuthenticationFailed(networkDown);
        }

        @Override
        public void onAuthenticationInvalidated() {
            AuthFragmentActivity.this.onAuthenticationInvalidated();
        }
    };

    private final WebResultReceiver _webReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            Log.v(TAG, "WebResultReceiver");
            _profileBounceProtect = false;
            Topics.dispatchProfileInvalid(AuthFragmentActivity.this);
        }

        @Override
        public Context getContext() {
            return AuthFragmentActivity.this;
        }
    };

    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (Topics.TOPIC_NEED_UPDATE.equals(topicId)) {
                _updateDialog.show();
            }

            if (Topics.TOPIC_PROFILE_UPDATE.equals(topicId)) {
                Log.v(TAG, "TOPIC_PROFILE_UPDATE");
                parcel.setClassLoader(getClassLoader());
                _profile = parcel.getParcelable(Topics.TOPIC_PROFILE_PARAM_PROFILE);
                gotProfile();
            }
        }
    };

    public abstract void onAuthentication(String username, String authToken, boolean isNew);

    public void onAuthenticationFailed(boolean networkDown) {
    }

    public void onAuthenticationInvalidated() {
    }

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
