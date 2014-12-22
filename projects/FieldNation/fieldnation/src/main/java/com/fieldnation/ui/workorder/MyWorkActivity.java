package com.fieldnation.ui.workorder;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.Log;
import android.view.View;

import com.fieldnation.R;
import com.fieldnation.ui.DrawerActivity;
import com.fieldnation.ui.dialog.CustomFieldDialog;

import java.util.List;

public class MyWorkActivity extends DrawerActivity {
    private static final String TAG = "ui.workorder.MyWorkActivity";

    // UI
    private ViewPager _viewPager;
    private WorkorderListFragment[] _fragments;
    private CustomFieldDialog _customFieldDialog;

    // Data
    private PagerAdapter _pagerAdapter;
    private boolean _created = false;
    private int _currentFragment = 0;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        setTitle(R.string.mywork_title);

        if (!_created) {
            addActionBarAndDrawer(R.id.container);
            buildTabs(savedInstanceState);
            _created = true;
        }

        _currentFragment = getSupportActionBar().getSelectedNavigationIndex();
        _viewPager.setCurrentItem(_currentFragment, false);

//        _customFieldDialog = CustomFieldDialog.getInstance(getSupportFragmentManager(), TAG);
//        _customFieldDialog.sho
    }

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        // TODO STUB com.fieldnation.ui.workorder.MyWorkActivity.onAuthentication()
        Log.v(TAG, "STUB com.fieldnation.ui.workorder.MyWorkActivity.onAuthentication()");
    }

    private void buildTabs(Bundle savedInstanceState) {
        _viewPager = (ViewPager) findViewById(R.id.content_viewpager);

        _fragments = new WorkorderListFragment[3];

        if (savedInstanceState != null) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    if (fragments.get(i) instanceof WorkorderListFragment) {
                        WorkorderListFragment frag = (WorkorderListFragment) fragments.get(i);

                        if (frag.getDisplayType() == WorkorderDataSelector.ASSIGNED) {
                            _fragments[0] = frag;
                        }
                        if (frag.getDisplayType() == WorkorderDataSelector.COMPLETED) {
                            _fragments[1] = frag;
                        }
                        if (frag.getDisplayType() == WorkorderDataSelector.CANCELED) {
                            _fragments[2] = frag;
                        }
                    }
                }
            }
        }

        if (_fragments[0] == null) {
            _fragments[0] = new WorkorderListFragment().setDisplayType(WorkorderDataSelector.ASSIGNED);
        }
        if (_fragments[1] == null) {
            _fragments[1] = new WorkorderListFragment().setDisplayType(WorkorderDataSelector.COMPLETED);
        }
        if (_fragments[2] == null) {
            _fragments[2] = new WorkorderListFragment().setDisplayType(WorkorderDataSelector.CANCELED);
        }

        ActionBar actionbar = getSupportActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // ActionBar.Tab tab1 =
        // actionbar.newTab().setText(R.string.my_work_category1);
        ActionBar.Tab tab2 = actionbar.newTab().setText(R.string.my_work_category2);
        ActionBar.Tab tab3 = actionbar.newTab().setText(R.string.my_work_category3);
        ActionBar.Tab tab4 = actionbar.newTab().setText(R.string.my_work_category4);

        // tab1.setTabListener(_tabListener);
        tab2.setTabListener(_tabListener);
        tab3.setTabListener(_tabListener);
        tab4.setTabListener(_tabListener);

        // actionbar.addTab(tab1);
        actionbar.addTab(tab2);
        actionbar.addTab(tab3);
        actionbar.addTab(tab4);

        _pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        _viewPager.setAdapter(_pagerAdapter);
        _viewPager.setOnPageChangeListener(_viewPager_onChange);
    }

    @Override
    public ActionBarDrawerToggle createActionBarDrawerToggle(DrawerLayout drawerLayout) {
        return new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.launcher_open,
                R.string.launcher_open) {

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState != 0) {
                    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                    supportInvalidateOptionsMenu();
                }
                super.onDrawerStateChanged(newState);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset == 0.0) {
                    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                    supportInvalidateOptionsMenu();
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = 0; i < _fragments.length; i++) {
            _fragments[i].update();
        }
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
    }

    // sync set actionbar tabs on page viewer change
    private ViewPager.SimpleOnPageChangeListener _viewPager_onChange = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            try {
                _currentFragment = position;
                getSupportActionBar().setSelectedNavigationItem(position);
            } catch (Exception ex) {
            }
        }

        ;
    };

    // sync pageviewer based on tab selection
    private TabListener _tabListener = new TabListener() {
        @Override
        public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
        }

        @Override
        public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
            try {
                if (_currentFragment != arg0.getPosition()) {
                    _currentFragment = arg0.getPosition();
                    _viewPager.setCurrentItem(_currentFragment, true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        }
    };

    @Override
    public void onRefresh() {
        _fragments[_currentFragment].update();
    }

}
