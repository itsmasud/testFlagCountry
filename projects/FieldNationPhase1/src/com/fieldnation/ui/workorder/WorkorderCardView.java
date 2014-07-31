package com.fieldnation.ui.workorder;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Label;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.content.Intent;
import android.os.Debug;
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
public class WorkorderCardView extends RelativeLayout {
	private static final String TAG = "ui.workorder.WorkorderCardView";

	public static final int MODE_NORMAL = 1;
	public static final int MODE_UNDO_NOT_INTERESTED = 2;
	public static final int MODE_DOING_WORK = 3;

	// UI
	// not interested
	/*-private RelativeLayout _notInterestedLayout;-*/
	private ImageView _backImageView;
	private LinearLayout _notInterestedButtonLayout;

	// background
	private RelativeLayout _backgroundLayout;

	// main content
	private RelativeLayout _contentLayout;
	// status
	private View _statusView;
	private TextView _statusTextView;
	// bundle
	private RelativeLayout _bundleLayout;
	/*-private ImageView _bundleImageView;-*/
	/*-private TextView _bundleTextView;-*/

	// center panel
	// title
	private ImageView _messageAlertImageView;
	private ImageView _notificationAlertImageView;
	private TextView _titleTextView;
	// items
	private TextView _clientNameTextView;
	private TextView _distanceTextView;
	private TextView _locationTextView;
	private TextView _whenTextView;
	private TextView _workorderIdTextView;

	// right panel
	private LinearLayout _rightLayout;
	private TextView _paymentTextView;
	private TextView _basisTextView;
	private Button _actionButton;

	// loading layout
	private RelativeLayout _loadingLayout;

	// undo layout
	private RelativeLayout _undoLayout;
	private Button _undoButton;

	// animations
	private Animation _slideAnimation;
	private Animation _slideBackAnimation;

	// Data
	private WorkorderDataSelector _dataView = null;
	private Workorder _workorder;
	private Listener _listener = null;
	private int _displayMode = MODE_NORMAL;

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

		// connect UI components
		// not interested
		/*-_notInterestedLayout = (RelativeLayout) findViewById(R.id.notinterested_layout);-*/
		_backImageView = (ImageView) findViewById(R.id.back_imageview);
		_backImageView.setOnClickListener(_back_onClick);
		_backImageView.setClickable(false);
		_notInterestedButtonLayout = (LinearLayout) findViewById(R.id.notinterested_button_layout);
		_notInterestedButtonLayout.setOnClickListener(_notInterestedButton_onClick);
		_notInterestedButtonLayout.setClickable(false);

		_backgroundLayout = (RelativeLayout) findViewById(R.id.background_layout);

		// main content
		_contentLayout = (RelativeLayout) findViewById(R.id.content_layout);

		// status
		_statusView = (View) findViewById(R.id.status_view);
		_statusTextView = (TextView) findViewById(R.id.status_textview);

		// bundle bar
		_bundleLayout = (RelativeLayout) findViewById(R.id.bundle_layout);
		/*-_bundleImageView = (ImageView) findViewById(R.id.bundle_imageview);-*/
		/*-_bundleTextView = (TextView) findViewById(R.id.bundle_textview);-*/

		// center panel
		// title box
		_messageAlertImageView = (ImageView) findViewById(R.id.message_alert_imageview);
		_notificationAlertImageView = (ImageView) findViewById(R.id.notification_alert_imageview);
		_titleTextView = (TextView) findViewById(R.id.title_textview);
		// items
		_clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
		_distanceTextView = (TextView) findViewById(R.id.distance_textview);
		_locationTextView = (TextView) findViewById(R.id.location_textview);
		_whenTextView = (TextView) findViewById(R.id.when_textview);
		_workorderIdTextView = (TextView) findViewById(R.id.workorderid_textview);

		// right panel
		_rightLayout = (LinearLayout) findViewById(R.id.right_layout);
		// _paymentLayout = (LinearLayout) findViewById(R.id.payment_layout);
		_paymentTextView = (TextView) findViewById(R.id.payment_textview);
		_basisTextView = (TextView) findViewById(R.id.basis_textview);

		_actionButton = (Button) findViewById(R.id.action_button);
		_actionButton.setOnClickListener(_actionButton_onClick);

		// loading layout
		_loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);

		// undo layout
		_undoLayout = (RelativeLayout) findViewById(R.id.undo_layout);
		_undoButton = (Button) findViewById(R.id.undo_button);
		_undoButton.setOnClickListener(_undoButton_onClick);

		// debugging
		// _detailButton.setVisibility(GONE);
		// _cashLinearLayout.setVisibility(GONE);

		// attach my listeners
		setOnLongClickListener(_this_onLongClickListener);
		setOnClickListener(_this_onClick);

		// hook up animations
		_slideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.wocard_slide_away);
		_slideBackAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.wocard_slide_back);

		// initialize ui
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

	/**
	 * See the MODE_* constants
	 * 
	 * @param displayMode
	 */
	public void setDisplayMode(int displayMode) {
		_displayMode = displayMode;
		refresh();
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _undoButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: _undoButton_onClick()
			Log.v(TAG, "Method Stub: _undoButton_onClick()");
		}
	};

	private View.OnClickListener _notInterestedButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (_listener != null) {
				// TODO trigger animation before removing
				_listener.startRemove(_workorder);
				_displayMode = MODE_UNDO_NOT_INTERESTED;
				_backImageView.setClickable(false);
				_notInterestedButtonLayout.setClickable(false);
				_contentLayout.startAnimation(_slideBackAnimation);
				_backgroundLayout.startAnimation(_slideBackAnimation);
				setDisplayMode(MODE_UNDO_NOT_INTERESTED);
			}
		}
	};

	private View.OnClickListener _back_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_backImageView.setClickable(false);
			_notInterestedButtonLayout.setClickable(false);
			_contentLayout.startAnimation(_slideBackAnimation);
			_backgroundLayout.startAnimation(_slideBackAnimation);
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
			_notInterestedButtonLayout.setClickable(true);
			_contentLayout.startAnimation(_slideAnimation);
			_backgroundLayout.startAnimation(_slideAnimation);
			return true;
		}
	};

	private View.OnClickListener _actionButton_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (_workorder.getButtonAction()) {
			case Workorder.BUTTON_ACTION_CHECKIN:
				if (_listener != null) {
					_listener.actionCheckin(_workorder);
				}
				break;
			case Workorder.BUTTON_ACTION_CHECKOUT:
				if (_listener != null) {
					_listener.actionCheckout(_workorder);
				}
				break;
			case Workorder.BUTTON_ACTION_ASSIGNMENT:
				if (_listener != null) {
					_listener.actionAssignment(_workorder);
				}
				break;
			case Workorder.BUTTON_ACTION_REQUEST:
				if (_listener != null) {
					_listener.actionRequest(_workorder);
				}
				break;
			case Workorder.BUTTON_ACTION_ACKNOWLEDGE_HOLD:
				if (_listener != null) {
					_listener.actionAcknowledgeHold(_workorder);
				}
			}
			setDisplayMode(MODE_DOING_WORK);
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
			_paymentTextView.setVisibility(GONE);
			_rightLayout.setVisibility(GONE);
		} else {
			_bundleLayout.setVisibility(GONE);
			_titleTextView.setVisibility(VISIBLE);
			_basisTextView.setVisibility(VISIBLE);
			_paymentTextView.setVisibility(VISIBLE);
			_rightLayout.setVisibility(VISIBLE);
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
		_statusView.setBackgroundResource(_workorder.getStatusBG());
		_statusTextView.setTextColor(getContext().getResources().getColor(_workorder.getStatusTextColor()));
		_actionButton.setBackgroundResource(_workorder.getStatusButtonBG());
		_actionButton.setTextColor(getContext().getResources().getColor(_workorder.getStatusButtonFG()));
	}

	private void refresh() {
		if (_workorder == null)
			return;
		_loadingLayout.setVisibility(GONE);
		_undoLayout.setVisibility(GONE);

		switch (_displayMode) {
		case MODE_NORMAL:
			refreshNormal();
			break;
		case MODE_DOING_WORK:
			refreshDoingWork();
			break;
		case MODE_UNDO_NOT_INTERESTED:
			refreshUndoNotInterested();
			break;
		}
	}

	private void refreshDoingWork() {
		_loadingLayout.setVisibility(VISIBLE);
	}

	private void refreshUndoNotInterested() {
		_undoLayout.setVisibility(VISIBLE);
	}

	private void refreshNormal() {
		try {
			buildStatus();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (Debug.isDebuggerConnected()) {
			_workorderIdTextView.setText("[ID " + _workorder.getWorkorderId() + "]");
			_workorderIdTextView.setVisibility(VISIBLE);
		} else {
			_workorderIdTextView.setVisibility(GONE);
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

				if (address1 == null) {
					address1 = address2;
				} else if (address2 != null) {
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
			_basisTextView.setText(pay.getPayRateBasis());
			if (desc != null) {
				_paymentTextView.setText(desc.substring(1));
			} else {
				_rightLayout.setVisibility(GONE);
			}
		} else {
			_rightLayout.setVisibility(GONE);
		}

		_contentLayout.clearAnimation();
		_backImageView.setClickable(false);
		_notInterestedButtonLayout.setClickable(false);
	}

	private void buildStatus() throws ParseException {
		if (_workorder.getStatus() != null) {
			_statusTextView.setText(_workorder.getStatus());
		} else {
			_statusTextView.setText("");
		}

		setNotInterestedEnabled(false);
		_titleTextView.setVisibility(GONE);
		_whenTextView.setVisibility(GONE);
		_clientNameTextView.setVisibility(GONE);
		_distanceTextView.setVisibility(GONE);
		_rightLayout.setVisibility(GONE);
		_actionButton.setVisibility(GONE);
		_locationTextView.setVisibility(GONE);

		switch (_dataView) {
		case ASSIGNED:
			buildStatusAssigned();
			break;
		case AVAILABLE:
			buildStatusAvailable();
			break;
		case REQUESTED:
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

	private void buildStatusAssigned() {
		switch (_workorder.getDisplayState()) {
		case 0:
			_statusTextView.setText("Confirmed");
			_actionButton.setText("Check In");
			_titleTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);

			break;
		case 1:
			_statusTextView.setText("On Hold");
			_actionButton.setText("Acknowledge");
			_titleTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			break;
		case 2:
			_statusTextView.setText("Unconfirmed");
			_actionButton.setText("Confirm");
			setNotInterestedEnabled(true);
			_titleTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			break;
		case 3:
			_statusTextView.setText("On Hold");
			_titleTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			break;
		}
	}

	public void buildStatusAvailable() {
		setNotInterestedEnabled(true);
		switch (_workorder.getDisplayState()) {
		case 0:
			_statusTextView.setText("Available");
			_actionButton.setText("Request");
			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_distanceTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_rightLayout.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			break;
		case 1:
			_statusTextView.setText("Routed");
			_actionButton.setText("Accept");
			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_distanceTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_rightLayout.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			break;
		case 2:
			_statusTextView.setText("Requested");
			_titleTextView.setVisibility(VISIBLE);
			_distanceTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_rightLayout.setVisibility(VISIBLE);
			break;
		case 3:
			_statusTextView.setText("Sent Counter");
			_actionButton.setVisibility(VISIBLE);
			_actionButton.setText("View Counter");
			_titleTextView.setVisibility(VISIBLE);
			_distanceTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_rightLayout.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			break;
		}
	}

	public void buildStatusInProgress() {
		switch (_workorder.getDisplayState()) {
		case 0:
			_statusTextView.setText("Checked Out");
			_actionButton.setText("Check In");
			_titleTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			// TODO show 'task' ui?
			break;
		case 1:
			_statusTextView.setText("On Hold");
			_actionButton.setText("Acknowledge");
			_titleTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			// TODO show 'task' ui?
			break;
		case 2:
			_statusTextView.setText("Checked In");
			_actionButton.setText("Check Out");
			_titleTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			// TODO show 'task' ui?
			break;
		case 3:
			_statusTextView.setText("On Hold");
			_titleTextView.setVisibility(VISIBLE);
			_locationTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			// TODO show 'task' ui?
			break;
		}
	}

	public void buildStatusCompleted() {
		switch (_workorder.getDisplayState()) {
		case 0:
			if (_workorder.hasLabel(19)) {
				_statusTextView.setText("Pending");
				_titleTextView.setVisibility(VISIBLE);
				_clientNameTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				_rightLayout.setVisibility(VISIBLE);
			} else {

				// label == 20
				_statusTextView.setText("In Review");

				_titleTextView.setVisibility(VISIBLE);
				_clientNameTextView.setVisibility(VISIBLE);
				_whenTextView.setVisibility(VISIBLE);
				_rightLayout.setVisibility(VISIBLE);
			}
			break;
		case 1:
			break;
		case 2:
			_statusTextView.setText("Processing");
			_actionButton.setText("Payments");
			// TODO BUTTON_ACTION_PAYMENTS!?!?!
			// _buttonAction = BUTTON_ACTION_PAYMENTS;

			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_rightLayout.setVisibility(VISIBLE);
			_actionButton.setVisibility(VISIBLE);
			break;
		case 3:
			_statusTextView.setText("Paid");

			_titleTextView.setVisibility(VISIBLE);
			_clientNameTextView.setVisibility(VISIBLE);
			_whenTextView.setVisibility(VISIBLE);
			_rightLayout.setVisibility(VISIBLE);
			break;
		}
	}

	private void buildStatusCancelled() {
		// TODO METHOD STUB buildStatusCancelled!
	}

	public interface Listener {
		public void startRemove(Workorder workorder);

		public void cancelRemove(Workorder workorder);

		public void notifyDataSetChanged();

		public void actionRequest(Workorder workorder);

		public void actionAssignment(Workorder workorder);

		public void actionCheckin(Workorder workorder);

		public void actionCheckout(Workorder workorder);

		public void actionAcknowledgeHold(Workorder workorder);
	}
}
