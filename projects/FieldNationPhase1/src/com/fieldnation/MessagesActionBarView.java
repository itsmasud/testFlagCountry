package com.fieldnation;

import com.fieldnation.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessagesActionBarView extends RelativeLayout {
	private static final String TAG = "MessagesActionBarView";
	private TextView _countTextView;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public MessagesActionBarView(Context context) {
		this(context, null, -1);
	}

	public MessagesActionBarView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MessagesActionBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_messages_action_bar, this);

		_countTextView = (TextView) findViewById(R.id.count_textview);

		if (isInEditMode())
			return;

		setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Method Stub: onClick()
				Log.v(TAG, "Method Stub: onClick()");

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
