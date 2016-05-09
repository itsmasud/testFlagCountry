package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 7/9/2015.
 */
public class TileTextView extends RelativeLayout {
    private static final String TAG = "TileTextView";

    // Ui
    private TextView _textview;

    public TileTextView(Context context) {
        super(context);
        init();
    }

    public TileTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TileTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_textview_tile, this);

        if (isInEditMode())
            return;

        _textview = (TextView) findViewById(R.id.textview);
    }

    public void setText(String text) {
        _textview.setText(text);
    }
}
