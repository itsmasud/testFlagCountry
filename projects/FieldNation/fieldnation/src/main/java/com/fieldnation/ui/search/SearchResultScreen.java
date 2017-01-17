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
import com.fieldnation.service.data.v2.workorder.WorkOrderClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.worecycler.BaseHolder;
import com.fieldnation.ui.worecycler.PagingAdapter;
import com.fieldnation.ui.worecycler.WorkOrderHolder;
import com.fieldnation.ui.workorder.v2.WorkOrderCard;

import java.util.List;

/**
 * Created by Michael on 7/27/2016.
 */
public class SearchResultScreen extends RelativeLayout {
    private static final String TAG = "SearchResultScreen";

    //UI
    private OverScrollRecyclerView _workOrderList;
    private RefreshView _refreshView;

    // Service
    private WorkOrderClient _workOrderClient;
    private SimpleGps _simpleGps;
    private WorkorderClient _workorderClientV1;

    // Data
    private SavedSearchParams _searchParams;
    private Location _location;
    private OnClickListener _onClickListener;
    private OnWorkOrderListReceivedListener _onListReceivedListener;

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

        _workOrderList = (OverScrollRecyclerView) findViewById(R.id.workOrderList_recyclerView);
        _workOrderList.setOnOverScrollListener(_refreshView);
        _workOrderList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _workOrderList.setAdapter(_adapter);

        _workOrderClient = new WorkOrderClient(_workOrderClient_listener);
        _workOrderClient.connect(App.get());
        _workorderClientV1 = new WorkorderClient(_workorderClientV1_listener);
        _workorderClientV1.connect(App.get());

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

        if (_workorderClientV1 != null && _workorderClientV1.isConnected())
            _workorderClientV1.disconnect(App.get());

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

    public void setOnChildClickListener(OnClickListener listener) {
        _onClickListener = listener;
    }

    public void setOnWorkOrderListReceivedListener(OnWorkOrderListReceivedListener listener) {
        _onListReceivedListener = listener;
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
            _workOrderClient.subActions();
        }

        @Override
        public void onSearch(SavedSearchParams searchParams, ListEnvelope envelope, List<WorkOrder> workOrders, boolean failed) {
            if (_searchParams == null || !_searchParams.toKey().equals(searchParams.toKey()))
                return;

            if (_onListReceivedListener != null)
                _onListReceivedListener.OnWorkOrderListReceived(envelope, workOrders);

            if (envelope == null || envelope.getTotal() == 0 || failed) {
                _refreshView.refreshComplete();
                return;
            }

            Log.v(TAG, "onSearch" + envelope.getPage() + ":" + envelope.getTotal());
            if (workOrders.size() > 0
                    && envelope.getPerPage() > 0
                    && envelope.getPage() <= (envelope.getTotal() / envelope.getPerPage()) + 1)
                _adapter.addObjects(envelope.getPage(), workOrders);
            else
                _adapter.addObjects(envelope.getPage(), null);

            _refreshView.refreshComplete();
        }

        @Override
        public void onAction(long workOrderId, String action, boolean failed) {
            getPage(0);
            _refreshView.startRefreshing();
        }
    };

    private final WorkorderClient.Listener _workorderClientV1_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClientV1.subActions();
        }

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            Log.v(TAG, "_workorderClientV1_listener.onAction " + workorderId + ", " + action + ", " + failed);

            if (failed)
                return;

            getPage(0);
            _refreshView.startRefreshing();
        }
    };

    private final PagingAdapter<WorkOrder> _adapter = new PagingAdapter<WorkOrder>(WorkOrder.class) {
        @Override
        public void requestPage(int page, boolean allowCache) {
            getPage(page);
        }

        @Override
        public BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType) {
            WorkOrderCard card = new WorkOrderCard(parent.getContext());

            if (_onClickListener != null)
                card.setOnClickListener(_card_onClick);

            return new WorkOrderHolder(card);
        }

        @Override
        public void onBindObjectViewHolder(BaseHolder holder, WorkOrder object) {
            WorkOrderHolder h = (WorkOrderHolder) holder;
            WorkOrderCard v = h.getView();
            v.setData(object, _location, _searchParams.title);
        }

        @Override
        public boolean useHeader() {
            if (_searchParams != null)
                return _searchParams.canEdit;

            return false;
        }

        @Override
        public BaseHolder onCreateHeaderViewHolder(ViewGroup parent) {
            HeaderView v = new HeaderView(parent.getContext());
            return new BaseHolder(v, BaseHolder.TYPE_HEADER);
        }

        @Override
        public void onBindHeaderViewHolder(BaseHolder holder) {
            ((HeaderView) holder.itemView).setSavedSearchParams(_searchParams);
        }

        @Override
        public BaseHolder onCreateEmptyViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_work, parent, false);
            return new BaseHolder(v, BaseHolder.TYPE_EMPTY);
        }

        @Override
        public void onBindEmptyViewHolder(BaseHolder holder) {
            // Nothing to do.
        }
    };

    private final View.OnClickListener _card_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_onClickListener != null)
                _onClickListener.onWorkOrderClicked(((WorkOrderCard) v).getWorkOrder());
        }
    };

    public interface OnClickListener {
        void onWorkOrderClicked(WorkOrder workOrder);
    }

    public interface OnWorkOrderListReceivedListener {
        void OnWorkOrderListReceived(ListEnvelope envelope, List<WorkOrder> workOrders);
    }
}