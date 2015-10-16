package com.fieldnation.ui;

import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.workorder.detail.NotificationView;

import java.util.List;

public class NotificationListActivity extends ItemListActivity<Notification> {
    private static final String TAG = "NotificationListActivity";

    // Data
    private ProfileClient _profiles;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onResume() {
        super.onResume();
        _profiles = new ProfileClient(_profile_listener);
        _profiles.connect(this);
    }

    @Override
    protected void onPause() {
        _profiles.disconnect(this);
        super.onPause();
    }

    @Override
    public void requestData(int page) {
        Log.v(TAG, "requestData " + page);
        ProfileClient.listNotifications(this, page);
    }

    @Override
    public View getView(Notification object, View convertView, ViewGroup parent) {
        NotificationView v = null;
        if (convertView == null) {
            v = new NotificationView(parent.getContext());
        } else if (convertView instanceof NotificationView) {
            v = (NotificationView) convertView;
        } else {
            v = new NotificationView(parent.getContext());
        }

        v.setNotification(object);

        return v;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notifications, menu);

        _messagesView = (MessagesActionBarView) MenuItemCompat.getActionView(menu.findItem(R.id.messages_menuitem));

        return true;
    }


    private final ProfileClient.Listener _profile_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profiles.subListNotifications();
        }

        @Override
        public void onNotificationList(List<Notification> list, int page, boolean failed, boolean isCached) {
            // TODO failed condition
            Log.v(TAG, "onAllNotificationPage " + page);
            addPage(page, list); // done
        }
    };
}
