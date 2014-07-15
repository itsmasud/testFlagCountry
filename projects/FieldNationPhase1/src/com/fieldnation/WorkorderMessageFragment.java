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

public class WorkorderMessageFragment extends WorkorderFragment {
	private static final String TAG = "WorkorderMessageFragment";

	// UI
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
	}

	@Override
	public void update() {
		// TODO Method Stub: update()
		Log.v(TAG, "Method Stub: update()");

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
