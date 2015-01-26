package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.topics.TopicService;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.Stopwatch;

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
    private RefreshView _refreshView;

    // Data
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

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);

        _listview = (ListView) view.findViewById(R.id.messages_listview);
        _inputView = (MessageInputView) view.findViewById(R.id.input_view);
        _inputView.setOnSendButtonClick(_send_onClick);
    }

    @Override
    public void onResume() {
        super.onResume();
        AuthTopicService.subscribeAuthState(getActivity(), 0, TAG, _authReceiver);
    }

    @Override
    public void onPause() {
        TopicService.delete(getActivity(), TAG);

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
        Log.v(TAG, "update");
        //getMessages();
    }

    @Override
    public void setWorkorder(Workorder workorder, boolean isCached) {
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

        if (getActivity() == null)
            return;

        _refreshView.startRefreshing();

        _messages.clear();
        if (_adapter != null)
            _adapter.notifyDataSetChanged();

        Log.v(TAG, "getMessages");
        WEB_GET_MESSAGES = _rand.nextInt();
        getActivity().startService(_workorderService.listMessages(WEB_GET_MESSAGES, _workorder.getWorkorderId(), false));
    }

    @Override
    public void setLoading(boolean isLoading) {
        if (_refreshView != null) {
            if (isLoading) {
                _refreshView.startRefreshing();
            } else {
                _refreshView.refreshComplete();
            }
        }
    }

    private void rebuildList() {
        if (getAdapter() != null) {
            getAdapter().setMessages(_messages);
            _listview.setSelection(getAdapter().getCount() - 1);
        }
        _refreshView.refreshComplete();
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
            _refreshView.startRefreshing();
            WEB_NEW_MESSAGE = _rand.nextInt();
            Log.v(TAG, "_send_onClick");
            getActivity().startService(_workorderService.addMessage(WEB_NEW_MESSAGE, _workorder.getWorkorderId(),
                    _inputView.getInputText()));
            _inputView.clearText();
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (getActivity() == null)
                return;

            if (_profileService == null || _workorderService == null || isNew) {
                _profileService = new ProfileService(getActivity(), username, authToken, _resultReceiver);
                _workorderService = new WorkorderService(getActivity(), username, authToken, _resultReceiver);
                WEB_GET_PROFILE = _rand.nextInt();
                Log.v(TAG, "_authReceiver");
                getActivity().startService(_profileService.getMyUserInformation(WEB_GET_PROFILE, true));
                getMessages();
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _profileService = null;
            _workorderService = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _profileService = null;
            _workorderService = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(getActivity());
        }
    };

    private class MessageAsyncTask extends AsyncTaskEx<Bundle, Object, List<Message>> {

        @Override
        protected List<Message> doInBackground(Bundle... params) {
            Bundle resultData = params[0];
            List<Message> list = new LinkedList<>();
            try {
                JsonArray messages = new JsonArray(new String(
                        resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));

                for (int i = 0; i < messages.size(); i++) {
                    JsonObject obj = messages.getJsonObject(i);
                    list.add(Message.fromJson(obj));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            super.onPostExecute(messages);
            _messages = messages;
            rebuildList();
            if (_messages.size() == 0) {
                _inputView.setHint(R.string.start_the_conversation);
            } else {
                _inputView.setHint(R.string.continue_the_conversation);
            }
        }
    }


    private WebResultReceiver _resultReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_GET_PROFILE) {
                Stopwatch stopwatch = new Stopwatch(true);
                new AsyncTaskEx<Bundle, Object, Profile>() {
                    @Override
                    protected Profile doInBackground(Bundle... params) {
                        try {
                            return Profile.fromJson(new JsonObject(new String(
                                    params[0].getByteArray(WebServiceConstants.KEY_RESPONSE_DATA))));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Profile profile) {
                        super.onPostExecute(profile);
                        _profile = profile;
                        ((GlobalState) getContext().getApplicationContext()).setProfileId(profile.getUserId());
                        getAdapter();
                        getMessages();
                    }
                }.executeEx(resultData);
                Log.v(TAG, "WEB_GET_PROFILE time " + stopwatch.finish());
            } else if (resultCode == WEB_GET_MESSAGES) {
                new MessageAsyncTask().executeEx(resultData);
            } else if (resultCode == WEB_NEW_MESSAGE) {
                Stopwatch stopwatch = new Stopwatch(true);
                _inputView.clearText();
                getMessages();
                Log.v(TAG, "WEB_NEW_MESSAGE time " + stopwatch.finish());
            }
        }

        @Override
        public Context getContext() {
            return MessageFragment.this.getActivity();
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);

            if (getActivity() == null)
                return;

            _profileService = null;
            _workorderService = null;

            AuthTopicService.requestAuthInvalid(getActivity());
            Toast.makeText(getActivity(), "Could not complete request", Toast.LENGTH_LONG).show();
        }
    };
}
