package com.fieldnation.ui.workorder;

import java.util.List;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.BaseActivity;
import com.fieldnation.ui.workorder.detail.DetailFragment;
import com.fieldnation.ui.workorder.detail.DeliverableFragment;
import com.fieldnation.ui.workorder.detail.MessageFragment;
import com.fieldnation.ui.workorder.detail.NotificationFragment;
import com.fieldnation.ui.workorder.detail.TasksFragment;

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

public class WorkorderActivity extends BaseActivity {
	private static final String TAG = "ui.workorder.WorkorderActivity";

	public static final String INTENT_FIELD_WORKORDER_ID = "com.fieldnation.ui.workorder.WorkorderActivity:workorder_id";
	public static final String INTENT_FIELD_CURRENT_TAB = "com.fieldnation.ui.workorder.WorkorderActivity:current_tab";

	public static final int TAB_DETAILS = 0;
	public static final int TAB_TASKS = 1;
	public static final int TAB_MESSAGE = 2;
	public static final int TAB_NOTIFICATIONS = 3;
	public static final int TAB_DELIVERABLES = 4;

	private static final int RPC_GET_DETAIL = 1;

	// UI
	private ViewPager _viewPager;
	private WorkorderFragment[] _fragments;
	private WorkorderTabView _tabview;
	private RelativeLayout _loadingLayout;

	// Data
	private GlobalState _gs;
	private long _workorderId = 0;
	private int _currentTab = 0;
	private int _currentFragment = 0;
	private boolean _created = false;
	private PagerAdapter _pagerAdapter;
	private Workorder _workorder = null;

	// Services
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

		_gs = (GlobalState) getApplicationContext();
		_gs.requestAuthentication(_authClient);
	}

	private void buildFragments(Bundle savedInstanceState) {
		_viewPager = (ViewPager) findViewById(R.id.content_viewpager);

		if (_fragments == null) {
			_fragments = new WorkorderFragment[5];

			if (savedInstanceState != null) {
				List<Fragment> fragments = getSupportFragmentManager().getFragments();

				for (int i = 0; i < fragments.size(); i++) {
					Fragment frag = fragments.get(i);
					_fragments[i] = (WorkorderFragment) frag;
				}
			}

			if (_fragments[0] == null)
				_fragments[0] = new DetailFragment();
			if (_fragments[1] == null)
				_fragments[1] = new TasksFragment();
			if (_fragments[2] == null)
				_fragments[2] = new MessageFragment();
			if (_fragments[3] == null)
				_fragments[3] = new NotificationFragment();
			if (_fragments[4] == null)
				_fragments[4] = new DeliverableFragment();
		}

		_pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		_viewPager.setAdapter(_pagerAdapter);
		_viewPager.setOnPageChangeListener(_viewPager_onChange);

		_tabview = (WorkorderTabView) findViewById(R.id.tabview);
		_tabview.setListener(_tabview_onChange);

		_viewPager.setCurrentItem(_currentTab, false);
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/

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
			}
		};
	};

	private WorkorderTabView.Listener _tabview_onChange = new WorkorderTabView.Listener() {
		@Override
		public void onChange(int index) {
			_currentFragment = index;
			_fragments[index].update();
			_viewPager.setCurrentItem(_currentFragment, true);
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
				// TODO need to get data selector from REST API
				String data = new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA));
				Log.v(TAG, data);
				_workorder = Workorder.fromJson(new JsonObject(data));

				_workorder.addListener(_workorder_listener);
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

				System.out.println("Have workorder");
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
			setLoading(true);
		}
	};

	@Override
	public void onRefresh() {
		startService(_woRpc.getDetails(RPC_GET_DETAIL, _workorderId, false));
		setLoading(true);
	}

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void setLoading(boolean loading) {
		if (loading) {
			_loadingLayout.setVisibility(View.VISIBLE);
			_viewPager.setVisibility(View.GONE);
		} else {
			_loadingLayout.setVisibility(View.GONE);
			_viewPager.setVisibility(View.VISIBLE);
		}
	}

}
