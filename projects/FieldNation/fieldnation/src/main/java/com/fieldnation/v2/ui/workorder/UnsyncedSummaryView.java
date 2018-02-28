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
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.UnsyncedActivity;

import java.util.List;

/**
 * Created by michaelcarver on 2/23/18.
 */

public class UnsyncedSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "UnsyncedSummaryView";

    // Ui
    private TextView _titleTextView;
    private TextView _countTextView;

    // Data
    private WorkOrder _workOrder;

    public UnsyncedSummaryView(Context context) {
        super(context);
        init();
    }

    public UnsyncedSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnsyncedSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_wod_alert_summary, this);

        if (isInEditMode())
            return;

        _titleTextView = findViewById(R.id.title_textview);
        _titleTextView.setText("Unsynced Activity");

        _countTextView = findViewById(R.id.count_textview);

        setOnClickListener(_this_onClick);
        setVisibility(GONE);

        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCASE_ON_CHANGE));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LocalBroadcastManager.getInstance(App.get()).unregisterReceiver(_webTransactionChanged);
    }

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateUi();
        }
    };

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_countTextView == null)
            return;

        List<WebTransaction> list = WebTransaction.getSyncing();
        int count = 0;
        for (WebTransaction webTransaction : list) {
            try {
                TransactionParams tl = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                int workOrderId = tl.getMethodParamInt("workOrderId");
                if (workOrderId == _workOrder.getId()) {
                    count++;
                }
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        if (count == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _countTextView.setText(count + "");
    }

    private final View.OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            UnsyncedActivity.startNew(App.get());
        }
    };

}
