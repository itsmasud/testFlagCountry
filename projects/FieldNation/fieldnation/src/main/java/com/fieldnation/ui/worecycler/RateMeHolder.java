package com.fieldnation.ui.worecycler;

import android.view.View;

import com.fieldnation.ui.RateMeView;

/**
 * Created by Michael on 4/8/2016.
 */
public class RateMeHolder extends BaseHolder {
    public RateMeHolder(View itemView) {
        super(itemView, TYPE_RATE_ME);
    }

    public RateMeView getView() {
        return (RateMeView) itemView;
    }
}
