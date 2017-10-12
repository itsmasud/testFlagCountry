package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.SignatureListDialog;

/**
 * Created by Shoaib on 10/11/17.
 */

public class SignatureSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "SignatureSummaryView";

    // Ui
    private TextView _titleTextView;
    private TextView _countTextView;

    //Data
    private WorkOrder _workOrder;

    public SignatureSummaryView(Context context) {
        super(context);
        init();
    }

    public SignatureSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null || _countTextView == null)
            return;

        _titleTextView.setText("Signatures");

        final Signature[] list = _workOrder.getSignatures().getResults();

        if (list == null || list.length == 0) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);
        _countTextView.setText(String.valueOf(list.length));
        setOnClickListener(_this_onClick);
    }

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            SignatureListDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
