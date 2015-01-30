package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.AuthActionBarActivity;
import com.fieldnation.ui.workorder.detail.DeliverableFragment;
import com.fieldnation.ui.workorder.detail.MessageFragment;
import com.fieldnation.ui.workorder.detail.NotificationFragment;
import com.fieldnation.ui.workorder.detail.WorkFragment;

import java.util.List;

public class WorkorderActivity extends AuthActionBarActivity {
    private static final String TAG = "ui.workorder.WorkorderActivity";

    public static final String INTENT_FIELD_WORKORDER_ID = "com.fieldnation.ui.workorder.WorkorderActivity:workorder_id";
    public static final String INTENT_FIELD_CURRENT_TAB = "com.fieldnation.ui.workorder.WorkorderActivity:current_tab";

    public static final int TAB_DETAILS = 0;
    public static final int TAB_MESSAGE = 1;
    public static final int TAB_DELIVERABLES = 2;
    public static final int TAB_NOTIFICATIONS = 3;

    private static final int RPC_GET_DETAIL = 1;

    // SavedInstance fields
    private static final String STATE_AUTHTOKEN = "STATE_AUTHTOKEN";
    private static final String STATE_USERNAME = "STATE_USERNAME";
    private static final String STATE_WORKORDERID = "STATE_WORKORDERID";
    private static final String STATE_CURRENT_TAB = "STATE_CURRENT_TAB";
    private static final String STATE_CURRENTFRAG = "STATE_CURRENT_FRAG";
    private static final String STATE_WORKORDER = "STATE_WORKORDER";

    // UI
    private ViewPager _viewPager;
    private WorkorderFragment[] _fragments;
    private WorkorderTabView _tabview;

    // Data
    private String _authToken;
    private String _username;
    private long _workorderId = 0;
    private int _currentTab = 0;
    private int _currentFragment = 0;
    private boolean _created = false;
    private Workorder _workorder = null;
    private boolean _hidingTasks;

    // Services
    private PagerAdapter _pagerAdapter;
    private WorkorderService _service;

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
                _workorderId = intent.getLongExtra(INTENT_FIELD_WORKORDER_ID, 0);
            }
            if (intent.hasExtra(INTENT_FIELD_CURRENT_TAB)) {
                _currentTab = intent.getIntExtra(INTENT_FIELD_CURRENT_TAB, 0);
            } else {
                _currentTab = TAB_DETAILS;
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
                    ex.printStackTrace();
                }
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_AUTHTOKEN)) {
                _authToken = savedInstanceState.getString(STATE_AUTHTOKEN);
            }
            if (savedInstanceState.containsKey(STATE_USERNAME)) {
                _username = savedInstanceState.getString(STATE_USERNAME);
            }
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
            if (_authToken != null && _username != null) {
                _service = new WorkorderService(this, _username, _authToken, _rpcReceiver);
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
        populateUi(true);

        TopicService.dispatchTopic(this, "NOTIFICATION_TEST", null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_authToken != null)
            outState.putString(STATE_AUTHTOKEN, _authToken);

        if (_username != null)
            outState.putString(STATE_USERNAME, _username);

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

    @Override
    public void onAuthentication(String username, String authToken, boolean isNew) {
        if (_service == null || isNew) {
            _service = new WorkorderService(WorkorderActivity.this, username, authToken, _rpcReceiver);
            getData(true);
        }
    }

    @Override
    public void onAuthenticationFailed(boolean networkDown) {
        super.onAuthenticationFailed(networkDown);
        _service = null;
    }

    @Override
    public void onAuthenticationInvalidated() {
        super.onAuthenticationInvalidated();
        _service = null;
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

//                        if (frag instanceof DetailFragment) {
//                            _fragments[0] = (WorkorderFragment) frag;
//                            _fragments[0].setPageRequestListener(_pageRequestListener);
//                            _fragments[0].setLoadingListener(_workorderFrag_loadingListener);
//                        }
//
//                        if (frag instanceof TasksFragment) {
//                            _fragments[1] = (WorkorderFragment) frag;
//                            _fragments[1].setPageRequestListener(_pageRequestListener);
//                            _fragments[1].setLoadingListener(_workorderFrag_loadingListener);
//                        }

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
//            if (_fragments[0] == null)
//                _fragments[0] = new DetailFragment();
//            if (_fragments[1] == null)
//                _fragments[1] = new TasksFragment();
            if (_fragments[1] == null)
                _fragments[1] = new MessageFragment();
            if (_fragments[2] == null)
                _fragments[2] = new DeliverableFragment();
            if (_fragments[3] == null)
                _fragments[3] = new NotificationFragment();

            for (int i = 0; i < _fragments.length; i++) {
                _fragments[i].setPageRequestListener(_pageRequestListener);
                _fragments[i].setLoadingListener(_workorderFrag_loadingListener);
            }
        }

        _pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        _viewPager.setAdapter(_pagerAdapter);
        _viewPager.setOnPageChangeListener(_viewPager_onChange);

        _tabview = (WorkorderTabView) findViewById(R.id.tabview);
        _tabview.setListener(_tabview_onChange);

        _viewPager.setCurrentItem(_currentTab, false);
    }

    private void populateUi(boolean isCached) {
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

        for (int i = 0; i < _fragments.length; i++) {
            _fragments[i].setWorkorder(_workorder, isCached);
        }

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


        if (isCached) {
            getData(false);
        } else {
            setLoading(false);
        }
    }

    private void setLoading(boolean loading) {
        for (int i = 0; i < _fragments.length; i++) {
            _fragments[i].setLoading(loading);
        }
    }

    public void getData(boolean allowCache) {
        if (_service == null)
            return;
        setLoading(true);
        startService(_service.getDetails(RPC_GET_DETAIL, _workorderId, allowCache));
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
            } catch (Exception ex) {
                ex.printStackTrace();
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
            }
        }
    };

    private Workorder.Listener _workorder_listener = new Workorder.Listener() {
        @Override
        public void onChange(Workorder workorder) {
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
        public void requestPage(Class<? extends WorkorderFragment> clazz);
    }

    /*-*****************************-*/
    /*-			Web Events			-*/
    /*-*****************************-*/
    private class WorkorderParseAsyncTask extends AsyncTaskEx<Bundle, Object, Workorder> {
        private boolean cached;

        @Override
        protected Workorder doInBackground(Bundle... params) {
            Bundle resultData = params[0];
            Workorder workorder = null;
            try {
                String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
                Log.v(TAG, data);
                workorder = Workorder.fromJson(new JsonObject(data));

                workorder.addListener(_workorder_listener);
                cached = resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED);
                Log.v(TAG, "Have workorder");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return workorder;
        }

        @Override
        protected void onPostExecute(Workorder workorder) {
            super.onPostExecute(workorder);
            if (workorder != null) {
                _workorder = workorder;
                populateUi(cached);
            }
        }
    }

    private WebResultReceiver _rpcReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            new WorkorderParseAsyncTask().executeEx(resultData);
        }

        @Override
        public Context getContext() {
            return WorkorderActivity.this;
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            AuthTopicService.requestAuthInvalid(WorkorderActivity.this);
        }
    };

}
