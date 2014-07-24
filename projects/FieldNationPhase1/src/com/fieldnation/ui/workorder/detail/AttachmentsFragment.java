package com.fieldnation.ui.workorder.detail;

import java.util.LinkedList;
import java.util.List;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.ui.workorder.WorkorderTabView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class AttachmentsFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.AttachmentsFragment";

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
		return inflater.inflate(R.layout.fragment_workorder_attachments, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_gs = (GlobalState) getActivity().getApplicationContext();
		// _gs.requestAuthentication(_authClient);

		_listview = (ListView) view.findViewById(R.id.messages_listview);
	}

	@Override
	public void update() {
		// TODO Method Stub: update()
		Log.v(TAG, "Method Stub: update()");

	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
