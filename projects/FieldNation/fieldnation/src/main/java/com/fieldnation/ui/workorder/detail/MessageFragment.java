package com.fieldnation.ui.workorder.detail;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fieldnation.GlobalState;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderFragment;

import java.util.LinkedList;
import java.util.List;

public class MessageFragment extends WorkorderFragment {
    private static final String TAG = "ui.workorder.detail.MessageFragment";

    // UI
    private ListView _listview;
    private MessageInputView _inputView;
    private RefreshView _refreshView;

    // Data
    private Workorder _workorder;
    private WorkorderClient _workorderClient;
    private List<Message> _messages = new LinkedList<Message>();
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

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);

        _listview = (ListView) view.findViewById(R.id.messages_listview);
        _inputView = (MessageInputView) view.findViewById(R.id.input_view);
        _inputView.setOnSendButtonClick(_send_onClick);
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
            WorkorderClient.listMessages(getActivity(), _workorder.getWorkorderId(), false);
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        subscribeData();
        getMessages();
    }

    private void getMessages() {
        if (_workorder == null)
            return;

        if (getActivity() == null)
            return;

        if (_refreshView == null)
            return;

        _refreshView.startRefreshing();

//        _messages.clear();
        if (_adapter != null)
            _adapter.notifyDataSetChanged();

        Log.v(TAG, "getMessages");

        WorkorderClient.listMessages(getActivity(), _workorder.getWorkorderId(), false);
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

        try {
            if (_adapter == null) {
                _adapter = new MessagesAdapter(GlobalState.getContext().getProfile());
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

                Log.v(TAG, "_send_onClick");

                WorkorderClient.actionAddMessage(getActivity(),
                        _workorder.getWorkorderId(), _inputView.getInputText());

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
        public void onMessageList(long workorderId, List<Message> messages) {
            _messages = messages;
            rebuildList();
        }

        @Override
        public void onAction(long workorderId, String ation) {
            getMessages();
        }
    };
}
