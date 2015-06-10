package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.ui.RightDrawerMessagesView;
import com.fieldnation.ui.RightDrawerNotificationsView;
import com.fieldnation.ui.TabActionBarFragmentActivity;
import com.fieldnation.utils.misc;

import java.util.List;

public class MyWorkActivity extends TabActionBarFragmentActivity {
    private static final String TAG = "MyWorkActivity";

    // Data
    private WorkorderListFragment[] _fragments;
    private String[] _titles;
    private RightDrawerMessagesView _messagesView;
    private RightDrawerNotificationsView _notificationsView;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        setTitle(R.string.mywork_title);
        super.onFinishCreate(savedInstanceState);

        _messagesView = (RightDrawerMessagesView) findViewById(R.id.rightDrawerMessages_view);
        _notificationsView = (RightDrawerNotificationsView) findViewById(R.id.rightDrawerNotifications_view);
    }

    @Override
    public void loadFragments() {
        _fragments = new WorkorderListFragment[3];
        _fragments[0] = getFragment(WorkorderDataSelector.ASSIGNED);
        _fragments[1] = getFragment(WorkorderDataSelector.COMPLETED);
        _fragments[2] = getFragment(WorkorderDataSelector.CANCELED);

        _titles = new String[]{getString(R.string.tab_assigned), getString(R.string.tab_completed), getString(R.string.tab_canceled)};
    }

    @Override
    public void onMessagesClick() {
        Log.v(TAG, "onMessagesClick");
        _messagesView.animateShow();
    }

    @Override
    public void onNotificationClick() {
        _notificationsView.animateShow();
    }

    private WorkorderListFragment getFragment(WorkorderDataSelector selector) {
        Fragment fragment = null;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                if (fragments.get(i) instanceof WorkorderListFragment) {
                    WorkorderListFragment frag = (WorkorderListFragment) fragments.get(i);
                    if (frag.getDisplayType() == selector) {
                        fragment = frag;
                        break;
                    }
                }
            }
        }

        if (fragment == null) {
            fragment = new WorkorderListFragment().setDisplayType(selector);
        }
        return (WorkorderListFragment) fragment;
    }

    @Override
    public int getFragmentCount() {
        return _fragments.length;
    }

    @Override
    public String getFragmentTitle(int index) {
        return _titles[index];
    }

    @Override
    public TabFragment getFragment(int index) {
        return _fragments[index];
    }

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        misc.printStackTrace("startNew");
        Intent intent = new Intent(context, MyWorkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
