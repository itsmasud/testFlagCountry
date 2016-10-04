package com.fieldnation.ui.workorder.v2;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.Pay;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.mapbox.Position;
import com.fieldnation.ui.IconFontButton;
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
    private IconFontButton _secondary1Button;
    private IconFontButton _secondary2Button;
    private IconFontButton _secondary3Button;
    private IconFontButton _locationButton;
    private Button _primaryButton;


    // Data
    private WorkOrder _workOrder;
    private Location _location;
    private Listener _listener;

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

        _secondary1Button = (IconFontButton) findViewById(R.id.secondary1_button);
        _secondary2Button = (IconFontButton) findViewById(R.id.secondary2_button);
        _secondary3Button = (IconFontButton) findViewById(R.id.secondary3_button);
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

    public void setListener(Listener listener) {
        _listener = listener;
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
        _workTypeTextView.setText(""); // TODO, we don't have work order type data yet

        populateLocation();
        populatePay();
        populateTime();
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
                if (_workOrder.getSchedule().getRange().getType().equals("business")) {
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

    private final View.OnClickListener _right_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener _left1_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener _left2_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private final View.OnClickListener _left3_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

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

    private final CheckBox.OnClickListener _checkbox_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
//                _listener.onChecked(WorkOrderCard.this, _workOrder, _checkBox.isChecked());
            }
        }
    };

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ActivityResultClient.startActivity(
                    App.get(),
                    WorkorderActivity.makeIntentShow(App.get(), _workOrder.getId()),
                    R.anim.activity_slide_in_right,
                    R.anim.activity_slide_out_left);
        }
    };

    public interface Listener {
        void onChecked(WorkOrderCard view, WorkOrder workorder, boolean isChecked);
    }
}