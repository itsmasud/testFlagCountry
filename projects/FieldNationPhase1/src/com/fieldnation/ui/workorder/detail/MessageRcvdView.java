package com.fieldnation.ui.workorder.detail;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Message;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageRcvdView extends RelativeLayout {

	private TextView _messageTextView;
	private ImageView _profileImageView;
	private TextView _timeTextView;
	private TextView _pendingTextView;
	private ImageView _checkImageView;

	private Message _message = null;

	public MessageRcvdView(Context context) {
		this(context, null, -1);
	}

	public MessageRcvdView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MessageRcvdView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_message_rcvd, this);

		_messageTextView = (TextView) findViewById(R.id.message_textview);
		_profileImageView = (ImageView) findViewById(R.id.profile_imageview);
		_timeTextView = (TextView) findViewById(R.id.time_textview);
		_pendingTextView = (TextView) findViewById(R.id.pending_textview);
		_checkImageView = (ImageView) findViewById(R.id.check_imageview);
	}

	public void setMessage(Message message) {
		_message = message;

		_messageTextView.setText(message.getMessage());
		try {
			_timeTextView.setText(misc.formatDateTime(ISO8601.toCalendar(message.getMsgCreateDate()), false));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
