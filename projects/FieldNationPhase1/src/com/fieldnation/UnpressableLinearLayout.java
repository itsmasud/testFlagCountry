package com.fieldnation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class UnpressableLinearLayout extends LinearLayout {

	public UnpressableLinearLayout(Context context) {
		super(context);
	}

	public UnpressableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setPressed(boolean pressed) {
		// Do nothing, this prevents the pressed event from propagating
	}

}
