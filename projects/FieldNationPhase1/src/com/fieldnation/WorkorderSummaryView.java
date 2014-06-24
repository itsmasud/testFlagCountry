package com.fieldnation;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Displays the summary of a workorder to the user. Will also allow some simple
 * actions.
 * 
 * @author michael.carver
 * 
 */
public class WorkorderSummaryView extends RelativeLayout {
	private static final String TAG = "WorkorderSummaryView";

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

	// animations
	private Animation _slideAnimation;
	private Animation _slideBackAnimation;

	// Data
	private GlobalState _gs;
	private DataSelector _dataView = null;

	private JsonObject _workorder;

	// status lookuptable
	private int _statusDisplayState = 0;
	private static final int[] _STATUS_LOOKUP_TABLE = { R.drawable.wosum_status_1, R.drawable.wosum_status_2, R.drawable.wosum_status_3, R.drawable.wosum_status_4 };
	private static final int[] _STATUS_TEXT_TABLE = { R.color.wosumStatusLabel1, R.color.wosumStatusLabel2, R.color.wosumStatusLabel3, R.color.wosumStatusLabel4 };
	private static final int[] _STATUS_BUTTON_FG = { R.color.wosumButton1Foreground, R.color.wosumButton2Foreground, R.color.wosumButton3Foreground, R.color.wosumButton1Foreground };
	private static final int[] _STATUS_BUTTON_BG = { R.drawable.wosum_button1_bg, R.drawable.wosum_button2_bg, R.drawable.wosum_button3_bg, R.drawable.wosum_button1_bg };

	public WorkorderSummaryView(Context context) {
		this(context, null, -1);
	}

	public WorkorderSummaryView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public WorkorderSummaryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_workorder_summary, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) getContext().getApplicationContext();

		_contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		_optionsLayout = (RelativeLayout) findViewById(R.id.options_layout);
		_optionsLayout.setOnClickListener(_options_onClick);
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
		_whenTextView = (TextView) findViewById(R.id.when_textview);

		_detailButton = (Button) findViewById(R.id.detail_button);
		_detailButton.setOnClickListener(_detailButton_onClick);

		_cashTextView = (TextView) findViewById(R.id.payment_textview);
		_basisTextView = (TextView) findViewById(R.id.basis_textview);

		// _detailButton.setVisibility(GONE);
		// _cashLinearLayout.setVisibility(GONE);

		setOnLongClickListener(_this_onLongClickListener);
		setOnClickListener(_this_onClick);

		_slideAnimation = AnimationUtils.loadAnimation(getContext(),
				R.anim.wosum_slide_away);

		_slideBackAnimation = AnimationUtils.loadAnimation(getContext(),
				R.anim.wosum_slide_back);

		setIsBundle(false);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _options_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_optionsLayout.setClickable(false);
			_contentLayout.startAnimation(_slideBackAnimation);
		}
	};

	private View.OnClickListener _this_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				Intent intent = new Intent(getContext(),
						WorkorderActivity.class);
				intent.putExtra("workorder_id",
						_workorder.getLong("workorder_id"));
				// TODO package up the workorder... or send the id
				getContext().startActivity(intent);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
	private View.OnLongClickListener _this_onLongClickListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			_optionsLayout.setClickable(true);
			_contentLayout.startAnimation(_slideAnimation);
			return true;
		}
	};

	private View.OnClickListener _detailButton_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			_statusDisplayState = (_statusDisplayState + 1) % 4;

			updateStatusUi();

			// TODO Method Stub: onClick()
			System.out.println("Method Stub: onClick()");

		}
	};

	/*-*********************************-*/
	/*-				Data				-*/
	/*-*********************************-*/
	public void setWorkorder(DataSelector dataSelector, JsonObject workorder) {
		_workorder = workorder;
		_dataView = dataSelector;
		refresh();
	}

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void updateStatusUi() {
		_statusLayout.setBackgroundResource(_STATUS_LOOKUP_TABLE[_statusDisplayState]);
		_statusTextView.setTextColor(getContext().getResources().getColor(
				_STATUS_TEXT_TABLE[_statusDisplayState]));
		_detailButton.setBackgroundResource(_STATUS_BUTTON_BG[_statusDisplayState]);
		_detailButton.setTextColor(getContext().getResources().getColor(
				_STATUS_BUTTON_FG[_statusDisplayState]));

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

	private void refresh() {
		try {
			// title
			_titleTextView.setText(_workorder.getString("title") + _workorder.getInt("workorder_id"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			// client name location.contact_name
			if (_workorder.has("location.contact_name")) {
				String t = _workorder.getString("location.contact_name");
				if (t == null) {
					_clientNameTextView.setVisibility(GONE);
				} else if (t.trim().equals("")) {
					_clientNameTextView.setVisibility(GONE);
				} else {
					_clientNameTextView.setVisibility(VISIBLE);
					_clientNameTextView.setText(t);
				}
			} else {
				_clientNameTextView.setVisibility(GONE);
			}
		} catch (Exception ex) {
			_clientNameTextView.setVisibility(GONE);
			ex.printStackTrace();
		}
		try {

			// distance/address? location.state, location.zip, location.city,
			// location.country,
			if (_workorder.has("location.address1") || _workorder.has("location.address2")) {
				String address1 = null;
				String address2 = null;

				if (_workorder.has("location.address1"))
					address1 = _workorder.getString("location.address1");
				if (_workorder.has("location.address2"))
					address2 = _workorder.getString("location.address2");

				if (misc.isEmptyOrNull(address1))
					address1 = null;
				if (misc.isEmptyOrNull(address2))
					address2 = null;

			} else if (_workorder.has("location.distance")) {
				_distanceTextView.setText(_workorder.getString("location.distance") + " mi");
			} else {
				_distanceTextView.setText("NA");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {

			// when scheduledTimeStart/scheduledTimeEnd
			if (_workorder.has("scheduledTimeStart") && _workorder.has("scheduledTimeEnd")) {
				long scheduledTimeStart = _workorder.getLong("scheduledTimeStart") * 1000;
				long scheduledTimeEnd = _workorder.getLong("scheduledTimeEnd") * 1000;
				String when = "";
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(scheduledTimeStart);

				when = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);

				if (scheduledTimeEnd > 0) {
					cal.setTimeInMillis(scheduledTimeEnd);

					when += " - ";
					when += (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
				}
				when += " @ ";

				when += cal.get(Calendar.HOUR) + (cal.get(Calendar.AM_PM) == Calendar.PM ? "pm" : "am");

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
					_cashTextView.setText(misc.toCurrency(_workorder.getFloat("pay.fixedAmount")));
				} else if (_workorder.has("pay.blendedAdditionalRate")) {
					_cashTextView.setText(misc.toCurrency(_workorder.getFloat("pay.blendedAdditionalRate")));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			buildStatus();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		_contentLayout.clearAnimation();
		_optionsLayout.setClickable(false);
	}

	private void buildStatus() throws ParseException {
		try {
			if (_workorder.has("status")) {
				String status = _workorder.getString("status");
				_statusTextView.setText(status);
			} else {
				_statusTextView.setText("");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		switch (_dataView) {
		case ASSIGNED:
			buildStatusAssigned();
			break;
		case AVAILABLE:
			buildStatusAvailable();
			break;
		case CANCELLED:
			// TODO setup status indicators for cancelled
			break;
		case COMPLETED:
			buildStatusCompleted();
			break;
		case IN_PROGRESS:
			buildStatusInProgress();
			break;
		}
	}

	private void buildStatusAssigned() throws ParseException {
		// if on-hold, then only check for acknowledged
		boolean isOnHold = false;
		boolean isAcked = false;
		boolean has16 = false;

		// get on-hold value
		JsonArray jsonLabels = _workorder.getJsonArray("label");

		for (int i = 0; i < jsonLabels.size(); i++) {
			JsonObject label = jsonLabels.getJsonObject(i);
			if (label.has("type")) {
				if (label.getString("type").equals("on-hold"))
					isOnHold = true;

				if (!label.has("action") || !label.getString("action").equals(
						"acknowledge")) {
					isAcked = true;
				}
			}

			if (label.getInt("label_id") == 16) {
				has16 = true;
			}
		}

		// TODO need to change the button's fg/bg
		if (isOnHold) {
			_statusTextView.setText("On Hold");
			if (isAcked) {
				_statusDisplayState = 3;
				_detailButton.setVisibility(GONE);
			} else {
				_detailButton.setText("Acknowledge");
				_detailButton.setVisibility(VISIBLE);
				_statusDisplayState = 1;
			}
		} else if (has16) {
			_statusDisplayState = 2;
			_statusTextView.setText("Unconfirmed");
			_detailButton.setText("Check In");
			_detailButton.setVisibility(VISIBLE);
		} else {
			_statusDisplayState = 0;
			_statusTextView.setText("Confirmed");
			_detailButton.setText("Check In");
			_detailButton.setVisibility(VISIBLE);
		}
		updateStatusUi();
	}

	private void buildStatusAvailable() throws ParseException {
		int statusId = _workorder.getInt("workorder_id");
		if (statusId == 9) {
			_statusDisplayState = 1;
			_statusTextView.setText("Routed");
			_detailButton.setText("Accept");
			_detailButton.setVisibility(VISIBLE);
		} else {
			boolean has11 = false;
			boolean has12 = false;
			boolean has13 = false;

			JsonArray jsonLabels = _workorder.getJsonArray("label");

			for (int i = 0; i < jsonLabels.size(); i++) {
				JsonObject label = jsonLabels.getJsonObject(i);
				if (label.getInt("label_id") == 11) {
					has11 = true;
				}
				if (label.getInt("label_id") == 12) {
					has12 = true;
				}
				if (label.getInt("label_id") == 12) {
					has12 = true;
				}
			}

			if (has11) {
				_statusDisplayState = 0;
				_statusTextView.setText("Available");
				_detailButton.setText("Request");
				_detailButton.setVisibility(VISIBLE);
			} else if (has12) {
				_statusDisplayState = 2;
				_statusTextView.setText("Requested");
				_detailButton.setVisibility(GONE);
			} else if (has13) {
				_statusDisplayState = 3;
				_statusTextView.setText("Sent Counter");
				_detailButton.setVisibility(VISIBLE);
				_detailButton.setText("View Counter");
			}

			updateStatusUi();
		}
	}

	private void buildStatusInProgress() throws ParseException {
		// if on-hold, then only check for acknowledged
		boolean isOnHold = false;
		boolean isAcked = false;
		boolean has1 = false;

		// get on-hold value
		JsonArray jsonLabels = _workorder.getJsonArray("label");

		for (int i = 0; i < jsonLabels.size(); i++) {
			JsonObject label = jsonLabels.getJsonObject(i);
			if (label.has("type")) {
				if (label.getString("type").equals("on-hold"))
					isOnHold = true;

				if (!label.has("action") || !label.getString("action").equals(
						"acknowledge")) {
					isAcked = true;
				}
			}

			if (label.getInt("label_id") == 1) {
				has1 = true;
			}
		}

		// TODO need to change the button's fg/bg
		if (isOnHold) {
			_statusTextView.setText("On Hold");
			if (isAcked) {
				_statusDisplayState = 3;
				_detailButton.setVisibility(GONE);
			} else {
				_statusDisplayState = 1;
				_detailButton.setText("Acknowledge");
				_detailButton.setVisibility(VISIBLE);
			}
		} else if (has1) {
			_statusDisplayState = 2;
			_statusTextView.setText("Checked In");
			_detailButton.setText("Check Out");
			_detailButton.setVisibility(VISIBLE);
		} else {
			_statusDisplayState = 0;
			_statusTextView.setText("Checked Out");
			_detailButton.setText("Check In");
			_detailButton.setVisibility(VISIBLE);
		}
		updateStatusUi();
	}

	private void buildStatusCompleted() throws ParseException {
		Set<Integer> labels = new HashSet<Integer>();

		JsonArray jsonlabels = _workorder.getJsonArray("label");
		for (int i = 0; i < jsonlabels.size(); i++) {
			int labelid = jsonlabels.getJsonObject(i).getInt("label_id");
			labels.add(labelid);
		}

		if (labels.contains(19)) {
			_statusDisplayState = 0;
			_statusTextView.setText("Pending");
			_detailButton.setVisibility(GONE);
			_detailButton.setText("Payments");
		} else if (labels.contains(20)) {
			_statusDisplayState = 0;
			_statusTextView.setText("In Review");
			_detailButton.setVisibility(GONE);
		} else if (labels.contains(21)) {
			_statusDisplayState = 2;
			_statusTextView.setText("Processing");
			_detailButton.setVisibility(VISIBLE);
			_detailButton.setText("Payments");
		} else if (_workorder.has("status") && _workorder.getString("status").equals(
				"Paid")) {
			_statusDisplayState = 3;
			_statusTextView.setText("Paid");
			_detailButton.setVisibility(GONE);
		}
		updateStatusUi();
	}
}
