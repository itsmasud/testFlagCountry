package com.fieldnation.v2.ui.worecycler;

import android.view.View;

import com.fieldnation.ui.EmptyCardView;


/**
 * Created by Shoaib on 07/03/2017.
 */
public class EmptyCardHolder extends BaseHolder {
    public EmptyCardHolder(View itemView) {
        super(itemView, TYPE_EMPTY);
    }

    public EmptyCardView getView() {
        return (EmptyCardView) itemView;
    }
}
