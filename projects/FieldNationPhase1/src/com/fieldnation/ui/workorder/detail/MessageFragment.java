package com.fieldnation.ui.workorder.detail;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
import com.fieldnation.rpc.common.WebServiceConstants;
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
import android.widget.RelativeLayout;

public class MessageFragment extends WorkorderFragment {
	private static final String TAG = "ui.workorder.detail.MessageFragment";

	private int WEB_GET_MESSAGES = 1;
	private int WEB_GET_PROFILE = 2;
	private int WEB_NEW_MESSAGE = 3;

	// UI
	private ListView _listview;
	private EditText _messageEditText;
	private Button _sendButton;
	private RelativeLayout _loadingLayout;

	// Data
	private Random _rand = new Random(System.currentTimeMillis());
	private WorkorderTabView.Listener _tabViewListener;
	private GlobalState _gs;
	private ProfileService _profileService;
	private Profile _profile;
	private WorkorderService _workorderService;
	private Workorder _workorder;
	private List<Message> _messages = new LinkedList<Message>();
	private MessagesAdapter _adapter;

	/*-*************************************-*/
	/*-				LifeCycle				-*/
	/*-*************************************-*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_workorder_messages, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		_gs = (GlobalState) getActivity().getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_listview = (ListView) view.findViewById(R.id.messages_listview);

		View input = LayoutInflater.from(_gs).inflate(R.layout.view_message_input, _listview, false);
		_messageEditText = (EditText) input.findViewById(R.id.message_edittext);
		_sendButton = (Button) input.findViewById(R.id.send_button);
		_sendButton.setOnClickListener(_send_onClick);

		_loadingLayout = (RelativeLayout) view.findViewById(R.id.loading_layout);
		_listview.addFooterView(input);

		_loadingLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void update() {
		getMessages();
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		getMessages();
	}

	private void getMessages() {
		if (_workorderService == null)
			return;

		if (_workorder == null)
			return;

		if (_profile == null)
			return;

		if (_gs == null)
			return;

		_messages.clear();
		WEB_GET_MESSAGES = _rand.nextInt();
		_gs.startService(_workorderService.listMessages(WEB_GET_MESSAGES, _workorder.getWorkorderId(), false));
	}

	private void rebuildList() {
		_adapter.setMessages(_messages);
		_loadingLayout.setVisibility(View.GONE);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _send_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			WEB_NEW_MESSAGE = _rand.nextInt();
			_gs.startService(_workorderService.addMessage(WEB_NEW_MESSAGE, _workorder.getWorkorderId(),
					_messageEditText.getText().toString()));
		}
	};

	private AuthenticationClient _authClient = new AuthenticationClient() {
		@Override
		public void onAuthentication(String username, String authToken) {
			_profileService = new ProfileService(_gs, username, authToken, _resultReceiver);
			_workorderService = new WorkorderService(_gs, username, authToken, _resultReceiver);
			WEB_GET_PROFILE = _rand.nextInt();
			_gs.startService(_profileService.getMyUserInformation(WEB_GET_PROFILE, true));
			getMessages();
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
			Log.v(TAG, "resultCode:" + resultCode);
			Log.v(TAG, "WEB_GET_PROFILE:" + WEB_GET_PROFILE);
			Log.v(TAG, "WEB_GET_MESSAGES:" + WEB_GET_MESSAGES);
			Log.v(TAG, "WEB_NEW_MESSAGE:" + WEB_NEW_MESSAGE);

			if (resultCode == WEB_GET_PROFILE) {
				try {
					_profile = Profile.fromJson(new JsonObject(new String(
							resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA))));

					_adapter = new MessagesAdapter(_profile);
					_listview.setAdapter(_adapter);

					getMessages();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (resultCode == WEB_GET_MESSAGES) {
				try {
					JsonArray messages = new JsonArray(new String(
							resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));

					for (int i = 0; i < messages.size(); i++) {
						JsonObject obj = messages.getJsonObject(i);

						_messages.add(Message.fromJson(obj));
					}

					rebuildList();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (resultCode == WEB_NEW_MESSAGE) {
				_messageEditText.setText("");
				getMessages();
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			Log.v(TAG, "WS Fail");
			if (_profileService != null) {
				// _gs.invalidateAuthToken(_profileService.getAuthToken());
			}
			// _gs.requestAuthentication(_authClient);

			// TODO, a fail here probably means that this workroder is not
			// assigned, therefore no messages.
		}
	};
}
