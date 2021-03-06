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

import com.caverock.androidsvg.SVG;
import com.fieldnation.BuildConfig;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.shortstraw.Point;
import com.fieldnation.shortstraw.Shape;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class SignatureView extends View {
    private static final String TAG = "SignatureView";

    // State
    private static final String STATE_SUPER = "STATE_SUPER";
    private static final String STATE_SHAPES = "STATE_SHAPES";

    // Data
    private Paint _myPaint;
    private Shape _shape;
    private List<Shape> _shapes;
    private boolean _isReadOnly = false;

    private float _min = 0;
    private float _max = 10;
    private float _scale = 1.0F;
    private float _xOff = 0;
    private float _yOff = 0;
    private String _json;
    private String _svgString;
    private SVG _svg;
    private boolean _isMeasured;
    private Canvas _canvas;
    private Bitmap _map;
    private boolean _redraw = false;

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
        _shape = new Shape();
        _shapes = new LinkedList<>();
        _shapes.add(_shape);
        _myPaint = new Paint();
        _myPaint.setColor(Color.BLACK);
        _myPaint.setStrokeWidth(3);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Stopwatch stopwatch = new Stopwatch();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState());

        JsonArray jshapes = new JsonArray();
        for (Shape shape : _shapes) {
            JsonArray jshape = new JsonArray();
            for (int i = 0; i < shape.size(); i++) {
                jshape.add(shape.get(i).toJson());
            }
            jshapes.add(jshape);
        }

        bundle.putString(STATE_SHAPES, jshapes.toString());

        Log.v(TAG, "onSaveInstanceState time " + stopwatch.finish());

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Stopwatch stopwatch = new Stopwatch();

        if (state instanceof Bundle) {
            super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE_SUPER));

            String raw = ((Bundle) state).getString(STATE_SHAPES);

            _shapes = new LinkedList<>();
            try {
                JsonArray jshapes = new JsonArray(raw);

                for (int i = 0; i < jshapes.size(); i++) {
                    JsonArray jshape = jshapes.getJsonArray(i);
                    Shape shape = new Shape();
                    for (int j = 0; j < jshape.size(); j++) {
                        shape.add(Point.fromJson(jshape.getJsonObject(j)));
                    }
                    _shapes.add(shape);
                }

            } catch (Exception ex) {
                Log.v(TAG, ex);
            }

            _shape = new Shape();
            _shapes.add(_shape);
        } else {
            super.onRestoreInstanceState(state);
        }
        Log.v(TAG, "onRestoreInstanceState time " + stopwatch.finish());
    }

    public byte[] getSignaturePng() {
        setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(getDrawingCache());
        setDrawingCacheEnabled(false);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);

        return out.toByteArray();
    }

    public String getSignatureSvg() {
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        for (int i = 0; i < _shapes.size(); i++) {
            Shape s = _shapes.get(i);
            //s.simplify();
            maxX = Math.max(maxX, s.getMaxX());
            maxY = Math.max(maxY, s.getMaxY());
            minX = Math.min(minX, s.getMinX());
            minY = Math.min(minY, s.getMinY());
        }
//        float scale = Math.max(maxX / 605, (maxY) / 115);
//        float xo = ((605 * scale) - (maxX + minX)) / 2;
//        float yo = ((115 * scale) - (maxY + minY)) / 2;
        float width = maxX - minX;
        float height = maxY - minY;

        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
        sb.append("<!-- Generator: Field Nation Android ").append((BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim()).append(" -->");
        sb.append("<svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"" + minX + "px\" y=\"" + minY + "px\" width=\"" + width + "px\" height=\"" + height + "px\" viewBox=\"" + minX + " " + minY + " " + width + " " + height + "\" enable-background=\"new " + minX + " " + minY + " " + width + " " + height + "\" xml:space=\"preserve\">");
        sb.append("<path fill=\"none\" stroke=\"#000000\" stroke-width=\"5\" stroke-miterlimit=\"10\" ");
        sb.append("d=\"");

        for (int i = 0; i < _shapes.size(); i++) {
            Shape shape = _shapes.get(i);

            if (shape.size() > 0) {
                Point lp = shape.get(0);

                sb.append("M" + lp.x + " " + lp.y);

                for (int j = 1; j < shape.size(); j++) {
                    Point p = shape.get(j);
                    sb.append("L" + p.x + " " + p.y);
                }
            }
        }
        sb.append("\"/></svg>");

        return sb.toString();
    }

/*
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

        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        for (int i = 0; i < _shapes.size(); i++) {
            Shape s = _shapes.get(i);
            //s.simplify();
            maxX = Math.max(maxX, s.getMaxX());
            maxY = Math.max(maxY, s.getMaxY());
            minX = Math.min(minX, s.getMinX());
            minY = Math.min(minY, s.getMinY());
        }


        float scale = Math.max(maxX / 605, (maxY) / 115);
        float xo = ((605 * scale) - (maxX + minX)) / 2;
        float yo = ((115 * scale) - (maxY + minY)) / 2;

        sb.append("[");
        for (int i = 0; i < _shapes.size(); i++) {
            Shape shape = _shapes.get(i);

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
*/

    public void setSignatureJson(String signatureJson, boolean isReadOnly) {
        Log.v(TAG, "setSignatureJson");
        _isReadOnly = isReadOnly;
        _json = signatureJson;

        populateUi();
    }

    public void setSignatureSvg(String signatureSvg, boolean isReadOnly) {
        Log.v(TAG, "setSignatureSvg");
        _isReadOnly = isReadOnly;
        _svgString = signatureSvg;

        populateUi();
    }

    private class SignatureParseAsyncTask extends AsyncTaskEx<String, Object, List<Shape>> {
        private int mw;
        private int mh;

        @Override
        protected void onPreExecute() {
            mw = getMeasuredWidth();
            mh = getMeasuredHeight();
        }

        @Override
        protected List<Shape> doInBackground(String... params) {
            try {
                JsonArray signature = new JsonArray(params[0]);

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
                        _shape = new Shape();
                        _shapes.add(_shape);
                        _shape.add(l);
                        _shape.add(m);
                        lp = m;
                    }
                }

                float maxX = Float.MIN_VALUE;
                float maxY = Float.MIN_VALUE;
                float minX = Float.MAX_VALUE;
                float minY = Float.MAX_VALUE;
                for (int i = 0; i < _shapes.size(); i++) {
                    Shape s = _shapes.get(i);
                    maxX = Math.max(maxX, s.getMaxX());
                    maxY = Math.max(maxY, s.getMaxY());
                    minX = Math.min(minX, s.getMinX());
                    minY = Math.min(minY, s.getMinY());
                }

                _xOff = minX;
                _yOff = minY;

                _scale = mw / (maxX - minX);
                if (mh / (maxY - minY) < _scale)
                    _scale = mh / (maxY - minY);

                float height = _scale * (maxY - minY);
                float width = _scale * (maxX - minX);

                _yOff -= ((mh - height) / 2) / _scale;
                _xOff -= ((mw - width) / 2) / _scale;


            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Shape> shapes) {
            _redraw = true;
            invalidate();
        }
    }

    private void populateUi() {
        if (!_isMeasured)
            return;

        if (_json != null) {
            clear();
            new SignatureParseAsyncTask().executeEx(_json);
        } else if (_svgString != null) {
            clear();
            try {
                _svg = SVG.getFromString(_svgString);
                _redraw = true;
                invalidate();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        _isMeasured = true;

        populateUi();
    }

    private void drawShape(Canvas canvas, Shape shape) {
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

    @Override
    protected void onDraw(Canvas canvas) {
//        Stopwatch stopwatch = new Stopwatch();
        // walk through the shapes list... draw those

        if (_map == null) {
            _map = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            _canvas = new Canvas(_map);
        }

        canvas.drawBitmap(_map, 0, 0, _myPaint);

        if (_redraw) {
            if (_svg != null) {
                Log.v(TAG, "SVG: " + _svg.getDocumentWidth() + "," + _svg.getDocumentHeight() + " " + canvas.getWidth() + "," + canvas.getHeight());
                _svg.setDocumentWidth(_canvas.getWidth());
                _svg.setDocumentHeight(_canvas.getHeight());
                _svg.renderToCanvas(_canvas);
            }
            for (int i = 0; i < _shapes.size(); i++) {
                drawShape(_canvas, _shapes.get(i));
            }
            _redraw = false;
        } else {
            drawShape(canvas, _shape);
        }

        super.onDraw(canvas);

//        Log.v(TAG, "onDraw time " + stopwatch.finish());
    }

    public void clear() {
        _shapes = new LinkedList<>();
        _shape = new Shape();
        _shapes.add(_shape);
        _scale = 1;
        _xOff = 0;
        _yOff = 0;

        if (_map != null) {
            _canvas = null;
            _map.recycle();
            _map = null;
        }

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
                    drawShape(_canvas, _shape);
                    _shape = new Shape();
                    _shapes.add(_shape);
                    invalidate();
                    return true;
            }
        }

        return super.dispatchTouchEvent(event);
    }

}
