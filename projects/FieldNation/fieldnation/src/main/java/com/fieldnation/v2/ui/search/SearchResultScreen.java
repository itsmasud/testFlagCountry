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
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.ListEnvelope;
import com.fieldnation.v2.data.model.SavedList;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.dialog.FilterDrawerDialog;
import com.fieldnation.v2.ui.worecycler.BaseHolder;
import com.fieldnation.v2.ui.worecycler.WoPagingAdapter;
import com.fieldnation.v2.ui.worecycler.WorkOrderHolder;
import com.fieldnation.v2.ui.workorder.WorkOrderCard;

/**
 * Created by Michael on 7/27/2016.
 */
public class SearchResultScreen extends RelativeLayout {
    private static final String TAG = "SearchResultScreen";

    // Dialogs
    private static final String DIALOG_FILTER_DRAWER = TAG + "filterDrawerDialog";

    //UI
    private OverScrollRecyclerView _workOrderList;
    private RefreshView _refreshView;

    // Service
    private SimpleGps _simpleGps;
    private WorkordersWebApi _workOrderClient;

    // Data
    private GetWorkOrdersOptions _workOrdersOptions;
    private SavedList _savedList;
    private FilterParams _filterParams;
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

    @Override
    protected void onAttachedToWindow() {
        _workOrderClient = new WorkordersWebApi(_workOrderClient_listener);
        _workOrderClient.connect(App.get());

        super.onAttachedToWindow();

        FilterDrawerDialog.addOnOkListener(DIALOG_FILTER_DRAWER, _filterDrawer_onOk);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.v(TAG, "onDetachedFromWindow");
        if (_workOrderClient != null) _workOrderClient.disconnect(App.get());

        FilterDrawerDialog.removeOnOkListener(DIALOG_FILTER_DRAWER, _filterDrawer_onOk);

        super.onDetachedFromWindow();
    }

    private final SimpleGps.Listener _gps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, Location location) {
            _location = location;

            if (_filterParams != null && _filterParams.uiLocationSpinner == 1 && _location != null) {
                _filterParams.latitude = _location.getLatitude();
                _filterParams.longitude = _location.getLongitude();
            }
            _simpleGps.stop();
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
            ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
        }
    };

    public WoPagingAdapter getAdapter() {
        return _adapter;
    }

    private void getPage(int page) {
        if (_workOrdersOptions == null)
            return;

        _workOrdersOptions = _filterParams.applyFilter(_workOrdersOptions);

        WorkordersWebApi.getWorkOrders(App.get(), _workOrdersOptions.page(page), true, false);

        if (_refreshView != null)
            _refreshView.startRefreshing();
    }

    public void startSearch(SavedList savedList) {
        startSearch(savedList, new GetWorkOrdersOptions());
    }

    public void startSearch(SavedList savedList, GetWorkOrdersOptions workOrdersOptions) {
        if (savedList == null) {
            return;
        }

        _workOrdersOptions = workOrdersOptions;
        _workOrdersOptions.setList(savedList.getId());
        _savedList = savedList;
        _filterParams = FilterParams.load(savedList.getId());

        if (_filterParams.uiLocationSpinner == 1 && _location != null) {
            _filterParams.latitude = _location.getLatitude();
            _filterParams.longitude = _location.getLongitude();
        }

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
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            //Log.v(TAG, "onWorkordersWebApi: " + methodName);
            if (methodName.equals("getWorkOrders")) {
                if (!success || successObject == null) {
                    _refreshView.refreshComplete();
                    return;
                }

                WorkOrders workOrders = (WorkOrders) successObject;
                if (_savedList == null || !_savedList.getId().equals(workOrders.getMetadata().getList()))
                    return;

                if (_onListReceivedListener != null)
                    _onListReceivedListener.OnWorkOrderListReceived(workOrders);

                if (workOrders.getMetadata() == null || workOrders.getResults() == null) {
                    _refreshView.refreshComplete();
                    return;
                }

                ListEnvelope envelope = workOrders.getMetadata();

                //Log.v(TAG, "onSearch" + envelope.getPage() + ":" + envelope.getTotal());

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

            if (methodName.startsWith("get") || methodName.toLowerCase().contains("attachment"))
                return;

            _adapter.refreshAll();
            post(new Runnable() {
                @Override
                public void run() {
                    _refreshView.startRefreshing();
                }
            });
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
            return _savedList.getId().equals("workorders_available");
        }

        @Override
        public BaseHolder onCreateHeaderViewHolder(ViewGroup parent) {
            HeaderView v = new HeaderView(parent.getContext());
            return new BaseHolder(v, BaseHolder.TYPE_HEADER);
        }

        @Override
        public void onBindHeaderViewHolder(BaseHolder holder) {
            HeaderView view = (HeaderView) holder.itemView;
            view.setFilterParams(_filterParams);
            view.setOnClickListener(_header_onClick);
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

    private final View.OnClickListener _header_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FilterDrawerDialog.show(App.get(), DIALOG_FILTER_DRAWER, _filterParams.listId);
        }
    };

    private final FilterDrawerDialog.OnOkListener _filterDrawer_onOk = new FilterDrawerDialog.OnOkListener() {
        @Override
        public void onOk(FilterParams filterParams) {
            _filterParams = filterParams;
            _adapter.refreshAll();
        }
    };

    private final View.OnClickListener _card_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_onClickListener != null)
                _onClickListener.onWorkOrderClicked(((WorkOrderCard) v).getWorkOrder());
        }
    };

    private final WorkOrderCard.OnActionListener _workOrderCard_onAction = new WorkOrderCard.OnActionListener() {
        @Override
        public void onAction() {
            _refreshView.startRefreshing();
        }
    };

    public interface OnClickListener {
        void onWorkOrderClicked(WorkOrder workOrder);
    }

    public interface OnWorkOrderListReceivedListener {
        void OnWorkOrderListReceived(WorkOrders workOrders);
    }
}