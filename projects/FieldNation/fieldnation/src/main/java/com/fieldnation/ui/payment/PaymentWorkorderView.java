package com.fieldnation.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.accounting.Workorder;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.fntools.misc;

public class PaymentWorkorderView extends RelativeLayout {
    private static final String TAG = "PaymentWorkorderView";

    // UI
    //private IconFontTextView _iconView;
    private TextView _titleTextView;
    private TextView _subTitleTextView;
    private TextView _paymentTextView;
    private TextView _payTypeTextView;

    // Data
    private Workorder _workorder;

    /*-*****************************-*/
    /*-			Life cycle			-*/
    /*-*****************************-*/
    public PaymentWorkorderView(Context context) {
        super(context);
        init();
    }

    public PaymentWorkorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaymentWorkorderView(Context context, AttributeSet attrs, int defStyle) {
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
        _subTitleTextView.setVisibility(GONE);

        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
        _payTypeTextView = (TextView) findViewById(R.id.paytype_textview);


        setOnClickListener(_this_onClick);
    }

    public void setWorkorder(Payment payment, Workorder wo) {
        _workorder = wo;

        _titleTextView.setText(wo.getTitle());
        _paymentTextView.setText(misc.toCurrency(wo.getAmount()));
        _payTypeTextView.setText(getResources().getString(R.string.wo_x, wo.getWorkorderId()));
    }

    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderActivity.startNew(getContext(), _workorder.getWorkorderId());
        }
    };

}
