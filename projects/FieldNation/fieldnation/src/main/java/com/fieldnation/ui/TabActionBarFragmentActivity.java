package com.fieldnation.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.fieldnation.R;
import com.fieldnation.fndialog.DialogManager;

/**
 * Created by Michael Carver on 1/30/2015.
 */
public abstract class TabActionBarFragmentActivity extends AuthSimpleActivity {

    // UI
    private ViewPager _viewPager;
    private PagerTabListView _tabListView;

    // Data

    @Override
    public int getLayoutResource() {
        return R.layout.activity_tabs;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        loadFragments();

        _viewPager = (ViewPager) findViewById(R.id.pager);
        _viewPager.setOffscreenPageLimit(getFragmentCount());
        _tabListView = (PagerTabListView) findViewById(R.id.tablist_view);

        _viewPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));
        _viewPager.setOnPageChangeListener(_pageChangeListener);
        _tabListView.setViewPager(_viewPager);
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    public abstract void loadFragments();

    public abstract int getFragmentCount();

    public abstract String getFragmentTitle(int index);

    public abstract TabFragment getFragment(int index);

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int postition) {
            return (Fragment) getFragment(postition);
        }

        @Override
        public int getCount() {
            return getFragmentCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getFragmentTitle(position);
        }
    }

    private final ViewPager.OnPageChangeListener _pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            _tabListView.setSelected(position);
            getFragment(position).isShowing();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void switchFragment(int position) {
        _viewPager.setCurrentItem(position);
    }

    public interface TabFragment {
        void isShowing();
    }
}
