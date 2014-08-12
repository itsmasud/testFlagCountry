package com.fieldnation.ui.workorder.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderFragment;

public class DocumentsFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.DocumentsFragment";

	// UI
	private ListView _listview;

	// Data
	private GlobalState _gs;
	private Workorder _workorder;

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
		// TODO Method Stub: setWorkorder()
		Log.v(TAG, "Method Stub: setWorkorder()");

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

			// TODO Method Stub: onAuthentication()
			Log.v(TAG, "Method Stub: onAuthentication()");

		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}
	};

}
