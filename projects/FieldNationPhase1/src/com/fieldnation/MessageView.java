package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.json.JsonObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageView extends RelativeLayout {
	private View _statusView;
	private TextView _titleTextView;
	private TextView _substatusTextView;
	private TextView _messageBodyTextView;
	private TextView _timeTextView;

	/*-*****************************-*/
	/*-			LifeCycle			-*/
	/*-*****************************-*/
	public MessageView(Context context) {
		this(context, null, -1);
	}

	public MessageView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MessageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.view_message, this);

		if (isInEditMode())
			return;

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_messageBodyTextView = (TextView) findViewById(R.id.messagebody_textview);
		_substatusTextView = (TextView) findViewById(R.id.substatus_textview);
	}

	public void setMessage(JsonObject message) {
		try {
			_titleTextView.setText(message.getString("workorderId"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			_messageBodyTextView.setText(message.getString("message"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
