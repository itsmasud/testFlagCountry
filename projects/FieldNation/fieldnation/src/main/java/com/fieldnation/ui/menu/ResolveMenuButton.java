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

public class ResolveMenuButton extends RelativeLayout {
    private static final String TAG = "ResolveMenuButton";

    private Button _button;

    public ResolveMenuButton(Context context) {
        super(context);
        init();
    }

    public ResolveMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ResolveMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_toolbar_resolve_button, this);

        if (isInEditMode())
            return;

        _button = (Button) findViewById(R.id.resolve_button);
    }

    public Button getButton() {
        return _button;
    }
}