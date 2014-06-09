package com.fieldnation;

import com.fieldnation.json.JsonObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkorderSummaryView extends RelativeLayout {
	private static final int[] STATE_ONE = { R.attr.state_one };
	private static final int[] STATE_TWO = { R.attr.state_two };
	private static final int[] STATE_THREE = { R.attr.state_three };

	// UI
	private View _statusView;
	private TextView _titleTextView;
	private TextView _clientNameTextView;
	private Button _detailButton;
	private TextView _cashTextView;
	private LinearLayout _cashLinearLayout;

	// Data
	private boolean _state_one = true;
	private boolean _state_two = false;
	private boolean _state_three = false;

	private JsonObject _workorder;

	public WorkorderSummaryView(Context context) {
		this(context, null, -1);
	}

	public WorkorderSummaryView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public WorkorderSummaryView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_workorder_summary, this);

		if (isInEditMode())
			return;

		_statusView = (View) findViewById(R.id.status_view);
		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
		_detailButton = (Button) findViewById(R.id.detail_button);
		_cashTextView = (TextView) findViewById(R.id.cash_textview);
		_cashLinearLayout = (LinearLayout) findViewById(R.id.cash_linearlayout);

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (_state_one) {
					_state_one = false;
					_state_two = true;
				} else if (_state_two) {
					_state_two = false;
					_state_three = true;
				} else if (_state_three) {
					_state_three = false;
					_state_one = true;
				}

				WorkorderSummaryView.this.refreshDrawableState();
			}
		});

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _detailButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onClick()");
		}
	};

	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);

		if (_state_one) {
			mergeDrawableStates(drawableState, STATE_ONE);
		} else if (_state_two) {
			mergeDrawableStates(drawableState, STATE_TWO);
		} else if (_state_three) {
			mergeDrawableStates(drawableState, STATE_THREE);
		}
		return drawableState;
	};

	/*-*********************************-*/
	/*-				Data				-*/
	/*-*********************************-*/
	public void setWorkorder(JsonObject workorder) {
		_workorder = workorder;
		refresh();
	}

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void refresh() {
		// TODO, stub
	}

	public void setStateOne(Boolean state) {
		_state_one = state;
	}

	public void setStateTwo(Boolean state) {
		_state_two = state;
	}

	public void setStateThree(Boolean state) {
		_state_three = state;
	}
}
