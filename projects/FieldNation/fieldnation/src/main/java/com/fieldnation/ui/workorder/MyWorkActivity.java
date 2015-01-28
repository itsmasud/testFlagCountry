package com.fieldnation.ui.workorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.PagerTabListView;
import com.fieldnation.ui.TabView;

import java.util.List;

public class MyWorkActivity extends AuthActionBarActivity {
    private static final String TAG = "ui.workorder.MyWorkActivity";

    // UI
    private ViewPager _viewPager;
    private PagerTabListView _tabListView;

    // Data
    private boolean _created = false;
    private Fragment[] _fragments;
    private String[] _titles;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        super.onFinishCreate(savedInstanceState);
        setTitle(R.string.mywork_title);

        if (!_created) {
            _created = true;
        }

        _viewPager = (ViewPager) findViewById(R.id.pager);
        _tabListView = (PagerTabListView) findViewById(R.id.tablist_view);

        _fragments = new WorkorderListFragment[3];
        _fragments[0] = getFragment(WorkorderDataSelector.ASSIGNED);
        _fragments[1] = getFragment(WorkorderDataSelector.COMPLETED);
        _fragments[2] = getFragment(WorkorderDataSelector.CANCELED);

        _titles = new String[]{getString(R.string.tab_assigned),
                getString(R.string.tab_completed),
                getString(R.string.tab_canceled)};

        _viewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        _tabListView.setTabFactory(_tab_factory);
        _tabListView.setViewPager(_viewPager);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_tabs;
    }

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
    }

    @Override
    public void onRefresh() {

    }

    private final PagerTabListView.TabFactory _tab_factory = new PagerTabListView.TabFactory() {
        @Override
        public View getTab(int position, CharSequence title) {
            TabView tv = new TabView(MyWorkActivity.this, null, R.style.TextViewEmptyLabel);
            tv.setText((String) title);

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT);
            param.weight = 1f;
            param.gravity = Gravity.CENTER;
            tv.setLayoutParams(param);

            return tv;
        }
    };

    private Fragment getFragment(WorkorderDataSelector selector) {
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
        return fragment;
    }


    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    // swaps fragments on a pager transition

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int postition) {
            return _fragments[postition];
        }

        @Override
        public int getCount() {
            return _fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return _titles[position];
        }
    }

}
