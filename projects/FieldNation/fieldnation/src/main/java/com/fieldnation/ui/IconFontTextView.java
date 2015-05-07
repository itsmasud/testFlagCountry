package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fieldnation.GlobalState;

/**
 * Created by Michael Carver on 5/7/2015.
 */
public class IconFontTextView extends TextView {
    public IconFontTextView(Context context) {
        super(context);
        init();
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(GlobalState.getContext().getIconFont());
    }
}
