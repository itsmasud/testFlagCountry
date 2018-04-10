package com.fieldnation.v2.ui.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionUtils;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 8/30/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private static final String TAG = "ChatAdapter";
    private static final int MAX_MESSAGE_SIZE = 2048;
    private static final SimpleDateFormat HEADER_FORMAT = new SimpleDateFormat("EEEE, MMMM dd, yyyy");

    private List<Tuple> _objects = new LinkedList<>();

    private List<Message> _messages = null;
    private List<MessageHolder> _addedMessages = new LinkedList<>();

    private int _workOrderId;
    private boolean _running = false;
    private boolean _runAgain = false;

    public void setMessages(int workOrderId, List<Message> messages) {
        this._messages = messages;
        _workOrderId = workOrderId;

        if (!_running) {
            _running = true;
            _objects.clear();
            _addedMessages.clear();
            WebTransactionUtils.setData(_addMessage, WebTransactionUtils.KeyType.ADD_MESSAGE, workOrderId);
        } else {
            _runAgain = true;
        }
    }

    public void refresh() {
        setMessages(_workOrderId, _messages);
    }

    private void rebuild() {
        if (_runAgain) {
            _runAgain = false;
            setMessages(_workOrderId, _messages);
            return;
        }
        _objects.clear();

        if ((_messages == null || _messages.size() == 0) && _addedMessages.size() == 0) {
            return;
        }

        // Split _messages
        List<MessageHolder> newList = new LinkedList<>();
        for (Message message : _messages) {
            if (message.getMessage().length() > MAX_MESSAGE_SIZE) {
                String msg = message.getMessage();
                while (msg.length() > MAX_MESSAGE_SIZE) {
                    try {
                        newList.add(new MessageHolder(copyMessage(message).message(msg.substring(0, MAX_MESSAGE_SIZE)), null));
                        msg = msg.substring(MAX_MESSAGE_SIZE);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }

                if (msg.length() > 0) {
                    try {
                        newList.add(new MessageHolder(copyMessage(message).message(msg), null));
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
            } else {
                newList.add(new MessageHolder(message, null));
            }
        }

        for (MessageHolder messageHolder : _addedMessages) {
            if (messageHolder.message.getMessage().length() > MAX_MESSAGE_SIZE) {
                String msg = messageHolder.message.getMessage();
                while (msg.length() > MAX_MESSAGE_SIZE) {
                    try {
                        newList.add(new MessageHolder(copyMessage(messageHolder.message).message(msg.substring(0, MAX_MESSAGE_SIZE)), messageHolder.webTransaction));
                        msg = msg.substring(MAX_MESSAGE_SIZE);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }

                if (msg.length() > 0) {
                    try {
                        newList.add(new MessageHolder(copyMessage(messageHolder.message).message(msg), messageHolder.webTransaction));
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                    }
                }
            } else {
                newList.add(new MessageHolder(messageHolder.message, messageHolder.webTransaction));
            }
        }

        // group by days
        List<List<MessageHolder>> dayGroup = new LinkedList<>();
        {
            Calendar lastCal = null;
            List<MessageHolder> day = null;
            for (MessageHolder messageHolder : newList) {
                try {
                    Calendar myCal = messageHolder.message.getCreated().getCalendar();
                    if (lastCal != null && DateUtils.isSameDay(myCal, lastCal)) {
                        day.add(messageHolder);
                    } else {
                        day = new LinkedList<>();
                        dayGroup.add(day);
                        day.add(messageHolder);
                        lastCal = myCal;
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        // group days by people
        int thisUserId = (int) App.getProfileId();
        {
            for (List<MessageHolder> day : dayGroup) {
                int groupId = 0;
                List<List<MessageHolder>> groups = new LinkedList<>();
                {
                    List<MessageHolder> group = null;
                    for (MessageHolder messageHolder : day) {
                        int userId = messageHolder.message.getFrom().getId();
                        if (groupId == userId && group != null) {
                            group.add(messageHolder);
                        } else {
                            group = new LinkedList<>();
                            groups.add(group);
                            group.add(messageHolder);
                            groupId = userId;
                        }
                    }
                }

                // Add the header
                try {
                    if (day.size() > 0) {
                        Tuple tuple = new Tuple();
                        tuple.type = ChatViewHolder.TYPE_HEADER_TIME;
                        tuple.object = day.get(0).message.getCreated().getCalendar().getTime();
                        _objects.add(tuple);
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

                // process the groups
                for (List<MessageHolder> group : groups) {
                    if (group.size() == 1) {
                        MessageHolder messageHolder = group.get(0);
                        boolean isMine = thisUserId == messageHolder.message.getFrom().getId();
                        Tuple t = new Tuple();
                        t.type = isMine ? ChatViewHolder.TYPE_RIGHT_FULL : ChatViewHolder.TYPE_LEFT_FULL;
                        t.object = messageHolder;
                        _objects.add(t);
                    } else {
                        for (int i = 0; i < group.size(); i++) {
                            Tuple t = new Tuple();
                            MessageHolder messageHolder = group.get(i);
                            boolean isMine = thisUserId == messageHolder.message.getFrom().getId();

                            t.object = messageHolder;
                            // first
                            if (i == 0) {
                                t.type = isMine ? ChatViewHolder.TYPE_RIGHT_TOP : ChatViewHolder.TYPE_LEFT_TOP;

                                // end
                            } else if (i == group.size() - 1) {
                                t.type = isMine ? ChatViewHolder.TYPE_RIGHT_BOTTOM : ChatViewHolder.TYPE_LEFT_BOTTOM;

                                // middle
                            } else {
                                t.type = isMine ? ChatViewHolder.TYPE_RIGHT_CENTER : ChatViewHolder.TYPE_LEFT_CENTER;
                            }
                            _objects.add(t);
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    private Message copyMessage(Message message) {
        try {
            return Message.fromJson(new JsonObject(message.getJson().toString()));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ChatViewHolder.TYPE_EMPTY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_messages, parent, false);
                break;
            case ChatViewHolder.TYPE_RIGHT_TOP:
            case ChatViewHolder.TYPE_RIGHT_CENTER:
            case ChatViewHolder.TYPE_RIGHT_BOTTOM:
            case ChatViewHolder.TYPE_RIGHT_FULL:
                view = new ChatRightView(parent.getContext());
                break;
            case ChatViewHolder.TYPE_LEFT_TOP:
            case ChatViewHolder.TYPE_LEFT_CENTER:
            case ChatViewHolder.TYPE_LEFT_BOTTOM:
            case ChatViewHolder.TYPE_LEFT_FULL:
                view = new ChatLeftView(parent.getContext());
                break;
            case ChatViewHolder.TYPE_HEADER_TIME:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_time_header, parent, false);
                break;
        }

        return new ChatViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        if (holder.type == ChatViewHolder.TYPE_HEADER_TIME) {
            TextView tv = (TextView) holder.itemView;
            try {
                tv.setText(HEADER_FORMAT.format((Date) _objects.get(position).object));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        } else if (holder.type != ChatViewHolder.TYPE_EMPTY) {
            ChatRenderer cr = (ChatRenderer) holder.itemView;
            cr.setMessage(((MessageHolder) _objects.get(position).object).message);
            switch (holder.type) {
                case ChatViewHolder.TYPE_RIGHT_TOP:
                case ChatViewHolder.TYPE_LEFT_TOP:
                    cr.setPosition(Position.TOP);
                    break;
                case ChatViewHolder.TYPE_LEFT_CENTER:
                case ChatViewHolder.TYPE_RIGHT_CENTER:
                    cr.setPosition(Position.CENTER);
                    break;
                case ChatViewHolder.TYPE_RIGHT_BOTTOM:
                case ChatViewHolder.TYPE_LEFT_BOTTOM:
                    cr.setPosition(Position.BOTTOM);
                    break;
                case ChatViewHolder.TYPE_RIGHT_FULL:
                case ChatViewHolder.TYPE_LEFT_FULL:
                    cr.setPosition(Position.FULL);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return _objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        return _objects.get(position).type;
    }

    private static class MessageHolder {
        public Message message;
        public WebTransaction webTransaction;

        public MessageHolder(Message message, WebTransaction webTransaction) {
            this.message = message;
            this.webTransaction = webTransaction;
        }
    }

    private static class Tuple {
        public int type;
        public Object object;

        public Tuple() {
        }
    }

    private final WebTransactionUtils.Listener _addMessage = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                Message message = Message.fromJson(new JsonObject(tp.methodParams).getJsonObject("json"));
                _addedMessages.add(new MessageHolder(message, webTransaction));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running = false;
            rebuild();
        }
    };
}
