package com.fieldnation.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by mc on 3/28/17.
 */

public class DoneMenuButton extends RelativeLayout {
    private static final String TAG = "DoneMenuButton";

    private Button _button;

    public DoneMenuButton(Context context) {
        super(context);
        init();
    }

    public DoneMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DoneMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_toolbar_done_button, this);

        if (isInEditMode())
            return;

        _button = (Button) findViewById(R.id.done_button);
    }

    public Button getButton() {
        return _button;
    }
}
