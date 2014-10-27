package com.fieldnation.ui.workorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.BaseActivity;
import com.fieldnation.ui.workorder.detail.DeliverableFragment;
import com.fieldnation.ui.workorder.detail.DetailFragment;
import com.fieldnation.ui.workorder.detail.MessageFragment;
import com.fieldnation.ui.workorder.detail.NotificationFragment;
import com.fieldnation.ui.workorder.detail.TasksFragment;

import java.util.List;

public class WorkorderActivity extends BaseActivity {
    private static final String TAG = "ui.workorder.WorkorderActivity";

    public static final String INTENT_FIELD_WORKORDER_ID = "com.fieldnation.ui.workorder.WorkorderActivity:workorder_id";
    public static final String INTENT_FIELD_CURRENT_TAB = "com.fieldnation.ui.workorder.WorkorderActivity:current_tab";

    public static final int TAB_DETAILS = 0;
    public static final int TAB_TASKS = 1;
    public static final int TAB_MESSAGE = 2;
    public static final int TAB_DELIVERABLES = 3;
    public static final int TAB_NOTIFICATIONS = 4;

    private static final int RPC_GET_DETAIL = 1;

    // SavedInstance fields
    private static final String AUTHTOKEN = "AUTHTOKEN";
    private static final String USERNAME = "USERNAME";
    private static final String WORKORDERID = "WORKORDERID";
    private static final String CURRENT_TAB = "CURRENT_TAB";
    private static final String CURRENTFRAG = "CURRENT_FRAG";
    private static final String WORKORDER = "WORKORDER";

    // UI
    private ViewPager _viewPager;
    private WorkorderFragment[] _fragments;
    private WorkorderTabView _tabview;
    private RelativeLayout _loadingLayout;

    // Data
    private GlobalState _gs;

    private String _authToken;
    private String _username;
    private long _workorderId = 0;
    private int _currentTab = 0;
    private int _currentFragment = 0;
    private boolean _created = false;
    private Workorder _workorder = null;

    // Services
    private PagerAdapter _pagerAdapter;
    private WorkorderService _woRpc;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public WorkorderActivity() {
        super();
        Log.v(TAG, "WorkorderActivity()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workorder);

        _gs = (GlobalState) getApplicationContext();

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

        if (savedInstanceState == null) {
            _gs.requestAuthentication(_authClient);
        } else {
            if (savedInstanceState.containsKey(AUTHTOKEN)) {
                _authToken = savedInstanceState.getString(AUTHTOKEN);
            }
            if (savedInstanceState.containsKey(USERNAME)) {
                _username = savedInstanceState.getString(USERNAME);
            }
            if (savedInstanceState.containsKey(WORKORDERID)) {
                _workorderId = savedInstanceState.getLong(WORKORDERID);
            }
            if (savedInstanceState.containsKey(CURRENT_TAB)) {
                _currentTab = savedInstanceState.getInt(CURRENT_TAB);
            }
            if (savedInstanceState.containsKey(CURRENTFRAG)) {
                _currentFragment = savedInstanceState.getInt(CURRENTFRAG);
            }
            if (savedInstanceState.containsKey(WORKORDER)) {
                _workorder = savedInstanceState.getParcelable(WORKORDER);
            }
            if (_authToken != null && _username != null) {
                _woRpc = new WorkorderService(this, _username, _authToken, _rpcReceiver);
            } else {
                _gs.requestAuthentication(_authClient);
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

        _loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
        setLoading(true);
        populateUi();
    }

    private void buildFragments(Bundle savedInstanceState) {
        _viewPager = (ViewPager) findViewById(R.id.content_viewpager);
        _viewPager.setOffscreenPageLimit(4);

        if (_fragments == null) {
            _fragments = new WorkorderFragment[5];

            if (savedInstanceState != null) {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                if (fragments != null) {
                    for (int i = 0; i < fragments.size(); i++) {
                        Fragment frag = fragments.get(i);
                        if (frag instanceof DetailFragment) {
                            _fragments[0] = (WorkorderFragment) frag;
                            _fragments[0].setPageRequestListener(_pageRequestListener);
                        }

                        if (frag instanceof TasksFragment) {
                            _fragments[1] = (WorkorderFragment) frag;
                            _fragments[1].setPageRequestListener(_pageRequestListener);
                        }

                        if (frag instanceof MessageFragment) {
                            _fragments[2] = (WorkorderFragment) frag;
                            _fragments[2].setPageRequestListener(_pageRequestListener);
                        }

                        if (frag instanceof DeliverableFragment) {
                            _fragments[3] = (WorkorderFragment) frag;
                            _fragments[3].setPageRequestListener(_pageRequestListener);
                        }

                        if (frag instanceof NotificationFragment) {
                            _fragments[4] = (WorkorderFragment) frag;
                            _fragments[4].setPageRequestListener(_pageRequestListener);
                        }
                    }
                }
            }

            if (_fragments[0] == null)
                _fragments[0] = new DetailFragment();
            if (_fragments[1] == null)
                _fragments[1] = new TasksFragment();
            if (_fragments[2] == null)
                _fragments[2] = new MessageFragment();
            if (_fragments[3] == null)
                _fragments[3] = new DeliverableFragment();
            if (_fragments[4] == null)
                _fragments[4] = new NotificationFragment();

            for (int i = 0; i < _fragments.length; i++) {
                _fragments[i].setPageRequestListener(_pageRequestListener);
            }
        }

        _pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        _viewPager.setAdapter(_pagerAdapter);
        _viewPager.setOnPageChangeListener(_viewPager_onChange);

        _tabview = (WorkorderTabView) findViewById(R.id.tabview);
        _tabview.setListener(_tabview_onChange);

        _viewPager.setCurrentItem(_currentTab, false);
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_tabview == null)
            return;

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
            _fragments[i].setWorkorder(_workorder);
        }

        // if (_workorder.getStatus().getWorkorderStatus() ==
        // WorkorderStatus.INPROGRESS) {
        // _viewPager.setCurrentItem(TAB_TASKS, false);
        // }

        setLoading(false);
    }

    private void setLoading(boolean loading) {
        if (loading) {
            _loadingLayout.setVisibility(View.VISIBLE);
            _viewPager.setVisibility(View.GONE);
        } else {
            _loadingLayout.setVisibility(View.GONE);
            _viewPager.setVisibility(View.VISIBLE);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
	/*-*************************-*/
    private PageRequestListener _pageRequestListener = new PageRequestListener() {

        @Override
        public void requestPage(Class<? extends WorkorderFragment> clazz, Bundle request) {
            for (int i = 0; i < _fragments.length; i++) {
                WorkorderFragment fragment = _fragments[i];
                if (clazz.isInstance(fragment)) {
                    _viewPager.setCurrentItem(i, true);
                    fragment.doAction(request);
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

        ;
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

    private AuthenticationClient _authClient = new AuthenticationClient() {
        @Override
        public void onAuthenticationFailed(Exception ex) {
            _gs.requestAuthenticationDelayed(_authClient);
        }

        @Override
        public void onAuthentication(String username, String authToken) {
            _woRpc = new WorkorderService(WorkorderActivity.this, username, authToken, _rpcReceiver);
            startService(_woRpc.getDetails(RPC_GET_DETAIL, _workorderId, false));
        }

        @Override
        public GlobalState getGlobalState() {
            return _gs;
        }
    };

    private WebServiceResultReceiver _rpcReceiver = new WebServiceResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            Log.v(TAG, "onSuccess()");
            Log.v(TAG, resultData.toString());

            try {
                String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
                Log.v(TAG, data);
                _workorder = Workorder.fromJson(new JsonObject(data));

                _workorder.addListener(_workorder_listener);
                populateUi();
                Log.v(TAG, "Have workorder");
                setLoading(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            if (_woRpc != null) {
                _gs.invalidateAuthToken(_woRpc.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authClient);
        }
    };

    private Workorder.Listener _workorder_listener = new Workorder.Listener() {
        @Override
        public void onChange(Workorder workorder) {
            startService(_woRpc.getDetails(RPC_GET_DETAIL, _workorderId, false));
            // setLoading(true);
        }
    };

    @Override
    public void onRefresh() {
        startService(_woRpc.getDetails(RPC_GET_DETAIL, _workorderId, false));
        setLoading(true);
    }

    public interface PageRequestListener {
        public void requestPage(Class<? extends WorkorderFragment> clazz, Bundle request);
    }
}
