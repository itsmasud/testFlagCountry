package com.fieldnation.ui;

import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.service.data.profile.ProfileDataClient;

import java.util.List;

public class NotificationListActivity extends ItemListActivity<Notification> {
    private static final String TAG = "ui.NotificationListActivity";

    // Data
    private ProfileDataClient _profiles;

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    public void requestData(int page) {
        ProfileDataClient.getAllNotifications(this, page);
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


/*
    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        if (_profiles == null || isNew) {
            _profiles = new ProfileDataClient(this, _profile_listener);
        }
    }
*/

    private ProfileDataClient.Listener _profile_listener = new ProfileDataClient.Listener() {
        @Override
        public void onProfile(Profile profile) {
        }

        @Override
        public void onAllNotificationPage(List<Notification> list, int page) {
            addPage(page, list);
        }

        @Override
        public void onConnected() {

        }
    };
}
