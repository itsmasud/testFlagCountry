package com.fieldnation.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.ui.IconFontButton;
import com.fieldnation.ui.IconFontTextView;

/**
 * Created by Michael on 7/26/2016.
 */
public class WorkOrderCard extends RelativeLayout {
    private static final String TAG = "WorkOrderCard";

    // Ui
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

    private Button _rightWhiteButton;
    private Button _rightOrangeButton;
    private Button _rightGreenButton;

    // Data
    private WorkOrder _workOrder;

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

        _rightWhiteButton = (Button) findViewById(R.id.rightWhite_button);
        _rightWhiteButton.setOnClickListener(_right_onClick);

        _rightOrangeButton = (Button) findViewById(R.id.rightOrange_button);
        _rightOrangeButton.setOnClickListener(_right_onClick);

        _rightGreenButton = (Button) findViewById(R.id.rightGreen_button);
        _rightGreenButton.setOnClickListener(_right_onClick);
    }

    public void setData(WorkOrder workOrder) {
        _workOrder = workOrder;

        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_rightGreenButton == null)
            return;

        _bundleIconFont.setVisibility(GONE);
        _titleTextView.setText(_workOrder.getTitle());
        _companyNameTextView.setText(_workOrder.getOrg().getName());
        _workOrderIdTextView.setText("WO " + _workOrder.getId());

        populateLocation();
        populatePay();
        populateButtons();
    }

    private void populateLocation() {

        _locationTextView.setText(_workOrder.getLocation().getFullAddressOneLine());
    }

    private void populatePay() {

    }

    private void populateButtons() {

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

}
