package com.fieldnation.ui.workorder.detail;

import java.util.LinkedList;
import java.util.List;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Message;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MessagesAdapter extends BaseAdapter {
	private static final String TAG = "ui.workorder.detail.MessagesAdapter";

	private List<Message> _messages;
	private Profile _profile;

	public MessagesAdapter(Profile profile, List<Message> messages) {
		super();

		_messages = messages;
		_profile = profile;
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
		Message message = _messages.get(position);

		if ((int) message.getMsgFrom() == (int) _profile.getUserId()) {
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
