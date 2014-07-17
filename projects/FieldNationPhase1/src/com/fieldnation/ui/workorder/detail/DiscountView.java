package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Discount;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiscountView extends RelativeLayout {
	private static final String TAG = "ui.workorder.detail.DiscountView";

	// UI
	private TextView _titleTextView;
	private TextView _amountTextView;
	private ImageButton _deleteButton;

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
		_deleteButton = (ImageButton) findViewById(R.id.delete_imagebutton);
	}

	public void setDiscount(Discount discount) {
		_titleTextView.setText(discount.getDescription());
		_amountTextView.setText(misc.toCurrency(discount.getAmount()));
	}

	public void setDeleteOnClickListener(View.OnClickListener listener) {
		_deleteButton.setOnClickListener(listener);
	}

}
