package com.fieldnation;

import android.util.Log;

import android.os.Bundle;

public class MessageListActivity extends DrawerActivity {
	private static final String TAG = "MessageListActivity";

	// UI
	private ListViewEx _messageListView;

	// Data

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_list);

		_messageListView = (ListViewEx) findViewById(R.id.messages_listview);

		addActionBarAndDrawer(R.id.container);
	}
}
