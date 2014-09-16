package com.fieldnation.ui.workorder.detail;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.utils.ISO8601;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;

public class MessagesAdapter extends BaseAdapter {
	private static final String TAG = "ui.workorder.detail.MessagesAdapter";

	private List<Message> _messages = new LinkedList<Message>();
	private Profile _profile;
	private MessageInputView _messageInput;
	private Listener _onClickListener;

	public MessagesAdapter(Profile profile, Listener listener) {
		super();
		_onClickListener = listener;
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

	private MessageInputView getMessageInputView(Context context) {
		if (_messageInput == null || _messageInput.getContext() != context) {
			_messageInput = new MessageInputView(context);
			_messageInput.setOnSendButtonClick(_send_onClick);
		}

		return _messageInput;
	}

	private View.OnClickListener _send_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String msg = getMessage();
			if (_onClickListener != null) {
				_onClickListener.onClick(msg);
			}
		}
	};

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
		Collections.sort(messages, _messageComparator);
		_messages = messages;

		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return _messages.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position < _messages.size())
			return _messages.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clearMessage() {
		if (_messageInput != null) {
			_messageInput.clearText();
		}
	}

	public String getMessage() {
		if (_messageInput != null) {
			return _messageInput.getInputText();
		}
		return null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position < _messages.size()) {
			Message message = _messages.get(position);

			if (message.getFromUser().getUserId() == _profile.getUserId()) {
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
		} else {
			MessageInputView v = getMessageInputView(parent.getContext());
			if (_messages.size() == 0) {
				v.setHint(R.string.start_the_conversation);
			} else {
				v.setHint(R.string.continue_the_conversation);
			}

			return getMessageInputView(parent.getContext());
		}
	}

	public interface Listener {
		public void onClick(String message);
	}
}
