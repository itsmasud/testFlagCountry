package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

/*
 * This is a very stupid signature collection class. 
 * TODO Improve speed by drawing the lines on a back buffer as we capture them. This way we only need to do all the calculations on the shapes once.
 * TODO implement short straw to filter out points we don't need
 */
public class SignatureView extends View {
    private static final String TAG = "ui.SignatureView";

    // State
    private static final String STATE_SUPER = "STATE_SUPER";
    private static final String STATE_SHAPES = "STATE_SHAPES";

    // Data
    private Paint _myPaint;
    private List<Point> _shape;
    private List<List<Point>> _shapes;
    private boolean _isReadOnly = false;

    private float _min = 0;
    private float _max = 10;
    private float _scale = 1.0F;
    private float _xOff = 0;
    private float _yOff = 0;
    private String _json;
    private boolean _isMeasured;


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

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());

        JsonArray jshapes = new JsonArray();
        for (int i = 0; i < _shapes.size(); i++) {
            List<Point> shape = _shapes.get(i);
            JsonArray jshape = new JsonArray();
            for (int j = 0; j < shape.size(); j++) {
                jshape.add(shape.get(j).toJson());
            }
            jshapes.add(jshape);
        }

        bundle.putString(STATE_SHAPES, jshapes.toString());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE_SUPER));

            String raw = ((Bundle) state).getString(STATE_SHAPES);

            Point.resetBounds();
            _shapes = new LinkedList<List<Point>>();
            try {
                JsonArray jshapes = new JsonArray(raw);

                for (int i = 0; i < jshapes.size(); i++) {
                    JsonArray jshape = jshapes.getJsonArray(i);
                    List<Point> shape = new LinkedList<Point>();
                    for (int j = 0; j < jshape.size(); j++) {
                        shape.add(Point.fromJson(jshape.getJsonObject(j)));
                    }
                    _shapes.add(shape);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            _shape = new LinkedList<Point>();
            _shapes.add(_shape);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public byte[] getSignaturePng() {
        setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(getDrawingCache());
        setDrawingCacheEnabled(false);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);

        return out.toByteArray();
    }

    public String getSignatureJson() {
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

        float scale = Math.max((Point.MAX_X) / 605, (Point.MAX_Y) / 115);
        float xo = ((605 * scale) - (Point.MAX_X + Point.MIN_X)) / 2;
        float yo = ((115 * scale) - (Point.MAX_Y + Point.MIN_Y)) / 2;

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
        //Log.v(TAG, sb.toString());

        return sb.toString();
    }

    public void setSignatureJson(String signatureJson, boolean isReadOnly) {
        _isReadOnly = isReadOnly;
        _json = signatureJson;

        populateUi();
    }

    private void populateUi() {
        if (_json == null)
            return;

        if (!_isMeasured)
            return;

        clear();
        try {
            JsonArray signature = new JsonArray(_json);

            Point lp = null;
            for (int i = 0; i < signature.size(); i++) {
                JsonObject seg = signature.getJsonObject(i);

                Point l = new Point(seg.getFloat("lx"), seg.getFloat("ly"), 0);
                Point m = new Point(seg.getFloat("mx"), seg.getFloat("my"), 0);

                if (lp == null) {
                    _shape.add(l);
                    _shape.add(m);
                    lp = m;
                } else if ((int) lp.x == (int) l.x && (int) lp.y == (int) l.y) {
                    _shape.add(m);
                    lp = m;
                } else {
                    _shape = new LinkedList<Point>();
                    _shapes.add(_shape);
                    _shape.add(l);
                    _shape.add(m);
                    lp = m;
                }
            }

            _xOff = Point.MIN_X;
            _yOff = Point.MIN_Y;

            _scale = getMeasuredWidth() / (Point.MAX_X - Point.MIN_X);
            if (getMeasuredHeight() / (Point.MAX_Y - Point.MIN_Y) < _scale)
                _scale = getMeasuredHeight() / (Point.MAX_Y - Point.MIN_Y);

            float height = _scale * (Point.MAX_Y - Point.MIN_Y);
            float width = _scale * (Point.MAX_X - Point.MIN_X);

            _yOff -= ((getMeasuredHeight() - height) / 2) / _scale;
            _xOff -= ((getMeasuredWidth() - width) / 2) / _scale;


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        _isMeasured = true;
        populateUi();
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
                    canvas.drawLine(
                            (lp.x - _xOff) * _scale,
                            (lp.y - _yOff) * _scale,
                            (p.x - _xOff) * _scale,
                            (p.y - _yOff) * _scale,
                            _myPaint);
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
        _scale = 1;
        _xOff = 0;
        _yOff = 0;
        Point.resetBounds();

        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!_isReadOnly) {
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
        }

        return super.dispatchTouchEvent(event);
    }

}
