package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Pay;
import com.fieldnation.data.workorder.Schedule;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.service.data.mapbox.Position;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.DateUtils;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


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
    private TextView _time2TextView;
    private TextView _priceTextView;
    private TextView _price2TextView;
    private TextView _extraTextView;
    private TextView _extra2TextView;
    private TextView _stateTextView;
    private TextView _state2TextView;
    private TextView _remoteWorkTextView;
    private LinearLayout _price_layout;
    private Button _leftButton;
    private Button _rightWhiteButton;
    private Button _rightOrangeButton;
    private Button _rightGreenButton;

    // Data
    private Workorder _workorder;
    private Listener _listener = null;
    private boolean _isBundle;
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
        _time2TextView = (TextView) findViewById(R.id.time2_textview);

        _priceTextView = (TextView) findViewById(R.id.price_textview);
        _price2TextView = (TextView) findViewById(R.id.price2_textview);

        _extraTextView = (TextView) findViewById(R.id.extra_textview);
        _extra2TextView = (TextView) findViewById(R.id.extra2_textview);

        _stateTextView = (TextView) findViewById(R.id.status_textview);
        _state2TextView = (TextView) findViewById(R.id.status2_textview);

        _remoteWorkTextView = (TextView) findViewById(R.id.remoteWork_textview);

        _price_layout = (LinearLayout) findViewById(R.id.price_layout);

        _leftButton = (Button) findViewById(R.id.left_button);
        _leftButton.setOnClickListener(_left_onClick);

        _rightWhiteButton = (Button) findViewById(R.id.rightWhite_button);
        _rightWhiteButton.setOnClickListener(_right_onClick);

        _rightGreenButton = (Button) findViewById(R.id.rightGreen_button);
        _rightGreenButton.setOnClickListener(_right_onClick);

        _rightOrangeButton = (Button) findViewById(R.id.rightOrange_button);
        _rightOrangeButton.setOnClickListener(_right_onClick);

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

    public void setDisplayMode(int mode) {
        // stub for now
    }

    public void setIsBundle(boolean isBundle) {
        if (isBundle)
            _bundleIconFontView.setVisibility(VISIBLE);
        else
            _bundleIconFontView.setVisibility(GONE);

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
        _remoteWorkTextView.setVisibility(GONE);
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
        if (_listener == null) return;

        switch (action) {
            case Workorder.BUTTON_ACTION_CHECKIN:
                _listener.actionCheckin(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_CHECKOUT:
                _listener.actionCheckout(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_ACCEPT:
                _listener.actionAssignment(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_REQUEST:
                _listener.actionRequest(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_RECOGNIZE_HOLD:
                _listener.actionAcknowledgeHold(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_VIEW_COUNTER:
                _listener.viewCounter(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_VIEW_PAYMENT:
                _listener.onViewPayments(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_WITHDRAW_REQUEST:
                _listener.actionWithdrawRequest(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_READY_TO_GO:
                _listener.actionReadyToGo(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_CONFIRM:
                _listener.actionConfirm(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_MAP:
                _listener.actionMap(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_REPORT_PROBLEM:
                _listener.actionReportProblem(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_MARK_INCOMPLETE:
                _listener.actionMarkIncomplete(WorkorderCardView.this, _workorder);
                break;
            case Workorder.BUTTON_ACTION_UPDATE_PAYMENT_INFO:
                _listener.actionUpdatePaymentInfo(WorkorderCardView.this, _workorder);
                break;
        }
    }

    /*-*********************************-*/
    /*-				Util				-*/
    /*-*********************************-*/
    private void populateUi() {
        if (_workorder == null)
            return;

        _time2TextView.setText("");
        _extra2TextView.setText("");
        _price2TextView.setText("");
        _state2TextView.setText("");
        _price_layout.setGravity(Gravity.RIGHT);

        // Bundle stuffs
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

        // Company name
        if (!misc.isEmptyOrNull(_workorder.getCompanyName())) {
            _companyNameTextView.setText(_workorder.getCompanyName());
        } else if (_workorder.getLocation() != null
                && !misc.isEmptyOrNull(_workorder.getLocation().getContactName())) {
            _companyNameTextView.setText(_workorder.getLocation().getContactName());
        } else {
            _companyNameTextView.setText(R.string.company_name_hidden);
        }

        _workorderIdTextView.setText(getResources().getString(R.string.wo_id_x, _workorder.getWorkorderId()));

        populateSchedule();
        populatePay();
        populateLeftActionButton();
        populateRightActionButton();
    }

    private void populateSchedule() {
        try {
            if (_workorder.getWorkorderStatus() == WorkorderStatus.PAID
                    || _workorder.getWorkorderStatus() == WorkorderStatus.COMPLETED
                    || _workorder.getWorkorderStatus() == WorkorderStatus.APPROVED) {
                if (!misc.isEmptyOrNull(_workorder.getCompleteTime())) {
                    Calendar completeCal = ISO8601.toCalendar(_workorder.getCompleteTime());
                    long completeTime = completeCal.getTimeInMillis();

                    if ((completeTime - System.currentTimeMillis()) / 3600000L <= 24) {
                        _timeTextView.setText(DateUtils.formatTime(completeCal, false));
                        _extraTextView.setText(R.string.work_complete);
                    } else {
                        _timeTextView.setText(DateUtils.formatDate(completeCal));
                        _extraTextView.setText(R.string.work_complete);
                    }
                    return;
                }
            }
        } catch (Exception ex) {
        }

        // date/time rules.
        // if date/time is <= now, then red text, display hour/min late #a21623
        // if date/time is within 1 hr of now, show min counter fn_orange color
        // if not range, then time/day, mo ## ... this is strange
        // if within 7 days, show week day/time
        // if > 7 days, show Feb 12 - Feb 22/time
        Schedule schedule = _workorder.getEstimatedSchedule();
        if (schedule == null)
            schedule = _workorder.getSchedule();

        _extraTextView.setVisibility(INVISIBLE);
        try {
            if (schedule == null) {
                _timeTextView.setVisibility(INVISIBLE);
                _extraTextView.setVisibility(INVISIBLE);
                return;
            }

            long startTime = ISO8601.toUtc(schedule.getStartTime());
            _timeTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));

            if (schedule.isExact()) {
                Calendar sCal = ISO8601.toCalendar(schedule.getStartTime());
                _timeTextView.setVisibility(VISIBLE);
                _extraTextView.setVisibility(VISIBLE);

                if (startTime - System.currentTimeMillis() <= 0
                        && _workorder.getWorkorderStatus() == WorkorderStatus.ASSIGNED
                        && _workorder.getWorkorderSubstatus() != WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED
                        && _workorder.getWorkorderSubstatus() != WorkorderSubstatus.ONHOLD_ACKNOWLEDGED) {
                    _timeTextView.setVisibility(VISIBLE);
                    _timeTextView.setText(getResources().getString(R.string.time_late,
                            DateUtils.toRoundDuration(Math.abs(System.currentTimeMillis() - startTime))));
                    _timeTextView.setTextColor(getResources().getColor(R.color.fn_red));
                    if (System.currentTimeMillis() - startTime > 86400000L)
                        _extraTextView.setText(schedule.getFormatedDate());
                    else
                        _extraTextView.setText(schedule.getFormatedTime());

                } else if (startTime - System.currentTimeMillis() <= 3600000
                        && startTime - System.currentTimeMillis() > 0
                        && _workorder.getWorkorderSubstatus() != WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED
                        && _workorder.getWorkorderSubstatus() != WorkorderSubstatus.ONHOLD_ACKNOWLEDGED
                        && (_workorder.getWorkorderStatus() == WorkorderStatus.ASSIGNED
                        || _workorder.getWorkorderStatus() == WorkorderStatus.INPROGRESS
                        || _workorder.getWorkorderStatus() == WorkorderStatus.AVAILABLE)) {
                    _timeTextView.setVisibility(VISIBLE);
                    _timeTextView.setText(getResources().getString(R.string.in_time,
                            DateUtils.toRoundDuration(Math.abs(startTime - System.currentTimeMillis()))));
                    _timeTextView.setTextColor(getResources().getColor(R.color.fn_brandcolor));
                    _extraTextView.setText(schedule.getFormatedTime());

                } else if (startTime < System.currentTimeMillis()) {
                    _timeTextView.setText(new SimpleDateFormat("MMM d, y", Locale.getDefault()).format(sCal.getTime()));
                    _extraTextView.setText(schedule.getFormatedTime());

                } else if ((startTime - System.currentTimeMillis()) / 3600000L <= 24) {
                    _timeTextView.setText(schedule.getFormatedTime());
                    _extraTextView.setText(schedule.getFormatedDate());

                } else if ((startTime - System.currentTimeMillis()) / 86400000L < 6) {
                    _timeTextView.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(sCal.getTime()));
                    _extraTextView.setText(schedule.getFormatedTime());

                } else {
                    _extraTextView.setText(schedule.getFormatedTime());
                    _timeTextView.setText(new SimpleDateFormat("MMMM d", Locale.getDefault()).format(sCal.getTime()));
                }
            } else {
                long endTime = ISO8601.toUtc(schedule.getEndTime());
                Calendar sCal = ISO8601.toCalendar(schedule.getStartTime());
                Calendar eCal = ISO8601.toCalendar(schedule.getEndTime());
                _timeTextView.setVisibility(VISIBLE);
                _extraTextView.setVisibility(VISIBLE);

                if (endTime < System.currentTimeMillis()) {
                    if (sCal.get(Calendar.MONTH) != eCal.get(Calendar.MONTH)
                            || sCal.get(Calendar.DAY_OF_MONTH) != eCal.get(Calendar.DAY_OF_MONTH)) {
                        if (sCal.get(Calendar.YEAR) != eCal.get(Calendar.YEAR)) {
                            _timeTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(sCal.getTime()) + " - ");
                            _time2TextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(eCal.getTime()));
                            _extraTextView.setText(new SimpleDateFormat("h:mma y", Locale.getDefault()).format(sCal.getTime()).toLowerCase());
                            _extra2TextView.setText(new SimpleDateFormat("h:mma y", Locale.getDefault()).format(eCal.getTime()).toLowerCase());

                        } else {
                            _timeTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(sCal.getTime()) + " - ");
                            _time2TextView.setText(new SimpleDateFormat("MMM d, y", Locale.getDefault()).format(eCal.getTime()));
                            _extraTextView.setText(DateUtils.formatTime(sCal, false));
                            _extra2TextView.setText(DateUtils.formatTime(eCal, false));
                        }
                    } else {
                        _timeTextView.setText(new SimpleDateFormat("MMM d, y", Locale.getDefault()).format(sCal.getTime()));
                        _extraTextView.setText(schedule.getFormatedTime());
                    }

                } else if ((endTime - System.currentTimeMillis()) / 86400000L < 6) {
                    String sDay = new SimpleDateFormat("c", Locale.getDefault()).format(sCal.getTime());
                    String eDay = new SimpleDateFormat("c", Locale.getDefault()).format(eCal.getTime());

                    if (sCal.get(Calendar.DAY_OF_MONTH) != eCal.get(Calendar.DAY_OF_MONTH)) {
                        if (eDay.equals(sDay)) {
                            _timeTextView.setText(new SimpleDateFormat("c d", Locale.getDefault()).format(sCal.getTime()) + " - ");
                            _time2TextView.setText(new SimpleDateFormat("c d", Locale.getDefault()).format(eCal.getTime()));
                            _extraTextView.setText(DateUtils.formatTime(sCal, false));
                            _extra2TextView.setText(DateUtils.formatTime(eCal, false));

                        } else {
                            _timeTextView.setText(sDay + " - ");
                            _time2TextView.setText(eDay);
                            _extraTextView.setText(DateUtils.formatTime(sCal, false));
                            _extra2TextView.setText(DateUtils.formatTime(eCal, false));
                        }
                    } else {
                        if (DateUtils.isToday(sCal)) {
                            _timeTextView.setText(R.string.today);
                            _extraTextView.setText(schedule.getFormatedTime());

                        } else if (DateUtils.isTomorrow(sCal)) {
                            _timeTextView.setText(R.string.tomorrow);
                            _extraTextView.setText(schedule.getFormatedTime());

                        } else {
                            _timeTextView.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(sCal.getTime()));
                            _extraTextView.setText(schedule.getFormatedTime());
                        }
                    }
                } else {
                    if (sCal.get(Calendar.MONTH) != eCal.get(Calendar.MONTH)
                            || sCal.get(Calendar.DAY_OF_MONTH) != eCal.get(Calendar.DAY_OF_MONTH)) {
                        if (sCal.get(Calendar.YEAR) != eCal.get(Calendar.YEAR)) {
                            _timeTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(sCal.getTime()) + " - ");
                            _time2TextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(eCal.getTime()));
                            _extraTextView.setText(new SimpleDateFormat("h:mma y", Locale.getDefault()).format(sCal.getTime()).toLowerCase());
                            _extra2TextView.setText(new SimpleDateFormat("h:mma y", Locale.getDefault()).format(eCal.getTime()).toLowerCase());

                        } else {
                            _timeTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(sCal.getTime()) + " - ");
                            _time2TextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(eCal.getTime()));
                            _extraTextView.setText(DateUtils.formatTime(sCal, false));
                            _extra2TextView.setText(DateUtils.formatTime(eCal, false));
                        }
                    } else {
                        _timeTextView.setText(new SimpleDateFormat("MMM d, y", Locale.getDefault()).format(sCal.getTime()));
                        _extraTextView.setText(schedule.getFormatedTime());
                    }
                }
            }
        } catch (Exception ex) {
            _timeTextView.setVisibility(INVISIBLE);
            _extraTextView.setVisibility(INVISIBLE);
        }
    }

    private void populatePay() {
        // formatting PAY
        // once paid, should be green (accent color)
        // if > 1000, then $1.xxK
        if (_workorder.getPay() == null || _workorder.getPay().hidePay()) {
            _priceTextView.setVisibility(INVISIBLE);
            _stateTextView.setVisibility(INVISIBLE);
            return;
        }

        _priceTextView.setTextColor(getResources().getColor(R.color.fn_dark_text));

        Pay pay = _workorder.getPay();
        if (_workorder.getWorkorderSubstatus() == WorkorderSubstatus.INREVIEW
                || _workorder.getWorkorderSubstatus() == WorkorderSubstatus.PENDINGREVIEW) {
            _stateTextView.setText(R.string.waiting_for_approval);
            _priceTextView.setText(misc.toCurrency(pay.getTotalPayment()));
            if (!_workorder.hasValidPaymentInfo()) {
                _priceTextView.setTextColor(getResources().getColor(R.color.fn_red));
                _stateTextView.setText(R.string.update_payment_info);
            }

        } else if (_workorder.getWorkorderSubstatus() == WorkorderSubstatus.APPROVED_PROCESSINGPAYMENT) {
            _stateTextView.setText(R.string.payment_pending);
            _priceTextView.setText(misc.toCurrency(pay.getTotalPayment()));
            if (!_workorder.hasValidPaymentInfo()) {
                _priceTextView.setTextColor(getResources().getColor(R.color.fn_red));
                _stateTextView.setText(R.string.update_payment_info);
            }

        } else if (_workorder.getWorkorderSubstatus() == WorkorderSubstatus.PAID) {
            _priceTextView.setTextColor(getResources().getColor(R.color.fn_accent_color));
            _stateTextView.setText(R.string.paid);
            _priceTextView.setText(misc.toCurrency(pay.getTotalPayment()));

        } else {
            if (pay.isBlendedRate()) {
                _price2TextView.setText(misc.toCurrency(pay.getBlendedStartRate()));
                _priceTextView.setText(misc.toCurrency(pay.getBlendedAdditionalRate()));
                _state2TextView.setText(getResources().getString(R.string.first_time_hours, pay.getBlendedFirstHours()));
                _stateTextView.setText(R.string.hourly_after);
                _price_layout.setGravity(Gravity.LEFT);
            } else if (pay.isFixedRate()) {
                _priceTextView.setText(misc.toCurrency(pay.getFixedAmount()));
                _stateTextView.setText(R.string.fixed);
            } else if (pay.isHourlyRate()) {
                _priceTextView.setText(misc.toCurrency(pay.getPerHour()));
                _stateTextView.setText(R.string.hourly);
            } else if (pay.isPerDeviceRate()) {
                _priceTextView.setText(misc.toCurrency(pay.getPerDevice()));
                _stateTextView.setText(R.string.per_device);
            }
        }
        _priceTextView.setVisibility(VISIBLE);
        _stateTextView.setVisibility(VISIBLE);
    }

    private void populateLeftActionButton() {
        // Left action button
        _remoteWorkTextView.setVisibility(GONE);
        switch (_workorder.getLeftButtonAction()) {
            case Workorder.BUTTON_ACTION_NONE:
                _leftButton.setVisibility(GONE);
                break;
            case Workorder.BUTTON_ACTION_MAP:
                Location location = _workorder.getLocation();
                if (location == null) {
                    _leftButton.setVisibility(GONE);
                    _remoteWorkTextView.setVisibility(VISIBLE);
                    _remoteWorkTextView.setText(R.string.btn_no_location);

                } else if (_workorder.getIsRemoteWork()) {
                    _leftButton.setVisibility(GONE);
                    _remoteWorkTextView.setVisibility(VISIBLE);
                    _remoteWorkTextView.setText(R.string.btn_remote_work);

                } else {
                    SharedPreferences settings = App.get().getSharedPreferences();
                    if (settings.getString(getResources().getString(R.string.pref_key_workorder_card_location), "city_state").equals("city_state")
                            || (location.getGeo() == null || _gpsLocation == null)) {
                        _leftButton.setVisibility(VISIBLE);
                        _leftButton.setText((location.getCity() + ", " + location.getState()).toUpperCase());

                    } else {
                        _leftButton.setVisibility(VISIBLE);

                        try {
                            Position siteLoc = new Position(location.getGeo().getLongitude(), location.getGeo().getLatitude());
                            Position myLoc = new Position(_gpsLocation.getLongitude(), _gpsLocation.getLatitude());
                            _leftButton.setText(myLoc.distanceTo(siteLoc) + " mi");
                        } catch (Exception ex) {
                            //Log.v(TAG, ex);
                            _leftButton.setText((location.getCity() + ", " + location.getState()).toUpperCase());
                        }
                    }

                }
                break;
            case Workorder.BUTTON_ACTION_REPORT_PROBLEM:
                _leftButton.setText(R.string.btn_report_problem);
                break;
            default:
                break;
        }
    }

    private void populateRightActionButton() {
        // Right action button
        _rightWhiteButton.setVisibility(GONE);
        _rightGreenButton.setVisibility(GONE);
        _rightOrangeButton.setVisibility(GONE);

        switch (_workorder.getRightButtonAction()) {
            case Workorder.BUTTON_ACTION_UPDATE_PAYMENT_INFO:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_update_payment_info);
                _rightWhiteButton.setEnabled(true);
                break;
            case Workorder.BUTTON_ACTION_NONE:
                if (_workorder.getWorkorderSubstatus() == WorkorderSubstatus.ONHOLD_ACKNOWLEDGED) {
                    _rightWhiteButton.setText(R.string.btn_on_hold);
                    _rightWhiteButton.setVisibility(VISIBLE);
                    _rightWhiteButton.setEnabled(false);
                }
                break;
            case Workorder.BUTTON_ACTION_REQUEST:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_request);
                _rightWhiteButton.setEnabled(true);
                break;
            case Workorder.BUTTON_ACTION_ACCEPT:
                _rightOrangeButton.setVisibility(VISIBLE);
                if (_workorder.isBundle())
                    _rightOrangeButton.setText(R.string.btn_accept_bundle);
                else
                    _rightOrangeButton.setText(R.string.btn_accept);
                break;
            case Workorder.BUTTON_ACTION_WITHDRAW_REQUEST:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_withdraw_request);
                _rightWhiteButton.setEnabled(true);
                break;
            case Workorder.BUTTON_ACTION_READY_TO_GO:
                _rightGreenButton.setVisibility(VISIBLE);
                _rightGreenButton.setText(R.string.btn_ready_to_go);
                break;
            case Workorder.BUTTON_ACTION_CONFIRM:
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText(R.string.btn_confirm);
                break;
            case Workorder.BUTTON_ACTION_CHECKIN:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_check_in);
                _rightWhiteButton.setEnabled(true);
                break;
            case Workorder.BUTTON_ACTION_RECOGNIZE_HOLD:
                _rightOrangeButton.setVisibility(VISIBLE);
                _rightOrangeButton.setText(R.string.brn_recognize_hold);
                break;
            case Workorder.BUTTON_ACTION_CHECKOUT:
                _rightGreenButton.setVisibility(VISIBLE);
                _rightGreenButton.setText(R.string.btn_check_out);
                break;
            case Workorder.BUTTON_ACTION_MARK_INCOMPLETE:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_mark_incomplete);
                _rightWhiteButton.setEnabled(true);
                break;
            case Workorder.BUTTON_ACTION_VIEW_PAYMENT:
                _rightWhiteButton.setVisibility(VISIBLE);
                _rightWhiteButton.setText(R.string.btn_view_payment);
                _rightWhiteButton.setEnabled(true);
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

        void actionUpdatePaymentInfo(WorkorderCardView view, Workorder workorder);
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

        @Override
        public void actionUpdatePaymentInfo(WorkorderCardView view, Workorder workorder) {
        }
    }
}