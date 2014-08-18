package com.fieldnation.ui.payment;

import java.util.Calendar;

import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PaymentCardView extends RelativeLayout {
	private static final String TAG = "ui.payment.PaymentCardView";

	private static final int[] _HEADER_BG = new int[] {
			R.drawable.payment_summary_header_bg,
			R.drawable.payment_summary_header_paid_bg };

	private TextView _titleTextView;
	private TextView _idTextView;
	private TextView _descriptionTextView;
	private TextView _dateTextView;
	private TextView _amountTextView;
	private TextView _paymentTypeTextView;
	private LinearLayout _paymentHeaderLayout;

	private Payment _paymentInfo;

	/*-*****************************-*/
	/*-			Life cycle			-*/
	/*-*****************************-*/
	public PaymentCardView(Context context) {
		this(context, null, -1);
	}

	public PaymentCardView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public PaymentCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_payment_card, this);

		if (isInEditMode())
			return;

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_idTextView = (TextView) findViewById(R.id.id_textview);
		_descriptionTextView = (TextView) findViewById(R.id.description_textview);
		_dateTextView = (TextView) findViewById(R.id.date_textview);
		_amountTextView = (TextView) findViewById(R.id.amount_textview);
		_paymentTypeTextView = (TextView) findViewById(R.id.paymenttype_textview);
		_paymentHeaderLayout = (LinearLayout) findViewById(R.id.paymentheader_layout);

		setOnClickListener(_this_onClick);
	}

	private View.OnClickListener _this_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), PaymentDetailActivity.class);
			intent.putExtra("PAYMENT_INFO", _paymentInfo.toJson().toString());
			getContext().startActivity(intent);
		}
	};

	public void setData(Payment paymentInfo) {
		_paymentInfo = paymentInfo;
		refresh();
	}

	/**
	 * repopulates the ui
	 */
	private void refresh() {
		// amount
		try {
			_amountTextView.setText(misc.toCurrency(_paymentInfo.getAmount()).substring(1));
		} catch (Exception ex) {
			ex.printStackTrace();
			_amountTextView.setText("NA");
		}
		try {
			// TODO create string resources.
			if (_paymentInfo.getFees() != null && _paymentInfo.getFees().length == 1) {
				_descriptionTextView.setText(_paymentInfo.getWorkorders().length + " " + getContext().getString(
						R.string.work_orders) + " / 1 Fee");
			} else if (_paymentInfo.getFees() != null && _paymentInfo.getFees().length > 0) {
				_descriptionTextView.setText(_paymentInfo.getWorkorders().length + " " + getContext().getString(
						R.string.work_orders) + " / " + _paymentInfo.getFees().length + " Fees");

			} else {
				_descriptionTextView.setText(_paymentInfo.getWorkorders().length + " " + getContext().getString(
						R.string.work_orders) + " / 0 Fees");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			_descriptionTextView.setText("");
		}
		// pay_method
		try {
			String method = _paymentInfo.getPayMethod().toLowerCase();
			method = misc.capitalize(method);
			_paymentTypeTextView.setText(method);
		} catch (Exception ex) {
			ex.printStackTrace();
			_paymentTypeTextView.setText("");
		}
		// payment_id
		try {
			_idTextView.setText("ID " + _paymentInfo.getPaymentId());
		} catch (Exception ex) {
			ex.printStackTrace();
			_idTextView.setText("ID ???");
		}
		// status
		try {
			String status = misc.capitalize(_paymentInfo.getStatus());

			if (status.toLowerCase().equals("paid")) {
				_paymentHeaderLayout.setBackgroundResource(_HEADER_BG[1]);
			} else {
				_paymentHeaderLayout.setBackgroundResource(_HEADER_BG[0]);
			}
			_titleTextView.setText(status);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// date_paid
		try {
			String d = _paymentInfo.getDatePaid();
			Calendar cal = ISO8601.toCalendar(d);

			_dateTextView.setText(getContext().getString(R.string.estimated) + " " + misc.formatDate(cal));
		} catch (Exception ex) {
			ex.printStackTrace();
			_dateTextView.setText("");
		}
	}
}
