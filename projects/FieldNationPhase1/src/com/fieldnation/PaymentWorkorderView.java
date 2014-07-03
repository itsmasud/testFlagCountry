package com.fieldnation;

import java.util.Calendar;

import com.fieldnation.data.payments.Payment;
import com.fieldnation.data.payments.Workorder;
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
	private static final String TAG = "PaymentWorkorderView";
	// UI
	private RelativeLayout _statusLayout;
	private TextView _titleTextView;
	private TextView _clientNameTextView;
	private Button _detailButton;
	private TextView _cashTextView;
	private TextView _basisTextView;
	private TextView _distanceTextView;
	private TextView _whenTextView;
	private TextView _statusTextView;
	private LinearLayout _contentLayout;
	private RelativeLayout _optionsLayout;
	private RelativeLayout _bundleLayout;
	private ImageView _bundleImageView;
	private TextView _bundleTextView;
	private View _bundleSeparator;

	// Data
	private GlobalState _gs;

	public PaymentWorkorderView(Context context) {
		this(context, null, -1);
	}

	public PaymentWorkorderView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public PaymentWorkorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(getContext()).inflate(
				R.layout.view_workorder_summary, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) getContext().getApplicationContext();

		_contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		_optionsLayout = (RelativeLayout) findViewById(R.id.options_layout);
		// _optionsLayout.setOnClickListener(_options_onClick);
		_optionsLayout.setClickable(false);

		_statusTextView = (TextView) findViewById(R.id.status_textview);

		_statusLayout = (RelativeLayout) findViewById(R.id.status_layout);

		_bundleLayout = (RelativeLayout) findViewById(R.id.bundle_layout);
		_bundleImageView = (ImageView) findViewById(R.id.bundle_imageview);
		_bundleTextView = (TextView) findViewById(R.id.bundle_textview);
		_bundleSeparator = findViewById(R.id.bundle_separator);

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
		_distanceTextView = (TextView) findViewById(R.id.distance_textview);
		_distanceTextView.setVisibility(GONE);
		_whenTextView = (TextView) findViewById(R.id.when_textview);

		_detailButton = (Button) findViewById(R.id.detail_button);
		_detailButton.setVisibility(GONE);
		// _detailButton.setOnClickListener(_detailButton_onClick);

		_cashTextView = (TextView) findViewById(R.id.payment_textview);
		_basisTextView = (TextView) findViewById(R.id.basis_textview);

		// _cashLinearLayout.setVisibility(GONE);

		setIsBundle(false);

		_statusLayout.setBackgroundResource(R.drawable.wosum_status_3);
		_statusTextView.setTextColor(getContext().getResources().getColor(
				R.color.wosumStatusLabel3));

	}

	private void setIsBundle(boolean isBundle) {
		if (isBundle) {
			_bundleLayout.setVisibility(VISIBLE);
			_titleTextView.setVisibility(GONE);
			_basisTextView.setVisibility(GONE);
			_cashTextView.setVisibility(GONE);
			_bundleSeparator.setVisibility(VISIBLE);
		} else {
			_bundleLayout.setVisibility(GONE);
			_bundleSeparator.setVisibility(GONE);
			_titleTextView.setVisibility(VISIBLE);
			_basisTextView.setVisibility(VISIBLE);
			_cashTextView.setVisibility(VISIBLE);
		}
	}

	public void setWorkorder(Payment payment, Workorder wo) {
		_titleTextView.setText(wo.getWoTitle());
		_clientNameTextView.setText(wo.getClientName());

		try {
			if (wo.getWoEndDate() != null) {
				String when = "";
				Calendar cal = ISO8601.toCalendar(wo.getWoEndDate());

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
		_cashTextView.setText(misc.toCurrency(wo.getAmount()).substring(1));
		_statusTextView.setText("Processing");

	}
}
