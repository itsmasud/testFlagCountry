package com.fieldnation.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by michael.carver on 10/31/2014.
 */
public class MoviePlayerView extends View {

    private Movie _movie;
    private int _mw;
    private int _mh;
    private long _md;
    private long _ms = 0;
    private Canvas _surface;
    private float _scale;
    private int _xOff;
    private int _yOff;

    public MoviePlayerView(Context context) {
        super(context);
        init();
    }

    public MoviePlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoviePlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public void init() {
//        _movie = Movie.decodeStream(getResources().openRawResource(R.raw.login_vid));

        _mw = _movie.width();
        _mh = _movie.height();
        _md = _movie.duration();
        _surface = new Canvas();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        int vw = View.MeasureSpec.getSize(widthMeasureSpec);
        int vh = View.MeasureSpec.getSize(heightMeasureSpec);

        _scale = vh * 1.0F / _mh;
        float scale2 = vw * 1.0F / _mw;

        if (scale2 > _scale)
            _scale = scale2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = android.os.SystemClock.uptimeMillis();

        if (_ms == 0) {
            _ms = now;
        }

        if (_movie != null) {
            int dur = _movie.duration();

            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - _ms) % dur);

            _movie.setTime(relTime);
            canvas.scale(_scale, _scale);
            _movie.draw(canvas, 0, 0);

            invalidate();
        }
    }
}
