package com.fieldnation.ui.workorder.v2;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.Contact;
import com.fieldnation.data.v2.Pay;
import com.fieldnation.data.v2.Range;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.data.v2.actions.Action;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.GpsTrackingService;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.gmaps.Position;
import com.fieldnation.service.data.v2.workorder.WorkOrderClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.dialog.v2.CheckInOutDialog;
import com.fieldnation.ui.dialog.v2.DeclineDialog;
import com.fieldnation.ui.dialog.v2.EtaDialog;
import com.fieldnation.ui.dialog.v2.MarkIncompleteWarningDialog;
import com.fieldnation.ui.dialog.v2.ReportProblemDialog;
import com.fieldnation.ui.dialog.v2.RunningLateDialog;
import com.fieldnation.ui.dialog.v2.WithdrawRequestDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderBundleDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Michael on 7/26/2016.
 */
public class WorkOrderCard extends RelativeLayout {
    private static final String TAG = "WorkOrderCard";

    // Ui
    private TextView _amountTextView;
    private TextView _payTypeTextView;
    private TextView _workTypeTextView;
    private TextView _titleTextView;
    private TextView _dateTextView;
    private TextView _timeTextView;
    private TextView _hyphenTextView;
    private TextView _date2TextView;
    private TextView _time2TextView;
    private TextView _locationTextView;
    private TextView _distanceTextView;
    private IconFontButton[] _secondaryButtons = new IconFontButton[4];
    private Button _primaryButton;

    // Data
    private WorkOrder _workOrder;
    private Location _location;

    public WorkOrderCard(Context context) {
        super(context);
        init();
    }

    public WorkOrderCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkOrderCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_work_order_card, this);

        if (isInEditMode())
            return;

        _amountTextView = (TextView) findViewById(R.id.amount_textview);
        _payTypeTextView = (TextView) findViewById(R.id.paytype_textview);
        _workTypeTextView = (TextView) findViewById(R.id.worktype_textview);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _dateTextView = (TextView) findViewById(R.id.date_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _hyphenTextView = (TextView) findViewById(R.id.hyphen_textview);
        _time2TextView = (TextView) findViewById(R.id.time2_textview);
        _date2TextView = (TextView) findViewById(R.id.date2_textview);
        _locationTextView = (TextView) findViewById(R.id.location_textview);
        _distanceTextView = (TextView) findViewById(R.id.distance_textview);


        _secondaryButtons[0] = (IconFontButton) findViewById(R.id.secondary1_button);
        _secondaryButtons[1] = (IconFontButton) findViewById(R.id.secondary2_button);
        _secondaryButtons[2] = (IconFontButton) findViewById(R.id.secondary3_button);
        _secondaryButtons[3] = (IconFontButton) findViewById(R.id.secondary4_button);

        _primaryButton = (Button) findViewById(R.id.primary_button);

        setOnClickListener(_this_onClick);
    }

    public void setData(WorkOrder workOrder, Location location) {
        _workOrder = workOrder;
        _location = location;

        populateUi();
    }

    public WorkOrder getWorkOrder() {
        return _workOrder;
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_primaryButton == null)
            return;

        _titleTextView.setText(_workOrder.getId() + " | " + _workOrder.getTitle());
        if (!misc.isEmptyOrNull(_workOrder.getType())) {
            _workTypeTextView.setText(_workOrder.getType().toUpperCase());
        } else {
            _workTypeTextView.setText("");
        }

        populateLocation();
        populatePay();
        populateTime();
        populateButtons();
    }

    private void populateTime() {
        _timeTextView.setVisibility(VISIBLE);
        _dateTextView.setVisibility(VISIBLE);
        _hyphenTextView.setVisibility(GONE);
        _time2TextView.setVisibility(GONE);
        _date2TextView.setVisibility(GONE);
        if (_workOrder.getSchedule() != null) {
            if (_workOrder.getSchedule().getEstimate() != null && _workOrder.getSchedule().getEstimate().getArrival() != null) {
                // estimated
                try {
                    Calendar cal = ISO8601.toCalendar(_workOrder.getSchedule().getEstimate().getArrival());
                    _timeTextView.setText(
                            new SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.getTime()));

                    _dateTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(cal.getTime()));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    _timeTextView.setVisibility(GONE);
                    _dateTextView.setVisibility(GONE);
                }

                // exact
            } else if (_workOrder.getSchedule().getExact() != null) {
                try {
                    Calendar cal = ISO8601.toCalendar(_workOrder.getSchedule().getExact());
                    _timeTextView.setText(
                            new SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.getTime()));
                    _dateTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(cal.getTime()));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    _timeTextView.setVisibility(GONE);
                    _dateTextView.setVisibility(GONE);
                }

                // range
            } else if (_workOrder.getSchedule().getRange() != null) {
                if (_workOrder.getSchedule().getRange().getType() == Range.Type.BUSINESS) {
                    // business
                    try {
                        Calendar scal = ISO8601.toCalendar(_workOrder.getSchedule().getRange().getBegin());
                        Calendar ecal = ISO8601.toCalendar(_workOrder.getSchedule().getRange().getEnd());
                        _timeTextView.setText(
                                new SimpleDateFormat("h:mm a", Locale.getDefault()).format(scal.getTime())
                                        + " - " + new SimpleDateFormat("h:mm a", Locale.getDefault()).format(ecal.getTime()));

                        _dateTextView.setText(
                                new SimpleDateFormat("MMM d", Locale.getDefault()).format(scal.getTime())
                                        + " - " + new SimpleDateFormat("d", Locale.getDefault()).format(ecal.getTime()));
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                        _timeTextView.setVisibility(GONE);
                        _dateTextView.setVisibility(GONE);
                    }

                } else {
                    // normal range
                    try {
                        Calendar scal = ISO8601.toCalendar(_workOrder.getSchedule().getRange().getBegin());
                        Calendar ecal = ISO8601.toCalendar(_workOrder.getSchedule().getRange().getEnd());
                        _timeTextView.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(scal.getTime()));
                        _dateTextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(scal.getTime()));
                        _time2TextView.setText(new SimpleDateFormat("h:mm a", Locale.getDefault()).format(ecal.getTime()));
                        _date2TextView.setText(new SimpleDateFormat("MMM d", Locale.getDefault()).format(ecal.getTime()));
                        _hyphenTextView.setVisibility(VISIBLE);
                        _time2TextView.setVisibility(VISIBLE);
                        _date2TextView.setVisibility(VISIBLE);
                    } catch (Exception ex) {
                        Log.v(TAG, ex);
                        _timeTextView.setVisibility(GONE);
                        _dateTextView.setVisibility(GONE);
                    }
                }
            } else {
                _timeTextView.setText("");
            }
        }
    }

    private void populateLocation() {
        com.fieldnation.data.v2.Location location = _workOrder.getLocation();
        if (location == null) {
            _locationTextView.setText(R.string.remote_work);
            _distanceTextView.setVisibility(GONE);
        } else {
            if (location.getGeo() == null || _location == null) {
                _locationTextView.setText(location.getCityState());
                _distanceTextView.setVisibility(GONE);
            } else {
                try {
                    Position siteLoc = new Position(location.getGeo().getLongitude(), location.getGeo().getLatitude());
                    Position myLoc = new Position(_location.getLongitude(), _location.getLatitude());
                    _locationTextView.setText(location.getCityState());
                    _distanceTextView.setVisibility(VISIBLE);
                    _distanceTextView.setText(myLoc.distanceTo(siteLoc) + " mi");
                } catch (Exception ex) {
                    _locationTextView.setText(location.getCityState());
                    _distanceTextView.setVisibility(GONE);
                }
            }
        }
    }

    private void populatePay() {
        if (_workOrder.getPay() == null || _workOrder.getPay().getType() == null) {
            _payTypeTextView.setVisibility(GONE);
            _amountTextView.setVisibility(GONE);
            return;
        }

        Pay pay = _workOrder.getPay();

        _payTypeTextView.setVisibility(VISIBLE);
        _amountTextView.setVisibility(VISIBLE);

        switch (pay.getType()) {
            case "fixed":
                _amountTextView.setText(misc.toShortCurrency(pay.getAmount()));
                _payTypeTextView.setText("FIXED");
                break;
            case "hourly":
                _amountTextView.setText(misc.toShortCurrency(pay.getAmount()));
                _payTypeTextView.setText("/ HOURLY");
                break;
            case "blended":
                _amountTextView.setText(misc.toShortCurrency(pay.getAmount() * pay.getUnits() + pay.getAdditionalAmount()));
                _payTypeTextView.setText("BLENDED");
                break;
            case "device":
                _amountTextView.setText(misc.toShortCurrency(pay.getAmount()));
                _payTypeTextView.setText("/ DEVICE");
                break;
            default:
                _payTypeTextView.setVisibility(GONE);
                _amountTextView.setVisibility(GONE);
                break;
        }
    }

    private void populateButtons() {
        // Primary actions
        _primaryButton.setVisibility(GONE);
        if (_workOrder.getPrimaryActions() != null
                && _workOrder.getPrimaryActions().length > 0) {

            Action[] actions = _workOrder.getPrimaryActions();
            if (actions != null) {
                for (Action action : actions) {
                    if (populatePrimaryButton(_primaryButton, action))
                        break;
                }
            }
        }

        for (Button button : _secondaryButtons) {
            button.setVisibility(GONE);
        }

        if (_workOrder.getSecondaryActions() != null && _workOrder.getSecondaryActions().length > 0) {
            int i = 0; // action index
            int j = 0; // button index
            // assign supported actions to buttons until no more actions or no more buttons
            Action[] actions = _workOrder.getSecondaryActions();
            while (i < actions.length && j < _secondaryButtons.length) {
                Action action = actions[i];

                // only if the action has been assigned do we move to the next button
                if (populateSecondaryButton(_secondaryButtons[j], action)) {
                    j++;
                }
                i++;
            }
        }
    }

    private boolean populatePrimaryButton(Button button, Action action) {
        switch (action.getType()) {
            case ACCEPT:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_accept_onClick);
                button.setText("ACCEPT");
                break;
            case CONFIRM:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_confirm_onClick);
                button.setText("CONFIRM");
                break;
            case ON_MY_WAY:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_onMyWay_onClick);
                button.setText(R.string.btn_on_my_way);
                break;
            case READY:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_readyToGo_onClick);
                button.setText("CONFIRM");
                break;
            case READY_TO_GO:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_readyToGo_onClick);
                button.setText("READY");
                break;
            case REPORT_PROBLEM:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_reportProblem_onClick);
                button.setText(R.string.btn_report_problem);
                break;
            case REQUEST:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_request_onClick);
                button.setText("REQUEST");
                break;
            case VIEW_BUNDLE:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_viewBundle_onClick);
                button.setText("VIEW BUNDLE (" + _workOrder.getBundle().getCount() + ")");
                break;
            case ACK_HOLD:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_ackHold_onClick);
                button.setText("ACKNOWLEDGE HOLD");
                break;
            case WITHDRAW:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_withdraw_onClick);
                button.setText("WITHDRAW");
                break;
            case CHECK_IN:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_checkIn_onClick);
                button.setText("CHECK IN");
                break;
            case CHECK_OUT:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_checkOut_onClick);
                button.setText("CHECK OUT");
                break;
            case MARK_INCOMPLETE:
                button.setVisibility(VISIBLE);
                button.setOnClickListener(_incomplete_onClick);
                button.setText("INCOMPLETE");
                break;
/*
                case MARK_COMPLETE:
                    button.setVisibility(VISIBLE);
                    button.setOnClickListener(_complete_onClick);
                    button.setText("COMPLETE");
                    break;
*/
/*              // don't have a payment id in the current data structure
                case VIEW_PAYMENT:
                    button.setVisibility(VISIBLE);
                    button.setOnClickListener(_viewPayment_onClick);
                    button.setText("VIEW PAYMENT");
                    break;
*/
            default:
                return false;
        }
        return true;
    }

    // other icons
    // phone-solid
    // map-location-solid
    // chat-solid
    // circle-x-solid
    // problem-solid
    // time-issue-solid
    private boolean populateSecondaryButton(IconFontButton button, Action action) {
        switch (action.getType()) {
            case DECLINE:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_circle_x_solid);
                button.setOnClickListener(_decline_onClick);
                break;
            case RUNNING_LATE:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_time_issue_solid);
                button.setOnClickListener(_runningLate_onClick);
                break;
            case REPORT_PROBLEM:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_problem_solid);
                button.setOnClickListener(_reportProblem_onClick);
                break;
            case PHONE:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_phone_solid);
                button.setOnClickListener(_phone_onClick);
                break;
            case MESSAGE:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_chat_solid);
                button.setOnClickListener(_message_onClick);
                break;
            case MAP:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_map_location_solid);
                button.setOnClickListener(_map_onClick);
                break;
            default:
                button.setVisibility(GONE);
                return false;
        }
        return true;
    }

    private final View.OnClickListener _incomplete_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MarkIncompleteWarningDialog.Controller.show(App.get(), _workOrder.getId());
        }
    };

    private final View.OnClickListener _ackHold_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderClient.actionAcknowledgeHold(App.get(), _workOrder.getId());
        }
    };

    private final View.OnClickListener _checkIn_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_workOrder.getPay() != null && _workOrder.getPay().getType().equals("device")) {
                CheckInOutDialog.Controller.show(App.get(), null, _workOrder.getId(), _location,
                        _workOrder.getPay().getUnits().intValue(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
            } else {
                CheckInOutDialog.Controller.show(App.get(), null, _workOrder.getId(), _location,
                        CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_IN);
            }
        }
    };

    private final View.OnClickListener _checkOut_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_workOrder.getPay() != null && _workOrder.getPay().getType().equals("device")) {
                CheckInOutDialog.Controller.show(App.get(), null, _workOrder.getId(), _location,
                        _workOrder.getPay().getUnits().intValue(), CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            } else {
                CheckInOutDialog.Controller.show(App.get(), null, _workOrder.getId(), _location,
                        CheckInOutDialog.PARAM_DIALOG_TYPE_CHECK_OUT);
            }
        }
    };

    private final View.OnClickListener _request_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EtaDialog.Controller.show(App.get(), _workOrder.getId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_REQUEST);
        }
    };

    private final View.OnClickListener _accept_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            EtaDialog.Controller.show(App.get(), _workOrder.getId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_ACCEPT);
        }
    };

    private final View.OnClickListener _confirm_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            WorkorderClient.actionReadyToGo(App.get(), _workOrder.getId());
            EtaDialog.Controller.show(App.get(), _workOrder.getId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);

        }
    };

    private final View.OnClickListener _decline_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            DeclineDialog.Controller.show(App.get(), null, _workOrder.getId(), _workOrder.getOrg().getId());
        }
    };

    private final View.OnClickListener _onMyWay_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_location != null)
                WorkOrderClient.actionOnMyWay(App.get(), _workOrder.getId(), _location.getLatitude(), _location.getLongitude());
            else
                WorkOrderClient.actionOnMyWay(App.get(), _workOrder.getId(), null, null);

            try {
                GpsTrackingService.start(App.get(), System.currentTimeMillis() + 3600000); // 1 hours
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private final View.OnClickListener _viewBundle_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderBundleDetailActivity.startNew(App.get(), _workOrder.getId(), _workOrder.getBundle().getId());
        }
    };

    private final View.OnClickListener _readyToGo_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderClient.actionReadyToGo(App.get(), _workOrder.getId());
        }
    };

    private final View.OnClickListener _reportProblem_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ReportProblemDialog.Controller.show(App.get(), _workOrder.getId());
        }
    };

    private final View.OnClickListener _phone_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Contact contact = _workOrder.getContacts()[0];
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(callIntent);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final View.OnClickListener _withdraw_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WithdrawRequestDialog.Controller.show(App.get(), _workOrder.getId());
        }
    };

    private final View.OnClickListener _message_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderActivity.startNew(App.get(), _workOrder.getId(), WorkorderActivity.TAB_MESSAGE);
        }
    };

    private final View.OnClickListener _runningLate_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            RunningLateDialog.Controller.show(App.get(), _workOrder);
        }
    };

    private final View.OnClickListener _map_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_workOrder != null) {
                com.fieldnation.data.v2.Location location = _workOrder.getLocation();
                if (location != null) {
                    try {
                        String _fullAddress = misc.escapeForURL(location.getFullAddressOneLine());
                        String _uriString = "geo:0,0?q=" + _fullAddress;
                        Uri _uri = Uri.parse(_uriString);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(_uri);
                        ActivityResultClient.startActivity(App.get(), intent);
                    } catch (Exception e) {
                        Log.v(TAG, e);
                        ToastClient.toast(App.get(), "Could not start map", Toast.LENGTH_SHORT);
                    }
                }
            }
        }
    };

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
//            EtaDialog.Controller.show(App.get(), _workOrder.getId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
            ActivityResultClient.startActivity(
                    App.get(),
                    WorkorderActivity.makeIntentShow(App.get(), _workOrder.getId()),
                    R.anim.activity_slide_in_right,
                    R.anim.activity_slide_out_left);
        }
    };
}