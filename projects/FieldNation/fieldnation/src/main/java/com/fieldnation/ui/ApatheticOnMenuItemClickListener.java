package com.fieldnation.ui;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by mc on 11/3/17.
 */

public abstract class ApatheticOnMenuItemClickListener implements Toolbar.OnMenuItemClickListener {

    private static final int BOUNCE_TIME = 500;

    private long wait = 0;

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (wait > System.currentTimeMillis()) {
            return false;
        }

        wait = System.currentTimeMillis() + BOUNCE_TIME;
        return onSingleMenuItemClick(item);
    }

    public abstract boolean onSingleMenuItemClick(MenuItem item);
}
