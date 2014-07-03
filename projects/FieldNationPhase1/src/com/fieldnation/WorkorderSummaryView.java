package com.fieldnation;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.fieldnation.data.workorder.Label;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.json.JsonObject;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
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
	private WorkorderDataSelector _dataView = null;

	private Workorder _workorder;

	// status lookuptable
	private int _statusDisplayState = 0;
	private static final int[] _STATUS_LOOKUP_TABLE = {
			R.drawable.wosum_status_1,
			R.drawable.wosum_status_2,
			R.drawable.wosum_status_3,
			R.drawable.wosum_status_4 };
	private static final int[] _STATUS_TEXT_TABLE = {
			R.color.wosumStatusLabel1,
			R.color.wosumStatusLabel2,
			R.color.wosumStatusLabel3,
			R.color.wosumStatusLabel4 };
	private static final int[] _STATUS_BUTTON_FG = {
			R.color.wosumButton1Foreground,
			R.color.wosumButton2Foreground,
			R.color.wosumButton3Foreground,
			R.color.wosumButton1Foreground };
	private static final int[] _STATUS_BUTTON_BG = {
			R.drawable.wosum_button1_bg,
			R.drawable.wosum_button2_bg,
			R.drawable.wosum_button3_bg,
			R.drawable.wosum_button1_bg };

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
			Intent intent = new Intent(getContext(), WorkorderActivity.class);
			intent.putExtra("workorder_id", _workorder.getWorkorderId());
			getContext().startActivity(intent);
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
		}
	};

	/*-*********************************-*/
	/*-				Data				-*/
	/*-*********************************-*/
	public void setWorkorder(WorkorderDataSelector workorderDataSelector,
			Workorder workorder) {
		_workorder = workorder;
		_dataView = workorderDataSelector;
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
		// title
		if (_workorder.getTitle() != null)
			_titleTextView.setText(_workorder.getTitle() + _workorder.getWorkorderId());
		else
			_titleTextView.setText(_workorder.getWorkorderId() + "");
		// client name location.contact_name
		Location location = _workorder.getLocation();
		if (location != null) {
			if (location.getContactName() != null) {
				String t = location.getContactName();
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
			// distance/address? location.state, location.zip, location.city,
			// location.country,
			if (location.getAddress1() != null || location.getAddress2() != null) {
				String address1 = null;
				String address2 = null;

				if (location.getAddress1() != null)
					address1 = location.getAddress1();
				if (location.getAddress2() != null)
					address2 = location.getAddress2();

				if (misc.isEmptyOrNull(address1))
					address1 = null;
				if (misc.isEmptyOrNull(address2))
					address2 = null;

			} else if (location.getDistance() != null) {
				_distanceTextView.setText(location.getDistance() + " mi");
			} else {
				_distanceTextView.setText("NA");
			}
		}
		// when scheduledTimeStart/scheduledTimeEnd
		try {
			if (_workorder.getScheduledTimeStart() != null) {
				String when = "";
				Calendar cal = ISO8601.toCalendar(_workorder.getScheduledTimeStart());

				when = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);

				if (!misc.isEmptyOrNull(_workorder.getScheduledTimeEnd())) {
					cal = ISO8601.toCalendar(_workorder.getScheduledTimeEnd());

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
			_whenTextView.setText("NA");
		}

		// pay.basis
		// basis/ pay : pay.basis
		// if Fixed, then use pay.fixedAmount
		// if Hourly, then use pay.fixedAmount
		// if Blended, then use payblendedAdditionalRate
		Pay pay = _workorder.getPay();
		if (pay.getBasis() != null) {
			String basis = pay.getBasis();
			_basisTextView.setText(basis);
			if (pay.getFixedAmount() != null) {
				_cashTextView.setText(misc.toCurrency(pay.getFixedAmount()));
			} else if (pay.getBlendedAdditionalRate() != null) {
				_cashTextView.setText(misc.toCurrency(pay.getBlendedAdditionalRate()));
			}
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
		if (_workorder.getStatus() != null) {
			_statusTextView.setText(_workorder.getStatus());
		} else {
			_statusTextView.setText("");
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
		updateStatusUi();
	}

	private void buildStatusAssigned() throws ParseException {
		// if on-hold, then only check for acknowledged
		boolean isOnHold = false;
		boolean isAcked = false;
		boolean has16 = false;

		// get on-hold value
		Label[] labels = _workorder.getLabels();

		for (int i = 0; i < labels.length; i++) {
			Label label = labels[i];
			if (label.getType() != null) {
				if (label.getType().equals("on-hold"))
					isOnHold = true;

				if (label.getAction() == null || label.getAction().equals(
						"acknowledge")) {
					isAcked = true;
				}
			}

			if (label.getLabelId() == 16) {
				has16 = true;
			}
		}

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
	}

	private void buildStatusAvailable() throws ParseException {
		long statusId = _workorder.getWorkorderId();
		if (statusId == 9) {
			_statusDisplayState = 1;
			_statusTextView.setText("Routed");
			_detailButton.setText("Accept");
			_detailButton.setVisibility(VISIBLE);
		} else {
			boolean has11 = false;
			boolean has12 = false;
			boolean has13 = false;

			Label[] labels = _workorder.getLabels();

			for (int i = 0; i < labels.length; i++) {
				Label label = labels[i];
				if (label.getLabelId() == 11) {
					has11 = true;
				}
				if (label.getLabelId() == 12) {
					has12 = true;
				}
				if (label.getLabelId() == 12) {
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
		}
	}

	private void buildStatusInProgress() throws ParseException {
		// if on-hold, then only check for acknowledged
		boolean isOnHold = false;
		boolean isAcked = false;
		boolean has1 = false;

		// get on-hold value
		Label[] labels = _workorder.getLabels();

		for (int i = 0; i < labels.length; i++) {
			Label label = labels[i];
			if (label.getType() != null) {
				if (label.getType().equals("on-hold"))
					isOnHold = true;

				if (label.getAction() == null || label.getAction().equals(
						"acknowledge")) {
					isAcked = true;
				}
			}

			if (label.getLabelId() == 1) {
				has1 = true;
			}
		}

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
	}

	private void buildStatusCompleted() throws ParseException {
		Set<Integer> labels = new HashSet<Integer>();

		Label[] slabels = _workorder.getLabels();
		for (int i = 0; i < slabels.length; i++) {
			int labelid = slabels[i].getLabelId();
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
		} else if (_workorder.getStatus() != null && _workorder.getStatus().equals(
				"Paid")) {
			_statusDisplayState = 3;
			_statusTextView.setText("Paid");
			_detailButton.setVisibility(GONE);
		}
	}
}
