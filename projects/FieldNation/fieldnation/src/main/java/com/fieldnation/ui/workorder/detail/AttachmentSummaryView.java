package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.AttachedFilesDialog;
import com.fieldnation.v2.ui.workorder.WorkOrderRenderer;

/**
 * Created by shoaib.ahmed on 08/02/2017.
 */

public class AttachmentSummaryView extends RelativeLayout implements WorkOrderRenderer {
    private static final String TAG = "AttachmentSummaryView";

    // Ui
    private View _attachmentView;
    private TextView _countTextView;

    // Data
    private WorkOrder _workOrder;

    public AttachmentSummaryView(Context context) {
        super(context);
        init();
    }

    public AttachmentSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AttachmentSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_attachment_summary, this);

        if (isInEditMode())
            return;

        _attachmentView = findViewById(R.id.attachment_layout);
        _countTextView = (TextView) findViewById(R.id.count_textview);

        _attachmentView.setOnClickListener(_this_onClick);
        setVisibility(GONE);

        populateUi();
    }

    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }


    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_countTextView == null)
            return;

        int count = 0;
        boolean canModify = false;

        final AttachmentFolder[] slots = _workOrder.getAttachments().getResults();
        for (AttachmentFolder ob : slots) {
            canModify = ob.getActionsSet().contains(AttachmentFolder.ActionsEnum.EDIT)
                    || ob.getActionsSet().contains(AttachmentFolder.ActionsEnum.DELETE)
                    || ob.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD);
            count += ob.getResults().length;
        }

        if (!canModify && count == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            _countTextView.setText(String.valueOf(count));
        }
    }

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AttachedFilesDialog.show(App.get(), "", _workOrder.getId(), _workOrder.getAttachments());
        }
    };

}
