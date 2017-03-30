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

public class RemindMeMenuButton extends RelativeLayout {
    private static final String TAG = "RemindMeMenuButton";

    private Button _button;

    public RemindMeMenuButton(Context context) {
        super(context);
        init();
    }

    public RemindMeMenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RemindMeMenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_toolbar_remindme_button, this);

        if (isInEditMode())
            return;

        _button = (Button) findViewById(R.id.remindme_button);
    }

    public Button getButton() {
        return _button;
    }
}
