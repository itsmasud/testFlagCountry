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
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.ui.dialog.AttachedFilesDialog;

/**
 * Created by shoaib.ahmed on 08/02/2017.
 */

public class AttachmentSummaryView extends RelativeLayout implements WorkOrderRenderer, UUIDView {
    private static final String TAG = "AttachmentSummaryView";

    // Ui
    private View _attachmentView;
    private TextView _countTextView;

    // Data
    private WorkOrder _workOrder;
    private String _myUUID;

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
        _countTextView = findViewById(R.id.count_textview);

        _attachmentView.setOnClickListener(_this_onClick);
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
                if (key == null || key.contains("addAttachmentByWorkOrderAndFolder") || key.contains("deleteAttachmentByWorkOrderAndFolderAndAttachment"))
                    populateUi();
            } else if (op.equals("delete") || op.equals("deleteAll")) {
                populateUi();
            }
        }
    };


    public void setWorkOrder(WorkOrder workOrder) {
        _workOrder = workOrder;
        populateUi();
    }

    @Override
    public void setUUID(String uuid) {
        _myUUID = uuid;
    }

    private void populateUi() {
        if (_workOrder == null)
            return;

        if (_countTextView == null)
            return;

        int addSize = WebTransaction.findByKey(WebTransactionUtils.WEB_TRANS_KEY_PREFIX_ADD_ATTACHMENT + _workOrder.getId() + "/%").size();
        int delSize = WebTransaction.findByKey(WebTransactionUtils.WEB_TRANS_KEY_PREFIX_DELETE_ATTACHMENT + _workOrder.getId() + "/%").size();


        int count = 0;
        boolean canModify = false;

        final AttachmentFolder[] slots = _workOrder.getAttachments().getResults();
        for (AttachmentFolder ob : slots) {
            canModify = ob.getActionsSet().contains(AttachmentFolder.ActionsEnum.EDIT)
                    || ob.getActionsSet().contains(AttachmentFolder.ActionsEnum.DELETE)
                    || ob.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD);
            count += ob.getResults().length;
        }

        int total = count + addSize - delSize;

        if (!canModify && total == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            _countTextView.setText(String.valueOf(total));
        }
    }

    private final OnClickListener _this_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            AttachedFilesDialog.show(App.get(), null, _myUUID, _workOrder.getId());
        }
    };

}
