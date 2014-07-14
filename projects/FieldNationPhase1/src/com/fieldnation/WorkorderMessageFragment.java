package com.fieldnation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.ScrollingTabContainerView.TabView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WorkorderMessageFragment extends Fragment {
	private static final String TAG = "WorkorderMessageFragment";

	// UI
	private WorkorderTabView _tabView;
	private ListView _listview;
	private EditText _messageEditText;
	private Button _sendButton;

	// Data
	private WorkorderTabView.Listener _tabViewListener;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Method Stub: onCreate()
		Log.v(TAG, "Method Stub: onCreate()");
		super.onCreate(savedInstanceState);
	}

	public WorkorderMessageFragment setTabBarListener(WorkorderTabView.Listener listener) {
		_tabViewListener = listener;

		if (_tabView != null) {
			_tabView.setListener(_tabViewListener);
		}

		return this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_messages, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		_listview = (ListView) view.findViewById(R.id.messages_listview);
		_messageEditText = (EditText) view.findViewById(R.id.message_edittext);
		_sendButton = (Button) view.findViewById(R.id.send_button);

		_tabView = new WorkorderTabView(getActivity());
		_listview.addHeaderView(_tabView);
		_tabView.setListener(_tabViewListener);

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
