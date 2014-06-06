package com.fieldnation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActionBarView extends RelativeLayout {
	private TextView _countTextView;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public NotificationActionBarView(Context context) {
		this(context, null, -1);
	}

	public NotificationActionBarView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public NotificationActionBarView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_notification_action_bar, this);

		_countTextView = (TextView) findViewById(R.id.count_textview);

		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Method Stub: onClick()");

			}
		});
	}

	public void setCount(int count) {
		if (count == 0) {
			_countTextView.setVisibility(GONE);
		} else {
			_countTextView.setVisibility(VISIBLE);
			_countTextView.setText(count + "");
		}
	}
}
