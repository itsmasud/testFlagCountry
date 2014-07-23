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

	private List<MessageSentView> _sentPool;
	private List<MessageRcvdView> _rcvdPool;
	private List<Message> _messages;
	private Profile _profile;

	public MessagesAdapter(Profile profile, List<Message> messages) {
		super();

		_sentPool = new LinkedList<MessageSentView>();
		_rcvdPool = new LinkedList<MessageRcvdView>();
		_messages = messages;
		_profile = profile;
	}

	private MessageSentView getMessageSentView(Context context) {
		MessageSentView v = null;
		if (_sentPool.size() == 0) {
			v = new MessageSentView(context);
		} else {
			v = _sentPool.remove(0);
		}
		return v;
	}

	private MessageRcvdView getMessageRcvdView(Context context) {
		MessageRcvdView v = null;
		if (_rcvdPool.size() == 0) {
			v = new MessageRcvdView(context);
		} else {
			v = _rcvdPool.remove(0);
		}
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

		if (message.getMsgFrom() == _profile.getUserId()) {
			MessageSentView v = null;

			if (convertView == null) {
				v = getMessageSentView(parent.getContext());
			} else if (convertView instanceof MessageSentView) {
				v = (MessageSentView) convertView;
			} else if (convertView instanceof MessageRcvdView) {
				_rcvdPool.add((MessageRcvdView) convertView);
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
				_sentPool.add((MessageSentView) convertView);
				v = getMessageRcvdView(parent.getContext());
			} else {
				v = getMessageRcvdView(parent.getContext());
			}

			v.setMessage(message);
			return v;
		}
	}
}
