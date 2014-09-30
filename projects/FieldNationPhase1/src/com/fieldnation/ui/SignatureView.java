package com.fieldnation.ui;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SignatureView extends View {
	private static final String TAG = "ui.SignatureView";

	// Data
	private Paint _myPaint;
	private List<Point> _shape;
	private List<List<Point>> _shapes;

	public SignatureView(Context context) {
		super(context);
		init();
	}

	public SignatureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		_shape = new LinkedList<Point>();
		_shapes = new LinkedList<List<Point>>();
		_shapes.add(_shape);
		_myPaint = new Paint();
		_myPaint.setColor(Color.BLACK);
		_myPaint.setStrokeWidth(2);
	}

	private float _min = 150;
	private float _max = 8;

	@Override
	protected void onDraw(Canvas canvas) {

		// walk through the shapes list... draw those
		for (int i = 0; i < _shapes.size(); i++) {
			List<Point> shape = _shapes.get(i);
			if (shape.size() > 0) {
				Point lp = shape.get(0);
				Point p = null;

				for (int j = 1; j < shape.size(); j++) {
					p = shape.get(j);
					float stroke = (p.t - lp.t);
					if (stroke > _max) {
						_max = stroke;
					}
					if (stroke < _min) {
						_min = stroke;
					}

					stroke = ((stroke - _min) / _max) * 8 + 2;

					if (stroke < 2) {
						stroke = 2;
					} else if (stroke > 10) {
						stroke = 10;
					}

					_myPaint.setStrokeWidth(stroke);
					canvas.drawLine(lp.x, lp.y, p.x, p.y, _myPaint);
					lp = p;
				}
			}
		}
		Log.v(TAG, "min: " + _min + "  max:" + _max);

		super.onDraw(canvas);
	}

	public void clear() {
		_shapes = new LinkedList<List<Point>>();
		_shape = new LinkedList<Point>();
		invalidate();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// _shape.add(new Point(event));
			// invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			int count = event.getHistorySize();
			for (int i = 0; i < count; i++) {
				_shape.add(new Point(event.getHistoricalX(i), event.getHistoricalY(i), event.getHistoricalEventTime(i)));
			}
			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			_shape.add(new Point(event));
			_shape = new LinkedList<Point>();
			_shapes.add(_shape);
			invalidate();
			return true;
		}

		return super.dispatchTouchEvent(event);
	}

	private class Point {
		public float x;
		public float y;
		public long t;

		public Point(MotionEvent event) {
			x = event.getX();
			y = event.getY();

			t = event.getEventTime();
		}

		public Point(float x, float y, long t) {
			this.x = x;
			this.y = y;
			this.t = t;
		}
	}
}
