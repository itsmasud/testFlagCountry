package com.fieldnation.ui;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.fieldnation.R;
import com.fieldnation.utils.misc;

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
    private DrawerView _drawerView;
    private ViewStub _switchUserOverlayViewStub;
    SwitchUserOverlayView _switchUserOverlay = null;

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
        _drawerView = (DrawerView) findViewById(R.id.leftDrawerView);
        _drawerView.setListener(_drawerView_lsitener);
        _switchUserOverlayViewStub = (ViewStub) findViewById(R.id.switchUserOverlay_viewstub);

    }

    private FrameLayout getBody() {
        if (_body == null)
            _body = (FrameLayout) findViewById(R.id.body_container);

        return _body;
    }

    public void showMessageNav() {
        misc.hideKeyboard(this);
        _messageDrawerView.animateShow();
    }

    public void showNotificationNav() {
        misc.hideKeyboard(this);
        _notificationDrawerView.animateShow();
    }

    public Toolbar getToolbar() {
        return _toolbar;
    }

    public void showLeftNav() {
        _drawerLayout.openDrawer(GravityCompat.START);
    }

    /**
     * @return true if action handled, false otherwise
     */
    public boolean onBackPressed() {
        boolean handled = false;

        if (_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            _drawerLayout.closeDrawer(GravityCompat.START);
            handled = true;
        }

        if (_messageDrawerView.isOpen()) {
            _messageDrawerView.animateHide();
            handled = true;
        }

        if (_notificationDrawerView.isOpen()) {
            _notificationDrawerView.animateHide();
            handled = true;
        }

        return handled;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.drawer_content || getBody() == null) {
            super.addView(child, index, params);
        } else {
            getBody().addView(child, index, params);
        }
    }

    private final DrawerView.Listener _drawerView_lsitener = new DrawerView.Listener() {
        @Override
        public void onSwitchUser(long userId) {
            if (_switchUserOverlay == null){
                _switchUserOverlay = (SwitchUserOverlayView) _switchUserOverlayViewStub.inflate();
            }
            _switchUserOverlay.startSwitch(userId);
        }
    };
}
