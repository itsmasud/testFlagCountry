package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.data.workorder.User;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.service.toast.ToastClient;
import com.fieldnation.ui.CardView;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.utils.misc;

import java.util.LinkedList;
import java.util.List;

public class MessageFragment extends WorkorderFragment {
    private static final String TAG = "MessageFragment";

    // UI
    private ListView _listview;
    private MessageInputView _inputView;
    private CardView _emptyMessageLayout;

    // Data
    private Workorder _workorder;
    private WorkorderClient _workorderClient;
    private List<Message> _messages = new LinkedList<>();
    private MessagesAdapter _adapter;
    private boolean _isSubbed = false;

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
        _emptyMessageLayout = (CardView) view.findViewById(R.id.container_empty_message);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _isSubbed = false;
        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(activity);
    }

    @Override
    public void onDetach() {
        _workorderClient.disconnect(getActivity());
        _workorderClient = null;
        _isSubbed = false;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        _markReadRunnable.run();

        populateUi();
    }

    // todo remove
    private final Runnable _markReadRunnable = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && _workorder != null) {
                WorkorderClient.actionMarkMessagesRead(getActivity(), _workorder.getWorkorderId());
            } else {
                new Handler().postDelayed(_markReadRunnable, 1000);
            }
        }
    };

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

        if (getActivity() != null && _workorder != null)
            WorkorderClient.listMessages(getActivity(), _workorder.getWorkorderId(), false, false);
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

        if (getActivity() == null)
            return;

        Log.v(TAG, "getMessages");

        WorkorderClient.listMessages(getActivity(), _workorder.getWorkorderId(), false, false);
    }

    private void populateUi() {
        if (_inputView == null)
            return;

        if (_workorder != null) {
            _inputView.setButtonEnabled(true);
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
            _emptyMessageLayout.setVisibility(View.VISIBLE);
        } else {
            _emptyMessageLayout.setVisibility(View.GONE);
        }

        if (getAdapter() != null) {
            // debug testing
            Log.v(TAG, "rebuildList: inside ELSE getAdapter() == null");

            getAdapter().setMessages(_messages);
            if (_messages != null && _messages.size() > 0)
                _listview.setSelection(_messages.size() - 1);
        }
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

                WorkorderClient.actionAddMessage(getActivity(), _workorder.getWorkorderId(),
                        _inputView.getInputText());

                _inputView.clearText();
            }
        }
    };

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
        _workorderClient.subActions(_workorder.getWorkorderId());
        _isSubbed = true;
    }

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
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

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            getMessages();
        }
    };
}
