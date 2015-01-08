package com.fieldnation.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
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
 * Created by michael.carver on 12/5/2014.
 */
public abstract class AuthFragmentActivity extends FragmentActivity {
    private final String TAG = UniqueTag.makeTag("ui.AuthFragmentActivity");

    private static final int AUTH_SERVICE = 1;

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
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(AuthFragmentActivity.this);
        }

        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
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
