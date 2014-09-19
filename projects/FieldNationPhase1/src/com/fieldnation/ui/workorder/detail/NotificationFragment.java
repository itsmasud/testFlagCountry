package com.fieldnation.ui.workorder.detail;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.workorder.WorkorderFragment;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.State;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotificationFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.NotificationFragment";
	private int WEB_LIST_NOTIFICATIONS = 1;

	// UI
	private PullToRefreshListView _listview;
	private SmoothProgressBar _loadingProgress;
	private TextView _emptyTextView;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private Random _rand = new Random();
	private WorkorderService _service;
	private List<Notification> _notes;
	private NotificationListAdapter _adapter;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_notifications, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.v(TAG, "onCreateView");

		_gs = (GlobalState) getActivity().getApplicationContext();
		_gs.requestAuthentication(_authclient);
		_listview = (PullToRefreshListView) view.findViewById(R.id.listview);
		_listview.setOnRefreshListener(_listview_onRefresh);
		_listview.setStateListener(_listview_stateListener);
		_loadingProgress = (SmoothProgressBar) view.findViewById(R.id.loading_progress);
		_loadingProgress.setSmoothProgressDrawableCallbacks(_progressCallback);
		_loadingProgress.setMax(100);
		_emptyTextView = (TextView) view.findViewById(R.id.empty_textview);
	}

	@Override
	public void onPause() {
		super.onPause();
		WEB_LIST_NOTIFICATIONS = 0;
		if (_adapter != null) {
			_adapter.notifyDataSetInvalidated();
			_adapter = null;
		}
	};

	@Override
	public void update() {
		getNotifications();
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		Log.v(TAG,
				"setWorkorder: wokorder==null:" + (workorder == null) + " _service==null:" + (_service == null) + " _gs==null:" + (_gs == null));
		_workorder = workorder;

		populateUi();
		getNotifications();
	}

	public void populateUi() {
		Log.v(TAG, "populateUi, _notes:" + (_notes == null) + " _workorder:" + (_workorder == null));
		if (_notes == null)
			return;
		if (_workorder == null)
			return;

		Log.v(TAG, "populateUi");

		if (getAdapter() != null)
			getAdapter().setNotifications(_notes);

		if (_notes.size() == 0) {
			_emptyTextView.setVisibility(View.VISIBLE);
		}

		_loadingProgress.setVisibility(View.GONE);
		_listview.onRefreshComplete();
	}

	private void getNotifications() {
		Log.v(TAG, "getNotifications, _service:" + (_service == null) + " _workorder:" + (_workorder == null));
		if (_service == null)
			return;

		if (_workorder == null)
			return;
		Log.v(TAG, "getNotifications");

		_listview.setRefreshing();
		_loadingProgress.setVisibility(View.VISIBLE);
		_notes = null;
		WEB_LIST_NOTIFICATIONS = _rand.nextInt();
		_emptyTextView.setVisibility(View.GONE);
		try {
			_gs.startService(_service.listNotifications(WEB_LIST_NOTIFICATIONS, _workorder.getWorkorderId(), false));
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.v(TAG, "BP");
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private SmoothProgressDrawable.Callbacks _progressCallback = new SmoothProgressDrawable.Callbacks() {

		@Override
		public void onStop() {
			_loadingProgress.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			_loadingProgress.setVisibility(View.VISIBLE);
		}

	};

	private PullToRefreshListView.OnRefreshListener _listview_onRefresh = new PullToRefreshListView.OnRefreshListener() {
		@Override
		public void onRefresh() {
			getNotifications();
		}
	};

	private PullToRefreshListView.StateListener _listview_stateListener = new PullToRefreshListView.StateListener() {
		@Override
		public void onPull(int pullPercent) {
			if (_listview.getState() == PullToRefreshListView.State.PULL_TO_REFRESH) {
				float sep = 4f - 4 * Math.abs(pullPercent) / 100f;
				if (sep < 0)
					sep = 0f;

				_loadingProgress.setSmoothProgressDrawableSpeed(sep);
			}
		}

		@Override
		public void onStopPull() {
			_loadingProgress.setSmoothProgressDrawableSpeed(2f);
			_loadingProgress.setSmoothProgressDrawableReversed(true);
			_loadingProgress.setSmoothProgressDrawableSectionsCount(1);
			_loadingProgress.progressiveStop();
			_loadingProgress.setVisibility(View.GONE);
		}

		@Override
		public void onStateChange(State state) {
			if (state == State.RELEASE_TO_REFRESH) {
				// if (getAdapter() != null)
				// getAdapter().update(false);
				_loadingProgress.progressiveStart();
			}
		}

		@Override
		public void onStartPull() {
			_loadingProgress.setSmoothProgressDrawableSectionsCount(1);
			_loadingProgress.setSmoothProgressDrawableReversed(true);
			_loadingProgress.progressiveStart();
		}

	};

	private AuthenticationClient _authclient = new AuthenticationClient() {
		@Override
		public void onAuthenticationFailed(Exception ex) {
			Log.v(TAG, "onAuthenticationFailed");
			_gs.requestAuthenticationDelayed(_authclient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			Log.v(TAG, "onAuthentication");
			_service = new WorkorderService(_gs, username, authToken, _resultReceiver);
			getNotifications();
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {
		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()" + errorType + resultData.toString());
		}

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "onSuccess");

			if (resultCode == WEB_LIST_NOTIFICATIONS) {
				Log.v(TAG, "onSuccess2");
				try {
					JsonArray ja = new JsonArray(new String(
							resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));
					_notes = new LinkedList<Notification>();

					for (int i = 0; i < ja.size(); i++) {
						JsonObject obj = ja.getJsonObject(i);
						_notes.add(Notification.fromJson(obj));
					}

					populateUi();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	private NotificationListAdapter getAdapter() {
		if (this.getActivity() == null)
			return null;
		try {
			if (_adapter == null) {
				_adapter = new NotificationListAdapter();
				_listview.setAdapter(_adapter);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

		return _adapter;
	}
}
