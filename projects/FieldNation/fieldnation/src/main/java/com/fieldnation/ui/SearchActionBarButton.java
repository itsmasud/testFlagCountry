package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.R;
import com.fieldnation.UniqueTag;

public class SearchActionBarButton extends RelativeLayout {
    private final String TAG = UniqueTag.makeTag("SearchActionBarView");

	/*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/

    public SearchActionBarButton(Context context) {
        super(context);
        init();
    }

    public SearchActionBarButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchActionBarButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_action_bar, this);
    }
}