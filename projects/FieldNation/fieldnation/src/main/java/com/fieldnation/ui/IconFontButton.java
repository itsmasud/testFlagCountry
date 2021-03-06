package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.fieldnation.App;

/**
 * Created by Michael Carver on 5/7/2015.
 */
public class IconFontButton extends Button {
    public IconFontButton(Context context) {
        super(context);
        init();
    }

    public IconFontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconFontButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode())
            return;

        setTypeface(App.get().getIconFont());
    }
}
