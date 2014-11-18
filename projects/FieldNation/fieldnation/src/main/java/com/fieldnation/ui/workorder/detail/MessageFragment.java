package com.fieldnation.ui.workorder.detail;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MessageFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.MessageFragment";

    private int WEB_GET_MESSAGES = 1;
    private int WEB_GET_PROFILE = 2;
    private int WEB_NEW_MESSAGE = 3;

    // UI
    private ListView _listview;
    private MessageInputView _inputView;

    // Data
    private GlobalState _gs;
    private Random _rand = new Random(System.currentTimeMillis());
    private Profile _profile;
    private ProfileService _profileService;
    private Workorder _workorder;
    private WorkorderService _workorderService;
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
        Log.v(TAG, "onViewCreated");

        _gs = (GlobalState) getActivity().getApplicationContext();
        _gs.requestAuthentication(_authClient);

        _listview = (ListView) view.findViewById(R.id.messages_listview);
        _inputView = (MessageInputView) view.findViewById(R.id.input_view);
        _inputView.setOnSendButtonClick(_send_onClick);

        setLoading(true);
    }

    @Override
    public void onPause() {
        WEB_GET_MESSAGES = 1;
        WEB_GET_PROFILE = 2;
        WEB_NEW_MESSAGE = 3;
        if (_adapter != null) {
            _adapter.notifyDataSetInvalidated();
            _adapter = null;
        }
        super.onPause();
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

        setLoading(true);

        _messages.clear();
        if (_adapter != null)
            _adapter.notifyDataSetChanged();

        WEB_GET_MESSAGES = _rand.nextInt();
        _gs.startService(_workorderService.listMessages(WEB_GET_MESSAGES, _workorder.getWorkorderId(), false));
        setLoading(true);
    }

    private void rebuildList() {
        if (getAdapter() != null) {
            getAdapter().setMessages(_messages);
            _listview.setSelection(getAdapter().getCount() - 1);
        }
        setLoading(false);
    }

    private MessagesAdapter getAdapter() {
        if (this.getActivity() == null)
            return null;

        if (_profile == null)
            return null;

        try {
            if (_adapter == null) {
                _adapter = new MessagesAdapter(_profile);
                _listview.setAdapter(_adapter);
            }
            return _adapter;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private View.OnClickListener _send_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLoading(true);
            WEB_NEW_MESSAGE = _rand.nextInt();
            _gs.startService(_workorderService.addMessage(WEB_NEW_MESSAGE, _workorder.getWorkorderId(),
                    _inputView.getInputText()));
            _inputView.clearText();
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

                    getAdapter();
                    getMessages();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                setLoading(false);
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

                if (_messages.size() == 0) {
                    _inputView.setHint(R.string.start_the_conversation);
                } else {
                    _inputView.setHint(R.string.continue_the_conversation);
                }
                setLoading(false);
            } else if (resultCode == WEB_NEW_MESSAGE) {
                _inputView.clearText();
                getMessages();
                setLoading(false);
            }
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            if (_profileService != null) {
                _gs.invalidateAuthToken(_profileService.getAuthToken());
            } else if (_workorderService != null) {
                _gs.invalidateAuthToken(_workorderService.getAuthToken());
            }
            _gs.requestAuthenticationDelayed(_authClient);
            Toast.makeText(getActivity(), "Could not complete request", Toast.LENGTH_LONG).show();
            setLoading(false);
        }
    };

    @Override
    public void doAction(Bundle bundle) {
        // TODO Method Stub: doAction()
        Log.v(TAG, "Method Stub: doAction()");

    }
}
