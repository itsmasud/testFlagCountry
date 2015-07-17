package com.fieldnation.ui;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 6/10/2015.
 */
public class ActionBarDrawerView extends FrameLayout {
    private static final String TAG = "ActionBarDrawerView";

    // Ui
    private Toolbar _toolbar;
    private FrameLayout _body;
    private RightDrawerMessagesView _messageDrawerView;
    private RightDrawerNotificationsView _notificationDrawerView;
    private WarningView _warningView;
    private DrawerLayout _drawerLayout;

    public ActionBarDrawerView(Context context) {
        super(context);
        init();
    }

    public ActionBarDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionBarDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_drawers_actionbar, this);

        if (isInEditMode())
            return;

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _body = (FrameLayout) findViewById(R.id.body_container);
        _messageDrawerView = (RightDrawerMessagesView) findViewById(R.id.rightDrawerMessages_view);
        _notificationDrawerView = (RightDrawerNotificationsView) findViewById(R.id.rightDrawerNotifications_view);
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_content);
    }

    private FrameLayout getBody() {
        if (_body == null)
            _body = (FrameLayout) findViewById(R.id.body_container);

        return _body;
    }

    public void showMessageNav() {
        _messageDrawerView.animateShow();
    }

    public void showNotificationNav() {
        _notificationDrawerView.animateShow();
    }

    public Toolbar getToolbar() {
        return _toolbar;
    }

    public void showLeftNav() {
        _drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.drawer_content) {
            super.addView(child, index, params);
        } else {
            getBody().addView(child, index, params);
        }
    }
}
