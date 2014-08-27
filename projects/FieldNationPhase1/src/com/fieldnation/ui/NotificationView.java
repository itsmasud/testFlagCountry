package com.fieldnation.ui;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationView extends RelativeLayout {
	private static final String TAG = "ui.NotificationView";

	// UI
	private LinearLayout _titleLayout;
	private ImageView _titleThumbImageView;
	private TextView _titleTextView;
	private LinearLayout _statusLayout;
	private TextView _statusTextView;
	private TextView _messageTextView;
	private TextView _dateTextView;

	// Data
	private Notification _note;
	private String[] _substatus;

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
		LayoutInflater.from(getContext()).inflate(R.layout.view_notification, this);

		if (isInEditMode())
			return;

		_substatus = getResources().getStringArray(R.array.workorder_substatus);

		_titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		_titleThumbImageView = (ImageView) findViewById(R.id.titlethumb_imageview);
		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_statusLayout = (LinearLayout) findViewById(R.id.status_layout);
		_statusTextView = (TextView) findViewById(R.id.status_textview);
		_messageTextView = (TextView) findViewById(R.id.message_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
	}

	public void setNotification(Notification notification) {
		_note = notification;
		populateUi();
	}

	private void populateUi() {
		_titleTextView.setText(_note.getWorkorder().getTitle());
		_statusTextView.setText(_substatus[_note.getWorkorder().getStatus().getWorkorderSubstatus().ordinal()]);
		_messageTextView.setText(_note.getMessage());
		try {
			_dateTextView.setText(misc.formatDateLong(ISO8601.toCalendar(_note.getDate())));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
