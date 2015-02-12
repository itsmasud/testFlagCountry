package com.fieldnation.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.rpc.server.ClockReceiver;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.TopicShutdownReciever;
import com.fieldnation.topics.Topics;
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
    private TwoButtonDialog _acceptTermsDialog;

    // Services
    private TopicShutdownReciever _shutdownListener;

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

        ClockReceiver.registerClock(this);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayUseLogoEnabled(true);
        //actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_previous_item);

        _updateDialog = UpdateDialog.getInstance(getSupportFragmentManager(), TAG);
        _acceptTermsDialog = TwoButtonDialog.getInstance(getSupportFragmentManager(), TAG);
        try {
            _acceptTermsDialog.setData(
                    getString(R.string.dialog_accept_terms_title),
                    String.format("By accepting you agree to the new <a href=\"https://app.fieldnation.com/legal/?a=provider\">Terms of Service</a> and acknowledge the additional %1$s%% fee per work order if you do not upload a certificate of insurance.<br/><br/>You have <b>%2$s days</b> until new <a href=\"https://app.fieldnation.com/legal/?a=provider\">Terms of Service</a> are in effect.", "1.3", "14"),
//                    getString(R.string.dialog_accept_terms_body, "1.3", "14"),
                    getString(R.string.btn_accept),
                    getString(R.string.btn_later), _acceptTerms_listener);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        super.onResume();
        AuthTopicService.subscribeAuthState(this, AUTH_SERVICE, TAG, _authReceiver);
        _shutdownListener = new TopicShutdownReciever(this, new Handler(), TAG + ":SHUTDOWN");
        TopicService.registerListener(this, 0, TAG + ":NEED_UPDATE", Topics.TOPIC_NEED_UPDATE, _topic_needUpdate);
        //_acceptTermsDialog.setCancelable(false);
        //_acceptTermsDialog.show();
    }

    @Override
    protected void onPause() {
        TopicService.delete(this, TAG);
        TopicService.unRegisterListener(this, 0, TAG + ":NEED_UPDATE", Topics.TOPIC_NEED_UPDATE);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (_shutdownListener != null)
            _shutdownListener.onPause();
        super.onDestroy();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final TwoButtonDialog.Listener _acceptTerms_listener = new TwoButtonDialog.Listener() {
        @Override
        public void onPositive() {

        }

        @Override
        public void onNegative() {

        }

        @Override
        public void onCancel() {

        }
    };

    private final AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            AuthActionBarActivity.this.onAuthentication(username, authToken, isNew);
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(AuthActionBarActivity.this);
        }


        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            AuthActionBarActivity.this.onAuthenticationFailed(networkDown);
        }

        @Override
        public void onAuthenticationInvalidated() {
            AuthActionBarActivity.this.onAuthenticationInvalidated();
        }

    };

    private final TopicReceiver _topic_needUpdate = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (Topics.TOPIC_NEED_UPDATE.equals(topicId)) {
                _updateDialog.show();
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
/*
            Intent gohome = new Intent(this, MyWorkActivity.class);
			startActivity(gohome);
*/
                onBackPressed();
                return true;
//            case R.id.action_settings:
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
//                return true;
//            case R.id.action_refresh:
//                onRefresh();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void onRefresh();

}
