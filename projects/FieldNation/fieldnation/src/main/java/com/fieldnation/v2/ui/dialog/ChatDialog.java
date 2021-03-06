package com.fieldnation.v2.ui.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.Message;
import com.fieldnation.v2.data.model.MessageFrom;
import com.fieldnation.v2.data.model.MessageTo;
import com.fieldnation.v2.data.model.Messages;
import com.fieldnation.v2.ui.chat.ChatAdapter;
import com.fieldnation.v2.ui.chat.ChatInputView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/30/17.
 */

public class ChatDialog extends FullScreenDialog {
    private static final String TAG = "ChatDialog";

    // UI
    private Toolbar _toolbar;
    private OverScrollRecyclerView _chatList;
    private ChatInputView _inputView;

    // Data
    private int _workOrderId;
    private String _companyName;
    private boolean _isMarkedRead = false;
    private final ChatAdapter _adapter = new ChatAdapter();

    public ChatDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_chat, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.setTitle("LOADING...");

        _chatList = v.findViewById(R.id.chat_listview);
        _inputView = v.findViewById(R.id.input_view);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _chatList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _chatList.setAdapter(_adapter);
        _adapter.registerAdapterDataObserver(_adapterObserver);
        _inputView.setOnSendButtonClick(_send_onClick);
        _inputView.setButtonEnabled(false);

        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCAST_ON_CHANGE));
    }

    @Override
    public void onResume() {
        super.onResume();
        _workOrderApi.sub();
        populateUi();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        App.get().getSpUiContext().page(WorkOrderTracker.Tab.MESSAGES.name());
        _workOrderId = params.getInt("workOrderId");
        _companyName = params.getString("companyName");

        WorkordersWebApi.getMessages(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
        ProfileClient.get(App.get(), false);
    }

    @Override
    public void onPause() {
        _workOrderApi.unsub();
        misc.hideKeyboard(_inputView);
        super.onPause();
    }

    @Override
    public void onStop() {
        _adapter.unregisterAdapterDataObserver(_adapterObserver);
        LocalBroadcastManager.getInstance(App.get()).unregisterReceiver(_webTransactionChanged);
        super.onStop();
    }

    private void populateUi() {
        if (_inputView == null)
            return;

        _inputView.setButtonEnabled(true);

        if (!_isMarkedRead) {
            _isMarkedRead = true;

            ProfileClient.get(App.get());
        }

        _adapter.refresh();
    }

    private void rebuildList() {
        _chatList.scrollToPosition(_adapter.getItemCount() - 1);
    }

    /*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/
    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    private final View.OnClickListener _send_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (misc.isEmptyOrNull(_inputView.getInputText())) {
                ToastClient.toast(App.get(), "Please enter a message", Toast.LENGTH_SHORT);
                return;
            }

            if (App.getProfile() == null) {
                ToastClient.toast(App.get(), "Can't send message right now, please try again later", Toast.LENGTH_LONG);
                return;
            }

            Log.v(TAG, "_send_onClick");

            try {
                Message msg = new Message();
                msg.setMessage(_inputView.getInputText());
                msg.setCreated(new com.fieldnation.v2.data.model.Date(Calendar.getInstance()));

                MessageTo msgTo = new MessageTo();
                msg.to(msgTo.name(_companyName));

                MessageFrom msgFrom = new MessageFrom();
                msg.from(msgFrom.id((int) App.getProfileId()));

                WorkordersWebApi.addMessage(App.get(), _workOrderId, msg, App.get().getSpUiContext());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            _inputView.clearText();
            misc.hideKeyboard(_inputView);
            _adapter.refresh();
        }
    };

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String op = intent.getStringExtra("op");
            if (intent.hasExtra("key")) {
                String key = intent.getStringExtra("key");
                if (key != null && (key.contains("addMessageByWorkOrder")))
                    populateUi();
            }
        }
    };

    private final RecyclerView.AdapterDataObserver _adapterObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            rebuildList();
        }
    };

    /*-*****************************-*/
    /*-				Web				-*/
    /*-*****************************-*/
    private final WorkordersWebApi _workOrderApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return methodName.equals("addMessage")
                    || methodName.equals("getMessages")
                    || methodName.equals("replyMessage");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            try {
                if (successObject != null && successObject instanceof Messages) {
                    Messages messages = (Messages) successObject;
                    Error error = (Error) failObject;
                    if (!success || error != null)
                        return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);

                    JsonObject methodParams = new JsonObject(transactionParams.methodParams);

                    if (methodParams.has("workOrderId")
                            && methodParams.getInt("workOrderId") != _workOrderId) {
                        Log.v(TAG, "not my work order!");
                        return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
                    }

                    if (messages == null || messages.getResults() == null) {
                        return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
                    }

                    // flatten the tree with a depth first search
                    // first create a stack to store nodes that need to be searched
                    // push the messages into the stack
                    List<Message> stack = new LinkedList<>();
                    Collections.addAll(stack, messages.getResults());

                    List<Message> flatList = new LinkedList<>();
                    while (stack.size() > 0) {
                        // pop the first item
                        Message message = stack.remove(0);
                        // add it to the list
                        flatList.add(message);

                        // get the replies and add them to the stack
                        if (message.getReplies() != null && message.getReplies().length > 0) {
                            Message[] replies = message.getReplies();
                            for (int i = replies.length - 1; i >= 0; i--) {
                                stack.add(replies[i]);
                            }
                        }
                    }

                    // sort by time
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

                    _adapter.setMessages(_workOrderId, flatList);
                    if (flatList.size() == 0) {
                        _toolbar.setTitle("New Message");
                    } else {
                        _toolbar.setTitle("Messages");
                    }
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, int workOrderId, String companyName) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);
        params.putString("companyName", companyName);
        Controller.show(context, null, ChatDialog.class, params);
    }
}
