package com.fieldnation.ui;

import android.view.View;

/**
 * Created by michael.carver on 11/25/2014.
 */
public interface OnOverScrollListener {
    void onOverScrolled(View view, int pixelsX, int pixelsY);

    void onOverScrollComplete(View view, int pixelsX, int pixelsY);
}
