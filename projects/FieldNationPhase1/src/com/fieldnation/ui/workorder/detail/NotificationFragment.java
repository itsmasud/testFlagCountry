package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;
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
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.ui.workorder.WorkorderListAdapter;

import eu.erikw.PullToRefreshListView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NotificationFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.NotificationFragment";

	private int WEB_LIST_NOTIFICATIONS = 1;

	// UI
	private PullToRefreshListView _listview;
	private SmoothProgressBar _loadingProgress;

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

		_gs = (GlobalState) getActivity().getApplicationContext();
		_gs.requestAuthentication(_authclient);
		_listview = (PullToRefreshListView) view.findViewById(R.id.listview);
		_listview.setOnRefreshListener(_listview_onRefresh);
		_adapter = new NotificationListAdapter();
		_listview.setAdapter(_adapter);
		_loadingProgress = (SmoothProgressBar) view.findViewById(R.id.loading_progress);

	}

	@Override
	public void update() {
		getNotifications();
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		populateUi();
		getNotifications();
	}

	public void populateUi() {
		if (_notes == null)
			return;
		if (_workorder == null)
			return;

		_adapter.setNotifications(_notes);

		_loadingProgress.setVisibility(View.GONE);
		_listview.onRefreshComplete();
	}

	private void getNotifications() {
		if (_service == null)
			return;

		_listview.setRefreshing();
		_loadingProgress.setVisibility(View.VISIBLE);
		_notes = null;
		WEB_LIST_NOTIFICATIONS = _rand.nextInt();
		_gs.startService(_service.listNotifications(WEB_LIST_NOTIFICATIONS, _workorder.getWorkorderId(), false));
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private PullToRefreshListView.OnRefreshListener _listview_onRefresh = new PullToRefreshListView.OnRefreshListener() {
		@Override
		public void onRefresh() {
			getNotifications();
		}
	};
	private AuthenticationClient _authclient = new AuthenticationClient() {
		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authclient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
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

			try {
				JsonArray ja = new JsonArray(new String(resultData.getByteArray(WorkorderService.KEY_RESPONSE_DATA)));
				_notes = new LinkedList<Notification>();

				for (int i = 0; i < ja.size(); i++) {
					JsonObject obj = ja.getJsonObject(i);
					_notes.add(Notification.fromJson(obj));
				}

				populateUi();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	};

}
