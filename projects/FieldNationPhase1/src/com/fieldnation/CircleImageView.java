package com.fieldnation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageView extends ImageView {
	private static final String TAG = "CircleImageView";

	public CircleImageView(Context context) {
		this(context, null, -1);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Drawable drawable = getDrawable();

		if (drawable == null)
			return;

	}

}
