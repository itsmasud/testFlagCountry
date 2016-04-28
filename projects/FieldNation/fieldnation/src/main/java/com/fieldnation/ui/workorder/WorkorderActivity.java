
package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.ActionBarDrawerView;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.market.MarketActivity;
import com.fieldnation.ui.workorder.detail.DeliverableFragment;
import com.fieldnation.ui.workorder.detail.MessageFragment;
import com.fieldnation.ui.workorder.detail.NotificationFragment;
import com.fieldnation.ui.workorder.detail.WorkFragment;

import java.util.List;

public class WorkorderActivity extends AuthActionBarActivity {
    private static final String TAG = "WorkorderActivity";

    public static final String INTENT_FIELD_WORKORDER_ID = "WorkorderActivity:workorder_id";
    public static final String INTENT_FIELD_WORKORDER = "WorkorderActivity:workorder";
    public static final String INTENT_FIELD_CURRENT_TAB = "WorkorderActivity:current_tab";
    public static final String INTENT_FIELD_ACTION = "WorkorderActivity:INTENT_FIELD_ACTION";
    public static final String ACTION_CONFIRM = "ACTION_CONFIRM";

    public static final int TAB_DETAILS = 0;
    public static final int TAB_MESSAGE = 1;
    public static final int TAB_DELIVERABLES = 2;
    public static final int TAB_NOTIFICATIONS = 3;

    // SavedInstance fields
    private static final String STATE_WORKORDERID = "STATE_WORKORDERID";
    private static final String STATE_CURRENT_TAB = "STATE_CURRENT_TAB";
    private static final String STATE_CURRENTFRAG = "STATE_CURRENT_FRAG";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // UI
    private ViewPager _viewPager;
    private WorkorderFragment[] _fragments;
    private WorkorderTabView _tabview;

    private ActionBarDrawerView _actionBarView;
    private Toolbar _toolbar;


    // Data
    private WorkorderClient _workorderClient;
    private long _workorderId = 0;
    private int _currentTab = 0;
    private int _currentFragment = 0;
    private boolean _created = false;
    private Workorder _workorder = null;
    private boolean _hidingTasks;

    // Services
    private PagerAdapter _pagerAdapter;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    @Override
    public int getLayoutResource() {
        return R.layout.activity_workorder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _actionBarView = (ActionBarDrawerView) findViewById(R.id.actionbardrawerview);
        _toolbar = _actionBarView.getToolbar();
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setNavigationOnClickListener(_toolbarNavication_listener);
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(INTENT_FIELD_WORKORDER_ID)) {
                _workorderId = intent.getLongExtra(INTENT_FIELD_WORKORDER_ID, 0);
            }
            if (intent.hasExtra(INTENT_FIELD_WORKORDER)) {
                _workorder = intent.getParcelableExtra(INTENT_FIELD_WORKORDER);
            }
            if (intent.hasExtra(INTENT_FIELD_CURRENT_TAB)) {
                _currentTab = intent.getIntExtra(INTENT_FIELD_CURRENT_TAB, 0);
            } else {
                _currentTab = TAB_DETAILS;
            }

            if (intent.hasExtra(INTENT_FIELD_ACTION)) {
                if (intent.getStringExtra(INTENT_FIELD_ACTION).equals(ACTION_CONFIRM)) {
                    Log.v(TAG, "INTENT_FIELD_ACTION/ACTION_CONFIRM");
                }
            }

            // taking a link from e-mail/browser
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                try {
                    final List<String> segments = intent.getData().getPathSegments();
                    if (segments.size() > 1) {
                        if (segments.get(0).equals("wo")) {
                            _workorderId = Long.parseLong(segments.get(1));
                        }
                        if (segments.get(0).equals("workorder")) {
                            _workorderId = Long.parseLong(segments.get(2));
                        }
                        if (segments.get(0).equals("marketplace")) {
                            _workorderId = Long.parseLong(intent.getData().getQueryParameter("workorder_id"));
                        }
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDERID)) {
                _workorderId = savedInstanceState.getLong(STATE_WORKORDERID);
            }
            if (savedInstanceState.containsKey(STATE_CURRENT_TAB)) {
                _currentTab = savedInstanceState.getInt(STATE_CURRENT_TAB);
            }
            if (savedInstanceState.containsKey(STATE_CURRENTFRAG)) {
                _currentFragment = savedInstanceState.getInt(STATE_CURRENTFRAG);
            }
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workorder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
        }

        if (_workorderId == 0) {
            // epic fail!
            Log.e(TAG, "must have a workorder id!");
            finish();
        }

        if (!_created) {
            // addActionBarAndDrawer(R.id.container);
            buildFragments(savedInstanceState);
            _created = true;
        }

//        _loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
        setLoading(true);
        populateUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_workorderId != 0)
            outState.putLong(STATE_WORKORDERID, _workorderId);

        if (_currentTab != 0)
            outState.putInt(STATE_CURRENT_TAB, _currentTab);

        if (_currentFragment != 0)
            outState.putInt(STATE_CURRENTFRAG, _currentFragment);

        if (_workorder != null)
            outState.putParcelable(STATE_WORKORDER, _workorder);

        super.onSaveInstanceState(outState);
    }

    private void buildFragments(Bundle savedInstanceState) {
        _viewPager = (ViewPager) findViewById(R.id.content_viewpager);
        _viewPager.setOffscreenPageLimit(4);

        if (_fragments == null) {
            _fragments = new WorkorderFragment[4];

            if (savedInstanceState != null) {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                if (fragments != null) {
                    for (int i = 0; i < fragments.size(); i++) {
                        Fragment frag = fragments.get(i);

                        if (frag instanceof WorkFragment) {
                            _fragments[0] = (WorkorderFragment) frag;
                            _fragments[0].setPageRequestListener(_pageRequestListener);
                            _fragments[0].setLoadingListener(_workorderFrag_loadingListener);
                        }

                        if (frag instanceof MessageFragment) {
                            _fragments[1] = (WorkorderFragment) frag;
                            _fragments[1].setPageRequestListener(_pageRequestListener);
                            _fragments[1].setLoadingListener(_workorderFrag_loadingListener);
                        }

                        if (frag instanceof DeliverableFragment) {
                            _fragments[2] = (WorkorderFragment) frag;
                            _fragments[2].setPageRequestListener(_pageRequestListener);
                            _fragments[2].setLoadingListener(_workorderFrag_loadingListener);
                        }

                        if (frag instanceof NotificationFragment) {
                            _fragments[3] = (WorkorderFragment) frag;
                            _fragments[3].setPageRequestListener(_pageRequestListener);
                            _fragments[3].setLoadingListener(_workorderFrag_loadingListener);
                        }
                    }
                }
            }

            if (_fragments[0] == null)
                _fragments[0] = new WorkFragment();
            if (_fragments[1] == null)
                _fragments[1] = new MessageFragment();
            if (_fragments[2] == null)
                _fragments[2] = new DeliverableFragment();
            if (_fragments[3] == null)
                _fragments[3] = new NotificationFragment();

            for (int i = 0; i < _fragments.length; i++) {
                _fragments[i].setPageRequestListener(_pageRequestListener);
                _fragments[i].setLoadingListener(_workorderFrag_loadingListener);
                if (getIntent() != null && getIntent().getExtras() != null) {
                    try {
                        _fragments[i].setArguments(getIntent().getExtras());
                    } catch (Exception ex) {
                    }
                }
            }
        }

        _pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        _viewPager.setAdapter(_pagerAdapter);
        _viewPager.addOnPageChangeListener(_viewPager_onChange);

        _tabview = (WorkorderTabView) findViewById(R.id.tabview);
        _tabview.setListener(_tabview_onChange);

        _viewPager.setCurrentItem(_currentTab, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());
    }

    @Override
    protected void onPause() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        super.onPause();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_tabview == null)
            return;

        setTitle("WO: " + _workorder.getWorkorderId());

        if (_workorder.getAlertCount() != null) {
            _tabview.setAlertsCount(_workorder.getAlertCount());
        } else {
            _tabview.setAlertsCount(0);
        }

        if (_workorder.getMessageCount() != null) {
            _tabview.setMessagesCount(_workorder.getMessageCount());
        } else {
            _tabview.setMessagesCount(0);
        }

//        for (int i = 0; i < _fragments.length; i++) {
//            _fragments[i].setWorkorder(_workorder);
//        }

        _fragments[_currentFragment].setWorkorder(_workorder);


//        if ((_workorder.getTasks() == null || _workorder.getTasks().length == 0) && !_workorder.canModify()) {
//            //_tabview.hideTab(TAB_TASKS);
//            _hidingTasks = true;
//        } else {
//            //_tabview.showTab(TAB_TASKS);
//            _hidingTasks = false;
//        }

        // if (_workorder.getStatus().getWorkorderStatus() ==
        // WorkorderStatus.INPROGRESS) {
        // _viewPager.setCurrentItem(TAB_TASKS, false);
        // }
        // setLoading(false);
    }

    private void setLoading(boolean loading) {
        for (int i = 0; i < _fragments.length; i++) {
            _fragments[i].setLoading(loading);
        }
    }

    public void getData(boolean allowCache) {
        Log.v(TAG, "getData");
        setLoading(true);
        WorkorderClient.get(this, _workorderId, allowCache);
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private PageRequestListener _pageRequestListener = new PageRequestListener() {
        @Override
        public void requestPage(Class<? extends WorkorderFragment> clazz) {
            for (int i = 0; i < _fragments.length; i++) {
                WorkorderFragment fragment = _fragments[i];
                if (clazz.isInstance(fragment)) {
                    _viewPager.setCurrentItem(i, true);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (_fragments != null) {
            for (Fragment fragment : _fragments) {
                if (fragment != null)
                    fragment.onActivityResult(arg0, arg1, arg2);
            }
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            _fragments[position].update();
            return _fragments[position];
        }

        @Override
        public int getCount() {
            return _fragments.length;
        }
    }

    private ViewPager.SimpleOnPageChangeListener _viewPager_onChange = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            try {
                _currentFragment = position;
                _tabview.setSelected(position);
                _fragments[position].update();
                populateUi();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private WorkorderTabView.Listener _tabview_onChange = new WorkorderTabView.Listener() {
        @Override
        public void onChange(int index) {
            if (index != _currentFragment) {
                _currentFragment = index;
                _fragments[index].update();
                _viewPager.setCurrentItem(_currentFragment, true);
                populateUi();
            }
        }
    };

    private Workorder.Listener _workorder_listener = new Workorder.Listener() {
        @Override
        public void onChange(Workorder workorder) {
            Log.v(TAG, "_workorder_listener");
            getData(false);
        }
    };

    private WorkorderFragment.LoadingListener _workorderFrag_loadingListener = new WorkorderFragment.LoadingListener() {
        @Override
        public void setLoading(boolean isLoading) {
            WorkorderActivity.this.setLoading(isLoading);
        }
    };


    public interface PageRequestListener {
        void requestPage(Class<? extends WorkorderFragment> clazz);
    }

    /*-*****************************-*/
    /*-			Web Events			-*/
    /*-*****************************-*/
    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workorderClient_listener.onConnected " + _workorderId);
            _workorderClient.subGet(_workorderId);
            getData(true);
        }

        @Override
        public void onGet(Workorder workorder, boolean failed, boolean isCached) {
            Log.v(TAG, "_workorderClient_listener.onDetails");
            if (workorder == null || failed) {
                if (isCached) {
                    WorkorderClient.get(App.get(), _workorderId, false);
                } else {
                    try {
                        Toast.makeText(WorkorderActivity.this, R.string.workorder_no_permission, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(WorkorderActivity.this, MarketActivity.class);
                        startActivity(intent);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                        setLoading(false);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
                return;
            }

            Debug.setLong("last_workorder", workorder.getWorkorderId());

            workorder.addListener(_workorder_listener);
            _workorder = workorder;
            populateUi();
        }
    };

    private View.OnClickListener _toolbarNavication_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public static Intent makeIntentConfirm(Context context, long workorderId) {
        Log.v(TAG, "makeIntentConfirm");
        Intent intent = new Intent(context, WorkorderActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_ACTION, ACTION_CONFIRM);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workorderId);
        intent.putExtra(INTENT_FIELD_CURRENT_TAB, TAB_DETAILS);
        return intent;
    }

    public static Intent makeIntentShow(Context context, long workorderId) {
        Intent intent = new Intent(context, WorkorderActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workorderId);
        intent.putExtra(INTENT_FIELD_CURRENT_TAB, TAB_DETAILS);
        return intent;
    }

}

