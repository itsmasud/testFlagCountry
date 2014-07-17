package com.fieldnation.ui.workorder;

import com.fieldnation.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkorderTabView extends RelativeLayout {
	private static final String TAG = "ui.workorder.WorkorderTabView";

	// UI
	private RelativeLayout _detailLayout;
	private TextView _messagesTextView;
	private RelativeLayout _messagesLayout;
	private TextView _alertTextView;
	private RelativeLayout _alertLayout;
	private RelativeLayout _attachmentsLayout;

	// Data
	private Listener _listener;
	private View[] _buttons;

	/*-*************************************-*/
	/*-				Lifecycle				-*/
	/*-*************************************-*/
	public WorkorderTabView(Context context) {
		this(context, null, -1);
	}

	public WorkorderTabView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public WorkorderTabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_tabview, this);

		if (isInEditMode())
			return;

		_detailLayout = (RelativeLayout) findViewById(R.id.detail_layout);
		_detailLayout.setOnClickListener(_detailLayout_onClick);

		_messagesTextView = (TextView) findViewById(R.id.messages_textview);
		_messagesLayout = (RelativeLayout) findViewById(R.id.messages_layout);
		_messagesLayout.setOnClickListener(_messagesLayout_onClick);

		_alertTextView = (TextView) findViewById(R.id.alert_textview);
		_alertLayout = (RelativeLayout) findViewById(R.id.alert_layout);
		_alertLayout.setOnClickListener(_alertLayout_onClick);

		_attachmentsLayout = (RelativeLayout) findViewById(R.id.attachments_layout);
		_attachmentsLayout.setOnClickListener(_attachmentsLayout_onClick);

		_buttons = new View[4];
		_buttons[0] = _detailLayout;
		_buttons[1] = _messagesLayout;
		_buttons[2] = _alertLayout;
		_buttons[3] = _attachmentsLayout;

		setSelected(0);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _detailLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setSelected(0);
			if (_listener != null)
				_listener.onChange(0);
		}
	};

	private View.OnClickListener _messagesLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setSelected(1);
			if (_listener != null)
				_listener.onChange(1);
		}
	};

	private View.OnClickListener _alertLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setSelected(2);
			if (_listener != null)
				_listener.onChange(2);
		}
	};

	private View.OnClickListener _attachmentsLayout_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			setSelected(3);
			if (_listener != null)
				_listener.onChange(3);
		}
	};

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/
	public void setSelected(int index) {
		for (int i = 0; i < _buttons.length; i++) {
			_buttons[i].setSelected(i == index);
		}
	}

	public void setMessagesCount(int count) {
		_messagesTextView.setText(count + "");
	}

	public void setAlertsCount(int count) {
		_alertTextView.setText(count + "");
	}

	public void setListener(Listener listener) {
		_listener = listener;
	}

	/*-*********************************-*/
	/*-				Private				-*/
	/*-*********************************-*/
	public interface Listener {
		public void onChange(int index);
	}
}
