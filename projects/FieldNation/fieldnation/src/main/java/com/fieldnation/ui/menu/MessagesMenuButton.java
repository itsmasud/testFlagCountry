package com.fieldnation.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by mc on 8/30/17.
 */

public class MessagesMenuButton extends RelativeLayout {
    private static final String TAG = "MessagesMenuButton";

    // Ui
    private IconFontTextView _iconTextView;
    private View _indicatorView;

    public MessagesMenuButton(Context context) {
        super(context);
        init();
    }

    public MessagesMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessagesMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.menu_messages_button, this, true);

        if (isInEditMode()) return;

        _iconTextView = findViewById(R.id.icon_textview);
        _indicatorView = findViewById(R.id.indicator_View);
    }
}
