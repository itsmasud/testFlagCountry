package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Deliverable;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.workorder.WorkorderFragment;

public class DeliverableFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DeliverableFragment";

	private static final int WEB_GET_DOCUMENTS = 1;
	private static final int WEB_GET_PROFILE = 2;

	// UI
	private ListView _listview;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private WorkorderService _service;
	private ProfileService _profileService;
	private Profile _profile = null;
	private List<Deliverable> _deliverables = null;
	private DeliverableListAdapter _adapter;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_deliverables, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_gs = (GlobalState) getActivity().getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_listview = (ListView) view.findViewById(R.id.listview);
		_adapter = new DeliverableListAdapter();
		_listview.setAdapter(_adapter);
	}

	@Override
	public void update() {
		getData();
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		getData();
	}

	private void getData() {
		if (_profileService != null && _profile == null)
			getActivity().startService(_profileService.getMyUserInformation(WEB_GET_PROFILE, true));

		if (_service == null)
			return;

		if (_workorder == null)
			return;

		getActivity().startService(_service.listDeliverables(WEB_GET_DOCUMENTS, _workorder.getWorkorderId(), false));
	}

	private void populateUi() {
		if (_deliverables == null)
			return;
		if (_profile == null)
			return;

		_adapter.setData(_profile.getUserId(), _deliverables);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private AuthenticationClient _authClient = new AuthenticationClient() {
		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getActivity(), username, authToken, _resultReceiver);
			_profileService = new ProfileService(getActivity(), username, authToken, _resultReceiver);
			getData();
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

	private WebServiceResultReceiver _resultReceiver = new WebServiceResultReceiver(new Handler()) {
		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			// TODO Method Stub: onSuccess()
			Log.v(TAG, "Method Stub: onSuccess()");
			if (resultCode == WEB_GET_DOCUMENTS) {
				_deliverables = null;
				try {
					_deliverables = new LinkedList<Deliverable>();

					String data = new String(resultData.getByteArray(WorkorderService.KEY_RESPONSE_DATA));
					JsonArray ja = new JsonArray(data);
					for (int i = 0; i < ja.size(); i++) {
						_deliverables.add(Deliverable.fromJson(ja.getJsonObject(i)));
					}
				} catch (Exception ex) {
					// TODO mulligan?
					ex.printStackTrace();
					_deliverables = null;
				}
			} else if (resultCode == WEB_GET_PROFILE) {
				_profile = null;
				try {
					_profile = Profile.fromJson(new JsonObject(new String(
							resultData.getByteArray(ProfileService.KEY_RESPONSE_DATA))));
				} catch (Exception e) {
					// TODO mulligan?
					e.printStackTrace();
					_profile = null;
				}
			}
			populateUi();
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			if (_service != null) {
				_gs.invalidateAuthToken(_service.getAuthToken());
			} else if (_profileService != null) {
				_gs.invalidateAuthToken(_profileService.getAuthToken());
			}
			_gs.requestAuthenticationDelayed(_authClient);
			_service = null;
			_profileService = null;
		}
	};

}
