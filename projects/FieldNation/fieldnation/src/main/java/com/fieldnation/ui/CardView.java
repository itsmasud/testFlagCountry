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
public class CardView extends FrameLayout {
    private static final String TAG = "CardView";

    // Ui
    private FrameLayout _container;

    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_card, this);
    }

    private FrameLayout getContainer() {
        if (_container == null) {
            _container = (FrameLayout) findViewById(R.id.card_container);
        }
        return _container;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child.getId() == R.id.card_content)
            super.addView(child, index, params);
        else
            getContainer().addView(child, index, params);
    }
}
