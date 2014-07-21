package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

public class GridBagLayout extends ViewGroup {
	private static String TAG = "ui.GridBagLayout";

	public GridBagLayout(Context context) {
		this(context, null, -1);
	}

	public GridBagLayout(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public GridBagLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// Log.v(TAG, "Method Stub: onLayout()");
		Log.v(TAG, "l:" + l + " t:" + t + " r:" + r + " b:" + b);
	}

}
