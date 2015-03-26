package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by michael.carver on 11/3/2014.
 */
public class FnVideoView extends VideoView {


    public FnVideoView(Context context) {
        super(context);
    }

    public FnVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FnVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (widthMeasureSpec > heightMeasureSpec) {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        } else {
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
        }
    }
}
