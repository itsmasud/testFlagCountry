package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 1/10/17.
 */

public class NestedScrollView extends android.support.v4.widget.NestedScrollView {
    private static final String TAG = "NestedScrollView";

    private OnOverScrollListener _onOverscrollListener;

    private float lastScrollY = 0;
    private int scrollAmountY = 0;
    private boolean isScrollDown = false;

    private float lastScrollX = 0;
    private int scrollAmountX = 0;
    private boolean isScrollRight = false;
    private boolean _startingPull = true;

    public NestedScrollView(Context context) {
        super(context);
        init();
    }

    public NestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    public void setOnOverScrollListener(OnOverScrollListener listener) {
        _onOverscrollListener = listener;
    }

    // Used to detect end/start of list
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (isTouchEvent) {
            scrollAmountX += deltaX;
            scrollAmountY += deltaY;

            isScrollRight = scrollAmountX < 0;
            isScrollDown = scrollAmountY < 0;
            if (_onOverscrollListener != null) {
                //Log.v(TAG, "OverScrollListView.overScrollBy(" + scrollAmountX + ", " + scrollAmountY + ") " + isTouchEvent);
                _onOverscrollListener.onOverScrolled(this, scrollAmountX, scrollAmountY);
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Log.v(TAG, "onTouchEvent " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Log.v(TAG, "onTouchEvent ACTION_DOWN" + ev.getAction());
                // if (getCount() == 0)
                // _startingPull = true;
                break;
            case MotionEvent.ACTION_UP:
                // Log.v(TAG, "onTouchEvent ACTION_UP" + ev.getAction());
                if (_onOverscrollListener != null) {
                    _onOverscrollListener.onOverScrollComplete(this, scrollAmountX, scrollAmountY);
                }
                scrollAmountX = 0;
                scrollAmountY = 0;
                lastScrollX = 0;
                lastScrollY = 0;
                isScrollRight = false;
                isScrollDown = false;
                _startingPull = true;
                break;

            case MotionEvent.ACTION_MOVE:
                // Log.v(TAG, "onTouchEvent ACTION_MOVE" + ev.getAction());
                // if we are in overscroll mode
                if (_startingPull && computeVerticalScrollOffset() == 0) {
                    int size = ev.getHistorySize();
                    for (int i = 0; i < size; i++) {
                        scrollAmountX += (int) (-ev.getHistoricalX(i));
                        scrollAmountY += (int) (-ev.getHistoricalY(i));
                    }

                    isScrollRight = scrollAmountX < 0;
                    isScrollDown = scrollAmountY < 0;

                    if (isScrollDown)
                        scrollAmountY = -1;
                    else
                        scrollAmountY = 1;

                    if (isScrollRight)
                        scrollAmountX = -1;
                    else
                        scrollAmountX = 1;

                    if (_onOverscrollListener != null) {
//                        Log.v(TAG, "ACTION_MOVE(" + scrollAmountX + ", " + scrollAmountY + ") ");
                        _onOverscrollListener.onOverScrolled(this, scrollAmountX, scrollAmountY);
                    }
                    _startingPull = false;

                } else if (scrollAmountX != 0 || scrollAmountY != 0) {
                    // figure out hte offset of the movements and add them
                    int size = ev.getHistorySize();
                    for (int i = 0; i < size; i++) {
                        // we clamp the one that did initially change.
                        if (scrollAmountX != 0) {
                            // ignore first data point. it will be bad
                            if (lastScrollX != 0)
                                scrollAmountX += (int) (lastScrollX - ev.getHistoricalX(i));
                            lastScrollX = ev.getHistoricalX(i);
                        }

                        if (scrollAmountY != 0) {
                            if (lastScrollY != 0)
                                scrollAmountY += (int) (lastScrollY - ev.getHistoricalY(i));
                            lastScrollY = ev.getHistoricalY(i);
                        }
                    }

                    if (isScrollDown && scrollAmountY >= 0) {
                        scrollAmountY = 0;
                    } else if (!isScrollDown && scrollAmountY <= 0) {
                        scrollAmountY = 0;
                    }

                    if (isScrollRight && scrollAmountX >= 0) {
                        scrollAmountX = 0;
                    } else if (!isScrollRight && scrollAmountY <= 0) {
                        scrollAmountX = 0;
                    }

                    // notify the listener
                    if (_onOverscrollListener != null) {
                        if (scrollAmountX < 0 || scrollAmountY < 0) {
//                            Log.v(TAG, "OverScrollListView.overScrollBy(" + scrollAmountX + ", " + scrollAmountY + ")");
                            _onOverscrollListener.onOverScrolled(this, scrollAmountX, scrollAmountY);
                            return super.onTouchEvent(ev);
                        } else {
                            _onOverscrollListener.onOverScrollComplete(this, scrollAmountX, scrollAmountY);
                            scrollAmountX = 0;
                            scrollAmountY = 0;
                            lastScrollX = 0;
                            lastScrollY = 0;
                            isScrollRight = false;
                            isScrollDown = false;
                            _startingPull = true;
                        }
                    }
                    // we don't allow the list to scroll when we are in overscroll mode.
                    //return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        try {
            View v = getChildAt(0);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenHeight = displaymetrics.heightPixels;

            int actionBarHeight = 0;
            TypedValue tv = new TypedValue();
            if (((Activity) getContext()).getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }

            v.setMinimumHeight(screenHeight - actionBarHeight / 4);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
    }
}
