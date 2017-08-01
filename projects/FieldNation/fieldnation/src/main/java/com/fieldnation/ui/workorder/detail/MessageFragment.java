package com.fieldnation.ui.workorder.detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderFragment;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.Message;
import com.fieldnation.v2.data.model.Messages;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.workorder.MessagePagingAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MessageFragment extends WorkorderFragment {
    private static final String TAG = "MessageFragment";

    // UI
    private OverScrollRecyclerView _messagesList;
    private MessageInputView _inputView;
    private RefreshView _refreshView;

    // Data
    private WorkOrder _workorder;
    private WorkordersWebApi _workOrderApi;
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

        _messagesList = (OverScrollRecyclerView) view.findViewById(R.id.messages_listview);
        _messagesList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        _messagesList.setAdapter(_adapter);

        _inputView = (MessageInputView) view.findViewById(R.id.input_view);
        _inputView.setOnSendButtonClick(_send_onClick);
        _inputView.setButtonEnabled(false);

        _refreshView = (RefreshView) view.findViewById(R.id.refresh_view);
    }

    @Override
    public void onResume() {
        super.onResume();

        _workOrderApi = new WorkordersWebApi(_workOrderApi_listener);
        _workOrderApi.connect(App.get());

        populateUi();
    }

    @Override
    public void onPause() {
        if (_workOrderApi != null) {
            _workOrderApi.disconnect(App.get());
            _workOrderApi = null;
        }

        misc.hideKeyboard(_inputView);
        super.onPause();
    }

    @Override
    public void update() {
        Log.v(TAG, "update");
        App.get().getSpUiContext().page(WorkOrderTracker.Tab.MESSAGES.name());
        if (_workorder != null) {
            _refreshView.startRefreshing();
            WorkordersWebApi.getMessages(App.get(), _workorder.getId(), true, false);
        }
    }

    @Override
    public void setWorkOrder(WorkOrder workorder) {
        _workorder = workorder;
        populateUi();
        getMessages();
    }

    private void getMessages() {
        if (_workorder == null)
            return;

        Log.v(TAG, "getMessages");
        WorkordersWebApi.getMessages(App.get(), _workorder.getId(), false, false);
    }

    private void populateUi() {
        if (_inputView == null)
            return;

        if (_workorder != null) {
            _inputView.setButtonEnabled(true);

            if (!_isMarkedRead) {
                _isMarkedRead = true;
// TODO                WorkorderClient.actionMarkMessagesRead(App.get(), _workorder.getWorkorderId());
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
        _messagesList.scrollToPosition(_adapter.getItemCount() - 1);
        _refreshView.refreshComplete();
    }

    private final MessagePagingAdapter _adapter = new MessagePagingAdapter() {
        @Override
        public void requestPage(int page, boolean allowCache) {
        }
    };

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

                //_messages.add(new Message(_workorder.getWorkorderId(), User.fromJson(App.get().getProfile().toJson()), _inputView.getInputText()));``

                try {
                    Message msg = new Message();
                    msg.setMessage(_inputView.getInputText());
                    WorkordersWebApi.addMessage(App.get(), _workorder.getId(), msg, App.get().getSpUiContext());
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

                _inputView.clearText();
                misc.hideKeyboard(_inputView);
            }
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/

    private final WorkordersWebApi.Listener _workOrderApi_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderApi.subWorkordersWebApi();
        }

        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("addMessage")
                    || methodName.equals("getMessages")
                    || methodName.equals("replyMessage");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            try {
                if (successObject instanceof Messages) {
                    Messages messages = (Messages) successObject;
                    Error error = (Error) failObject;
                    if (!success || error != null)
                        return;

                    JsonObject methodParams = new JsonObject(transactionParams.methodParams);

                    if (methodParams.has("workOrderId") && _workorder != null
                            && methodParams.getInt("workOrderId") != _workorder.getId()) {
                        Log.v(TAG, "not my work order!");
                        return;
                    }

                    if (messages == null || messages.getResults() == null || _workorder == null) {
                        _refreshView.refreshComplete();
                        return;
                    }

                    // flatten the tree with a depth first search
                    // first create a stack to store nodes that need to be searched
                    // push the messages into the stack
                    List<Message> stack = new LinkedList<>();
                    for (Message message : messages.getResults()) {
                        stack.add(message);
                    }

                    List<Message> flatList = new LinkedList<>();
                    while (stack.size() > 0) {
                        // pop the first item
                        Message message = stack.remove(0);
                        // add it to the list
                        flatList.add(message);

                        // get the replies and add them to the stack
                        if (message.getReplies() != null
                                && message.getReplies().length > 0) {
                            Message[] replies = message.getReplies();
                            for (int i = replies.length - 1; i >= 0; i--) {
                                stack.add(replies[i]);
                            }
                        }
                    }

                    Collections.sort(flatList, new Comparator<Message>() {
                        @Override
                        public int compare(Message lhs, Message rhs) {
                            try {
                                long lhsT = lhs.getCreated().getUtcLong();
                                long rhsT = rhs.getCreated().getUtcLong();
                                if (lhsT < rhsT) {
                                    return -1;
                                } else if (lhsT > rhsT) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            } catch (Exception ex) {
                                Log.v(TAG, ex);
                                return 0;
                            }
                        }
                    });

                    _adapter.addObjects(messages.getMetadata().getPage(), flatList);

                    rebuildList();
                } else if (successObject instanceof Message) {
                    rebuildList();
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };
}
