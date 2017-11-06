package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTwoHorizView;
import com.fieldnation.v2.ui.dialog.TimeLogListDialog;

/**
 * Created by Shoaib on 10/20/17.
 */

public class TimeLogSummaryView extends RelativeLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "TimeLogSummaryView";

    // Ui
    private ListItemTwoHorizView _summaryView;

    // Data
    private WorkOrder _workOrder;
    private String _myUUID;

    public TimeLogSummaryView(Context context) {
        super(context);
        init();
    }

    public TimeLogSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeLogSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_timelog_summary_view, this, true);

        if (isInEditMode())
            return;

        _summaryView = findViewById(R.id.summary_view);
        setVisibility(GONE);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    public void setUUID(String uuid) {
        _myUUID = uuid;
    }

    private void populateUi() {
        if (_summaryView == null) return;

        if (_workOrder == null) return;

        if (_workOrder.getTimeLogs() == null
                || _workOrder.getTimeLogs().getResults() == null
                || _workOrder.getTimeLogs().getResults().length == 0) {
            setVisibility(GONE);
            return;
        }


        if (_workOrder.getTimeLogs().getHours() == null) {
            setVisibility(GONE);
            return;
        } else if (_workOrder.getPay() != null
                && _workOrder.getPay().getType() != null
                && _workOrder.getPay().getType() == Pay.TypeEnum.DEVICE) {
            _summaryView.set(_summaryView.getContext().getString(R.string.devices_complete),
                    String.valueOf(_workOrder.getPay().getNumberOfDevices()));
            setVisibility(VISIBLE);
        } else if (_workOrder.getTimeLogs() != null
                && _workOrder.getTimeLogs().getHours() != null) {
            _summaryView.set(_summaryView.getContext().getString(R.string.time_logged),
                    String.format("%.2f", _workOrder.getTimeLogs().getHours()) + " hrs");
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }

        setOnClickListener(_this_onClick);
    }

    private final View.OnClickListener _this_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View view) {
            final String dialogTitle = _workOrder.getPay().getType().equals(Pay.TypeEnum.DEVICE) ? _summaryView.getContext().getString(R.string.devices_complete) : _summaryView.getContext().getString(R.string.time_logged);
            TimeLogListDialog.show(App.get(), null, _myUUID, _workOrder.getId(), dialogTitle);
        }
    };

}
