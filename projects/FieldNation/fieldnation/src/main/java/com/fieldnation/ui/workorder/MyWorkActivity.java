package com.fieldnation.ui.workorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.fieldnation.R;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.PagerTabListView;

import java.util.List;

public class MyWorkActivity extends AuthActionBarActivity {
    private static final String TAG = "ui.workorder.MyWorkActivity";

    // UI
    private ViewPager _viewPager;
    private PagerTabListView _tabListView;

    // Data
    private boolean _created = false;
    private WorkorderListFragment[] _fragments;
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
        _viewPager.setOffscreenPageLimit(3);
        _tabListView = (PagerTabListView) findViewById(R.id.tablist_view);

        _fragments = new WorkorderListFragment[3];
        _fragments[0] = getFragment(WorkorderDataSelector.ASSIGNED);
        _fragments[1] = getFragment(WorkorderDataSelector.COMPLETED);
        _fragments[2] = getFragment(WorkorderDataSelector.CANCELED);

        _titles = new String[]{getString(R.string.tab_assigned), getString(R.string.tab_completed), getString(R.string.tab_canceled)};

        _viewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        _viewPager.setOnPageChangeListener(_pageChangeListener);
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

    private final ViewPager.OnPageChangeListener _pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            _tabListView.setSelected(position);
            _fragments[position].isShowing();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
