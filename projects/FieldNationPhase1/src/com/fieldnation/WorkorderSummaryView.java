package com.fieldnation;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.cocosw.undobar.UndoBarController;
import com.fieldnation.data.workorder.Label;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

	private static final int BUTTON_ACTION_NONE = 0;
	private static final int BUTTON_ACTION_REQUEST = 1;
	private static final int BUTTON_ACTION_ACCEPT = 2;
	private static final int BUTTON_ACTION_VIEW_COUNTER = 3;
	private static final int BUTTON_ACTION_CONFIRM = 4;
	private static final int BUTTON_ACTION_CHECK_IN = 5;
	private static final int BUTTON_ACTION_ACKNOWLEDGE = 6;
	private static final int BUTTON_ACTION_CHECK_OUT = 7;
	private static final int BUTTON_ACTION_PAYMENTS = 8;

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
	private TextView _idTextView;
	private ViewGroup _paymentLayout;
	private LinearLayout _notInterestedLayout;

	// animations
	private Animation _slideAnimation;
	private Animation _slideBackAnimation;

	// Data
	private GlobalState _gs;
	private WorkorderDataSelector _dataView = null;
	private Workorder _workorder;
	private int _buttonAction = BUTTON_ACTION_NONE;
	private WorkorderService _dataService;
	private MyAuthClient _authClient;

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
		_notInterestedLayout = (LinearLayout) findViewById(R.id.notinterested_layout);
		_notInterestedLayout.setOnClickListener(_notInterested_onClick);
		_notInterestedLayout.setClickable(false);

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

		_idTextView = (TextView) findViewById(R.id.workorderid_textview);
		_paymentLayout = (ViewGroup) findViewById(R.id.payment_layout);

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
	private View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: _notInterested_onClick()");
		}
	};
	private View.OnClickListener _options_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_optionsLayout.setClickable(false);
			_notInterestedLayout.setClickable(false);
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
			_notInterestedLayout.setClickable(true);
			_contentLayout.startAnimation(_slideAnimation);
			return true;
		}
	};

	private View.OnClickListener _detailButton_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (_buttonAction) {
			case BUTTON_ACTION_ACCEPT:
				// TODO BUTTON_ACTION_ACCEPT
				break;
			case BUTTON_ACTION_ACKNOWLEDGE:
				break;
			case BUTTON_ACTION_CHECK_IN:
				getContext().startService(
						_dataService.checkin(BUTTON_ACTION_CHECK_IN,
								_workorder.getWorkorderId(),
								System.currentTimeMillis(), false));
				break;
			case BUTTON_ACTION_CHECK_OUT:
				getContext().startService(
						_dataService.checkout(BUTTON_ACTION_CHECK_IN,
								_workorder.getWorkorderId(),
								System.currentTimeMillis(), false));
				break;
			case BUTTON_ACTION_CONFIRM:
				// TODO BUTTON_ACTION_CONFIRM
				break;
			case BUTTON_ACTION_NONE:
				// TODO BUTTON_ACTION_NONE
				break;
			case BUTTON_ACTION_PAYMENTS:
				// TODO BUTTON_ACTION_PAYMENTS
				break;
			case BUTTON_ACTION_REQUEST:
				// TODO, set a time for expiration
				getContext().startService(
						_dataService.request(BUTTON_ACTION_REQUEST,
								_workorder.getWorkorderId(), 0, false));
				break;
			case BUTTON_ACTION_VIEW_COUNTER:
				// TODO BUTTON_ACTION_VIEW_COUNTER
				break;
			}
		}
	};

	private class MyAuthClient extends AuthenticationClient {

		public MyAuthClient(Context context) {
			super(context);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_dataService = new WorkorderService(getContext(), username,
					authToken, _resultReciever);
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			Log.v(TAG, "onAuthenticationFailed(), delayed re-request");
			_gs.requestAuthenticationDelayed(_authClient);
		}
	}

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(
			new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "WebServiceResultReceiver.onSuccess");
			switch (resultCode) {
			case BUTTON_ACTION_ACCEPT:
				break;
			case BUTTON_ACTION_ACKNOWLEDGE:
				break;
			case BUTTON_ACTION_CHECK_IN:
				break;
			case BUTTON_ACTION_CHECK_OUT:
				break;
			case BUTTON_ACTION_CONFIRM:
				break;
			case BUTTON_ACTION_NONE:
				break;
			case BUTTON_ACTION_PAYMENTS:
				break;
			case BUTTON_ACTION_REQUEST:
				break;
			case BUTTON_ACTION_VIEW_COUNTER:
				break;
			}
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");

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
		try {
			buildStatus();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (Debug.isDebuggerConnected()) {
			_idTextView.setText("[ID " + _workorder.getWorkorderId() + "]");
			_idTextView.setVisibility(VISIBLE);
		} else {
			_idTextView.setVisibility(GONE);
		}

		// title
		String title = _workorder.getTitle();
		if (title != null) {
			_titleTextView.setText(title);
		}

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

				if (address1 == null)
					address1 = address2;
				else if (address2 != null) {
					address1 += address2;
				}

				if (address1 != null) {
					_distanceTextView.setText(address1);
					_distanceTextView.setVisibility(VISIBLE);
				} else {
					_distanceTextView.setVisibility(GONE);
				}

			} else if (location.getDistance() != null) {
				_distanceTextView.setText(location.getDistance() + " mi");
			} else {
				_distanceTextView.setVisibility(GONE);
			}
		} else {
			_distanceTextView.setVisibility(GONE);
			_clientNameTextView.setVisibility(GONE);
		}
		// when scheduledTimeStart/scheduledTimeEnd
		try {
			if (_workorder.getScheduledTimeStart() != null) {
				String when = "";
				Calendar cal = null;
				cal = ISO8601.toCalendar(_workorder.getScheduledTimeStart());
				when = misc.formatDate(cal);

				if (!misc.isEmptyOrNull(_workorder.getScheduledTimeEnd())) {
					cal = ISO8601.toCalendar(_workorder.getScheduledTimeEnd());
					if (cal.get(Calendar.YEAR) > 2000) {
						when += " - ";
						when += misc.formatDate(cal);
					}
				}
				when += " @ ";

				when += (cal.get(Calendar.HOUR) + 1) + (cal.get(Calendar.AM_PM) == Calendar.PM ? "pm" : "am");

				_whenTextView.setVisibility(VISIBLE);
				_whenTextView.setText(when);
			} else {
				_whenTextView.setVisibility(GONE);
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			_whenTextView.setVisibility(GONE);
		}

		// pay.basis
		// basis/ pay : pay.basis
		// if Fixed, then use pay.fixedAmount
		// if Hourly, then use pay.fixedAmount
		// if Blended, then use payblendedAdditionalRate
		Pay pay = _workorder.getPay();
		if (pay != null) {
			if (pay.getBasis() != null) {
				String basis = pay.getBasis();
				_paymentLayout.setVisibility(VISIBLE);
				_basisTextView.setText(basis);
				if (pay.getFixedAmount() != null) {
					_cashTextView.setText(misc.toCurrency(pay.getFixedAmount()).substring(
							1));
				} else if (pay.getBlendedAdditionalRate() != null) {
					_cashTextView.setText(misc.toCurrency(
							pay.getBlendedAdditionalRate()).substring(1));
				} else {
					_paymentLayout.setVisibility(GONE);
				}
			} else {
				_paymentLayout.setVisibility(GONE);
			}
		} else {
			_paymentLayout.setVisibility(GONE);
		}

		_contentLayout.clearAnimation();
		_optionsLayout.setClickable(false);
		_notInterestedLayout.setClickable(false);
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
				_buttonAction = BUTTON_ACTION_ACKNOWLEDGE;
				_statusDisplayState = 1;
			}
		} else if (has16) {
			_statusDisplayState = 2;
			_statusTextView.setText("Unconfirmed");
			_detailButton.setText("Check In");
			_detailButton.setVisibility(VISIBLE);
			_buttonAction = BUTTON_ACTION_CHECK_IN;
		} else {
			_statusDisplayState = 0;
			_statusTextView.setText("Confirmed");
			_detailButton.setText("Check In");
			_buttonAction = BUTTON_ACTION_CHECK_IN;
			_detailButton.setVisibility(VISIBLE);
		}
	}

	private void buildStatusAvailable() throws ParseException {
		long statusId = _workorder.getWorkorderId();
		if (statusId == 9) {
			_statusDisplayState = 1;
			_statusTextView.setText("Routed");
			_detailButton.setText("Accept");
			_buttonAction = BUTTON_ACTION_ACCEPT;
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
				_buttonAction = BUTTON_ACTION_REQUEST;
			} else if (has12) {
				_statusDisplayState = 2;
				_statusTextView.setText("Requested");
				_detailButton.setVisibility(GONE);
			} else if (has13) {
				_statusDisplayState = 3;
				_statusTextView.setText("Sent Counter");
				_detailButton.setVisibility(VISIBLE);
				_detailButton.setText("View Counter");
				_buttonAction = BUTTON_ACTION_VIEW_COUNTER;
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
				_buttonAction = BUTTON_ACTION_ACKNOWLEDGE;
			}
		} else if (has1) {
			_statusDisplayState = 2;
			_statusTextView.setText("Checked In");
			_detailButton.setText("Check Out");
			_detailButton.setVisibility(VISIBLE);
			_buttonAction = BUTTON_ACTION_CHECK_OUT;
		} else {
			_statusDisplayState = 0;
			_statusTextView.setText("Checked Out");
			_detailButton.setText("Check In");
			_detailButton.setVisibility(VISIBLE);
			_buttonAction = BUTTON_ACTION_CHECK_IN;
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
		} else if (labels.contains(20)) {
			_statusDisplayState = 0;
			_statusTextView.setText("In Review");
			_detailButton.setVisibility(GONE);
		} else if (labels.contains(21)) {
			_statusDisplayState = 2;
			_statusTextView.setText("Processing");
			_detailButton.setVisibility(VISIBLE);
			_detailButton.setText("Payments");
			_buttonAction = BUTTON_ACTION_PAYMENTS;
		} else if (_workorder.getStatus() != null && _workorder.getStatus().equals(
				"Paid")) {
			_statusDisplayState = 3;
			_statusTextView.setText("Paid");
			_detailButton.setVisibility(GONE);
		}
	}
}
