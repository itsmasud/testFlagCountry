package com.fieldnation;

import java.text.ParseException;
import java.util.Calendar;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.rpc.WorkorderRpc;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkorderSummaryView extends RelativeLayout {
	// UI
	private View _statusView;
	private TextView _titleTextView;
	private TextView _clientNameTextView;
	private Button _detailButton;
	private TextView _cashTextView;
	private LinearLayout _cashLinearLayout;
	private TextView _basisTextView;
	private TextView _distanceTextView;
	private TextView _whenTextView;

	// Data
	private GlobalState _gs;
	private int _state = 0;
	private AttributeSet _attrs;

	private boolean _hasDetail;
	private JsonObject _workorder;

	// state lookuptable
	private static final int[] _STATUS_LOOKUP_TABLE = {
			R.drawable.wosum_status_1_no_highlight,
			R.drawable.wosum_status_2_no_highlight,
			R.drawable.wosum_status_3_no_highlight,
			R.drawable.wosum_status_4_no_highlight,
			R.drawable.wosum_status_5_no_highlight,
			R.drawable.wosum_status_6_no_highlight,
			R.drawable.wosum_status_7_no_highlight,
			R.drawable.wosum_status_8_no_highlight,
			R.drawable.wosum_status_9_no_highlight };

	public WorkorderSummaryView(Context context) {
		this(context, null, -1);
	}

	public WorkorderSummaryView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public WorkorderSummaryView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_workorder_summary, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) getContext().getApplicationContext();

		_statusView = findViewById(R.id.status_view);

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
		_distanceTextView = (TextView) findViewById(R.id.distance_textview);
		_whenTextView = (TextView) findViewById(R.id.when_textview);

		_detailButton = (Button) findViewById(R.id.detail_button);
		_detailButton.setOnClickListener(_detailButton_onClick);

		_cashLinearLayout = (LinearLayout) findViewById(R.id.payment_linearlayout);
		_cashTextView = (TextView) findViewById(R.id.payment_textview);
		_basisTextView = (TextView) findViewById(R.id.basis_textview);

		_detailButton.setVisibility(GONE);

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _detailButton_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			_state = (_state + 1) % 9;

			_statusView.setBackgroundResource(_STATUS_LOOKUP_TABLE[_state]);

			// TODO Method Stub: onClick()
			System.out.println("Method Stub: onClick()");

		}
	};

	private ResultReceiver _rpcReceiver = new ResultReceiver(new Handler()) {
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (resultCode == 1) {
				long workorderId = resultData.getLong("PARAM_WORKORDER_ID");
				try {
					if (workorderId == _workorder.getLong("workorder_id")) {
						String data = new String(
								resultData.getByteArray("PARAM_DATA"));
						// getDetails

					}
				} catch (Exception ex) {
					// TODO handle better?
					ex.printStackTrace();
				}

			}
			// TODO Method Stub: onReceiveResult()
			System.out.println("Method Stub: onReceiveResult()");
			super.onReceiveResult(resultCode, resultData);
		}
	};

	/*-*********************************-*/
	/*-				Data				-*/
	/*-*********************************-*/
	public void setWorkorder(JsonObject workorder) {
		_workorder = workorder;
		refresh();
	}

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void refresh() {
		try {
			// title
			_titleTextView.setText(_workorder.getString("title"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			// client name location.contact_name
			if (_workorder.has("location.contact_name")) {
				_clientNameTextView.setText(_workorder
						.getString("location.contact_name"));
			} else {
				_clientNameTextView.setText("NA");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {

			// distance/address? location.state, location.zip, location.city,
			// location.country,
			if (_workorder.has("location.distance")) {
				_distanceTextView.setText(_workorder
						.getString("location.distance") + " mi");
			} else {
				_distanceTextView.setText("NA");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {

			// when scheduledTimeStart/scheduledTimeEnd
			if (_workorder.has("scheduledTimeStart")
					&& _workorder.has("scheduledTimeEnd")) {
				long scheduledTimeStart = _workorder
						.getLong("scheduledTimeStart") * 1000;
				long scheduledTimeEnd = _workorder.getLong("scheduledTimeEnd") * 1000;
				String when = "";
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(scheduledTimeStart);

				when = (cal.get(Calendar.MONTH) + 1) + "/"
						+ cal.get(Calendar.DAY_OF_MONTH) + "/"
						+ cal.get(Calendar.YEAR);

				if (scheduledTimeEnd > 0) {
					cal.setTimeInMillis(scheduledTimeEnd);

					when += " - ";
					when += (cal.get(Calendar.MONTH) + 1) + "/"
							+ cal.get(Calendar.DAY_OF_MONTH) + "/"
							+ cal.get(Calendar.YEAR);
				}
				when += " @ ";

				when += cal.get(Calendar.HOUR)
						+ (cal.get(Calendar.AM_PM) == Calendar.PM ? "pm" : "am");

				_whenTextView.setText(when);
			} else {
				_whenTextView.setText("NA");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {

			// pay.basis
			// basis/ pay : pay.basis
			// if Fixed, then use pay.fixedAmount
			// if Hourly, then use pay.fixedAmount
			// if Blended, then use payblendedAdditionalRate
			if (_workorder.has("pay.basis")) {
				String basis = _workorder.getString("pay.basis");
				_basisTextView.setText(basis);
				if (_workorder.has("pay.fixedAmount")) {
					_cashTextView.setText(misc.toCurrency(_workorder
							.getFloat("pay.fixedAmount")));
				} else if (_workorder.has("pay.blendedAdditionalRate")) {
					_cashTextView.setText(misc.toCurrency(_workorder
							.getFloat("pay.blendedAdditionalRate")));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
