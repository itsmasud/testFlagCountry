package com.fieldnation.ui.share;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.fieldnation.R;
import com.fieldnation.ui.FnToolBarView;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.WorkOrder;

/**
 * Created by Michael on 9/27/2016.
 */

public class WorkOrderPickerScreen extends FrameLayout {
    private static final String TAG = "WorkOrderPickerScreen";

    private static SavedList _searchParams;

    static {
        try {
            _searchParams = new SavedList()
                    .id("workorders_assignments")
                    .label("assigned");
        } catch (Exception ex) {
        }
    }

    // UI
    private FnToolBarView _fnToolbarView;
    private PickerResultScreen _workOrderScreen;

    // Data
    private Listener _listener;

    public WorkOrderPickerScreen(Context context) {
        super(context);
        init();
    }

    public WorkOrderPickerScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkOrderPickerScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_share_work_order_picker, this);

        if (isInEditMode())
            return;

        _fnToolbarView = findViewById(R.id.fnToolbar);
        _fnToolbarView.getToolbar().setNavigationIcon(R.drawable.ic_signature_x);
        _fnToolbarView.getToolbar().setOnClickListener(_toolbar_onClick);
        _fnToolbarView.getToolbar().setTitle(R.string.select_a_work_order);

        _workOrderScreen = findViewById(R.id.recyclerView);
        _workOrderScreen.setOnChildClickListener(_searchResultScreen_listener);
        _workOrderScreen.startSearch(_searchParams);
    }

    public void onStart() {
        _workOrderScreen.onStart();
    }

    public void onResume() {
        _workOrderScreen.onResume();
    }

    public void onPause() {
        _workOrderScreen.onPause();
    }

    public void onStop() {
        _workOrderScreen.onStop();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private final PickerResultScreen.OnClickListener _searchResultScreen_listener = new PickerResultScreen.OnClickListener() {
        @Override
        public void onWorkOrderClicked(WorkOrder workOrder) {
            if (_listener != null)
                _listener.onWorkOrderSelected(workOrder);
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

        void onWorkOrderSelected(WorkOrder workOrder);
    }
}
