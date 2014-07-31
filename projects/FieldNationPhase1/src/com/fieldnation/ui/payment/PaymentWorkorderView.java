package com.fieldnation.ui.payment;

import java.util.Calendar;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.data.accounting.Workorder;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PaymentWorkorderView extends RelativeLayout {
	private static final String TAG = "ui.payment.PaymentWorkorderView";
	// UI
	// status
	private View _statusView;
	private TextView _statusTextView;
	// bundle
	private RelativeLayout _bundleLayout;

	// center panel
	// title
	private TextView _titleTextView;
	// items
	private TextView _clientNameTextView;
	private TextView _distanceTextView;
	private TextView _whenTextView;

	// right panel
	private TextView _paymentTextView;
	private TextView _basisTextView;
	private Button _actionButton;

	public PaymentWorkorderView(Context context) {
		this(context, null, -1);
	}

	public PaymentWorkorderView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public PaymentWorkorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_card, this);

		if (isInEditMode())
			return;

		// connect UI
		// main content
		// status
		_statusView = (RelativeLayout) findViewById(R.id.status_view);
		_statusTextView = (TextView) findViewById(R.id.status_textview);

		// bundle
		_bundleLayout = (RelativeLayout) findViewById(R.id.bundle_layout);

		// center panel
		// title box
		_titleTextView = (TextView) findViewById(R.id.title_textview);
		// items
		_clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
		_distanceTextView = (TextView) findViewById(R.id.distance_textview);
		_distanceTextView.setVisibility(GONE);
		_whenTextView = (TextView) findViewById(R.id.when_textview);

		_actionButton = (Button) findViewById(R.id.action_button);
		_actionButton.setVisibility(GONE);

		_paymentTextView = (TextView) findViewById(R.id.payment_textview);
		_basisTextView = (TextView) findViewById(R.id.basis_textview);

		setIsBundle(false);

		_statusView.setBackgroundResource(R.drawable.wo_card_status_green);
		_statusTextView.setTextColor(getContext().getResources().getColor(R.color.woCardStatusLabel3));

	}

	private void setIsBundle(boolean isBundle) {
		if (isBundle) {
			_bundleLayout.setVisibility(VISIBLE);
			_titleTextView.setVisibility(GONE);
			_basisTextView.setVisibility(GONE);
			_paymentTextView.setVisibility(GONE);
		} else {
			_bundleLayout.setVisibility(GONE);
			_titleTextView.setVisibility(VISIBLE);
			_basisTextView.setVisibility(VISIBLE);
			_paymentTextView.setVisibility(VISIBLE);
		}
	}

	public void setWorkorder(Payment payment, Workorder wo) {
		_titleTextView.setText(wo.getTitle());
		_clientNameTextView.setText(wo.getClientName());

		try {
			if (wo.getEndTime() != null) {
				String when = "";
				Calendar cal = ISO8601.toCalendar(wo.getEndTime());

				when = misc.formatDate(cal);

				_whenTextView.setVisibility(VISIBLE);
				_whenTextView.setText(when);
			} else {
				_whenTextView.setVisibility(GONE);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			_whenTextView.setVisibility(GONE);
		}
		_basisTextView.setText(misc.capitalize(payment.getPayMethod()));
		_paymentTextView.setText(misc.toCurrency(wo.getAmount()).substring(1));
		_statusTextView.setText(R.string.processing);

	}
}
