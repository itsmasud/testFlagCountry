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
import com.fieldnation.topics.TopicService;
import com.fieldnation.topics.TopicShutdownReciever;

/**
 * This is the base of all the activities in this project. It provides
 * authentication and sets up the action bars.
 *
 * @author michael.carver
 */
public abstract class AuthActionBarActivity extends ActionBarActivity {
    private String TAG = UniqueTag.makeTag("ui.AuthActionBarActivity");

    private final static int AUTH_SERVICE = 1;

    // UI
    NotificationActionBarView _notificationsView;
    MessagesActionBarView _messagesView;

    // Services
    private TopicShutdownReciever _shutdownListener;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClockReceiver.registerClock(this);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayUseLogoEnabled(true);
        //actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_previous_item);

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
    }

    @Override
    protected void onPause() {
        TopicService.delete(this, TAG);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        _shutdownListener.onPause();
        super.onDestroy();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
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
