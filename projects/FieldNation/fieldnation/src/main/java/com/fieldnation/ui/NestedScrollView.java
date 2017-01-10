package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.fieldnation.App;
import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 1/10/17.
 */

public class NestedScrollView extends android.support.v4.widget.NestedScrollView {
    private static final String TAG = "NestedScrollView";

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

            v.setMinimumHeight(screenHeight);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
    }
}
