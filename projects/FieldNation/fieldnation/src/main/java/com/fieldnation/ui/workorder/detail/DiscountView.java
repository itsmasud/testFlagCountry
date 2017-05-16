package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.PayModifier;

public class DiscountView extends RelativeLayout {
    private static final String TAG = "ui.workorder.detail.DiscountView";

    // UI
    private TextView _titleTextView;
    private TextView _amountTextView;

    // Data
    private PayModifier _discount;

    public DiscountView(Context context) {
        this(context, null, -1);
    }

    public DiscountView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DiscountView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.view_discount, this);

        if (isInEditMode())
            return;

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _amountTextView = (TextView) findViewById(R.id.amount_textview);
    }

    public void setDiscount(PayModifier discount) {
        _discount = discount;
        if (discount.getName() != null)
            _titleTextView.setText(discount.getName());
        else
            _titleTextView.setText("NA");

        if (discount.getAmount() != null)
            _amountTextView.setText(misc.toCurrency(discount.getAmount()));
        else
            _amountTextView.setText(misc.toCurrency(0));
    }

    public PayModifier getDiscount() {
        return _discount;
    }
}
