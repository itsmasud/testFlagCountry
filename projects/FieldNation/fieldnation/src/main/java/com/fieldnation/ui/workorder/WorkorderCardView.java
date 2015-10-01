package com.fieldnation.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.text.ParseException;

/**
 * Displays the summary of a workorder to the user. Will also allow some simple
 * actions.
 *
 * @author michael.carver
 */
public class WorkorderCardView extends RelativeLayout {
    private static final String TAG = "WorkorderCardView";

    public static final int MODE_NORMAL = 1;
    public static final int MODE_UNDO_NOT_INTERESTED = 2;
    public static final int MODE_DOING_WORK = 3;
    public static final int MODE_SELECTED = 4;

    // UI
    // not interested
    /*-private RelativeLayout _notInterestedLayout;-*/
    /*-private ImageView _backImageView;-*/
    /*-private LinearLayout _notInterestedButtonLayout;-*/

    // background
//    private ViewGroup _backgroundLayout;
    private View _statusView;
//    private View _backgroundView;

    // main content
//    private RelativeLayout _contentLayout;
    // status
    private TextView _statusTextView;
    // bundle
    private TextView _bundleIconFont;

    // center panel
    // title
    private TextView _messageAlertIconFont;
    private ImageView _notificationAlertImageView;
    private TextView _titleTextView;
    // items
    private TextView _clientNameTextView;
    private TextView _distanceTextView;
    private TextView _locationTextView;
    private TextView _whenTextView;
    private TextView _workorderIdTextView;

    // right panel
//    private LinearLayout _rightLayout;
//    private TextView _moneySymbolTextView;
    private LinearLayout _paymentLayout;
    private TextView _paymentTextView;
    private TextView _basisTextView;
    private Button _actionButton;
    private Button _actionButtonGreen;
    private Button _actionButtonWhite;
    private Button _actionButtonOrange;

    // loading layout
    private RelativeLayout _loadingLayout;

    // undo layout
    private RelativeLayout _undoLayout;
    private Button _undoButton;

    // animations
    // private Animation _slideAnimation;
    // private Animation _slideBackAnimation;

    // Data
    private Workorder _workorder;
    private Listener _listener = null;
    private int _displayMode = MODE_NORMAL;
    private boolean _isBundle;
    private String[] _statusStrings;
    private String[] _substatusStrings;
    private android.location.Location _gpsLocation;

    public WorkorderCardView(Context context) {
        super(context);
        init();
    }

    public WorkorderCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkorderCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_card, this);

        if (isInEditMode())
            return;

        // connect UI components
        // not interested
        /*-_notInterestedLayout = (RelativeLayout) findViewById(R.id.notinterested_layout);-*/
        /* _backImageView = (ImageView) findViewById(R.id.back_imageview); */
        /* _backImageView.setOnClickListener(_back_onClick); */
        /* _backImageView.setClickable(false); */
        /*
         * _notInterestedButtonLayout = (LinearLayout)
		 * findViewById(R.id.notinterested_button_layout);
		 */
        /*
         * _notInterestedButtonLayout.setOnClickListener(
		 * _notInterestedButton_onClick);
		 */
        /* _notInterestedButtonLayout.setClickable(false); */

//        _backgroundLayout = (ViewGroup) findViewById(R.id.background_layout);
//        _backgroundView = findViewById(R.id.background_view);
        _statusView = findViewById(R.id.status_view);

        // main content
//        _contentLayout = (RelativeLayout) findViewById(R.id.content_layout);

        // status
        _statusTextView = (TextView) findViewById(R.id.status_textview);

        // bundle bar
        _bundleIconFont = (TextView) findViewById(R.id.bundle_imageview);

        // center panel
        // title box
        _messageAlertIconFont = (TextView) findViewById(R.id.message_alert_imageview);
        _notificationAlertImageView = (ImageView) findViewById(R.id.notification_alert_imageview);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        // items
        _clientNameTextView = (TextView) findViewById(R.id.clientname_textview);
        _distanceTextView = (TextView) findViewById(R.id.distance_textview);
        _locationTextView = (TextView) findViewById(R.id.location_textview);
        _whenTextView = (TextView) findViewById(R.id.when_textview);
        _workorderIdTextView = (TextView) findViewById(R.id.workorderid_textview);

        // right panel
//        _rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        _paymentLayout = (LinearLayout) findViewById(R.id.payment_layout);
        _paymentTextView = (TextView) findViewById(R.id.payment_textview);
        _basisTextView = (TextView) findViewById(R.id.basis_textview);
//        _moneySymbolTextView = (TextView) findViewById(R.id.moneysymbol_textview);

        _actionButtonWhite = (Button) findViewById(R.id.action_button_white);
        _actionButtonWhite.setOnClickListener(_actionButton_onClick);
        //_actionButtonWhite.setEnabled(false);

        _actionButtonGreen = (Button) findViewById(R.id.action_button_green);
        _actionButtonGreen.setOnClickListener(_actionButton_onClick);
        //_actionButtonGreen.setEnabled(false);

        _actionButtonOrange = (Button) findViewById(R.id.action_button_orange);
        _actionButtonOrange.setOnClickListener(_actionButton_onClick);
        //_actionButtonOrange.setEnabled(false);
        //_actionButton.setEnabled(false);
        //_actionButton.setMinTextSize(1F);

        // loading layout
        _loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);

        // undo layout
        _undoLayout = (RelativeLayout) findViewById(R.id.undo_layout);
        _undoButton = (Button) findViewById(R.id.undo_button);

        _statusStrings = getContext().getResources().getStringArray(R.array.workorder_status);
        _substatusStrings = getContext().getResources().getStringArray(R.array.workorder_substatus);

        // debugging
        // _detailButton.setVisibility(GONE);
        // _cashLinearLayout.setVisibility(GONE);

        // attach my listeners
        setOnClickListener(_this_onClick);

        // hook up animations
        // _slideAnimation = AnimationUtils.loadAnimation(getContext(),
        // R.anim.wocard_slide_away);
        // _slideBackAnimation = AnimationUtils.loadAnimation(getContext(),
        // R.anim.wocard_slide_back);

        // initialize ui
        setIsBundle(false);
        showMessageAlertIcon(true);
        showAlertIcon(true);
    }

    /*-*****************************************-*/
    /*-				Setters Getters				-*/
    /*-*****************************************-*/
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        refresh();
    }

    public void setWorkorder(Workorder workorder, android.location.Location location) {
        _workorder = workorder;
        _gpsLocation = location;
        refresh();
    }

    public Workorder getWorkorder() {
        return _workorder;
    }

    public void showMessageAlertIcon(boolean enabled) {
        if (enabled) {
            _messageAlertIconFont.setVisibility(VISIBLE);
        } else {
            _messageAlertIconFont.setVisibility(GONE);
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

    public void setIsBundle(boolean isBundle) {
        if (isBundle) {
            _bundleIconFont.setVisibility(VISIBLE);
        } else {
            _bundleIconFont.setVisibility(GONE);
        }
        _isBundle = isBundle;
    }

    public boolean isBundle() {
        return _isBundle;
    }

//    public void setNotInterestedEnabled(boolean enabled) {
//        this.setLongClickable(enabled);
//    }

    public void setWorkorderSummaryListener(Listener listener) {
        _listener = listener;
    }


    public void makeButtonsGone() {
        _actionButtonGreen.setVisibility(View.GONE);
        _actionButtonWhite.setVisibility(View.GONE);
        _actionButtonOrange.setVisibility(View.GONE);
        _statusTextView.setVisibility(View.GONE);
        _statusView.setVisibility(View.GONE);
    }



	/*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onClick(WorkorderCardView.this, _workorder);
            }
        }
    };

    private View.OnClickListener _actionButton_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GoogleAnalyticsTopicClient.dispatchEvent(getContext(), "WorkorderCardView", GoogleAnalyticsTopicClient.EventAction.CLICK, _actionButton.getText().toString(), 1);

            switch (_workorder.getButtonAction()) {
                case Workorder.BUTTON_ACTION_CHECKIN:
                    if (_listener != null) {
                        _listener.actionCheckin(WorkorderCardView.this, _workorder);
                    }
                    break;
                case Workorder.BUTTON_ACTION_CHECKOUT:
                    if (_listener != null) {
                        _listener.actionCheckout(WorkorderCardView.this, _workorder);
                    }
                    break;
                case Workorder.BUTTON_ACTION_ASSIGNMENT:
                    if (_listener != null) {
                        _listener.actionAssignment(WorkorderCardView.this, _workorder);
                    }
                    break;
                case Workorder.BUTTON_ACTION_REQUEST:
                    if (_listener != null) {
                        _listener.actionRequest(WorkorderCardView.this, _workorder);
                    }
                    break;
                case Workorder.BUTTON_ACTION_ACKNOWLEDGE_HOLD:
                    if (_listener != null) {
                        _listener.actionAcknowledgeHold(WorkorderCardView.this, _workorder);
                    }
                    break;
                case Workorder.BUTTON_ACTION_VIEW_COUNTER:
                    if (_listener != null) {
                        _listener.viewCounter(WorkorderCardView.this, _workorder);
                    }
                    break;
                case Workorder.BUTTON_ACTION_VIEW_PAYMENT:
                    if (_listener != null) {
                        _listener.onViewPayments(WorkorderCardView.this, _workorder);
                    }
                    break;
                case Workorder.BUTTON_ACTION_WITHDRAW_REQUEST:
                    if (_listener != null) {
                        _listener.actionWithdrawRequest(WorkorderCardView.this, _workorder);
                    }
                    break;
                case Workorder.BUTTON_ACTION_READY_TO_GO:
                    if (_listener != null) {
                        _listener.actionReadyToGo(WorkorderCardView.this, _workorder);
                    }
                    break;
            }
        }
    };

    /*-*********************************-*/
    /*-				Util				-*/
    /*-*********************************-*/
    private void updateStatusUiColors() {
        _statusView.setBackgroundResource(_workorder.getStatusBG());
        _statusTextView.setTextColor(getContext().getResources().getColor(_workorder.getStatusTextColor()));
        switch (_workorder.getStatus().getStatusIntent()) {
            case NORMAL:
                _actionButton = _actionButtonWhite;
                break;
            case SUCCESS:
                _actionButton = _actionButtonGreen;
                break;
            case WAITING:
                _actionButton = _actionButtonWhite;
                break;
            case WARNING:
                _actionButton = _actionButtonOrange;
                break;
        }

        if (_workorder.getNeedsReadyToGo())
            _actionButton = _actionButtonOrange;
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
            case MODE_SELECTED:
                refreshSelected();
                break;
        }
    }

    private void refreshDoingWork() {
        refreshNormal();
        _loadingLayout.setVisibility(VISIBLE);
    }

    private void refreshUndoNotInterested() {
        refreshNormal();
        _undoLayout.setVisibility(VISIBLE);
    }

    private void refreshSelected() {
        refreshNormal();

        int color = getContext().getResources().getColor(R.color.fn_white_text);
        _basisTextView.setTextColor(color);
        _titleTextView.setTextColor(color);
        _clientNameTextView.setTextColor(color);
        _distanceTextView.setTextColor(color);
        _locationTextView.setTextColor(color);
        _whenTextView.setTextColor(color);
        _workorderIdTextView.setTextColor(color);
        _paymentTextView.setTextColor(color);
        _statusTextView.setTextColor(color);
//        _moneySymbolTextView.setTextColor(color);

//        _backgroundView.setBackgroundResource(R.drawable.card_right_selected);
        _statusView.setBackgroundResource(R.drawable.card_status_black);
        _statusTextView.setText(getResources().getString(R.string.workorder_card_status_selected));
//        _bundleLayout.setBackgroundResource(R.drawable.wo_bundle_bg_select);
    }

    private void refreshNormal() {
        int color = getContext().getResources().getColor(R.color.fn_dark_text);
        _basisTextView.setTextColor(color);
        _titleTextView.setTextColor(color);
        _clientNameTextView.setTextColor(color);
        _distanceTextView.setTextColor(color);
        _locationTextView.setTextColor(color);
        _whenTextView.setTextColor(color);
        _workorderIdTextView.setTextColor(color);
        _paymentTextView.setTextColor(color);
        _statusTextView.setTextColor(color);
//        _moneySymbolTextView.setTextColor(color);
//        _bundleTextView.setTextColor(color);
//        _bundleTitleTextView.setTextColor(color);

//        _backgroundView.setBackgroundResource(R.drawable.card_right);
        _statusView.setBackgroundResource(R.drawable.card_status_black);
//        _bundleLayout.setBackgroundResource(R.drawable.wo_bundle_bg);

        try {
            buildStatus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        if (Debug.isDebuggerConnected()) {
        _workorderIdTextView.setText(_workorder.getWorkorderId() + "");
        _workorderIdTextView.setVisibility(VISIBLE);
//        } else {
//            _workorderIdTextView.setVisibility(GONE);
//        }

        // bundle
/*
        if (_workorder.getBundleCount() != null) {
            _bundleTextView.setText(_workorder.getBundleCount() + " Work Orders");
        }
*/

        // title
        String title = _workorder.getTitle();
        if (title != null) {
            _titleTextView.setText(title);
        }

        showMessageAlertIcon(_workorder.getMessageCount() != null && _workorder.getMessageCount() > 0);
        showAlertIcon(_workorder.getAlertCount() != null && _workorder.getAlertCount() > 0);

        Location location = _workorder.getLocation();
        if (location != null) {

            // contact name
            if (location.getContactName() != null) {
                String t = location.getContactName();
                if (misc.isEmptyOrNull(t)) {
                    _clientNameTextView.setVisibility(GONE);
                } else {
                    _clientNameTextView.setText(t);
                }
            } else {
                _clientNameTextView.setVisibility(GONE);
            }

            // location
            if (_workorder.getIsRemoteWork()) {
                _locationTextView.setVisibility(View.GONE);
                // distance/address? location.state, location.zip, location.city,
                // location.country,
            } else if (location.getAddress1() != null || location.getAddress2() != null) {
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

            // TODO hook up to geocoding
            // distance
            if (_workorder.getIsRemoteWork()) {
                _distanceTextView.setText("Remote");
            } else if (location.getGeo() != null && _gpsLocation != null) {
                _distanceTextView.setVisibility(VISIBLE);
                try {
                    LatLng siteLoc = new LatLng(location.getGeo().getLatitude(), location.getGeo().getLongitude());
                    LatLng myLoc = new LatLng(_gpsLocation);

                    _distanceTextView.setText(((int) ((myLoc.distanceTo(siteLoc) * 0.000621371) + 0.5)) + " mi");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    _distanceTextView.setText("Unknown mi");
                }
            } else {
                _distanceTextView.setVisibility(GONE);
            }
        } else {
            // distance
            if (_workorder.getIsRemoteWork()) {
                _distanceTextView.setVisibility(VISIBLE);
                _distanceTextView.setText("Remote");
                _clientNameTextView.setVisibility(GONE);
            } else {
                _distanceTextView.setVisibility(GONE);
                _clientNameTextView.setVisibility(GONE);
            }
        }
        // when scheduledTimeStart/scheduledTimeEnd
        if (_workorder.getEstimatedSchedule() != null) {
            String when = _workorder.getEstimatedSchedule().getFormatedStartTime();

            if (when == null) {
                _whenTextView.setVisibility(GONE);
            } else {
                _whenTextView.setText(when);
            }
        } else if (_workorder.getSchedule() != null) {
            String when = _workorder.getSchedule().getFormatedStartTime();

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
        if (pay != null && !pay.hidePay()) {
            String desc = pay.toDisplayStringShort();
            _basisTextView.setText(pay.getPayRateBasis());
            if (desc != null) {
                _paymentTextView.setText(desc);
            } else {
                _paymentLayout.setVisibility(GONE);
            }
        } else {
            _paymentLayout.setVisibility(GONE);
        }

//        _contentLayout.clearAnimation();
        /*-_backImageView.setClickable(false);
        _notInterestedButtonLayout.setClickable(false);-*/
    }

    private void buildStatus() throws ParseException {
        if (_workorder.getStatus() != null) {
            _statusTextView.setText(_substatusStrings[_workorder.getStatus().getWorkorderSubstatus().ordinal()]);
        } else {
            _statusTextView.setText("");
        }
        setIsBundle(_workorder.isBundle());

        updateStatusUiColors();

        //setNotInterestedEnabled(false);
//        _titleTextView.setVisibility(GONE);
        _whenTextView.setVisibility(GONE);
        _clientNameTextView.setVisibility(GONE);
        _distanceTextView.setVisibility(GONE);
        _paymentLayout.setVisibility(GONE);
        _actionButtonWhite.setVisibility(GONE);
        _actionButtonGreen.setVisibility(GONE);
        _actionButtonOrange.setVisibility(GONE);
        _locationTextView.setVisibility(GONE);

        switch (_workorder.getStatus().getWorkorderStatus()) {
            case ASSIGNED:
                buildStatusAssigned();
                break;
            case AVAILABLE:
                buildStatusAvailable();
                break;
            case CANCELED:
                buildStatusCanceled();
                break;
            case COMPLETED:
            case APPROVED:
            case PAID:
                buildStatusCompleted();
                break;
            case INPROGRESS:
                buildStatusInProgress();
                break;
            default:
                buildStatusDefault();
                break;
        }

        if (_workorder.getStatus().getStatusIntent() == null) {
            Log.v(TAG, "BP");
        }
    }

    private void buildStatusDefault() {
//        _titleTextView.setVisibility(VISIBLE);
        _whenTextView.setVisibility(VISIBLE);
        _clientNameTextView.setVisibility(VISIBLE);
        _locationTextView.setVisibility(VISIBLE);
    }

    private void buildStatusAssigned() {
        switch (_workorder.getStatus().getWorkorderSubstatus()) {
            case CONFIRMED:
                if (_workorder.getNeedsReadyToGo()) {
                    _actionButton.setText("READY-TO-GO");
                } else {
                    _actionButton.setText(R.string.btn_check_in);
                }
//                _titleTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                break;
            case ONHOLD_UNACKNOWLEDGED:
                _actionButton.setText(R.string.btn_acknowledge);
//                _titleTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                break;
            case UNCONFIRMED:
                if (_workorder.getNeedsReadyToGo()) {
                    _actionButton.setText("READY-TO-GO");
                } else {
                    _actionButton.setText(R.string.btn_confirm);
                }
                //setNotInterestedEnabled(true);
//                _titleTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                break;
            case ONHOLD_ACKNOWLEDGED:
//                _titleTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                break;
            case CHECKEDIN:
                _actionButton.setText(R.string.btn_check_out);
//                _titleTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                break;
            case CHECKEDOUT:
                _actionButton.setText(R.string.btn_check_in);
//                _titleTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                break;
            default:
//                _titleTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                Log.v(TAG,
                        "Unknown state: " + _workorder.getWorkorderId() + " - " + _workorder.getStatus().toJson().toString());
                break;
        }
    }

    public void buildStatusAvailable() {
        //setNotInterestedEnabled(true);
        switch (_workorder.getStatus().getWorkorderSubstatus()) {
            case AVAILABLE:
                _actionButton.setText(R.string.btn_request);
//                _titleTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _distanceTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                break;
            case ROUTED:
                _actionButton.setText(R.string.btn_accept);
//                _titleTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _distanceTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                break;
            case REQUESTED:
                _actionButton.setText(R.string.btn_withdraw_request);
                _actionButton.setVisibility(VISIBLE);
//                _titleTextView.setVisibility(VISIBLE);
                _distanceTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);
                break;
            case COUNTEROFFERED:
                if (_workorder.canCounterOffer()) {
                    _actionButton.setVisibility(VISIBLE);
                    // _actionButton.setTextSize(10F);
                    _actionButton.setText(R.string.btn_view_counter);
                }

//                _titleTextView.setVisibility(VISIBLE);
                _distanceTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);
                break;
            default:
//                _titleTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _distanceTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);

                Log.v(TAG,
                        "Unknown state: " + _workorder.getWorkorderId() + " - " + _workorder.getStatus().toJson().toString());
                break;
        }
    }

    public void buildStatusInProgress() {
        switch (_workorder.getStatus().getWorkorderSubstatus()) {
            case CHECKEDOUT:
                _actionButton.setText(R.string.btn_check_in);
//                _titleTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                // TODO show 'task' ui?
                break;
            case ONHOLD_UNACKNOWLEDGED:
                _actionButton.setText(R.string.btn_acknowledge);
//                _titleTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                // TODO show 'task' ui?
                break;
            case CHECKEDIN:
                _actionButton.setText(R.string.btn_check_out);
//                _titleTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _actionButton.setVisibility(VISIBLE);
                // TODO show 'task' ui?
                break;
            case ONHOLD_ACKNOWLEDGED:
//                _titleTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                // TODO show 'task' ui?
                break;
            default:
//                _titleTextView.setVisibility(VISIBLE);
                _locationTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                Log.v(TAG,
                        "Unknown state: " + _workorder.getWorkorderId() + " - " + _workorder.getStatus().toJson().toString());
                break;
        }
    }

    public void buildStatusCompleted() {
        switch (_workorder.getStatus().getWorkorderSubstatus()) {
            case PENDINGREVIEW:
//                _titleTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);
                break;
            case INREVIEW:
//                _titleTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);
                break;
            case APPROVED_PROCESSINGPAYMENT:
                Profile profile = App.get().getProfile();
                if (profile != null && profile.getCanViewPayments()) {
                    _actionButton.setVisibility(VISIBLE);
                    _actionButton.setText(R.string.btn_payments);
                }

//                _titleTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);
                break;
            case PAID:
//                _titleTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);
                break;
            default:
//                _titleTextView.setVisibility(VISIBLE);
                _clientNameTextView.setVisibility(VISIBLE);
                _whenTextView.setVisibility(VISIBLE);
                _paymentLayout.setVisibility(VISIBLE);

                Log.v(TAG,
                        "Unknown state: " + _workorder.getWorkorderId() + " - " + _workorder.getStatus().toJson().toString());
                break;
        }
    }

    private void buildStatusCanceled() {
//        _titleTextView.setVisibility(VISIBLE);
        _clientNameTextView.setVisibility(VISIBLE);
        _whenTextView.setVisibility(VISIBLE);
        _paymentLayout.setVisibility(GONE);
        _actionButton.setVisibility(GONE);

        switch (_workorder.getStatus().getWorkorderSubstatus()) {
            case CANCELED:
                break;
            case CANCELED_LATEFEEPAID:
                break;
            case CANCELED_LATEFEEPROCESSING:
                Profile profile = App.get().getProfile();
                if (profile != null && profile.getCanViewPayments()) {
                    _actionButton.setVisibility(VISIBLE);
                    _actionButton.setText(R.string.btn_payments);
                }
                break;
            default:
                Log.v(TAG,
                        "Unknown state: " + _workorder.getWorkorderId() + " - " + _workorder.getStatus().toJson().toString());
                break;
        }
    }

    public interface Listener {

        void actionRequest(WorkorderCardView view, Workorder workorder);

        void actionWithdrawRequest(WorkorderCardView view, Workorder workorder);

        void actionAssignment(WorkorderCardView view, Workorder workorder);

        void actionCheckin(WorkorderCardView view, Workorder workorder);

        void actionCheckout(WorkorderCardView view, Workorder workorder);

        void actionAcknowledgeHold(WorkorderCardView view, Workorder workorder);

        void viewCounter(WorkorderCardView view, Workorder workorder);

        void onClick(WorkorderCardView view, Workorder workorder);

        void onViewPayments(WorkorderCardView view, Workorder workorder);

        void actionReadyToGo(WorkorderCardView view, Workorder workorder);
    }
}
