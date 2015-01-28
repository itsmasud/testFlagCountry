package com.fieldnation.ui.workorder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.ui.AuthActionBarActivity;

public class MyWorkActivity extends AuthActionBarActivity {
    private static final String TAG = "ui.workorder.MyWorkActivity";

    // UI
    private FragmentTabHost _tabHost;

    // Data
    private boolean _created = false;
    private int _currentFragment = 0;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        super.onFinishCreate(savedInstanceState);
        setTitle(R.string.mywork_title);

        if (!_created) {
            _created = true;
        }

        _tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        _tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        setupTab(getString(R.string.tab_assigned), WorkorderDataSelector.ASSIGNED);
        setupTab(getString(R.string.tab_completed), WorkorderDataSelector.COMPLETED);
        setupTab(getString(R.string.tab_canceled), WorkorderDataSelector.CANCELED);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_tabs;
    }

    private void setupTab(final String tag, WorkorderDataSelector selector) {
        View tabview = createTabView(this, tag);
        TabHost.TabSpec setContent = _tabHost.newTabSpec(tag).setIndicator(tabview);

        Bundle bundle = new Bundle();
        bundle.putString("STATE_DISPLAY", selector.name());

        _tabHost.addTab(setContent, WorkorderListFragment.class, bundle);
    }

    private static View createTabView(final Context context, final String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_list_tab, null);
        TextView tv = (TextView) view.findViewById(R.id.tab_text);
        tv.setText(text);
        return view;
    }

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
    }

/*
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
        ActionBar.Tab tab2 = actionbar.newTab().setText(R.string.assigned);
        ActionBar.Tab tab3 = actionbar.newTab().setText(R.string.completed);
        ActionBar.Tab tab4 = actionbar.newTab().setText(R.string.canceled);

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
*/

//    @Override
//    public ActionBarDrawerToggle createActionBarDrawerToggle(DrawerLayout drawerLayout) {
//        return new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.launcher_open,
//                R.string.launcher_open) {
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//                if (newState != 0) {
//                    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//                    supportInvalidateOptionsMenu();
//                }
//                super.onDrawerStateChanged(newState);
//            }
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                if (slideOffset == 0.0) {
//                    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//                    supportInvalidateOptionsMenu();
//                }
//                super.onDrawerSlide(drawerView, slideOffset);
//            }
//        };
//    }

    @Override
    protected void onResume() {
        super.onResume();

//        for (int i = 0; i < _fragments.length; i++) {
//            _fragments[i].update();
//        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    // swaps fragments on a pager transition
/*
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
*/

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
//                    _viewPager.setCurrentItem(_currentFragment, true);
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
//        _fragments[_currentFragment].update();
    }

}
