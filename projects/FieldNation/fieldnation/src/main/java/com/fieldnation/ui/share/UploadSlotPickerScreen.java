package com.fieldnation.ui.share;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.Debug;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.AttachmentFolder;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 9/27/2016.
 */

public class UploadSlotPickerScreen extends FrameLayout {
    private static final String TAG = "UploadSlotPickerScreen";

    // UI
    private Toolbar _toolbar;
    private TextView _workOrderTitleTextView;
    private LinearLayout _uploadSlotLayout;
    private RefreshView _refreshView;

    // Data
    private int _workOrderId;
    private WorkOrder _workOrder;
    private Listener _listener;


    public UploadSlotPickerScreen(Context context) {
        super(context);
        init();
    }

    public UploadSlotPickerScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UploadSlotPickerScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_share_upload_slot_picker, this);

        if (isInEditMode())
            return;

        _toolbar = findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle(R.string.select_a_task);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _workOrderTitleTextView = findViewById(R.id.workOrderTitle_textview);

        _uploadSlotLayout = findViewById(R.id.uploadSlot_layout);

        _refreshView = findViewById(R.id.refresh_view);
    }

    @Override
    protected void onDetachedFromWindow() {
        _workOrderApi.unsub();
        super.onDetachedFromWindow();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setWorkOrderId(int workOrderId) {
        _workOrderId = workOrderId;
        _workOrderApi.sub();
        WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                _refreshView.startRefreshing();
            }
        }, 100);
    }


    private void populateUi() {
        _workOrderTitleTextView.setText(_workOrder.getTitle());

        AttachmentFolder[] folders = _workOrder.getAttachments().getResults();

        boolean canUpload = false;
        for (AttachmentFolder folder : folders) {
            if (folder.getType() == AttachmentFolder.TypeEnum.SLOT && folder.getActionsSet().contains(AttachmentFolder.ActionsEnum.UPLOAD)) {
                canUpload = true;
                break;
            }
        }

        if (!canUpload) {
            ToastClient.toast(App.get(), "Cannot upload to this workorder.", Toast.LENGTH_LONG);
            if (_listener != null) _listener.onBackPressed();
        }


        final List<AttachmentFolder> slots = new LinkedList<>();
        for (AttachmentFolder folder : folders) {
            if (folder.getType() == AttachmentFolder.TypeEnum.SLOT) {
                slots.add(folder);
            }
        }

        if (slots == null || folders.length == 0) {
            ToastClient.toast(App.get(), getResources().getString(R.string.cannot_upload_to_work_order_num, _workOrder.getTitle()), Toast.LENGTH_LONG);
            if (_listener != null)
                _listener.onBackPressed();
            return;
        }

        Runnable r = new ForLoopRunnable(slots.size(), new Handler()) {
            private final AttachmentFolder[] _slots = slots.toArray(new AttachmentFolder[slots.size()]);
            private final List<UploadSlotView> _views = new LinkedList<>();

            @Override
            public void next(int i) throws Exception {
                UploadSlotView v = new UploadSlotView(getContext());
                v.setData(_slots[i]);
                v.setListener(_shareUploadSlotView_listener);
                _views.add(v);
            }

            @Override
            public void finish(int count) throws Exception {
                _uploadSlotLayout.removeAllViews();
                for (UploadSlotView v : _views) {
                    _uploadSlotLayout.addView(v);
                }
                _refreshView.refreshComplete();
            }
        };

        post(r);
    }

    private final WorkordersWebApi _workOrderApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return !methodName.equals("getWorkOrders");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && successObject instanceof WorkOrder) {
                WorkOrder workOrder = (WorkOrder) successObject;
                if (!success) {
                    _refreshView.refreshComplete();
                    return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
                }

                if (_workOrderId == workOrder.getId()) {
                    Debug.setLong("last_workorder", workOrder.getId());
                    _workOrder = workOrder;
                    populateUi();
                }
            } else if (!methodName.startsWith("get")) {
                WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
            }

            if (methodName.startsWith("get") || !success)
                return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);

            //Log.v(TAG, "onWorkordersWebApi " + methodName);

            WorkordersWebApi.getWorkOrder(App.get(), _workOrderId, false, WebTransaction.Type.NORMAL);
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };


    private final UploadSlotView.Listener _shareUploadSlotView_listener = new UploadSlotView.Listener() {
        public void onClick(UploadSlotView view, AttachmentFolder slot) {

            if (!view.isChecked()) {
                view.changeCheckStatus();
            }

            if (_listener != null)
                _listener.onSlotSelected(slot);
        }
    };

    private final View.OnClickListener _toolbar_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onBackPressed();
        }
    };

    public interface Listener {
        void onBackPressed();

        void onSlotSelected(AttachmentFolder uploadSlot);
    }
}

