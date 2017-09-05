package com.fieldnation.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 8/30/17.
 */

public class MoreMenuButton extends RelativeLayout {
    private static final String TAG = "MoreMenuButton";

    // Ui
    private IconFontTextView _iconTextView;

    public MoreMenuButton(Context context) {
        super(context);
        init();
    }

    public MoreMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoreMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.menu_more_button, this, true);

        if (isInEditMode()) return;

        _iconTextView = findViewById(R.id.icon_textview);
    }
}
