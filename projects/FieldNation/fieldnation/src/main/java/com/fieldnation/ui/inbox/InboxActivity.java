package com.fieldnation.ui.inbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.ui.ActionBarDrawerView;
import com.fieldnation.ui.TabActionBarFragmentActivity;

import java.util.List;

public class InboxActivity extends TabActionBarFragmentActivity {
    private static final String TAG = "InboxActivity";

    // Data
    private TabFragment[] _fragments;
    private String[] _titles;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBarDrawerView actionBarView = (ActionBarDrawerView) findViewById(R.id.actionbardrawerview);
        Toolbar toolbar = actionBarView.getToolbar();
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(_toolbarNavication_listener);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        setTitle(R.string.inbox);
        super.onFinishCreate(savedInstanceState);
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
        return _titles[index];
    }

    @Override
    public TabFragment getFragment(int index) {
        return _fragments[index];
    }

    private final View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public static void startNew(Context context) {
        Log.v(TAG, "startNew");
        Intent intent = new Intent(context, InboxActivity.class);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_right, 0);
        }
    }
}
