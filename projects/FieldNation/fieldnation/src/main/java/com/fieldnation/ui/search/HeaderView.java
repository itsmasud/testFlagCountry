package com.fieldnation.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by mc on 12/22/16.
 */

public class HeaderView extends RelativeLayout {
    public HeaderView(Context context) {
        super(context);
        init();
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_work_order_list_header, this);

        if (isInEditMode())
            return;
    }
}
