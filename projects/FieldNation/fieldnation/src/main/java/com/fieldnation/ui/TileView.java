package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 6/9/2015.
 */
public class TileView extends FrameLayout {
    private static final String TAG = "TileView";

    // Ui
    private FrameLayout _container;


    public TileView(Context context) {
        super(context);
        init();
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_tile, this);
    }

    private FrameLayout getContainer() {
        if (_container == null) {
            _container = (FrameLayout) findViewById(R.id.container);
        }
        return _container;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.container) {
            super.addView(child, index, params);
        } else {
            getContainer().addView(child, index, params);
        }

    }
}
