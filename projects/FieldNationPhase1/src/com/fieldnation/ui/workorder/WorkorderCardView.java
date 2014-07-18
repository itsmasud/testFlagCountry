package com.fieldnation.ui.workorder;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import com.cocosw.undobar.UndoBarController;
import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.workorder.Label;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.rpc.client.WorkorderService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.utils.misc;

import android.app.Activity;
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
import android.widget.Toast;

/**
 * Displays the summary of a workorder to the user. Will also allow some simple
 * actions.
 * 
 * @author michael.carver
 * 
 */
public class WorkorderCardView extends RelativeLayout {
	static final String TAG = "ui.workorder.WorkorderCardView";

	public static final int BUTTON_ACTION_NONE = 0;
	public static final int BUTTON_ACTION_REQUEST = 1;
	public static final int BUTTON_ACTION_ASSIGNMENT = 2;
	public static final int BUTTON_ACTION_CHECKIN = 3;
	public static final int BUTTON_ACTION_CHECKOUT = 4;

	public static final int NOT_INTERESTED_ACTION_NONE = 0;
	public static final int NOT_INTERESTED_ACTION_DECLINE = 101;
	public static final int NOT_INTERESTED_ACTION_WITHDRAW_REQUEST = 102;
	public static final int NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT = 103;

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
	private ImageView _backImageView;
	private TextView _locationTextView;
	private ImageView _messageAlertImageView;
	private ImageView _notificationAlertImageView;

	// animations
	private Animation _slideAnimation;
	private Animation _slideBackAnimation;

	// Data
	private GlobalState _gs;
	private WorkorderDataSelector _dataView = null;
	private Workorder _workorder;
	private int _buttonAction = 0;
	int _notInterestedAction = 0;
	WorkorderService _dataService;
	private MyAuthClient _authClient;
	private int _statusDisplayState = 0;
	private Listener _listener = null;
	private String _username;
	private String _authToken;

	// status colors lookuptable
	private static final int[] _STATUS_LOOKUP_TABLE = {
			R.drawable.wo_card_status_white,
			R.drawable.wo_card_status_orange,
			R.drawable.wo_card_status_green,
			R.drawable.wo_card_status_gray };
	private static final int[] _STATUS_TEXT_TABLE = {
			R.color.woCardStatusLabel1,
			R.color.woCardStatusLabel2,
			R.color.woCardStatusLabel3,
			R.color.woCardStatusLabel4 };
	private static final int[] _STATUS_BUTTON_FG = {
			R.color.woCardButton1Foreground,
			R.color.woCardButton2Foreground,
			R.color.woCardButton3Foreground,
			R.color.woCardButton1Foreground };
	private static final int[] _STATUS_BUTTON_BG = {
			R.drawable.btn_white,
			R.drawable.btn_orange,
			R.drawable.btn_green,
			R.drawable.btn_white };

	public WorkorderCardView(Context context) {
		this(context, null, -1);
	}

	public WorkorderCardView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public WorkorderCardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_card, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) getContext().getApplicationContext();
		_authClient = new MyAuthClient(context);
		_gs.requestAuthentication(_authClient);

		// connect UI components
		_contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		_optionsLayout = (RelativeLayout) findViewById(R.id.options_layout);
		_backImageView = (ImageView) findViewById(R.id.back_imageview);
		_backImageView.setOnClickListener(_back_onClick);
		_backImageView.setClickable(false);
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
		_messageAlertImageView = (ImageView) findViewById(R.id.message_alert_imageview);
		_notificationAlertImageView = (ImageView) findViewById(R.id.notification_alert_imageview);

		_detailButton = (Button) findViewById(R.id.detail_button);
		_detailButton.setOnClickListener(_detailButton_onClick);

		_cashTextView = (TextView) findViewById(R.id.payment_textview);
		_basisTextView = (TextView) findViewById(R.id.basis_textview);

		_idTextView = (TextView) findViewById(R.id.workorderid_textview);
		_paymentLayout = (ViewGroup) findViewById(R.id.payment_layout);

		_locationTextView = (TextView) findViewById(R.id.location_textview);

		// _detailButton.setVisibility(GONE);
		// _cashLinearLayout.setVisibility(GONE);

		setOnLongClickListener(_this_onLongClickListener);
		setOnClickListener(_this_onClick);

		_slideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.wocard_slide_away);

		_slideBackAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.wocard_slide_back);

		setIsBundle(false);

		showMessageAlertIcon(true);
		showAlertIcon(true);
	}

	/*-*********************************-*/
	/*-	set message/notification icon	-*/
	/*-*********************************-*/
	public void showMessageAlertIcon(boolean enabled) {
		if (enabled) {
			_messageAlertImageView.setVisibility(VISIBLE);
		} else {
			_messageAlertImageView.setVisibility(GONE);
		}
	}

	public void showAlertIcon(boolean enabled) {
		if (enabled) {
			_notificationAlertImageView.setVisibility(VISIBLE);
		} else {
			_notificationAlertImageView.setVisibility(GONE);
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	private View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_dataService == null) {
				Toast.makeText(getContext(), getContext().getString(R.string.network_not_ready), Toast.LENGTH_LONG).show();
				return;
			}

			if (_listener != null) {
				// TODO trigger animation before removing
				_listener.startRemove(_workorder);
			}

			UndoBarController.show((Activity) getContext(), R.string.tap_to_undo,
					new WorkorderSummaryAdvancedUndoListener(_workorder, getContext(), _username, _authToken,
							_listener, _notInterestedAction));
		}
	};

	private View.OnClickListener _back_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_backImageView.setClickable(false);
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
			_backImageView.setClickable(true);
			_notInterestedLayout.setClickable(true);
			_contentLayout.startAnimation(_slideAnimation);
			return true;
		}
	};

	private View.OnClickListener _detailButton_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (_dataService == null) {
				Toast.makeText(getContext(), R.string.network_not_ready, Toast.LENGTH_LONG).show();
				return;
			}

			switch (_buttonAction) {
			case BUTTON_ACTION_CHECKIN:
				getContext().startService(
						_dataService.checkin(BUTTON_ACTION_CHECKIN, _workorder.getWorkorderId(),
								System.currentTimeMillis()));
				break;
			case BUTTON_ACTION_CHECKOUT:
				getContext().startService(
						_dataService.checkout(BUTTON_ACTION_CHECKOUT, _workorder.getWorkorderId(),
								System.currentTimeMillis()));
				break;
			case BUTTON_ACTION_ASSIGNMENT:
				// TODO, set times
				getContext().startService(
						_dataService.confirmAssignment(BUTTON_ACTION_ASSIGNMENT, _workorder.getWorkorderId(), 0, 0));
				break;
			case BUTTON_ACTION_REQUEST:
				// TODO, set a time for expiration
				getContext().startService(_dataService.request(BUTTON_ACTION_REQUEST, _workorder.getWorkorderId(), 600));
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
			_authToken = authToken;
			_username = username;
			_dataService = new WorkorderService(getContext(), _username, _authToken, _resultReciever);
			Log.v(TAG, "got data service!");
		}

		@Override
		public void onAuthenticationFailed(Exception ex) {
			Log.v(TAG, "onAuthenticationFailed(), delayed re-request");
			_gs.requestAuthenticationDelayed(_authClient);
		}
	}

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {

		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			Log.v(TAG, "WebServiceResultReceiver.onSuccess");

			Log.v(TAG, resultData.toString());
			Log.v(TAG, new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));
			switch (resultCode) {
			case BUTTON_ACTION_ASSIGNMENT:
				// TODO BUTTON_ACTION_ASSIGNMENT
				break;
			case BUTTON_ACTION_CHECKIN:
				// TODO BUTTON_ACTION_CHECKIN
				break;
			case BUTTON_ACTION_CHECKOUT:
				// TODO BUTTON_ACTION_CHECKOUT
				break;
			case BUTTON_ACTION_REQUEST:
				// TODO BUTTON_ACTION_REQUEST
				break;
			case NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT:
				break;
			case NOT_INTERESTED_ACTION_DECLINE:
				break;
			case NOT_INTERESTED_ACTION_WITHDRAW_REQUEST:
				break;
			}
			if (_listener != null)
				_listener.notifyDataSetChanged();
			Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG).show();

		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			Log.v(TAG, errorType);
			Log.v(TAG, resultData.toString());
			Toast.makeText(getContext(), R.string.try_again, Toast.LENGTH_LONG).show();
		}
	};

	/*-*********************************-*/
	/*-				Data				-*/
	/*-*********************************-*/
	public void setWorkorder(WorkorderDataSelector workorderDataSelector, Workorder workorder) {
		_workorder = workorder;
		_dataView = workorderDataSelector;
		refresh();
	}

	public Workorder getWorkorder() {
		return _workorder;
	}

	/*-*****************************************-*/
	/*-				State Change				-*/
	/*-*****************************************-*/
	public void setIsBundle(boolean isBundle) {
		if (isBundle) {
			_bundleLayout.setVisibility(VISIBLE);
			_titleTextView.setVisibility(GONE);
			_basisTextView.setVisibility(GONE);
			_cashTextView.setVisibility(GONE);
			_paymentLayout.setVisibility(GONE);
			_bundleSeparator.setVisibility(VISIBLE);
		} else {
			_bundleLayout.setVisibility(GONE);
			_bundleSeparator.setVisibility(GONE);
			_titleTextView.setVisibility(VISIBLE);
			_basisTextView.setVisibility(VISIBLE);
			_cashTextView.setVisibility(VISIBLE);
			_paymentLayout.setVisibility(VISIBLE);
		}
	}

	public void setNotInterestedEnabled(boolean enabled) {
		this.setLongClickable(enabled);
	}

	public void setWorkorderSummaryListener(Listener listener) {
		_listener = listener;
	}

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void updateStatusUiColors() {
		_statusLayout.setBackgroundResource(_STATUS_LOOKUP_TABLE[_statusDisplayState]);
		_statusTextView.setTextColor(getContext().getResources().getColor(_STATUS_TEXT_TABLE[_statusDisplayState]));
		_detailButton.setBackgroundResource(_STATUS_BUTTON_BG[_statusDisplayState]);
		_detailButton.setTextColor(getContext().getResources().getColor(_STATUS_BUTTON_FG[_statusDisplayState]));

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
					_locationTextView.setText(address1);
				} else {
					_locationTextView.setVisibility(GONE);
				}
			}

			if (location.getDistance() != null) {
				_distanceTextView.setText(location.getDistance() + " mi");
			} else if (_workorder.getDistance() != null) {
				_distanceTextView.setText(_workorder.getDistance() + " mi");
			} else {
				_distanceTextView.setVisibility(GONE);
			}
		} else {
			_distanceTextView.setVisibility(GONE);
			_clientNameTextView.setVisibility(GONE);
		}
		// when scheduledTimeStart/scheduledTimeEnd

		if (_workorder.getSchedule() != null) {
			String when = _workorder.getSchedule().getFormatedTime();

			if (when == null) {
				_whenTextView.setVisibility(GONE);
			} else {
				_whenTextView.setText(when);
			}
		}

		// pay.basis
		// basis/ pay : pay.basis
		// if Fixed, then use pay.fixedAmount
		// if Hourly, then use pay.fixedAmount
		// if Blended, then use payblendedAdditionalRate
		Pay pay = _workorder.getPay();
		if (pay != null) {
			String desc = pay.toDisplayStringShort();
			_basisTextView.setText(pay.getBasis());
			if (desc != null) {
				_cashTextView.setText(desc.substring(1));
			} else {
				_paymentLayout.setVisibility(GONE);
			}
		} else {
			_paymentLayout.setVisibility(GONE);
		}

		_contentLayout.clearAnimation();
		_backImageView.setClickable(false);
		_notInterestedLayout.setClickable(false);
	}

	private void buildStatus() throws ParseException {
		if (_workorder.getStatus() != null) {
			_statusTextView.setText(_workorder.getStatus());
		} else {
			_statusTextView.setText("");
		}

		setNotInterestedEnabled(false);
		_notInterestedAction = NOT_INTERESTED_ACTION_NONE;
		_buttonAction = BUTTON_ACTION_NONE;
		_titleTextView.setVisibility(GONE);
		_whenTextView.setVisibility(GONE);
		_clientNameTextView.setVisibility(GONE);
		_distanceTextView.setVisibility(GONE);
		_paymentLayout.setVisibility(GONE);
		_detailButton.setVisibility(GONE);
		_locationTextView.setVisibility(GONE);

		switch (_dataView) {
		case ASSIGNED:
			buildStatusAssigned();
			break;
		case AVAILABLE:
			buildStatusAvailable();
			break;
		case CANCELLED:
			buildStatusCancelled();
			break;
		case COMPLETED:
			buildStatusCompleted();
			break;
		case IN_PROGRESS:
			buildStatusInProgress();
			break;
		}
		updateStatusUiColors();

		// _titleTextView.setVisibility(VISIBLE);
		// _clientNameTextView.setVisibility(VISIBLE);
		// _distanceTextView.setVisibility(VISIBLE);
		// _locationTextView.setVisibility(VISIBLE);
		// _whenTextView.setVisibility(VISIBLE);
		// _paymentLayout.setVisibility(VISIBLE);
		// _detailButton.setVisibility(VISIBLE);
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

				if (label.getAction() == null || label.getAction().equals("acknowledge")) {
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
				_titleTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				_clientNameTextView.setVisibility(VISIBLE);
				_locationTextView.setVisibility(VISIBLE);
			} else {
				_detailButton.setText("Acknowledge");
				// TODO BUTTON_ACTION_ACKNOWLEDGE
				// _buttonAction = BUTTON_ACTION_ACKNOWLEDGE;
				_statusDisplayState = 1;

				_titleTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				_clientNameTextView.setVisibility(VISIBLE);
				_locationTextView.setVisibility(VISIBLE);
				_detailButton.setVisibility(VISIBLE);
			}
		} else if (has16) {
			_statusDisplayState = 2;
			_statusTextView.setText("Unconfirmed");
			_detailButton.setText("Confirm");
			_buttonAction = BUTTON_ACTION_ASSIGNMENT;
			setNotInterestedEnabled(true);
			_notInterestedAction = NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT;

			_titleTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_detailButton.setVisibility(VISIBLE);

		} else {
			_statusDisplayState = 0;
			_statusTextView.setText("Confirmed");
			_detailButton.setText("Check In");
			_buttonAction = BUTTON_ACTION_CHECKIN;

			_titleTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_detailButton.setVisibility(VISIBLE);

		}
	}

	private void buildStatusAvailable() throws ParseException {
		long statusId = _workorder.getStatusId();
		setNotInterestedEnabled(true);
		if (statusId == 9) {
			_statusDisplayState = 1;
			_statusTextView.setText("Routed");
			_detailButton.setText("Accept");
			_buttonAction = BUTTON_ACTION_ASSIGNMENT;
			_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;

			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_distanceTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_paymentLayout.setVisibility(VISIBLE);
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
				if (label.getLabelId() == 13) {
					has13 = true;
				}
			}

			if (has11) {
				_statusDisplayState = 0;
				_statusTextView.setText("Available");
				_detailButton.setText("Request");
				_buttonAction = BUTTON_ACTION_REQUEST;
				_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;

				_titleTextView.setVisibility(VISIBLE);
				_clientNameTextView.setVisibility(VISIBLE);
				_distanceTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				_paymentLayout.setVisibility(VISIBLE);
				_detailButton.setVisibility(VISIBLE);

			} else if (has12) {
				_statusDisplayState = 2;
				_statusTextView.setText("Requested");
				_notInterestedAction = NOT_INTERESTED_ACTION_WITHDRAW_REQUEST;

				_titleTextView.setVisibility(VISIBLE);
				_distanceTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				_paymentLayout.setVisibility(VISIBLE);
			} else if (has13) {
				_statusDisplayState = 3;
				_statusTextView.setText("Sent Counter");
				_detailButton.setVisibility(VISIBLE);
				_detailButton.setText("View Counter");
				// TODO BUTTON_ACTION_VIEW_COUNTER!?
				// _buttonAction = BUTTON_ACTION_VIEW_COUNTER;
				_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;

				_titleTextView.setVisibility(VISIBLE);
				_distanceTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				_paymentLayout.setVisibility(VISIBLE);
				_detailButton.setVisibility(VISIBLE);
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

				if (label.getAction() == null || label.getAction().equals("acknowledge")) {
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
				_titleTextView.setVisibility(VISIBLE);
				_locationTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				// TODO show 'task' ui?

			} else {
				_statusDisplayState = 1;
				_detailButton.setText("Acknowledge");
				// TODO BUTTON_ACTION_ACKNOWLEDGE!?!?!
				// _buttonAction = BUTTON_ACTION_ACKNOWLEDGE;
				_titleTextView.setVisibility(VISIBLE);
				_locationTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				_detailButton.setVisibility(VISIBLE);
				// TODO show 'task' ui?
			}
		} else if (has1) {
			_statusDisplayState = 2;
			_statusTextView.setText("Checked In");
			_detailButton.setText("Check Out");
			_buttonAction = BUTTON_ACTION_CHECKOUT;

			_titleTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_detailButton.setVisibility(VISIBLE);
			// TODO show 'task' ui?

		} else {
			_statusDisplayState = 0;
			_statusTextView.setText("Checked Out");
			_detailButton.setText("Check In");
			_buttonAction = BUTTON_ACTION_CHECKIN;

			_titleTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_detailButton.setVisibility(VISIBLE);
			// TODO show 'task' ui?

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

			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_paymentLayout.setVisibility(VISIBLE);

		} else if (labels.contains(20)) {
			_statusDisplayState = 0;
			_statusTextView.setText("In Review");

			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_paymentLayout.setVisibility(VISIBLE);
		} else if (labels.contains(21)) {
			_statusDisplayState = 2;
			_statusTextView.setText("Processing");
			_detailButton.setText("Payments");
			// TODO BUTTON_ACTION_PAYMENTS!?!?!
			// _buttonAction = BUTTON_ACTION_PAYMENTS;

			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_paymentLayout.setVisibility(VISIBLE);
			_detailButton.setVisibility(VISIBLE);

		} else if (_workorder.getStatus() != null && _workorder.getStatus().equals("Paid")) {
			_statusDisplayState = 3;
			_statusTextView.setText("Paid");

			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_paymentLayout.setVisibility(VISIBLE);
		}
	}

	private void buildStatusCancelled() {
		// TODO METHOD STUB buildStatusCancelled!
	}

	public interface Listener {
		public void startRemove(Workorder wo);

		public void cancelRemove(Workorder wo);

		public void finishRemove(Workorder wo);

		public void notifyDataSetChanged();
	}
}
