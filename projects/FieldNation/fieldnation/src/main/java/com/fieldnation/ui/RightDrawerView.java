package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 5/20/2015.
 */
public class RightDrawerView extends RelativeLayout {
    public RightDrawerView(Context context) {
        super(context);
        init();
    }

    public RightDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RightDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_right_drawer, this, true);
    }
}
