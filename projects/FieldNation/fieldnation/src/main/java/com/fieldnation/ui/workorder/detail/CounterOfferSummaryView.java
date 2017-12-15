package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.ListItemTwoHorizView;
import com.fieldnation.v2.ui.dialog.CounterOfferDialog;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

/**
 * Created by Michael Carver on 6/5/2015.
 */
public class CounterOfferSummaryView extends LinearLayout implements WorkOrderRenderer {
    private static final String TAG = "CounterOfferSummaryView";

    // UI
    private ListItemTwoHorizView _viewCoView;

    // Data
    private WorkOrder _workOrder;

    public CounterOfferSummaryView(Context context) {
        super(context);
        init();
    }

    public CounterOfferSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CounterOfferSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_counter_offer_summary, this);

        if (isInEditMode())
            return;

        _viewCoView = findViewById(R.id.viewCo_view);
        _viewCoView.set("View Counter Offer", null);
        _viewCoView.setOnClickListener(_counterOffer_onClick);
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_viewCoView == null)
            return;

        Requests requests = _workOrder.getRequests();

        setVisibility(GONE);

        if (requests.getOpenRequest().getId() == null)
            return;

        setVisibility(VISIBLE);
    }

    private final View.OnClickListener _counterOffer_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {

            Requests requests = _workOrder.getRequests();

            Request request = requests.getOpenRequest();

            CounterOfferDialog.show(App.get(), _workOrder.getId(), true);
        }
    };
}