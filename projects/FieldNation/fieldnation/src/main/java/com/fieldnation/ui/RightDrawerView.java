package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 5/20/2015.
 */
public class RightDrawerView extends FrameLayout {
    private static final String TAG = "RightDrawerView";

    // Ui
    private FrameLayout _container;

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
        LayoutInflater.from(getContext()).inflate(R.layout.view_right_drawer, this);

        if (isInEditMode())
            return;


    }

    private FrameLayout getContainer() {
        if (_container == null) {
            _container = (FrameLayout) findViewById(R.id.container);
        }

        return _container;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.content) {
            super.addView(child, index, params);
        } else {
            getContainer().addView(child, index, params);
        }
    }

}
