package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Standard progress bar with the bar comming from the right instead of the left
 * 
 * @author michael.carver
 * 
 */
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
		// flip the drawing horizontally
		canvas.scale(-1f, 1f, getWidth() / 2, getHeight() / 2);
		super.onDraw(canvas);
	}
}
