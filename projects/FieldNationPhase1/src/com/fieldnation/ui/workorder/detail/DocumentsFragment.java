package com.fieldnation.ui.workorder.detail;

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
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.workorder.WorkorderFragment;

public class DocumentsFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DocumentsFragment";

	private static final int WEB_GET_DOCUMENTS = 1;

	// UI
	private ListView _listview;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;
	private WorkorderService _service;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_documents, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_gs = (GlobalState) getActivity().getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_listview = (ListView) view.findViewById(R.id.listview);
	}

	@Override
	public void update() {
		// TODO Method Stub: update()
		Log.v(TAG, "Method Stub: update()");

	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;

		if (_service != null) {
			_service.getDeliverableList(WEB_GET_DOCUMENTS, _workorder.getWorkorderId(), false);
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private AuthenticationClient _authClient = new AuthenticationClient() {
		@Override
		public void onAuthenticationFailed(Exception ex) {
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_service = new WorkorderService(getActivity(), username, authToken, _resultReceiver);
			getActivity().startService(
					_service.getDeliverableList(WEB_GET_DOCUMENTS, _workorder.getWorkorderId(), false));
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

			String data = new String(resultData.getByteArray(WorkorderService.KEY_RESPONSE_DATA));

			Log.v(TAG, data);
			Log.v(TAG, "BP");
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");
		}
	};

}
