package com.fieldnation.ui;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationView extends RelativeLayout {
	private static final String TAG = "ui.NotificationView";

	// UI
	private View _statusView;
	private TextView _messageTextView;
	private TextView _dateTextView;

	// Data
	private Notification _note;

	// UI state

	public NotificationView(Context context) {
		this(context, null, -1);
	}

	public NotificationView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public NotificationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_notification, this);

		if (isInEditMode())
			return;

		_statusView = (View) findViewById(R.id.status_view);
		_messageTextView = (TextView) findViewById(R.id.message_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
	}

	public void setNotification(Notification notification) {
		_note = notification;
		if (notification.getViewed() == 0) {
			_statusView.setBackgroundResource(R.drawable.card_status_white);
		} else {
			_statusView.setBackgroundResource(R.drawable.card_status_orange);
		}

		_messageTextView.setText(notification.getMessage());

		try {
			_dateTextView.setVisibility(VISIBLE);
			_dateTextView.setText(misc.formatDateTime(ISO8601.toCalendar(_note.getDate()), false));
		} catch (ParseException e) {
			_dateTextView.setVisibility(GONE);
			e.printStackTrace();
		}
	}
}
