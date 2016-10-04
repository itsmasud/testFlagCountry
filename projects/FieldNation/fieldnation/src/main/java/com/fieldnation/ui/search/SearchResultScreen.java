package com.fieldnation.ui.search;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.v2.ListEnvelope;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.data.v2.WorkOrder;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.service.data.v2.workorder.WorkOrderClient;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.worecycler.BaseHolder;
import com.fieldnation.ui.worecycler.TimeHeaderAdapter;
import com.fieldnation.ui.worecycler.WorkOrderHolder;
import com.fieldnation.ui.workorder.v2.WorkOrderCard;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Michael on 7/27/2016.
 */
public class SearchResultScreen extends RelativeLayout {
    private static final String TAG = "SearchResultScreen";

    //UI
    private OverScrollRecyclerView _workOrderList;
    private RefreshView _refreshView;
    private View _unavailableView;

    // Service
    private WorkOrderClient _workOrderClient;
    private SimpleGps _simpleGps;

    // Data
    private SavedSearchParams _searchParams;
    private Location _location;
    private Listener _listener;

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
        LayoutInflater.from(getContext()).inflate(R.layout.screen_search_result, this);

        if (isInEditMode())
            return;

        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _unavailableView = findViewById(R.id.marketplaceUnavailable_layout);

        _workOrderList = (OverScrollRecyclerView) findViewById(R.id.workOrderList_recyclerView);
        _workOrderList.setOnOverScrollListener(_refreshView);
        _workOrderList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _workOrderList.setAdapter(_adapter);

        _workOrderClient = new WorkOrderClient(_workOrderClient_listener);
        _workOrderClient.connect(App.get());

        _adapter.clear();

        post(new Runnable() {
            @Override
            public void run() {
                _refreshView.startRefreshing();
            }
        });

        _simpleGps = new SimpleGps(App.get())
                .updateListener(_gps_listener)
                .start(App.get());
    }

    private final SimpleGps.Listener _gps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(Location location) {
            _location = location;
            _simpleGps.stop();
        }

        @Override
        public void onFail() {
            ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (_workOrderClient != null && _workOrderClient.isConnected())
            _workOrderClient.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    private void getPage(int page) {
        if (_searchParams == null)
            return;

        WorkOrderClient.search(App.get(), _searchParams, page);

        if (_refreshView != null)
            _refreshView.startRefreshing();
    }

    public void startSearch(SavedSearchParams searchParams) {
        _searchParams = searchParams;
        _adapter.clear();
        getPage(0);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            getPage(0);
        }
    };

    private final WorkOrderClient.Listener _workOrderClient_listener = new WorkOrderClient.Listener() {
        @Override
        public void onConnected() {
            _workOrderClient.subSearch();
        }

        @Override
        public void onSearch(SavedSearchParams searchParams, ListEnvelope envelope, List<WorkOrder> workOrders, boolean failed) {
            if (!_searchParams.toKey().equals(searchParams.toKey()))
                return;

            if (envelope == null || envelope.getTotal() == 0) {
                _refreshView.refreshComplete();
                if (_adapter.getItemCount() == 0)
                    _unavailableView.setVisibility(VISIBLE);
                else
                    _unavailableView.setVisibility(GONE);
                return;
            }

            Log.v(TAG, "onSearch" + envelope.getPage() + ":" + envelope.getTotal());
            if (envelope.getPage() <= (envelope.getTotal() / envelope.getPerPage()) + 1)
                _adapter.addObjects(envelope.getPage(), workOrders);
            else
                _adapter.addObjects(envelope.getPage(), null);

            _refreshView.refreshComplete();
            if (_adapter.getItemCount() == 0)
                _unavailableView.setVisibility(VISIBLE);
            else
                _unavailableView.setVisibility(GONE);
        }
    };

    private final TimeHeaderAdapter<WorkOrder> _adapter = new TimeHeaderAdapter<WorkOrder>(WorkOrder.class) {
        @Override
        public void requestPage(int page, boolean allowCache) {
            getPage(page);
        }

        @Override
        public Comparator<WorkOrder> getTimeComparator() {
            return WorkOrder.getTimeComparator();
        }

        @Override
        public Calendar getObjectTime(WorkOrder object) {
            try {
                return ISO8601.toCalendar(object.getSchedule().getBegin());
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        public BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType) {
            WorkOrderCard card = new WorkOrderCard(parent.getContext());

            if (_listener != null)
                card.setOnClickListener(_card_onClick);

            return new WorkOrderHolder(card);
        }

        @Override
        public void onBindObjectViewHolder(BaseHolder holder, WorkOrder object) {
            WorkOrderHolder h = (WorkOrderHolder) holder;
            WorkOrderCard v = h.getView();
            v.setData(object, _location);
        }
    };

    private final View.OnClickListener _card_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onWorkOrderClicked(((WorkOrderCard) v).getWorkOrder());
        }
    };

    public interface Listener {
        void onWorkOrderClicked(WorkOrder workOrder);
    }
}
