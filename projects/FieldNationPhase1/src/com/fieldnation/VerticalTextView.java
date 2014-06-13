package com.fieldnation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class VerticalTextView extends TextView {
	private static final String TAG = "VerticalTextView";
	private Paint _paint = new Paint();

	public VerticalTextView(Context context) {
		super(context);
	}

	public VerticalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VerticalTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		_paint.setTypeface(getTypeface());
		_paint.setStyle(Paint.Style.FILL);
		_paint.setColor(getCurrentTextColor());
		// paint.setShadowLayer(1, 0, 1, Color.parseColor("#000000"));
		_paint.setTextSize(getTextSize());
		canvas.rotate(-90, 0, 0);
		canvas.drawText(getText() + "", 0, 0, _paint);
		super.onDraw(canvas);
	}

}
