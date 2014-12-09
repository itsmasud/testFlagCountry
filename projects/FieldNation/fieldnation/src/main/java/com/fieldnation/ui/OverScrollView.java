package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by michael.carver on 11/25/2014.
 */
public class OverScrollView extends ScrollView {
    private static final String TAG = "ui.OverScrollListView";

    private OnOverScrollListener _onOverscrollListener;

    private float lastScrollY = 0;
    private int scrollAmountY = 0;
    private boolean isScrollDown = false;

    private float lastScrollX = 0;
    private int scrollAmountX = 0;
    private boolean isScrollRight = false;
    private boolean _startingPull = true;


    public OverScrollView(Context context) {
        super(context);
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init() {
        setOverScrollMode(OVER_SCROLL_ALWAYS);
    }

    public void setOnOverScrollListener(OnOverScrollListener listener) {
        _onOverscrollListener = listener;
    }

    // Used to detect end/start of list
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (isTouchEvent && getScrollY() == 0 || getMaxScrollAmount() == 0) {
            scrollAmountX += deltaX;
            scrollAmountY += deltaY;

            isScrollRight = scrollAmountX < 0;
            isScrollDown = scrollAmountY < 0;
            if (_onOverscrollListener != null) {
                Log.v(TAG, "OverScrollListView.overScrollBy(" + scrollAmountX + ", " + scrollAmountY + ") " + isTouchEvent);
                _onOverscrollListener.onOverScrolled(this, scrollAmountX, scrollAmountY);
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    public boolean onTouchEvent(MotionEvent ev) {
        Log.v(TAG, "onTouchEvent " + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                if (getCount() == 0)
//                    _startingPull = true;
                break;
            case MotionEvent.ACTION_UP:
                if (_onOverscrollListener != null) {
                    _onOverscrollListener.onOverScrollComplete(this, scrollAmountX, scrollAmountY);
                }
                scrollAmountX = 0;
                scrollAmountY = 0;
                lastScrollX = 0;
                lastScrollY = 0;
                break;

            case MotionEvent.ACTION_MOVE:
                // if we are in overscroll mode
                if (_startingPull && getScrollY() == 0 || getMaxScrollAmount() == 0) {
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
                        Log.v(TAG, "ACTION_MOVE(" + scrollAmountX + ", " + scrollAmountY + ") ");
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
                        if (scrollAmountX != 0 || scrollAmountY != 0) {
                            Log.v(TAG, "OverScrollListView.overScrollBy(" + scrollAmountX + ", " + scrollAmountY + ")");
                            _onOverscrollListener.onOverScrolled(this, scrollAmountX, scrollAmountY);
                        } else {
                            if (_onOverscrollListener != null) {
                                _onOverscrollListener.onOverScrollComplete(this, scrollAmountX, scrollAmountY);
                            }
                            scrollAmountX = 0;
                            scrollAmountY = 0;
                            lastScrollX = 0;
                            lastScrollY = 0;
                        }
                    }
                    // we don't allow the list to scroll when we are in overscroll mode.
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

}
