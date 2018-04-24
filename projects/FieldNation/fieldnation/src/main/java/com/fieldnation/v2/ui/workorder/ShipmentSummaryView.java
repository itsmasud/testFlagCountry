package com.fieldnation.v2.ui.workorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionUtils;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.ShipmentListDialog;

/**
 * Created by mc on 10/10/17.
 */

public class ShipmentSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "ShipmentSummaryView";

    // Ui
    private TextView _titleTextView;
    private TextView _countTextView;

    //Data
    private WorkOrder _workOrder;

    public ShipmentSummaryView(Context context) {
        super(context);
        init();
    }

    public ShipmentSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShipmentSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_task_summary_row, this, true);

        if (isInEditMode()) return;

        _titleTextView = findViewById(R.id.title_textview);
        _countTextView = findViewById(R.id.count_textview);
        _countTextView.setBackgroundResource(R.drawable.round_rect_gray);

        setVisibility(GONE);

        populateUi();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCAST_ON_CHANGE));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LocalBroadcastManager.getInstance(App.get()).unregisterReceiver(_webTransactionChanged);
    }

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String op = intent.getStringExtra("op");
            if (intent.hasExtra("key")) {
                String key = intent.getStringExtra("key");
                if (key == null || key.contains("addShipmentByWorkOrder") || key.contains("deleteShipmentByWorkOrderAndShipment"))
                    populateUi();
            } else if (op.equals("delete") || op.equals("deleteAll")) {
                populateUi();
            }
        }
    };


    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null || _countTextView == null)
            return;

        _titleTextView.setText("Shipments");

        int addSize = WebTransaction.findByKey(WebTransactionUtils.WEB_TRANS_KEY_PREFIX_ADD_SHIPMENT + _workOrder.getId() + "/%").size();
        int delSize = WebTransaction.findByKey(WebTransactionUtils.WEB_TRANS_KEY_PREFIX_DELETE_SHIPMENT + _workOrder.getId() + "/%").size();

        if ((_workOrder.getShipments() == null
                || _workOrder.getShipments().getResults() == null
                || _workOrder.getShipments().getResults().length == 0)
                && addSize == 0 && delSize == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _countTextView.setText((_workOrder.getShipments().getResults().length  + addSize - delSize) + "");
        setOnClickListener(_this_onClick);
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            ShipmentListDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
