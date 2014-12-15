package com.fieldnation.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fieldnation.FutureWaitAsyncTask;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.rpc.server.ClockReceiver;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.workorder.MyWorkActivity;

/**
 * Created by michael.carver on 12/5/2014.
 */
public abstract class AuthFragmentActivity extends FragmentActivity {
    private static final String TAG = "ui.BaseActivity";

    private static final int AUTH_SERVICE = 1;

    // UI
    NotificationActionBarView _notificationsView;
    MessagesActionBarView _messagesView;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthTopicService.startService(this);
        AuthTopicService.subscribeAuthState(this, AUTH_SERVICE, TAG, _authReceiver);

        ClockReceiver.registerClock(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        _notificationsView = (NotificationActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.notifications_menuitem));
        // _notificationsView.setCount(10);
        _messagesView = (MessagesActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.messages_menuitem));

        return true;
    }

    @Override
    protected void onPause() {
        TopicService.delete(this, 0, TAG);
        super.onPause();
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver() {
        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(AuthFragmentActivity.this);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            AuthFragmentActivity.this.onAuthentication(username, authToken);
        }

        @Override
        public void onAuthenticationFailed() {
            AuthTopicService.requestAuthentication(AuthFragmentActivity.this);
            AuthFragmentActivity.this.onAuthenticationFailed();
        }

        @Override
        public void onAuthenticationInvalidated() {
            AuthTopicService.requestAuthentication(AuthFragmentActivity.this);
            AuthFragmentActivity.this.onAuthenticationInvalidated();
        }

    };

    public abstract void onAuthentication(String username, String authToken);

    public void onAuthenticationFailed() {
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
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                onRefresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract void onRefresh();

}
