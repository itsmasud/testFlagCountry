package com.fieldnation.ui.workorder.detail;

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
	private static final String TAG = "ui.workorder.detail.NotificationView";

	// UI
	private ImageView _alertThumbImageView;
	private TextView _messageTextView;
	private TextView _usernameTextView;
	private TextView _dateTextView;

	// Data
	private Notification _notification;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public NotificationView(Context context) {
		super(context);
		init();
	}

	public NotificationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NotificationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_note, this);

		if (isInEditMode())
			return;

		_alertThumbImageView = (ImageView) findViewById(R.id.alertthumb_imageview);
		_messageTextView = (TextView) findViewById(R.id.message_textview);
		_messageTextView.setOnClickListener(_message_onClick);
		_usernameTextView = (TextView) findViewById(R.id.username_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
	}

	/*-*************************************-*/
	/*-				Modifiers				-*/
	/*-*************************************-*/
	public void setNotification(Notification notification) {
		_notification = notification;
		rePopulate();
	}

	private void rePopulate() {

		String msg = _notification.getMessage();

		if (msg.length() > 170) {
			msg = msg.substring(0, 170) + "...";
		}
		_messageTextView.setText(msg);

		_usernameTextView.setText(_notification.getFromUser().getFirstname() + _notification.getFromUser().getLastname());
		try {
			_dateTextView.setText(misc.formatDateTime(ISO8601.toCalendar(_notification.getDate()), false));
		} catch (Exception e) {
			_dateTextView.setText(_notification.getDate());
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _message_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_messageTextView.setText(_notification.getMessage());
		}
	};
}
