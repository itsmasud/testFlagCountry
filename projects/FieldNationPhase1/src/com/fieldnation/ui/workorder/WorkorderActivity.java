package com.fieldnation.ui.workorder;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.DrawerActivity;
import com.fieldnation.ui.workorder.detail.AttachmentsFragment;
import com.fieldnation.ui.workorder.detail.DetailFragment;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class WorkorderActivity extends DrawerActivity {
	private static final String TAG = "ui.workorder.WorkorderActivity";

	public static final String INTENT_FIELD_WORKORDER_ID = "com.fieldnation.ui.workorder.WorkorderActivity:workorder_id";

	private static final int RPC_GET_DETAIL = 1;

	// UI
	private ViewPager _viewPager;
	private WorkorderFragment[] _fragments;
	private WorkorderTabView _tabview;
	private RelativeLayout _loadingLayout;

	// Data
	private GlobalState _gs;
	private long _workorderId = 0;
	private int _currentFragment = 0;
	private boolean _created = false;
	private PagerAdapter _pagerAdapter;
	private Workorder _workorder = null;

	// Services
	private WorkorderService _woRpc;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workorder);

		Intent intent = getIntent();
		if (intent != null) {
			if (intent.hasExtra(INTENT_FIELD_WORKORDER_ID)) {
				_workorderId = intent.getLongExtra(INTENT_FIELD_WORKORDER_ID, 0);
			}
		}

		if (_workorderId == 0) {
			// epic fail!
			Log.e(TAG, "must have a workorder id!");
			finish();
		}

		if (!_created) {
			addActionBarAndDrawer(R.id.container);
			buildFragments();
			_created = true;
		}

		_loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
		setLoading(true);

		_gs = (GlobalState) getApplicationContext();
		_gs.requestAuthentication(_authClient);
	}

	private void buildFragments() {
		_viewPager = (ViewPager) findViewById(R.id.content_viewpager);

		_fragments = new WorkorderFragment[5];
		_fragments[0] = new DetailFragment();
		_fragments[1] = new TasksFragment();
		_fragments[2] = new MessageFragment();
		_fragments[3] = new NotificationFragment();
		_fragments[4] = new AttachmentsFragment();

		_pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		_viewPager.setAdapter(_pagerAdapter);
		_viewPager.setOnPageChangeListener(_viewPager_onChange);

		_tabview = (WorkorderTabView) findViewById(R.id.tabview);
		_tabview.setListener(_tabview_onChange);

		_viewPager.setCurrentItem(0, false);
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/

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
			// TODO Method Stub: onAuthenticationFailed()
			Log.v(TAG, "Method Stub: onAuthenticationFailed()");

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
				_workorder = Workorder.fromJson(
						new JsonObject(new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA))),
						WorkorderDataSelector.ASSIGNED);

				_workorder.addListener(_workorder_listener);

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
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");
		}
	};

	private Workorder.Listener _workorder_listener = new Workorder.Listener() {
		@Override
		public void onChange(Workorder workorder) {
			startService(_woRpc.getDetails(RPC_GET_DETAIL, _workorderId, false));
			setLoading(true);
		}
	};

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
