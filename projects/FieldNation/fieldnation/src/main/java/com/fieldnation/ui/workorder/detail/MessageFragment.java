package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.ScreenName;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.data.workorder.User;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderFragment;

import java.util.LinkedList;
import java.util.List;

public class MessageFragment extends WorkorderFragment {
    private static final String TAG = "MessageFragment";

    // UI
    private ListView _listview;
    private MessageInputView _inputView;
    private ViewStub _emptyMessageViewStub;
    private RefreshView _refreshView;

    // Data
    private Workorder _workorder;
    private WorkorderClient _workorderClient;
    private List<Message> _messages = new LinkedList<>();
    private MessagesAdapter _adapter;
    private boolean _isSubbed = false;
    private boolean _isMarkedRead = false;

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

        _listview = (ListView) view.findViewById(R.id.messages_listview);
        _inputView = (MessageInputView) view.findViewById(R.id.input_view);
        _inputView.setOnSendButtonClick(_send_onClick);
        _inputView.setButtonEnabled(false);
        _emptyMessageViewStub = (ViewStub) view.findViewById(R.id.emptyMessage_viewstub);

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _isSubbed = false;
        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());
    }

    @Override
    public void onDetach() {
        if (_workorderClient != null && _workorderClient.isConnected()) {
            _workorderClient.disconnect(App.get());
            _workorderClient = null;
        }
        _isSubbed = false;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateUi();
    }

    @Override
    public void onPause() {
        if (_adapter != null) {
            _adapter.notifyDataSetInvalidated();
            _adapter = null;
        }
        super.onPause();
    }

    @Override
    public void update() {
        Log.v(TAG, "update");
        Tracker.screen(App.get(), ScreenName.workOrderDetailsMessages());
        if (_workorder != null) {
            _refreshView.startRefreshing();
            WorkorderClient.listMessages(App.get(), _workorder.getWorkorderId(), false, false);
        }
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        populateUi();
        subscribeData();
        getMessages();
    }

    private void getMessages() {
        if (_workorder == null)
            return;

        Log.v(TAG, "getMessages");

        WorkorderClient.listMessages(App.get(), _workorder.getWorkorderId(), false, false);
    }

    private void populateUi() {
        if (_inputView == null)
            return;

        if (_workorder != null) {
            _inputView.setButtonEnabled(true);

            if (!_isMarkedRead) {
                _isMarkedRead = true;
                WorkorderClient.actionMarkMessagesRead(App.get(), _workorder.getWorkorderId());
                ProfileClient.get(App.get());
            }

        } else {
            _inputView.setButtonEnabled(false);
        }

    }

    @Override
    public void setLoading(boolean isLoading) {
    }

    private void rebuildList() {
        // debug testing
        Log.v(TAG, "rebuildList");
        if (_messages == null || _messages.size() == 0) {
            _emptyMessageViewStub.setVisibility(View.VISIBLE);
        } else {
            _emptyMessageViewStub.setVisibility(View.GONE);
        }

        if (getAdapter() != null) {
            // debug testing
            Log.v(TAG, "rebuildList: inside ELSE getAdapter() == null");

            getAdapter().setMessages(_messages);
            if (_messages != null && _messages.size() > 0)
                _listview.setSelection(_messages.size() - 1);
        }

        _refreshView.refreshComplete();
    }

    private MessagesAdapter getAdapter() {
        if (this.getActivity() == null)
            return null;

        try {
            if (_adapter == null && App.get().getProfile() != null) {
                _adapter = new MessagesAdapter();
                _listview.setAdapter(_adapter);
            }
            return _adapter;
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _send_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {

                if (misc.isEmptyOrNull(_inputView.getInputText())) {
                    ToastClient.toast(App.get(), "Please enter a message", Toast.LENGTH_SHORT);
                    return;
                }

                if (App.get().getProfile() == null) {
                    ToastClient.toast(App.get(), "Can't send message right now, please try again later", Toast.LENGTH_LONG);
                    return;
                }

                Log.v(TAG, "_send_onClick");

                _messages.add(new Message(_workorder.getWorkorderId(),
                        User.fromJson(App.get().getProfile().toJson()), _inputView.getInputText()));
                rebuildList();

                WorkorderClient.actionAddMessage(App.get(), _workorder.getWorkorderId(),
                        _inputView.getInputText());

                _inputView.clearText();
            }
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private void subscribeData() {
        if (_workorder == null)
            return;

        if (_workorderClient == null)
            return;

        if (!_workorderClient.isConnected())
            return;

        if (_isSubbed)
            return;

        _workorderClient.subListMessages(_workorder.getWorkorderId(), false);
        _isSubbed = true;
    }

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            subscribeData();
        }

        @Override
        public void onMessageList(long workorderId, List<Message> messages, boolean failed) {
            if (failed || messages == null)
                return;

            _messages = messages;
            rebuildList();
        }
    };
}
