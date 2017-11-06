package com.fieldnation.ui;

import android.view.View;

/**
 * This click listener will skip bouncy clicks
 * Created by mc on 11/3/17.
 */

public abstract class ApatheticOnClickListener implements View.OnClickListener {
    /**
     * Configure as needed. 500ms is the default time to wait between clicks
     */
    private static final int BOUNCE_TIME = 500;

    private long wait = 0;

    @Override
    public void onClick(View v) {
        if (wait > System.currentTimeMillis()) {
            return;
        }
        wait = System.currentTimeMillis() + BOUNCE_TIME;
        onSingleClick(v);
    }

    public abstract void onSingleClick(View v);
}
