package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * A textview that displays its text vertically.
 * 
 * taken from: http://blog.stylingandroid.com/archives/796
 * 
 * @author michael.carver
 * 
 */
public class VerticalTextView extends TextView {
	private static final String TAG = "VerticalTextView";
	private final boolean _topDown;

	public VerticalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final int gravity = getGravity();
		if (Gravity.isVertical(gravity) && (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
			setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
			_topDown = false;
		} else {
			_topDown = true;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		TextPaint textPaint = getPaint();
		textPaint.setColor(getCurrentTextColor());
		textPaint.drawableState = getDrawableState();

		canvas.save();

		if (!_topDown) {
			canvas.translate(getWidth(), 0);
			canvas.rotate(90);
		} else {
			canvas.translate(getWidth() / 5, getHeight());
			canvas.rotate(-90);
		}

		canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

		getLayout().draw(canvas);
		canvas.restore();
	}
}