package com.fieldnation.ui.search;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.service.data.v2.workorder.SearchParams;
import com.fieldnation.service.data.v2.workorder.WorkOrderClient;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.worecycler.BaseHolder;
import com.fieldnation.ui.worecycler.TimeHeaderAdapter;
import com.fieldnation.ui.worecycler.WorkOrderHolder;
import com.fieldnation.ui.workorder.v2.WorkOrderCard;
import com.fieldnation.utils.ISO8601;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Michael on 7/27/2016.
 */
public class SearchResultScreen extends OverScrollRecyclerView {
    private static final String TAG = "SearchResultScreen";

    // Service
    private WorkOrderClient _workOrderClient;

    public SearchResultScreen(Context context) {
        super(context);
        init();
    }

    public SearchResultScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchResultScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        _workOrderClient = new WorkOrderClient(_workOrderClient_listener);
        _workOrderClient.connect(App.get());

        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setAdapter(_adapter);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_workOrderClient != null && _workOrderClient.isConnected())
            _workOrderClient.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    public void startSearch(SearchParams searchParams) {
        WorkOrderClient.search(App.get(), searchParams);
    }

    private final WorkOrderClient.Listener _workOrderClient_listener = new WorkOrderClient.Listener() {
        @Override
        public void onConnected() {
            _workOrderClient.subSearch();
        }

        @Override
        public void onSearch(SearchParams searchParams, List<WorkOrder> workorder) {
            Log.v(TAG, "onSearch");
            _adapter.addObjects(workorder);
        }
    };

    private final TimeHeaderAdapter<WorkOrder> _adapter = new TimeHeaderAdapter<WorkOrder>(WorkOrder.class) {
        @Override
        public void requestPage(int page, boolean allowCache) {

        }

        @Override
        public Comparator<WorkOrder> getTimeComparator() {
            return WorkOrder.getTimeComparator();
        }

        @Override
        public Calendar getObjectTime(WorkOrder object) {
            try {
                return ISO8601.toCalendar(object.getRequirements().getSchedule().getStart());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        public BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType) {
            return new WorkOrderHolder(new WorkOrderCard(parent.getContext()));
        }

        @Override
        public void onBindObjectViewHolder(BaseHolder holder, WorkOrder object) {
            WorkOrderHolder h = (WorkOrderHolder) holder;
            WorkOrderCard v = h.getView();
            v.setData(object);
        }
    };
}
