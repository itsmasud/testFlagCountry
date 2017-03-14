package com.fieldnation.v2.ui.search;

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
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.v2.data.client.GetWorkOrdersOptions;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.ListEnvelope;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.worecycler.BaseHolder;
import com.fieldnation.v2.ui.worecycler.WoPagingAdapter;
import com.fieldnation.v2.ui.worecycler.WorkOrderHolder;
import com.fieldnation.v2.ui.workorder.WorkOrderCard;

/**
 * Created by Michael on 7/27/2016.
 */
public class SearchResultScreen extends RelativeLayout {
    private static final String TAG = "SearchResultScreen";

    //UI
    private OverScrollRecyclerView _workOrderList;
    private RefreshView _refreshView;

    // Service
    private SimpleGps _simpleGps;
    private WorkordersWebApi _workOrderClient;

    // Data
    private SavedList _savedList;
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
        _adapter.setRateMeAllowed(true);

        post(new Runnable() {
            @Override
            public void run() {
                _refreshView.startRefreshing();
            }
        });

        _simpleGps = new SimpleGps(App.get())
                .updateListener(_gps_listener)
                .priority(SimpleGps.Priority.HIGHEST)
                .start(App.get());
    }

    private final SimpleGps.Listener _gps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, Location location) {
            _location = location;
/*
TODO            if (_searchParams != null && _searchParams.uiLocationSpinner == 1 && _location != null) {
                _searchParams.location(_location.getLatitude(), _location.getLongitude());
            }
*/
            _simpleGps.stop();
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
            ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        _workOrderClient = new WorkordersWebApi(_workOrderClient_listener);
        _workOrderClient.connect(App.get());

        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.v(TAG, "onDetachedFromWindow");
        if (_workOrderClient != null && _workOrderClient.isConnected())
            _workOrderClient.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    private void getPage(int page) {
        if (_savedList == null)
            return;

        WorkordersWebApi.getWorkOrders(App.get(),
                new GetWorkOrdersOptions()
                        .list(_savedList.getId())
                        .page(page),
                false, false);

        if (_refreshView != null)
            _refreshView.startRefreshing();
    }

    public void startSearch(SavedList savedList) {
        if (savedList == null) {
            return;
        }

        _savedList = savedList;

/*
TODO        if (_searchParams.uiLocationSpinner == 1 && _location != null) {
            _searchParams.location(_location.getLatitude(), _location.getLongitude());
        }
*/

        _adapter.clear();
        _adapter.refreshAll();
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
            _adapter.refreshAll();
        }
    };

    private final WorkordersWebApi.Listener _workOrderClient_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrderClient.subWorkordersWebApi();
        }

        @Override
        public void onGetWorkOrders(WorkOrders workOrders, boolean success, Error error) {
/*
TODO            if (_searchParams == null || !_searchParams.toKey().equals(searchParams.toKey()))
                return;
*/
            // TODO see if getList() is the list ID
            if (_savedList == null || !_savedList.getId().equals(workOrders.getMetadata().getList()))
                return;

            if (_onListReceivedListener != null)
                _onListReceivedListener.OnWorkOrderListReceived(workOrders);

            if (workOrders == null || workOrders.getMetadata() == null || workOrders.getResults() == null || !success) {
                _refreshView.refreshComplete();
                return;
            }

            ListEnvelope envelope = workOrders.getMetadata();

            Log.v(TAG, "onSearch" + envelope.getPage() + ":" + envelope.getTotal());

            if (envelope.getTotal() == 0) {
                _adapter.clear();
            } else if (workOrders.getResults().length > 0
                    && envelope.getPerPage() > 0
                    && envelope.getPage() <= envelope.getTotal() / envelope.getPerPage() + 1)
                _adapter.addObjects(envelope.getPage(), workOrders.getResults());
            else
                _adapter.addObjects(envelope.getPage(), (WorkOrder[]) null);

            _refreshView.refreshComplete();
        }

        @Override
        public void onWorkordersWebApi(String methodName, Object successObject, boolean success, Object failObject) {
            Log.v(TAG, "onWorkordersWebApi: " + methodName);

            if (methodName.startsWith("get"))
                return;

            _adapter.refreshAll();
            _refreshView.startRefreshing();
        }
    };

    private final WoPagingAdapter _adapter = new WoPagingAdapter() {
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
            v.setData(object, _location, _savedList.getLabel());
        }

        @Override
        public boolean useHeader() {
/*
TODO            if (_searchParams != null)
                return _searchParams.canEdit;
*/
            return false;
        }

        @Override
        public BaseHolder onCreateHeaderViewHolder(ViewGroup parent) {
            HeaderView v = new HeaderView(parent.getContext());
            return new BaseHolder(v, BaseHolder.TYPE_HEADER);
        }

        @Override
        public void onBindHeaderViewHolder(BaseHolder holder) {
            ((HeaderView) holder.itemView).setSavedList(_savedList);
        }


        @Override
        public BaseHolder onCreateEmptyViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_work, parent, false);
            return new BaseHolder(v, BaseHolder.TYPE_EMPTY);
        }

        @Override
        public void onBindEmptyViewHolder(BaseHolder holder) {
            // nothing
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
        void OnWorkOrderListReceived(WorkOrders workOrders);
    }
}