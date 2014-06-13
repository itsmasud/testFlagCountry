package com.fieldnation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class SaneLayout extends ViewGroup {

	public SaneLayout(Context context) {
		this(context, null, -1);
	}

	public SaneLayout(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public SaneLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

}
