package com.fieldnation.ui.payment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Fee;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.misc;

public class PaymentFeeView extends RelativeLayout {
    private static final String TAG = "PaymentFeeView";

    // UI
    //private IconFontTextView _iconView;
    private TextView _titleTextView;
    private TextView _subTitleTextView;
    private TextView _paymentTextView;
    private TextView _payTypeTextView;

    /*-*****************************-*/
    /*-			Life cycle			-*/
    /*-*****************************-*/
    public PaymentFeeView(Context context) {
        super(context);
        init();
    }

    public PaymentFeeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentFeeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_payment_card_noicon, this);

        if (isInEditMode())
            return;

//        _iconView = (IconFontTextView) findViewById(R.id.icon_view);
//        _iconView.setTextColor(getResources().getColor(R.color.fn_accent_color));
//        _iconView.setText(R.string.icon_circle_add);

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _subTitleTextView = (TextView) findViewById(R.id.subtitle_textview);

        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
        _payTypeTextView = (TextView) findViewById(R.id.paytype_textview);

        _subTitleTextView.setVisibility(GONE);
    }

    public void setWorkorder(Payment payment, Fee fee) {
        _titleTextView.setText(fee.getTitle());
        _paymentTextView.setText(misc.toCurrency(fee.getAmount()));
        _payTypeTextView.setText("Fee");
    }
}
