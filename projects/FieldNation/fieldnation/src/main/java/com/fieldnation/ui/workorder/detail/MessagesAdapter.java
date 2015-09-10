package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.utils.ISO8601;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MessagesAdapter extends BaseAdapter {
    private static final String TAG = "MessagesAdapter";

    private List<Message> _messages = new LinkedList<>();

    public MessagesAdapter() {
        super();
    }

    private MessageSentView getMessageSentView(Context context) {
        MessageSentView v = null;
        v = new MessageSentView(context);
        return v;
    }

    private MessageRcvdView getMessageRcvdView(Context context) {
        MessageRcvdView v = null;
        v = new MessageRcvdView(context);
        return v;
    }

    private Comparator<Message> _messageComparator = new Comparator<Message>() {

        @Override
        public int compare(Message lhs, Message rhs) {
            try {
                long lhsUtc = ISO8601.toUtc(lhs.getMsgCreateDate());
                long rhsUtc = ISO8601.toUtc(rhs.getMsgCreateDate());

                if (lhsUtc < rhsUtc)
                    return -1;
                else if (lhsUtc == rhsUtc)
                    return 0;
                else
                    return 1;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return 0;
        }
    };

    public void setMessages(List<Message> messages) {
        if (messages != null)
            Collections.sort(messages, _messageComparator);
        _messages = messages;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _messages.size();
    }

    @Override
    public Object getItem(int position) {
        return _messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position >= _messages.size()) {
            notifyDataSetChanged();
            return convertView;
        }

        Profile profile = App.get().getProfile();

        Log.v(TAG, "getView(" + position + ") / " + _messages.size());
        Message message = _messages.get(position);
        if (message.getFromUser().getUserId() != null
                && message.getFromUser().getUserId().equals(profile.getUserId())) {
            MessageSentView v = null;

            if (convertView == null) {
                v = getMessageSentView(parent.getContext());
            } else if (convertView instanceof MessageSentView) {
                v = (MessageSentView) convertView;
            } else if (convertView instanceof MessageRcvdView) {
                v = getMessageSentView(parent.getContext());
            } else {
                v = getMessageSentView(parent.getContext());
            }

            v.setMessage(message);
            return v;
        } else {
            MessageRcvdView v = null;

            if (convertView == null) {
                v = getMessageRcvdView(parent.getContext());
            } else if (convertView instanceof MessageRcvdView) {
                v = (MessageRcvdView) convertView;
            } else if (convertView instanceof MessageSentView) {
                v = getMessageRcvdView(parent.getContext());
            } else {
                v = getMessageRcvdView(parent.getContext());
            }

            v.setMessage(message);
            return v;
        }
    }
}
