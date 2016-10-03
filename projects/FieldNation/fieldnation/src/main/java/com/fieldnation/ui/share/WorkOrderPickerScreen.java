package com.fieldnation.ui.share;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.fieldnation.R;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;
import com.fieldnation.ui.search.SearchResultScreen;

/**
 * Created by Michael on 9/27/2016.
 */

public class WorkOrderPickerScreen extends FrameLayout {
    private static final String TAG = "WorkOrderPickerScreen";

    private static final SavedSearchParams _searchParams =
            new SavedSearchParams()
                    .type(WorkOrderListType.ATTACHABLE.getType())
                    .status(WorkOrderListType.ATTACHABLE.getStatuses())
                    .title("Select a Work Order");

    // UI
    private Toolbar _toolbar;
    private SearchResultScreen _workOrderScreen;

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

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _toolbar.setTitle(R.string.select_a_work_order);

        _workOrderScreen = (SearchResultScreen) findViewById(R.id.recyclerView);
        _workOrderScreen.setListener(_searchResultScreen_listener);
        _workOrderScreen.startSearch(_searchParams);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private final SearchResultScreen.Listener _searchResultScreen_listener = new SearchResultScreen.Listener() {
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
