package com.fieldnation.ui;

import java.text.ParseException;

import com.fieldnation.R;
import com.fieldnation.data.profile.Notification;
import com.fieldnation.data.profile.Workorder;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
	private int[] _colors = new int[5];

	/*-*****************************-*/
	/*-			Lifecycle			-*/
	/*-*****************************-*/
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
		_colors[0] = getResources().getColor(R.color.text_normal);
		_colors[1] = getResources().getColor(R.color.text_success);
		_colors[2] = getResources().getColor(R.color.text_waiting);
		_colors[3] = getResources().getColor(R.color.text_warning);
		_colors[4] = getResources().getColor(R.color.text_normal);

		_titleLayout = (LinearLayout) findViewById(R.id.title_layout);
		_titleThumbImageView = (ImageView) findViewById(R.id.titlethumb_imageview);
		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_statusLayout = (LinearLayout) findViewById(R.id.status_layout);
		_statusTextView = (TextView) findViewById(R.id.status_textview);
		_messageTextView = (TextView) findViewById(R.id.message_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
		setOnClickListener(_this_onClick);
	}

	/*-*****************************-*/
	/*-			Modifiers			-*/
	/*-*****************************-*/
	public void setNotification(Notification notification) {
		_note = notification;
		populateUi();
	}

	private void populateUi() {
		if (_note.getWorkorder() != null) {
			Workorder work = _note.getWorkorder();
			_titleTextView.setText(work.getTitle());
			_statusTextView.setText(_substatus[work.getStatus().getWorkorderSubstatus().ordinal()]);
			_statusTextView.setTextColor(_colors[work.getStatus().getStatusIntent().ordinal()]);
			_titleLayout.setVisibility(View.VISIBLE);
		} else {
			_titleLayout.setVisibility(View.GONE);
		}
		_messageTextView.setText(_note.getMessage());
		try {
			_dateTextView.setText(misc.formatDateLong(ISO8601.toCalendar(_note.getDate())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
	private View.OnClickListener _this_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), WorkorderActivity.class);
			intent.putExtra(WorkorderActivity.INTENT_FIELD_CURRENT_TAB, WorkorderActivity.TAB_NOTIFICATIONS);
			intent.putExtra(WorkorderActivity.INTENT_FIELD_WORKORDER_ID, _note.getWorkorder().getWorkorderId());
			getContext().startActivity(intent);
		}
	};
}
