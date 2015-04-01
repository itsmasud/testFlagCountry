package com.fieldnation.ui.workorder.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderDataClient;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderFragment;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MessageFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.MessageFragment";

    private int WEB_GET_MESSAGES = 1;
    private int WEB_NEW_MESSAGE = 3;
    private int WEB_MARK_READ = 4;

    // UI
    private ListView _listview;
    private MessageInputView _inputView;
    private RefreshView _refreshView;

    // Data
    private Random _rand = new Random(System.currentTimeMillis());
    private Profile _profile;
    private Workorder _workorder;
    private WorkorderDataClient _workorderClient;
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
// TODO remove
//        AuthTopicService.subscribeAuthState(getActivity(), 0, TAG, _authReceiver);
//        Topics.subscrubeProfileUpdated(getActivity(), TAG + ":ProfileService", _profile_topicReceiver);

        _markReadRunnable.run();
    }

    // todo remove
    private final Runnable _markReadRunnable = new Runnable() {
        @Override
        public void run() {
/*
            if (getActivity() != null && _workorderService != null && _workorder != null) {
                getActivity().startService(_workorderService.markMessagesRead(WEB_MARK_READ, _workorder.getWorkorderId()));
            } else {
                new Handler().postDelayed(_markReadRunnable, 1000);
            }
*/

        }
    };

    @Override
    public void onPause() {
// todo remove
//        TopicService.delete(getActivity(), TAG);
//        TopicService.delete(getActivity(), TAG + ":ProfileService");

        WEB_GET_MESSAGES = 1;
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
//        if (_workorderService != null) {
//            getActivity().startService(
//                    _workorderService.listMessages(WEB_MARK_READ, _workorder.getWorkorderId(), true, false));
//        }
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        getMessages();
    }

    private void getMessages() {
//        if (_workorderService == null)
//            return;

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
// todo remove
//        getActivity().startService(_workorderService.listMessages(WEB_GET_MESSAGES, _workorder.getWorkorderId(), false));
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
            if (getActivity() != null) {
                _refreshView.startRefreshing();
                WEB_NEW_MESSAGE = _rand.nextInt();
                Log.v(TAG, "_send_onClick");
// todo remove
//                getActivity().startService(_workorderService.addMessage(WEB_NEW_MESSAGE, _workorder.getWorkorderId(),
//                        _inputView.getInputText()));

                _inputView.clearText();
            }
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
// todo remove
/*
    private final TopicReceiver _profile_topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (getActivity() == null)
                return;

            if (Topics.TOPIC_PROFILE_UPDATE.equals(topicId)) {
                parcel.setClassLoader(getActivity().getClassLoader());
                _profile = parcel.getParcelable(Topics.TOPIC_PROFILE_PARAM_PROFILE);
            }
            getAdapter();
            getMessages();
        }
    };
*/

    // todo remove
/*
    private final AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (getActivity() == null)
                return;

            if (_workorderService == null || isNew) {
                _workorderService = new WorkorderWebClient(getActivity(), username, authToken, _resultReceiver);
                Log.v(TAG, "_authReceiver");
                getMessages();
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _workorderService = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _workorderService = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(getActivity());
        }
    };
*/

/*
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
*/


/*
    private WebResultReceiver _resultReceiver = new WebResultReceiver(new Handler()) {
        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            if (resultCode == WEB_GET_MESSAGES) {
                new MessageAsyncTask().executeEx(resultData);
            } else if (resultCode == WEB_NEW_MESSAGE) {
                Stopwatch stopwatch = new Stopwatch(true);
                _inputView.clearText();
                getMessages();
                Log.v(TAG, "WEB_NEW_MESSAGE time " + stopwatch.finish());
            } else if (resultCode == WEB_MARK_READ) {
// todo remove
                Topics.dispatchProfileInvalid(getActivity());
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

            _workorderService = null;

            AuthTopicService.requestAuthInvalid(getActivity());
            Toast.makeText(getActivity(), "Could not complete request", Toast.LENGTH_LONG).show();
        }
    };
*/
}
