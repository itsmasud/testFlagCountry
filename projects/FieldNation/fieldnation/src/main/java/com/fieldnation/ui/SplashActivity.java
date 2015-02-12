package com.fieldnation.ui;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.fieldnation.GlobalState;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.auth.server.AuthActivity;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.Topics;
import com.fieldnation.ui.dialog.OneButtonDialog;
import com.fieldnation.ui.dialog.TwoButtonDialog;
import com.fieldnation.ui.workorder.MyWorkActivity;

/**
 * Created by michael.carver on 12/18/2014.
 */
public class SplashActivity extends AuthFragmentActivity {
    private static final String TAG = "ui.SplashActivity";

    private static final String STATE_PROFILE = "STATE_PROFILE";
    private static final String STATE_IS_AUTH = "STATE_IS_AUTH";
    private static final String STATE_SHOWING_DIALOG = "STATE_SHOWING_DIALOG";

    private Profile _profile = null;
    private boolean _isAuth = false;
    private boolean _showingDialog = false;
    private boolean _calledMyWork = false;

    private OneButtonDialog _notProviderDialog;
    private TwoButtonDialog _acceptTermsDialog;

    public SplashActivity() {
        super();
        Log.v(TAG, "Construct");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_IS_AUTH)) {
                _isAuth = savedInstanceState.getBoolean(STATE_IS_AUTH);
            }
            if (savedInstanceState.containsKey(STATE_PROFILE)) {
                _profile = savedInstanceState.getParcelable(STATE_PROFILE);
            }
            if (savedInstanceState.containsKey(STATE_SHOWING_DIALOG)) {
                _showingDialog = savedInstanceState.getBoolean(STATE_SHOWING_DIALOG);
            }
        }

        Log.v(TAG, "onCreate");

        _notProviderDialog = OneButtonDialog.getInstance(getSupportFragmentManager(), TAG);

        _acceptTermsDialog = TwoButtonDialog.getInstance(getSupportFragmentManager(), TAG);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_IS_AUTH, _isAuth);
        outState.putBoolean(STATE_SHOWING_DIALOG, _showingDialog);
        if (_profile != null) {
            outState.putParcelable(STATE_PROFILE, _profile);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        Topics.subscrubeProfileUpdated(this, TAG + ":PROFILE", _topicReceiver);
        AuthTopicService.requestAuthInvalid(this);
        AuthTopicService.subscribeNeedUsernameAndPassword(this, TAG, _topicReceiver);
        AuthTopicService.requestAuthentication(this);
        _notProviderDialog.setData("User Not Supported",
                "Currently Buyer and Service Company accounts are not supported. Please log in with a provider account.",
                "OK", _notProvider_listener);
    }

    @Override
    protected void onDestroy() {
        TopicService.unRegisterListener(this, 0, TAG, AuthTopicService.TOPIC_AUTH_COMMAND);
        TopicService.unRegisterListener(this, 0, TAG + ":PROFILE", Topics.TOPIC_PROFILE_UPDATE);
        super.onDestroy();
    }

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        Log.v(TAG, "onAuthentication");
        _isAuth = true;
        doNextStep();
    }

    @Override
    public void onAuthenticationFailed(boolean networkDown) {
        Log.v(TAG, "onAuthenticationFailed");
        if (!networkDown)
            AuthTopicService.requestAuthentication(this);
    }

    @Override
    public void onAuthenticationInvalidated() {
        Log.v(TAG, "onAuthenticationInvalidated");
        AuthTopicService.requestAuthentication(this);
    }

    private void doNextStep() {
        if (!_isAuth)
            return;

        if (_profile == null)
            return;

        if (_showingDialog)
            return;

        Log.v(TAG, "doNextStep");

        if (_profile.isProvider()) {

            try {
                _acceptTermsDialog.setData(
                        getString(R.string.dialog_accept_terms_title),
                        String.format("By accepting you agree to the new <a href=\"https://app.fieldnation.com/legal/?a=provider\">Terms of Service</a> and acknowledge the additional %1$s%% fee per work order if you do not upload a certificate of insurance.<br/><br/>You have <b>%2$s days</b> until new <a href=\"https://app.fieldnation.com/legal/?a=provider\">Terms of Service</a> are in effect.", "1.3", "14"),
                        getString(R.string.btn_accept),
                        getString(R.string.btn_later),
                        _acceptTerms_listener);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // todo test ToS checked
            // if ToS Not checked, then
            if (GlobalState.getContext().canRemindTos()) {
//                _acceptTermsDialog.show();
            }

            if (!_calledMyWork) {
                _calledMyWork = true;
                MyWorkActivity.startNew(this);
                finish();
            }
        } else {
            _showingDialog = true;
            _notProviderDialog.show();
        }

    }

    private final TwoButtonDialog.Listener _acceptTerms_listener = new TwoButtonDialog.Listener() {
        @Override
        public void onPositive() {
            // TODO do something
        }

        @Override
        public void onNegative() {
            // later
            GlobalState.getContext().setTosLater();
        }

        @Override
        public void onCancel() {
            GlobalState.getContext().setTosLater();
        }
    };

    private final OneButtonDialog.Listener _notProvider_listener = new OneButtonDialog.Listener() {
        @Override
        public void onButtonClick() {
            AuthTopicService.requestAuthRemove(SplashActivity.this);
        }

        @Override
        public void onCancel() {
            AuthTopicService.requestAuthRemove(SplashActivity.this);
        }
    };

    private final TopicReceiver _topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            Log.v(TAG, "onTopic");
            if (SplashActivity.this == null)
                return;

            if (AuthTopicService.TOPIC_AUTH_STARTUP.equals(topicId)) {
                if (parcel.getString(AuthTopicService.BUNDLE_PARAM_TYPE)
                        .equals(AuthTopicService.BUNDLE_PARAM_TYPE_NEED_PASSWORD)
                        && parcel.containsKey(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)) {

                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);

                    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                            parcel.getParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE));

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            if (Topics.TOPIC_PROFILE_UPDATE.equals(topicId)) {
                Log.v(TAG, "TOPIC_PROFILE_UPDATE");
                parcel.setClassLoader(getClassLoader());
                _profile = parcel.getParcelable(Topics.TOPIC_PROFILE_PARAM_PROFILE);
                doNextStep();
            }

        }
    };


    @Override
    public void onRefresh() {
    }

    public static void startNew(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
