package com.fieldnation.ui.workorder.v2;

import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.Pay;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.mapbox.Position;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.IconFontTextView;
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
    private CheckBox _checkBox;
    private IconFontTextView _bundleIconFont;
    private TextView _titleTextView;
    private TextView _companyNameTextView;
    private TextView _timeTextView;
    private TextView _locationTextView;
    private TextView _workOrderIdTextView;

    private LinearLayout _payLeftLayout;
    private TextView _priceLeftTextView;
    private TextView _statusLeftTextView;

    private LinearLayout _payRightLayout;
    private TextView _priceRightTextView;
    private TextView _statusRightTextView;

    private IconFontButton _left1Button;
    private IconFontButton _left2Button;
    private IconFontButton _left3Button;

    private Button _action1Button;
    private Button _action2Button;
    private Button _action3Button;

    // Data
    private boolean _enableCheckbox = false;
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

        _checkBox = (CheckBox) findViewById(R.id.checkbox);
        _checkBox.setOnClickListener(_checkbox_onClick);
        _bundleIconFont = (IconFontTextView) findViewById(R.id.bundle_iconFont);
        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _companyNameTextView = (TextView) findViewById(R.id.companyName_textview);
        _timeTextView = (TextView) findViewById(R.id.time_textview);
        _locationTextView = (TextView) findViewById(R.id.location_textview);
        _workOrderIdTextView = (TextView) findViewById(R.id.workorderId_textview);

        _payLeftLayout = (LinearLayout) findViewById(R.id.payLeft_layout);
        _priceLeftTextView = (TextView) findViewById(R.id.priceLeft_textview);
        _statusLeftTextView = (TextView) findViewById(R.id.statusLeft_textview);

        _payRightLayout = (LinearLayout) findViewById(R.id.payRight_layout);
        _priceRightTextView = (TextView) findViewById(R.id.priceRight_textview);
        _statusRightTextView = (TextView) findViewById(R.id.statusRight_textview);

        _left1Button = (IconFontButton) findViewById(R.id.left1_button);
        _left1Button.setOnClickListener(_left1_onClick);

        _left2Button = (IconFontButton) findViewById(R.id.left2_button);
        _left2Button.setOnClickListener(_left2_onClick);

        _left3Button = (IconFontButton) findViewById(R.id.left3_button);
        _left3Button.setOnClickListener(_left3_onClick);

        _action1Button = (Button) findViewById(R.id.action1_button);
        _action1Button.setOnClickListener(_right_onClick);

        _action2Button = (Button) findViewById(R.id.action2_button);
        _action2Button.setOnClickListener(_right_onClick);

        _action3Button = (Button) findViewById(R.id.action3_button);
        _action3Button.setOnClickListener(_right_onClick);

        setOnClickListener(_this_onClick);
    }

    public void setData(WorkOrder workOrder, Location location) {
        _workOrder = workOrder;
        _location = location;

        populateUi();
    }

    public void enableCheckbox(boolean enable) {
        _enableCheckbox = enable;

        if (_checkBox != null)
            _checkBox.setVisibility(_enableCheckbox ? VISIBLE : GONE);
    }

    public void setChecked(boolean checked) {
        _checkBox.setEnabled(checked);
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

        if (_action1Button == null)
            return;

        _checkBox.setVisibility(_enableCheckbox ? VISIBLE : GONE);

        if (_workOrder.isBundle())
            _bundleIconFont.setVisibility(VISIBLE);
        else
            _bundleIconFont.setVisibility(GONE);

        _titleTextView.setText(_workOrder.getTitle());
        _companyNameTextView.setText(_workOrder.getOrg().getName());
        _workOrderIdTextView.setText("WO " + _workOrder.getId());

        populateLocation();
        populatePay();
        populateButtons();
        populateTime();
    }

    private void populateTime() {
        if (_workOrder.getSchedule() != null) {
            if (_workOrder.getSchedule().getEstimate() != null && _workOrder.getSchedule().getEstimate().getArrival() != null) {
                try {
                    Calendar cal = ISO8601.toCalendar(_workOrder.getSchedule().getEstimate().getArrival());
                    _timeTextView.setText(
                            new SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.getTime()).toUpperCase());
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

            } else if (_workOrder.getSchedule().getExact() != null) {
                try {
                    Calendar cal = ISO8601.toCalendar(_workOrder.getSchedule().getExact());
                    _timeTextView.setText(
                            new SimpleDateFormat("h:mm a", Locale.getDefault()).format(cal.getTime()).toUpperCase());
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }

            } else if (_workOrder.getSchedule().getRange() != null) {
                try {
                    Calendar scal = ISO8601.toCalendar(_workOrder.getSchedule().getRange().getBegin());
                    Calendar ecal = ISO8601.toCalendar(_workOrder.getSchedule().getRange().getEnd());
                    _timeTextView.setText(
                            new SimpleDateFormat("h:mm a", Locale.getDefault()).format(scal.getTime()).toUpperCase()
                                    + " - " + new SimpleDateFormat("h:mm a", Locale.getDefault()).format(ecal.getTime()).toUpperCase());
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            } else {
                _timeTextView.setText("");
            }
        }
    }

    private void populateLocation() {
        com.fieldnation.data.v2.Location location = _workOrder.getLocation();
        if (location == null)
            _locationTextView.setText(R.string.remote_work);
        else {
            if (location.getGeo() == null || _location == null) {
                _locationTextView.setText(location.getFullAddressOneLine());
            } else {
                try {
                    Position siteLoc = new Position(location.getGeo().getLongitude(), location.getGeo().getLatitude());
                    Position myLoc = new Position(_location.getLongitude(), _location.getLatitude());
                    _locationTextView.setText(location.getFullAddressOneLine() + " (" + myLoc.distanceTo(siteLoc) + "mi)");
                } catch (Exception ex) {
                    //Log.v(TAG, ex);
                    _locationTextView.setText(location.getFullAddressOneLine());
                }
            }
        }
    }

    private void populatePay() {
        _payLeftLayout.setVisibility(GONE);
        if (_workOrder.getPay() == null) {
            _payRightLayout.setVisibility(INVISIBLE);
            return;
        }

        Pay pay = _workOrder.getPay();
        if (misc.isEmptyOrNull(pay.getType())) {
            Log.v(TAG, "PAY TYPE IS BAD!!! " + _workOrder.getId());
            _payRightLayout.setVisibility(INVISIBLE);
            return;
        }

        LinearLayout.LayoutParams params;
        params = ((LinearLayout.LayoutParams) _statusRightTextView.getLayoutParams());
        params.gravity = Gravity.RIGHT;
        _statusRightTextView.setLayoutParams(params);
        params = ((LinearLayout.LayoutParams) _priceRightTextView.getLayoutParams());
        params.gravity = Gravity.RIGHT;
        _priceRightTextView.setLayoutParams(params);

        switch (pay.getType()) {
            case "blended":
                _payLeftLayout.setVisibility(VISIBLE);
                _payRightLayout.setVisibility(VISIBLE);

                params = ((LinearLayout.LayoutParams) _statusRightTextView.getLayoutParams());
                params.gravity = Gravity.LEFT;
                _statusRightTextView.setLayoutParams(params);
                params = ((LinearLayout.LayoutParams) _priceRightTextView.getLayoutParams());
                params.gravity = Gravity.LEFT;
                _priceRightTextView.setLayoutParams(params);

                _priceLeftTextView.setText(misc.toCurrency(pay.getAmount()));
                _statusLeftTextView.setText(getResources().getString(R.string.first_time_hours, pay.getUnits() + ""));
                _priceRightTextView.setText(misc.toCurrency(pay.getAdditionalAmount()));
                _statusRightTextView.setText(R.string.hourly_after);
                break;
            case "fixed":
                _payRightLayout.setVisibility(VISIBLE);
                _priceRightTextView.setText(misc.toCurrency(pay.getAmount()));
                _statusRightTextView.setText(R.string.fixed);
                break;
            case "hourly":
                _payRightLayout.setVisibility(VISIBLE);
                _priceRightTextView.setText(misc.toCurrency(pay.getAmount()));
                _statusRightTextView.setText(R.string.hourly);
                break;
            case "device":
                _payRightLayout.setVisibility(VISIBLE);
                _priceRightTextView.setText(misc.toCurrency(pay.getAmount()));
                _statusRightTextView.setText(R.string.per_device);
                break;
            default:
                Log.v(TAG, "PAY TYPE:" + pay.getType());
                break;
        }
    }

    private void populateButtons() {
        //* Assigned
        // location
        // chat
        // alert?


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

    private final CheckBox.OnClickListener _checkbox_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.onChecked(WorkOrderCard.this, _workOrder, _checkBox.isChecked());
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