package com.fieldnation.v2.ui.workorder;

import android.content.Context;
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
import com.fieldnation.v2.ui.dialog.AttachedFilesDialog;

import java.util.List;

/**
 * Created by mc on 5/24/17.
 */

public class FailedUploadsView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "FailedUploadsView";

    // Ui
    private TextView _countTextView;
    private TextView _titleTextView;

    // Data
    private WorkOrder _workOrder;

    public FailedUploadsView(Context context) {
        super(context);
        init();
    }

    public FailedUploadsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FailedUploadsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_v2_wod_alert_summary, this);

        if (isInEditMode())
            return;

        _countTextView = findViewById(R.id.count_textview);
        _titleTextView = findViewById(R.id.title_textview);
        _titleTextView.setText("Failed Uploads");

        setOnClickListener(_this_onClick);
        setVisibility(GONE);

        populateUi();
    }

    @Override
    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_countTextView == null)
            return;

        if (_workOrder.getAttachments() != null)
            WebTransaction.cleanZombies(_workOrder.getAttachments());
        
        List<WebTransaction> zombies = WebTransaction.getZombies();

        int count = 0;
        for (WebTransaction zombie : zombies) {
            try {
                TransactionParams params = TransactionParams.fromJson(new JsonObject(zombie.getListenerParams()));
                JsonObject methodParams = new JsonObject(params.methodParams);

                if (methodParams.has("workOrderId")) {
                    if (methodParams.getInt("workOrderId") == _workOrder.getId())
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

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AttachedFilesDialog.show(App.get(), null, _workOrder.getId());
        }
    };
}
