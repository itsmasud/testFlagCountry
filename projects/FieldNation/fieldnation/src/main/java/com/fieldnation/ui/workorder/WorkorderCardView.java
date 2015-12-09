package com.fieldnation.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.misc;

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
    private IconFontTextView _bundleIconFontView;
    private TextView _titleTextView;
    private TextView _companyNameTextView;
    private TextView _workorderIdTextView;
    private TextView _timeTextView;
    private TextView _priceTextView;
    private TextView _extraTextView;
    private TextView _stateTextView;
    private Button _leftButton;
    private Button _rightWhiteButton;
    private Button _rightOrangeButton;
    private Button _rightGreenButton;

    // Data
    private Workorder _workorder;
    private Listener _listener = null;
    //private int _displayMode = MODE_NORMAL;
    private boolean _isBundle;
    //private String[] _statusStrings;
    //private String[] _substatusStrings;
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

        _bundleIconFontView = (IconFontTextView) findViewById(R.id.bundle_iconFont);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _companyNameTextView = (TextView) findViewById(R.id.companyName_textview);
        _workorderIdTextView = (TextView) findViewById(R.id.workorderId_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _priceTextView = (TextView) findViewById(R.id.price_textview);
        _extraTextView = (TextView) findViewById(R.id.extra_textview);
        _stateTextView = (TextView) findViewById(R.id.status_textview);

        _leftButton = (Button) findViewById(R.id.left_button);
        _leftButton.setOnClickListener(_left_onClick);

        _rightWhiteButton = (Button) findViewById(R.id.rightWhite_button);
        _rightWhiteButton.setOnClickListener(_right_onClick);

        _rightGreenButton = (Button) findViewById(R.id.rightGreen_button);
        _rightGreenButton.setOnClickListener(_right_onClick);

        _rightOrangeButton = (Button) findViewById(R.id.rightOrange_button);
        _rightOrangeButton.setOnClickListener(_right_onClick);

//        _statusStrings = getContext().getResources().getStringArray(R.array.workorder_status);
//        _substatusStrings = getContext().getResources().getStringArray(R.array.workorder_substatus);

        // attach my listeners
        setOnClickListener(_this_onClick);

        // initialize ui
        setIsBundle(false);
    }

    /*-*****************************************-*/
    /*-				Setters Getters				-*/
    /*-*****************************************-*/
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;
        populateUi();
    }

    public void setWorkorder(Workorder workorder, android.location.Location location) {
        _workorder = workorder;
        _gpsLocation = location;
        populateUi();
    }

    public Workorder getWorkorder() {
        return _workorder;
    }

    public void setIsBundle(boolean isBundle) {
        if (isBundle) {
            _bundleIconFontView.setVisibility(VISIBLE);
        } else {
            _bundleIconFontView.setVisibility(GONE);
        }
        _isBundle = isBundle;
    }

    public boolean isBundle() {
        return _isBundle;
    }

    public void setWorkorderSummaryListener(Listener listener) {
        _listener = listener;
    }

    public void makeButtonsGone() {
        _leftButton.setVisibility(GONE);
        _rightGreenButton.setVisibility(GONE);
        _rightOrangeButton.setVisibility(GONE);
        _rightWhiteButton.setVisibility(GONE);
    }

	/*-*********************************-*/
    /*-				Events				-*/
    /*-*********************************-*/

    private final View.OnClickListener _this_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "_this_onClick.onClick");

            if (_listener != null) {
                _listener.onClick(WorkorderCardView.this, _workorder);
            }
        }
    };

    private final View.OnClickListener _left_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GoogleAnalyticsTopicClient.dispatchEvent(getContext(), "WorkorderCardView",
                    GoogleAnalyticsTopicClient.EventAction.CLICK, _leftButton.getText().toString(), 1);
            handleButtonClick(_workorder.getLeftButtonAction());
        }
    };

    private final View.OnClickListener _right_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_rightWhiteButton.getVisibility() == VISIBLE) {
                GoogleAnalyticsTopicClient.dispatchEvent(getContext(), "WorkorderCardView",
                        GoogleAnalyticsTopicClient.EventAction.CLICK, _rightWhiteButton.getText().toString(), 1);
            } else if (_rightOrangeButton.getVisibility() == VISIBLE) {
                GoogleAnalyticsTopicClient.dispatchEvent(getContext(), "WorkorderCardView",
                        GoogleAnalyticsTopicClient.EventAction.CLICK, _rightOrangeButton.getText().toString(), 1);
            } else if (_rightGreenButton.getVisibility() == VISIBLE) {
                GoogleAnalyticsTopicClient.dispatchEvent(getContext(), "WorkorderCardView",
                        GoogleAnalyticsTopicClient.EventAction.CLICK, _rightGreenButton.getText().toString(), 1);
            }
            handleButtonClick(_workorder.getRightButtonAction());
        }
    };

    private void handleButtonClick(int action) {
        switch (action) {
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
            case Workorder.BUTTON_ACTION_ACCEPT:
                if (_listener != null) {
                    _listener.actionAssignment(WorkorderCardView.this, _workorder);
                }
                break;
            case Workorder.BUTTON_ACTION_REQUEST:
                if (_listener != null) {
                    _listener.actionRequest(WorkorderCardView.this, _workorder);
                }
                break;
            case Workorder.BUTTON_ACTION_RECOGNIZE_HOLD:
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
            case Workorder.BUTTON_ACTION_CONFIRM:
                if (_listener != null) {
                    _listener.actionConfirm(WorkorderCardView.this, _workorder);
                }
                break;
            case Workorder.BUTTON_ACTION_MAP:
                if (_listener != null) {
                    _listener.actionMap(WorkorderCardView.this, _workorder);
                }
                break;
            case Workorder.BUTTON_ACTION_REPORT_PROBLEM:
                if (_listener != null) {
                    _listener.actionReportProblem(WorkorderCardView.this, _workorder);
                }
                break;
            case Workorder.BUTTON_ACTION_MARK_INCOMPLETE:
                if (_listener != null) {
                    _listener.actionMarkIncomplete(WorkorderCardView.this, _workorder);
                }
                break;
        }
    }

    /*-*********************************-*/
    /*-				Util				-*/
    /*-*********************************-*/
    private void populateUi() {
        if (_workorder == null)
            return;
        refreshNormal();
    }

    private void refreshNormal() {
        if (_workorder.isBundle()) {
            _bundleIconFontView.setVisibility(VISIBLE);
        } else {
            _bundleIconFontView.setVisibility(GONE);
        }

        // title
        String title = _workorder.getTitle();
        if (title != null) {
            _titleTextView.setText(title);
        }

        if (!misc.isEmptyOrNull(_workorder.getCompanyName())) {
            _companyNameTextView.setText(_workorder.getCompanyName());
        } else if (_workorder.getLocation() != null && !misc.isEmptyOrNull(_workorder.getLocation().getContactName())) {
            _companyNameTextView.setText(_workorder.getLocation().getContactName());
        } else {
            _companyNameTextView.setText("Company Name Hidden");
        }

        _workorderIdTextView.setText("WO ID: " + _workorder.getWorkorderId());
        _extraTextView.setVisibility(INVISIBLE);

        if (_workorder.getEstimatedSchedule() != null) {
            _timeTextView.setVisibility(VISIBLE);
            _extraTextView.setVisibility(VISIBLE);
            _timeTextView.setText(_workorder.getEstimatedSchedule().getFormatedTime());
            _extraTextView.setText(_workorder.getEstimatedSchedule().getFormatedDate());
        } else if (_workorder.getSchedule() != null) {
            _timeTextView.setVisibility(VISIBLE);
            _extraTextView.setVisibility(VISIBLE);
            _timeTextView.setText(_workorder.getSchedule().getFormatedTime());
            _extraTextView.setText(_workorder.getSchedule().getFormatedDate());
        } else {
            _timeTextView.setVisibility(INVISIBLE);
            _extraTextView.setVisibility(INVISIBLE);
        }

        if (_workorder.getPay() != null && !_workorder.getPay().hidePay()) {
            Pay pay = _workorder.getPay();
            if (pay.isBlendedRate()) {
                _priceTextView.setText(misc.toCurrency(pay.getBlendedStartRate())
                        + " " + misc.toCurrency(pay.getBlendedAdditionalRate()));
                _stateTextView.setText("First " + pay.getBlendedFirstHours() + " hours  Hourly after");
            } else if (pay.isFixedRate()) {
                _priceTextView.setText(misc.toCurrency(pay.getFixedAmount()));
                _stateTextView.setText("Fixed");
            } else if (pay.isHourlyRate()) {
                _priceTextView.setText(misc.toCurrency(pay.getPerHour()));
                _stateTextView.setText("Hourly");
            } else if (pay.isPerDeviceRate()) {
                _priceTextView.setText(misc.toCurrency(pay.getPerDevice()));
                _stateTextView.setText("Per Device");
            }
            _priceTextView.setVisibility(VISIBLE);
            _stateTextView.setVisibility(VISIBLE);
        } else {
            _priceTextView.setVisibility(INVISIBLE);
            _stateTextView.setVisibility(INVISIBLE);
        }


        switch (_workorder.getLeftButtonAction()) {
            case Workorder.BUTTON_ACTION_NONE:
                _leftButton.setVisibility(GONE);
                break;
            case Workorder.BUTTON_ACTION_MAP:
                if (_workorder.getLocation() == null || _workorder.getIsRemoteWork()) {
                    _leftButton.setVisibility(GONE);
                } else {
                    Location location = _workorder.getLocation();
                    _leftButton.setVisibility(VISIBLE);
                    _leftButton.setText((location.getCity() + ", " + location.getState()).toUpperCase());
                }
                break;
            case Workorder.BUTTON_ACTION_REPORT_PROBLEM:
                _leftButton.setText("REPORT PROBLEM");
                break;
            default:
                break;
        }

        _rightWhiteButton.setVisibility(GONE);
        _rightGreenButton.setVisibility(GONE);
        _rightOrangeButton.setVisibility(GONE);
        _rightWhiteButton.setEnabled(true);

        switch (_workorder.getRightButtonAction()) {
            case Workorder.BUTTON_ACTION_NONE:
                if (_workorder.getWorkorderSubstatus() == WorkorderSubstatus.ONHOLD_ACKNOWLEDGED) {
                    _rightWhiteButton.setText("ON HOLD");
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setEnabled(false);
                }
                break;
            case Workorder.BUTTON_ACTION_REQUEST:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText("REQUEST");
                break;
            case Workorder.BUTTON_ACTION_ACCEPT:
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText("ACCEPT");
                break;
            case Workorder.BUTTON_ACTION_WITHDRAW_REQUEST:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText("WITHDRAW");
                break;
            case Workorder.BUTTON_ACTION_READY_TO_GO:
                _rightGreenButton.setVisibility(VISIBLE);
                _rightGreenButton.setText("READY TO GO");
                break;
            case Workorder.BUTTON_ACTION_CONFIRM:
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText("CONFIRM");
                break;
            case Workorder.BUTTON_ACTION_CHECKIN:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText("CHECK IN");
                break;
            case Workorder.BUTTON_ACTION_RECOGNIZE_HOLD:
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText("RECOGNIZE HOLD");
                break;
            case Workorder.BUTTON_ACTION_CHECKOUT:
                _rightGreenButton.setVisibility(VISIBLE);
                _rightGreenButton.setText("CHECK OUT");
                break;
            case Workorder.BUTTON_ACTION_MARK_INCOMPLETE:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText("MARK INCOMPLETE");
                break;
            case Workorder.BUTTON_ACTION_VIEW_PAYMENT:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText("VIEW PAYMENT");
                break;
            default:
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

        void actionConfirm(WorkorderCardView view, Workorder workorder);

        void actionMap(WorkorderCardView view, Workorder workorder);

        void actionReportProblem(WorkorderCardView view, Workorder workorder);

        void actionMarkIncomplete(WorkorderCardView view, Workorder workorder);
    }

    public static class DefaultListener implements Listener {
        @Override
        public void actionRequest(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void actionWithdrawRequest(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void actionAssignment(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void actionCheckin(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void actionCheckout(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void actionAcknowledgeHold(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void viewCounter(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void onViewPayments(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void actionReadyToGo(WorkorderCardView view, Workorder workorder) {
        }

        @Override
        public void actionConfirm(WorkorderCardView view, Workorder workorder) {
            
        }

        @Override
        public void actionMap(WorkorderCardView view, Workorder workorder) {

        }

        @Override
        public void actionReportProblem(WorkorderCardView view, Workorder workorder) {

        }

        @Override
        public void actionMarkIncomplete(WorkorderCardView view, Workorder workorder) {

        }
    }
}
