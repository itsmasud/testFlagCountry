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

public class WorkorderDetailFragment extends Fragment {
	private static final String TAG = "WorkorderDetailFragment";

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

	public WorkorderDetailFragment setTabBarListener(WorkorderTabView.Listener listener) {
		_tabViewListener = listener;

		if (_tabView != null) {
			_tabView.setListener(_tabViewListener);
		}

		return this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_detail, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_tabView = (WorkorderTabView) view.findViewById(R.id.tabview);
		_tabView.setListener(_tabViewListener);
	}

	@Override
	public void onResume() {
		_tabView.setSelected(0);
		super.onResume();
	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	public void update() {
		_tabView.setSelected(0);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

}
