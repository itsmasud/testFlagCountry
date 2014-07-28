package com.fieldnation.ui.workorder.detail;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
