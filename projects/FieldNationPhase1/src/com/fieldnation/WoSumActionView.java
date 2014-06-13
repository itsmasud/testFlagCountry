package com.fieldnation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class WoSumActionView extends RelativeLayout {

	public WoSumActionView(Context context) {
		this(context, null, -1);
	}

	public WoSumActionView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public WoSumActionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_wosum_actions, this);

	}
}
