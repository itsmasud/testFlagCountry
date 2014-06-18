package com.fieldnation;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ProgressBarRev extends ProgressBar {

	public ProgressBarRev(Context context) {
		this(context, null, -1);
	}

	public ProgressBarRev(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ProgressBarRev(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		canvas.save();
		canvas.rotate(180, getWidth() / 2, getHeight() / 2);
		super.onDraw(canvas);
		canvas.restore();
	}
}
