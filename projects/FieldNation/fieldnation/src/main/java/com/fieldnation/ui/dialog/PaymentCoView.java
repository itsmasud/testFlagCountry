package com.fieldnation.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.fieldnation.R;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class PaymentCoView extends RelativeLayout {
    public PaymentCoView(Context context) {
        super(context);
        init();
    }

    public PaymentCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_payment_tile, this);

        if (isInEditMode())
            return;
    }
}
