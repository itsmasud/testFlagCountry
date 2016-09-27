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
import com.fieldnation.R;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ForLoopRunnable;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.RefreshView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 9/27/2016.
 */

public class UploadSlotPickerScreen extends FrameLayout {
    private static final String TAG = "TaskPicker";

    // UI
    private Toolbar _toolbar;
    private TextView _workOrderTitleTextView;
    private LinearLayout _uploadSlotLayout;
    private RefreshView _refreshView;

    // Data
    private long _workOrderId;
    private Listener _listener;

    // Services
    private WorkorderClient _workorderClient;

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

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.setTitle("Select a task");
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _workOrderTitleTextView = (TextView) findViewById(R.id.workOrderTitle_textview);

        _uploadSlotLayout = (LinearLayout) findViewById(R.id.uploadSlot_layout);

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_workorderClient != null && _workorderClient.isConnected()) {
            _workorderClient.disconnect(App.get());
        }
        super.onDetachedFromWindow();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setWorkOrderId(WorkOrder workOrder) {
        _workOrderTitleTextView.setText(workOrder.getTitle());
        _workOrderId = workOrder.getId();
        WorkorderClient.get(App.get(), _workOrderId, false);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                _refreshView.startRefreshing();
            }
        }, 100);
    }

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClient.subGet(false);
        }

        @Override
        public void onGet(long workorderId, Workorder workorder, boolean failed, boolean isCached) {
            if (workorder == null || failed || _workOrderId != workorderId)
                return;

            final UploadSlot[] slots = workorder.getUploadSlots();

            if (slots == null || slots.length == 0) {
                ToastClient.toast(App.get(), "Cannot upload to work order" + workorder.getTitle() + " please select another", Toast.LENGTH_LONG);
                if (_listener != null)
                    _listener.onBackPressed();
                return;
            }

            Runnable r = new ForLoopRunnable(slots.length, new Handler()) {
                private final UploadSlot[] _slots = slots;
                private final List<ShareUploadSlotView> _views = new LinkedList<>();

                @Override
                public void next(int i) throws Exception {
                    ShareUploadSlotView v = new ShareUploadSlotView(getContext());
                    v.setData(_slots[i]);
                    v.setListener(_shareUploadSlotView_listener);
                    _views.add(v);
                }

                @Override
                public void finish(int count) throws Exception {
                    _uploadSlotLayout.removeAllViews();
                    for (ShareUploadSlotView v : _views) {
                        _uploadSlotLayout.addView(v);
                    }
                    _refreshView.refreshComplete();
                }
            };

            post(r);
        }
    };

    private final ShareUploadSlotView.Listener _shareUploadSlotView_listener = new ShareUploadSlotView.Listener() {
        public void onClick(ShareUploadSlotView view, UploadSlot slot) {

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

        void onSlotSelected(UploadSlot uploadSlot);
    }
}

