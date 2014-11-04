package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by michael.carver on 11/4/2014.
 */
public class OverScrollListView extends ListView {


    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public OverScrollListView(Context context) {
        super(context);
        init();
    }

    public OverScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }
}


