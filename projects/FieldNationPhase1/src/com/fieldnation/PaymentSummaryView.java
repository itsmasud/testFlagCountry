package com.fieldnation;

import com.fieldnation.json.JsonObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PaymentSummaryView extends RelativeLayout {
	private static final String TAG = "PaymentSummaryView";

	private TextView _titleTextView;
	private TextView _idTextView;
	private TextView _descriptionTextView;
	private TextView _dateTextView;
	private TextView _amountTextView;
	private TextView _paymentTypeTextView;

	private JsonObject _paymentInfo;

	/*-*****************************-*/
	/*-			Life cycle			-*/
	/*-*****************************-*/
	public PaymentSummaryView(Context context) {
		this(context, null, -1);
	}

	public PaymentSummaryView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public PaymentSummaryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_payment_summary,
				this);

		if (isInEditMode())
			return;

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_idTextView = (TextView) findViewById(R.id.id_textview);
		_descriptionTextView = (TextView) findViewById(R.id.description_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
		_amountTextView = (TextView) findViewById(R.id.amount_textview);
		_paymentTypeTextView = (TextView) findViewById(R.id.paymenttype_textview);
	}

	public void setData(JsonObject paymentInfo) {
		_paymentInfo = paymentInfo;

		Log.v(TAG, paymentInfo.display());
	}

	/**
	 * repopulates the ui
	 */
	private void refresh() {
	}
}
