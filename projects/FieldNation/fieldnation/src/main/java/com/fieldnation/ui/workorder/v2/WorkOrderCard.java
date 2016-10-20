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
import com.fieldnation.service.data.mapbox.Position;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.dialog.v2.EtaDialog;
import com.fieldnation.ui.dialog.v2.ReportProblemDialog;
import com.fieldnation.ui.dialog.v2.RunningLateDialog;
import com.fieldnation.ui.workorder.WorkorderActivity;

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
    private IconFontButton _contactButton;
    private IconFontButton _optionalButton;
    private IconFontButton _reportProblemButton;
    private IconFontButton _locationButton;
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

        _contactButton = (IconFontButton) findViewById(R.id.contact_button);

        _optionalButton = (IconFontButton) findViewById(R.id.secondary1_button);
        _reportProblemButton = (IconFontButton) findViewById(R.id.reportproblem_button);
        _locationButton = (IconFontButton) findViewById(R.id.location_button);
        _locationButton.setOnClickListener(_locationButton_onClick);
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

        _titleTextView.setText(_workOrder.getTitle());
        _workTypeTextView.setText(_workOrder.getType().toUpperCase());

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
                                        + " - " + new SimpleDateFormat("d", Locale.getDefault()).format(scal.getTime()));
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
            _locationButton.setVisibility(GONE);
        } else {
            if (location.getGeo() == null || _location == null) {
                _locationTextView.setText(location.getCityState());
                _distanceTextView.setVisibility(GONE);
                _locationButton.setVisibility(VISIBLE);
            } else {
                try {
                    Position siteLoc = new Position(location.getGeo().getLongitude(), location.getGeo().getLatitude());
                    Position myLoc = new Position(_location.getLongitude(), _location.getLatitude());
                    _locationTextView.setText(location.getCityState());
                    _distanceTextView.setVisibility(VISIBLE);
                    _distanceTextView.setText(myLoc.distanceTo(siteLoc) + " mi");
                    _locationButton.setVisibility(VISIBLE);
                } catch (Exception ex) {
                    _locationTextView.setText(location.getCityState());
                    _distanceTextView.setVisibility(GONE);
                    _locationButton.setVisibility(VISIBLE);
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
                && _workOrder.getPrimaryActions().length > 0
                && _workOrder.getPrimaryActions()[0] != null) {
            Action action = _workOrder.getPrimaryActions()[0];

            switch (action.getType()) {
                case CONFIRM:
                    _primaryButton.setVisibility(VISIBLE);
                    _primaryButton.setOnClickListener(_confirm_onClick);
                    _primaryButton.setText(R.string.btn_confirm);
                    break;
                case ON_MY_WAY:
                    _primaryButton.setVisibility(VISIBLE);
                    _primaryButton.setOnClickListener(_onMyWay_onClick);
                    _primaryButton.setText(R.string.btn_on_my_way);
                    break;
                case READY:
                    _primaryButton.setVisibility(VISIBLE);
                    _primaryButton.setOnClickListener(_readyToGo_onClick);
                    _primaryButton.setText(R.string.btn_ready_to_go);
                    break;
                case REPORT_PROBLEM:
                    _primaryButton.setVisibility(VISIBLE);
                    _primaryButton.setOnClickListener(_reportProblem_onClick);
                    _primaryButton.setText(R.string.btn_report_problem);
                    break;
                default:
                    break;
            }
        }

        if (_workOrder.getContacts() == null || _workOrder.getContacts().length == 0) {
            _contactButton.setText(R.string.icon_chat_solid);
            _contactButton.setOnClickListener(_messageBuyer_onClick);
        } else {
            _contactButton.setText(R.string.icon_phone_solid);
            _contactButton.setOnClickListener(_phone_onClick);
        }
        _reportProblemButton.setVisibility(VISIBLE);
        _reportProblemButton.setText(R.string.icon_problem_solid);
        _reportProblemButton.setOnClickListener(_reportProblem_onClick);

        _optionalButton.setVisibility(GONE);
        if (_workOrder.getSecondaryActions() != null && _workOrder.getSecondaryActions().length > 0) {
            for (int i = 0; i < _workOrder.getSecondaryActions().length; i++) {
                if (_workOrder.getSecondaryActions()[i].getType() == Action.ActionType.RUNNING_LATE) {
                    populateSecondaryButton(_optionalButton, _workOrder.getSecondaryActions()[i]);
                    break;
                }
            }
        }
//        if (_workOrder.getSecondaryActions() != null && _workOrder.getSecondaryActions().length > 0) {
//            for (int i = 0; i < _workOrder.getSecondaryActions().length && i < _secondaryButtons.length; i++) {
//                populateSecondaryButton(_secondaryButtons[i], _workOrder.getSecondaryActions()[i]);
//            }
//        }
        //populateSecondaryButton(_secondaryButtons[1], new Action(Action.ActionType.RUNNING_LATE));
        //populateSecondaryButton(_secondaryButtons[2], new Action(Action.ActionType.REPORT_PROBLEM));
    }

    // other icons
    // phone-solid
    // map-location-solid
    // chat-solid
    // circle-x-solid
    // problem-solid
    // time-issue-solid
    private void populateSecondaryButton(IconFontButton button, Action action) {
        switch (action.getType()) {
            case RUNNING_LATE:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_time_issue_solid);
                button.setOnClickListener(_runningLate_onClick);
                break;
/*
            case REPORT_PROBLEM:
                button.setVisibility(VISIBLE);
                button.setText(R.string.icon_problem_solid);
                button.setOnClickListener(_reportProblem_onClick);
                break;
*/
            default:
                button.setVisibility(GONE);
                break;
        }
    }

    private final View.OnClickListener _confirm_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            EtaDialog.Controller.show(App.get(), _workOrder.getId(), _workOrder.getSchedule(), EtaDialog.PARAM_DIALOG_TYPE_CONFIRM);
            try {
                GpsTrackingService.start(App.get(), System.currentTimeMillis() + 7200000); // 2 hours
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private final View.OnClickListener _onMyWay_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkorderClient.actionReadyToGo(App.get(), _workOrder.getId());
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
            //ReportIssueDialog.Controller.show(App.get(), _workOrder);
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

    private final View.OnClickListener _messageBuyer_onClick = new OnClickListener() {
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

    private final View.OnClickListener _locationButton_onClick = new View.OnClickListener() {
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