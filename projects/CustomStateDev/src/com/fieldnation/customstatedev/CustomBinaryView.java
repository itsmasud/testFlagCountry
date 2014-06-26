package com.fieldnation.customstatedev;

import android.R.drawable;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class CustomBinaryView extends RelativeLayout {
	private static final String TAG = "CustomView";

	// states
	private static final int[] FRIED_STATE_SET = { R.attr.state_fried };
	private static final int[] BAKED_STATE_SET = { R.attr.state_baked };
	private static final int[] BOTH_STATE_SET = { R.attr.state_fried, R.attr.state_baked };
	private static final int[] NONE_STATE_SET = {};

	private Button _button;
	private View _stateView;

	private int _state = 0;
	private StateListDrawable _drawable;

	public CustomBinaryView(Context context) {
		this(context, null, -1);
	}

	public CustomBinaryView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public CustomBinaryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(getContext()).inflate(R.layout.view_custom, this);

		_button = (Button) findViewById(R.id.button1);
		_button.setOnClickListener(_button_onClick);

		_stateView = (View) findViewById(R.id.stateview);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.MyCustomState, defStyle, 0);

		int resId = a.getResourceId(
				R.styleable.MyCustomState_status_background, 0);

		_stateView.setBackgroundResource(resId);
		// setBackgroundResource(R.drawable.custom_background);
		a.recycle();

		if (getBackground() == null) {
			// MUST have a StateListDrawable as the background for this thing to
			// work right.
			setBackgroundResource(R.drawable.custom_background);
			Log.v(TAG, "back is null");
		}
	}

	private void updateStateView(int[] stateSet) {
		if (_stateView == null)
			return;
		if (_stateView.getBackground() != null) {
			_stateView.getBackground().setState(stateSet);
		}

	}

	private View.OnClickListener _button_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_state = (_state + 1) % 4;
			CustomBinaryView.this.refreshDrawableState();
		}
	};

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
		Log.v(TAG, "onCreateDrawableState");
		if (isBoth()) {
			Log.v(TAG, "isBoth");
			updateStateView(BOTH_STATE_SET);
		} else if (isNone()) {
			Log.v(TAG, "isNone");
			updateStateView(NONE_STATE_SET);
		} else if (isFried()) {
			Log.v(TAG, "isFried");
			mergeDrawableStates(drawableState, FRIED_STATE_SET);
			updateStateView(FRIED_STATE_SET);
		} else if (isBaked()) {
			Log.v(TAG, "isBaked");
			mergeDrawableStates(drawableState, BAKED_STATE_SET);
			updateStateView(BAKED_STATE_SET);
		}

		return drawableState;
	};

	public boolean isFried() {
		return (_state & 0x01) > 0;
	}

	public boolean isBaked() {
		return (_state & 0x02) > 0;
	}

	public boolean isBoth() {
		return _state == 0x03;
	}

	public boolean isNone() {
		return _state == 0x00;
	}

}
