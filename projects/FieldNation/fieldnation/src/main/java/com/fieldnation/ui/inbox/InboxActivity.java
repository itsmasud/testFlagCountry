package com.fieldnation.ui.inbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.InboxTracker;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnactivityresult.ActivityClient;
import com.fieldnation.fnlog.Log;
import com.fieldnation.ui.TabActionBarFragmentActivity;

import java.util.List;

public class InboxActivity extends TabActionBarFragmentActivity {
    private static final String TAG = "InboxActivity";

    // UI
    private TabFragment[] _fragments;

    // Data
    private String[] _titles;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onFinishCreate");
        setTitle(R.string.inbox);
        super.onFinishCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");

        switchFragment(0);
    }

    @Override
    public void loadFragments() {
        _fragments = new TabFragment[2];
        _fragments[0] = getFragment(InboxMessagesListFragment.class);
        _fragments[1] = getFragment(InboxNotificationListFragment.class);

        _titles = new String[]{getString(R.string.tab_messages), getString(R.string.tab_notifications)};
    }

    private TabFragment getFragment(Class<? extends TabFragment> klass) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (int i = 0; i < fragments.size(); i++) {
                if (klass.isInstance(fragments.get(i))) {
                    return (TabFragment) fragments.get(i);
                }
            }
        }
        if (klass == InboxMessagesListFragment.class)
            return new InboxMessagesListFragment();
        else if (klass == InboxNotificationListFragment.class)
            return new InboxNotificationListFragment();

        return null;
    }

    @Override
    public int getFragmentCount() {
        return _fragments.length;
    }

    @Override
    public String getFragmentTitle(int index) {
        if (App.getProfile() != null) {
            if (index == 0 && App.getProfile().getUnreadMessageCount() > 0)
                return _titles[0] + " (" + App.getProfile().getUnreadMessageCount() + ")";
            if (index == 1 && App.getProfile().getNewNotificationCount() > 0)
                return _titles[1] + " (" + App.getProfile().getNewNotificationCount() + ")";
        }
        return _titles[index];
    }

    @Override
    public TabFragment getFragment(int index) {
        return _fragments[index];
    }

    @Override
    public void onProfile(Profile profile) {
    }

    @Override
    public void switchFragment(int position) {
        super.switchFragment(position);
        if (position == 0) {
            InboxTracker.onClickTab(App.get(), InboxTracker.Item.MESSAGES);
            InboxTracker.onShowMessages(App.get());
        } else {
            InboxTracker.onClickTab(App.get(), InboxTracker.Item.NOTIFICATIONS);
            InboxTracker.onShowNotifications(App.get());
        }
    }

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, InboxActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        ActivityClient.startActivity(intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}
