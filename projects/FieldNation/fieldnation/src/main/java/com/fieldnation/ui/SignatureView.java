package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/*
 * This is a very stupid signature collection class. 
 * TODO Improve speed by drawing the lines on a back buffer as we capture them. This way we only need to do all the calculations on the shapes once.
 */
public class SignatureView extends View {
    private static final String TAG = "ui.SignatureView";

    // Data
    private Paint _myPaint;
    private List<Point> _shape;
    private List<List<Point>> _shapes;

    private float _min = 0;
    private float _max = 10;

    private float _minX = Float.MAX_VALUE;
    private float _minY = Float.MAX_VALUE;
    private float _maxX = Float.MIN_VALUE;
    private float _maxY = Float.MIN_VALUE;

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
        _myPaint.setStrokeWidth(3);
    }

    public byte[] getSignature() {
        // setDrawingCacheEnabled(true);
        // // we make a copy, this ensures we don't screw up the canvas
        // Bitmap bmp = Bitmap.createBitmap(getDrawingCache());
        // setDrawingCacheEnabled(false);
        //
        // ByteArrayOutputStream out = new ByteArrayOutputStream();
        //
        // bmp.compress(CompressFormat.PNG, 100, out);
        //
        // return out.toByteArray();

        StringBuilder sb = new StringBuilder();

        float scale = Math.max((_maxX) / 605, (_maxY) / 115);
        float xo = ((605 * scale) - (_maxX + _minX)) / 2;
        float yo = ((115 * scale) - (_maxY + _minY)) / 2;

        sb.append("[");
        for (int i = 0; i < _shapes.size(); i++) {
            List<Point> shape = _shapes.get(i);

            if (shape.size() > 0) {
                Point lp = shape.get(0);

                for (int j = 1; j < shape.size(); j++) {
                    Point p = shape.get(j);
                    sb.append("{\"lx\":");
                    sb.append((int) ((lp.x + xo) / scale));
                    sb.append(",\"ly\":");
                    sb.append((int) ((lp.y + yo) / scale));
                    sb.append(",\"mx\":");
                    sb.append((int) ((p.x + xo) / scale));
                    sb.append(",\"my\":");
                    sb.append((int) ((p.y + yo) / scale));
                    sb.append("}");

                    if (j < shape.size() - 1) {
                        sb.append(",");
                    }
                    lp = p;
                }

                if (i < _shapes.size() - 2) {
                    sb.append(",");
                }
            }
        }
        sb.append("]");
        Log.v(TAG, sb.toString());

        return sb.toString().getBytes();
    }

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
                    // this way we calculate the stroke for new points only
                    // makes rendering the drawing faster in the future
                    if (p.stroke == null) {
                        long dur = (p.t - lp.t);
                        float d1 = p.x - lp.x;
                        float d2 = p.y - lp.y;
                        float dist = (float) Math.sqrt(d1 * d1 + d2 * d2);
                        // inverted speed calc, we want thinner lines for faster
                        // speeds
                        float stroke = dur / dist;

                        // scale
                        stroke = ((stroke - _min) / _max) * 8 + 3;

                        // cap
                        if (stroke < 4) {
                            stroke = 4;
                        } else if (stroke > 10) {
                            stroke = 10;
                        }

                        if (lp.stroke != null) {
                            if (stroke > lp.stroke + 1.0) {
                                stroke = lp.stroke + 1.0F;
                            } else if (stroke < lp.stroke - 1.0) {
                                stroke = lp.stroke - 1.0F;
                            }
                        }

                        p.stroke = stroke;
                    }

                    _myPaint.setStrokeWidth(p.stroke);
                    canvas.drawLine(lp.x, lp.y, p.x, p.y, _myPaint);
                    lp = p;
                }
            }
        }
        // Log.v(TAG, "min: " + _min + "  max:" + _max);
        super.onDraw(canvas);
    }

    public void clear() {
        _shapes = new LinkedList<List<Point>>();
        _shape = new LinkedList<Point>();
        _shapes.add(_shape);
        _minX = Float.MAX_VALUE;
        _minY = Float.MAX_VALUE;
        _maxX = Float.MIN_VALUE;
        _maxY = Float.MIN_VALUE;

        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // must return true here otherwise we wont receive move/up events.
                return true;
            case MotionEvent.ACTION_MOVE:
                int count = event.getHistorySize();
                for (int i = 0; i < count; i++) {
                    _shape.add(new Point(event.getHistoricalX(i), event.getHistoricalY(i), event.getHistoricalEventTime(i)));
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
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
        public Float stroke = null;

        public Point(MotionEvent event) {
            x = event.getX();
            y = event.getY();
            t = event.getEventTime();
        }

        public Point(float x, float y, long t) {
            this.x = x;
            this.y = y;
            this.t = t;

            if (x < _minX)
                _minX = x;
            if (y < _minY)
                _minY = y;
            if (x > _maxX)
                _maxX = x;
            if (y > _maxY)
                _maxY = y;
        }
    }
}
