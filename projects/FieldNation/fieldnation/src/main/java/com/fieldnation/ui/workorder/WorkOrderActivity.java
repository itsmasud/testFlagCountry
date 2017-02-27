
package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.workorder.detail.DeliverableFragment;
import com.fieldnation.ui.workorder.detail.MessageFragment;
import com.fieldnation.ui.workorder.detail.NotificationFragment;
import com.fieldnation.ui.workorder.detail.WorkFragment;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.List;

public class WorkOrderActivity extends AuthSimpleActivity {
    private static final String TAG = "WorkOrderActivity";

    public static final String INTENT_FIELD_WORKORDER_ID = TAG + ".workOrderId";
    public static final String INTENT_FIELD_WORKORDER = TAG + ".workOrder";
    public static final String INTENT_FIELD_CURRENT_TAB = TAG + ".currentTab";
    public static final String INTENT_FIELD_ACTION = TAG + ".action";
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

    // Data
    private WorkordersWebApi _workOrderApi;
    private int _workOrderId = 0;
    private int _currentTab = 0;
    private int _currentFragment = 0;
    private boolean _created = false;
    private WorkOrder _workOrder = null;
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
    public void onFinishCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(INTENT_FIELD_WORKORDER_ID)) {
                _workOrderId = intent.getIntExtra(INTENT_FIELD_WORKORDER_ID, 0);
            }
            if (intent.hasExtra(INTENT_FIELD_WORKORDER)) {
                _workOrder = intent.getParcelableExtra(INTENT_FIELD_WORKORDER);
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
                            _workOrderId = Integer.parseInt(segments.get(1));
                        } else if (segments.get(0).equals("workorder")) {
                            _workOrderId = Integer.parseInt(segments.get(2));
                        } else if (segments.get(0).equals("marketplace")) {
                            _workOrderId = Integer.parseInt(intent.getData().getQueryParameter("workorder_id"));
                        } else if (segments.get(0).equals("w") && segments.get(1).equals("r")) {
                            _workOrderId = Integer.parseInt(segments.get(2));
                        }
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_WORKORDERID)) {
                _workOrderId = savedInstanceState.getInt(STATE_WORKORDERID);
            }
            if (savedInstanceState.containsKey(STATE_CURRENT_TAB)) {
                _currentTab = savedInstanceState.getInt(STATE_CURRENT_TAB);
            }
            if (savedInstanceState.containsKey(STATE_CURRENTFRAG)) {
                _currentFragment = savedInstanceState.getInt(STATE_CURRENTFRAG);
            }
            if (savedInstanceState.containsKey(STATE_WORKORDER)) {
                _workOrder = savedInstanceState.getParcelable(STATE_WORKORDER);
            }
        }

        if (_workOrderId == 0) {
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
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_workOrderId != 0)
            outState.putInt(STATE_WORKORDERID, _workOrderId);

        if (_currentTab != 0)
            outState.putInt(STATE_CURRENT_TAB, _currentTab);

        if (_currentFragment != 0)
            outState.putInt(STATE_CURRENTFRAG, _currentFragment);

        if (_workOrder != null)
            outState.putParcelable(STATE_WORKORDER, _workOrder);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onProfile(Profile profile) {
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
        _workOrderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workOrderApi.connect(App.get());
    }

    @Override
    protected void onPause() {
        if (_workOrderApi != null && _workOrderApi.isConnected())
            _workOrderApi.disconnect(App.get());

        super.onPause();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_tabview == null)
            return;

        setTitle("WO: " + _workOrder.getWorkOrderId());
        if (_workOrder.getMessages() != null
                && _workOrder.getMessages().getMetadata() != null
                && _workOrder.getMessages().getMetadata().getTotal() != null) {
            _tabview.setMessagesCount(_workOrder.getMessages().getMetadata().getTotal());
        } else {
            _tabview.setMessagesCount(0);
        }

        for (int i = 0; i < _fragments.length; i++) {
            _fragments[i].setWorkorder(_workOrder);
        }
    }

    private void setLoading(boolean loading) {
        for (int i = 0; i < _fragments.length; i++) {
            _fragments[i].setLoading(loading);
        }
    }

    public void getData() {
        Log.v(TAG, "getData");
        setLoading(true);
        WorkordersWebApi.getWorkOrder(this, _workOrderId, false);
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private final PageRequestListener _pageRequestListener = new PageRequestListener() {
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

    private final ViewPager.SimpleOnPageChangeListener _viewPager_onChange = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            try {
                WorkOrderTracker.onTabSwitchEvent(App.get(), WorkOrderTracker.Tab.values()[_currentFragment], WorkOrderTracker.Tab.values()[position]);
                WorkOrderTracker.onShow(App.get(), WorkOrderTracker.Tab.values()[position], _workOrderId);
                _currentFragment = position;
                _tabview.setSelected(position);
                _fragments[position].update();
                populateUi();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final WorkorderTabView.Listener _tabview_onChange = new WorkorderTabView.Listener() {
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

    private final WorkorderFragment.LoadingListener _workorderFrag_loadingListener = new WorkorderFragment.LoadingListener() {
        @Override
        public void setLoading(boolean isLoading) {
            WorkOrderActivity.this.setLoading(isLoading);
        }
    };


    public interface PageRequestListener {
        void requestPage(Class<? extends WorkorderFragment> clazz);
    }

    /*-*****************************-*/
    /*-			Web Events			-*/
    /*-*****************************-*/
    private final WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_workOrderApi_listener.onConnected " + _workOrderId);
            _workOrderApi.subWorkordersWebApi();
            getData();
        }

        @Override
        public void onGetWorkOrder(WorkOrder workOrder, boolean success, Error error) {
            Log.v(TAG, "_workOrderApi_listener.onGetWorkOrder");
            if (workOrder == null || !success) {
                setLoading(false);
                return;
            }

            Debug.setLong("last_workorder", workOrder.getWorkOrderId());
            _workOrder = workOrder;
            populateUi();
        }

        @Override
        public void onWorkordersWebApi(String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.startsWith("get") || !success)
                return;

            Log.v(TAG, "onWorkordersWebApi " + methodName);

            WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false);
        }
    };

    public static void startNew(Context context, int workOrderId) {
        startNew(context, workOrderId, TAB_DETAILS);
    }

    public static void startNew(Context context, int workOrderId, int tab) {
        Intent intent = new Intent(context, WorkOrderActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        intent.putExtra(INTENT_FIELD_CURRENT_TAB, tab);
        ActivityResultClient.startActivity(context, intent);
    }

    public static Intent makeIntentConfirm(Context context, int workOrderId) {
        Log.v(TAG, "makeIntentConfirm");
        Intent intent = new Intent(context, WorkOrderActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_ACTION, ACTION_CONFIRM);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        intent.putExtra(INTENT_FIELD_CURRENT_TAB, TAB_DETAILS);
        return intent;
    }

    public static Intent makeIntentShow(Context context, int workOrderId) {
        Intent intent = new Intent(context, WorkOrderActivity.class);
        intent.setAction("DUMMY");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INTENT_FIELD_WORKORDER_ID, workOrderId);
        intent.putExtra(INTENT_FIELD_CURRENT_TAB, TAB_DETAILS);
        return intent;
    }

}

