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
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.DiscountListDialog;

/**
 * Created by mc on 10/9/17.
 */

public class DiscountSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "DiscountSummaryView";

    // Ui
    private TextView _titleTextView;
    private TextView _countTextView;

    //Data
    private WorkOrder _workOrder;

    public DiscountSummaryView(Context context) {
        super(context);
        init();
    }

    public DiscountSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiscountSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
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
            populateUi();
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

        _titleTextView.setText("Discounts");

        int addSize = WebTransaction.findByKey(WebTransactionUtils.WEB_TRANS_KEY_PREFIX_ADD_DISCOUNT + _workOrder.getId() + "/%").size();
        int delSize = WebTransaction.findByKey(WebTransactionUtils.WEB_TRANS_KEY_PREFIX_DELETE_DISCOUNT + _workOrder.getId() + "/%").size();

        if ((_workOrder.getPay() == null
                || _workOrder.getPay().getDiscounts() == null
                || _workOrder.getPay().getDiscounts().getResults() == null
                || _workOrder.getPay().getDiscounts().getResults().length == 0)
                && addSize == 0 && delSize == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _countTextView.setText((
                _workOrder.getPay().getDiscounts().getResults().length + addSize - delSize) + "");
        setOnClickListener(_this_onClick);
    }

    private final View.OnClickListener _this_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View view) {
            DiscountListDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
