package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.workorder.detail.NotificationView;

import java.util.List;

/**
 * Created by Michael Carver on 6/10/2015.
 */
public class RightDrawerNotificationsView extends FrameLayout {
    private static final String TAG = "RightDrawerNotificationsView";

    // Ui
    private ListView _listView;
    private RightDrawerView _rightDrawer;

    private Button _closeButton;

    // Data
    private ProfileClient _profileClient;

    public RightDrawerNotificationsView(Context context) {
        super(context);
        init();
    }

    public RightDrawerNotificationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RightDrawerNotificationsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_right_drawer_alerts, this);

        if (isInEditMode())
            return;

        _rightDrawer = (RightDrawerView) findViewById(R.id.drawerView);
        _rightDrawer.setVisibility(GONE);

        _closeButton = (Button) findViewById(R.id.close_button);
        _closeButton.setOnClickListener(_close_onClick);
        _listView = (ListView) findViewById(R.id.listview);
        _listView.setAdapter(_adapter);

        _profileClient = new ProfileClient(_profileClient_listener);
        _profileClient.connect(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        _profileClient.disconnect(getContext());
        super.onDetachedFromWindow();
    }

    public void animateShow() {
        _rightDrawer.animateShow();
    }

    private final OnClickListener _close_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _rightDrawer.animateHide();
        }
    };

    private final PagingAdapter<Notification> _adapter = new PagingAdapter<Notification>() {
        @Override
        public View getView(int page, int position, Notification object, View convertView, ViewGroup parent) {
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
        public void requestPage(int page, boolean allowCache) {
            ProfileClient.listNotifications(getContext(), page);
        }
    };

    /*-*****************************************-*/
    /*-                Data Events              -*/
    /*-*****************************************-*/

    private final ProfileClient.Listener _profileClient_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.subListNotifications();
        }

        @Override
        public void onNotificationList(List<Notification> list, int page) {
            _adapter.setPage(page, list);
        }
    };
}
