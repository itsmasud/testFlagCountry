package com.fieldnation.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 12/17/2015.
 */
public class NoPaymentsTileView extends RelativeLayout {

    public NoPaymentsTileView(Context context) {
        super(context);
        init();
    }

    public NoPaymentsTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoPaymentsTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_no_payments_tile, this);
    }
}
