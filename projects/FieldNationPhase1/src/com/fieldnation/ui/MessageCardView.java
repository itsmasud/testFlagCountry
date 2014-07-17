package com.fieldnation.ui;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.data.messages.Message;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageCardView extends RelativeLayout {
	private static final String TAG = "ui.MessageView";
	private View _statusView;
	private TextView _titleTextView;
	private TextView _substatusTextView;
	private TextView _messageBodyTextView;
	private TextView _timeTextView;

	/*-*****************************-*/
	/*-			LifeCycle			-*/
	/*-*****************************-*/
	public MessageCardView(Context context) {
		this(context, null, -1);
	}

	public MessageCardView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MessageCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.view_message_card, this);

		if (isInEditMode())
			return;

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);
		_substatusTextView = (TextView) findViewById(R.id.substatus_textview);
		_timeTextView = (TextView) findViewById(R.id.time_textview);
	}

	public void setMessage(Message message) {
		try {
			_titleTextView.setText(message.getMessageId() + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			_messageBodyTextView.setText(message.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Calendar cal = ISO8601.toCalendar(message.getDate());

			String date = misc.formatDateLong(cal);

			_timeTextView.setText(date);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
