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
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.ui.EmptyCardView;
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
    private GlobalTopicClient _globalTopicClient;

    // Data
    private GetWorkOrdersOptions _workOrdersOptions;
    private SavedList _savedList;
    private FilterParams _filterParams;
    private Location _location;
    private OnClickListener _onClickListener;
    private OnWorkOrderListReceivedListener _onListReceivedListener;
    private ListEnvelope _envelope = null;

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
        super.onAttachedToWindow();

        FilterDrawerDialog.addOnOkListener(DIALOG_FILTER_DRAWER, _filterDrawer_onOk);
    }

    public void onResume() {
        _workOrderApi.sub();
        _adapter.refreshAll();

        _globalTopicClient = new GlobalTopicClient(_globalTopicClient_listener);
        _globalTopicClient.connect(App.get());
    }

    public void onPause() {
        _workOrderApi.unsub();
        if (_globalTopicClient != null) _globalTopicClient.disconnect(App.get());
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.v(TAG, "onDetachedFromWindow");

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

        @Override
        public void onPermissionDenied(SimpleGps simpleGps) {
        }
    };

    public WoPagingAdapter getAdapter() {
        return _adapter;
    }

    private void getPage(int page) {
        if (_workOrdersOptions == null)
            return;

        if (_envelope == null || page <= _envelope.getPages() || page <= 1) {
            _workOrdersOptions = _filterParams.applyFilter(_workOrdersOptions);

            WorkordersWebApi.getWorkOrders(App.get(), _workOrdersOptions.page(page), true, false);

            if (_refreshView != null)
                _refreshView.startRefreshing();
        } else {
            _refreshView.refreshComplete();
        }
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

    private final WorkordersWebApi _workOrderApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.equals("getWorkOrders")
                    || (!methodName.startsWith("get") && !methodName.toLowerCase().contains("attachment"));
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

                ListEnvelope envelope = workOrders.getMetadata();
                _envelope = envelope;

                if (envelope.getTotal() == 0) {
                    _adapter.clear();
                } else if (workOrders.getResults().length > 0
                        && envelope.getPerPage() > 0
                        && envelope.getPage() <= envelope.getTotal() / envelope.getPerPage() + 1)
                    _adapter.addObjects(envelope.getPage(), workOrders.getResults());
                else
                    _adapter.addObjects(envelope.getPage(), (WorkOrder[]) null);

                _refreshView.refreshComplete();
            } else {
                if (methodName.startsWith("get") || methodName.toLowerCase().contains("attachment"))
                    return;

                WorkordersWebApi.getWorkOrderLists(App.get(), false, false);

                _adapter.refreshAll();
                post(new Runnable() {
                    @Override
                    public void run() {
                        _refreshView.startRefreshing();
                    }
                });
            }
        }
    };

    private final GlobalTopicClient.Listener _globalTopicClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.subUserSwitched();
        }

        @Override
        public void onUserSwitched(Profile profile) {
            _adapter.refreshAll();
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
            EmptyCardView v = new EmptyCardView(getContext());

            if (_savedList.getLabel().equalsIgnoreCase("available")) {
                v.setData(EmptyCardView.PARAM_VIEW_TYPE_AVAILABLE);
            } else if (_savedList.getLabel().equalsIgnoreCase("routed")) {
                v.setData(EmptyCardView.PARAM_VIEW_TYPE_ROUTED);
            } else if (_savedList.getLabel().equalsIgnoreCase("requested")) {
                v.setData(EmptyCardView.PARAM_VIEW_TYPE_REQUESTED);
            } else if (_savedList.getLabel().equalsIgnoreCase("counter")) {
                v.setData(EmptyCardView.PARAM_VIEW_TYPE_COUNTER);
            } else if (_savedList.getLabel().equalsIgnoreCase("assigned")) {
                v.setData(EmptyCardView.PARAM_VIEW_TYPE_ASSIGNED);
            } else if (_savedList.getLabel().equalsIgnoreCase("pending")) {
                v.setData(EmptyCardView.PARAM_VIEW_TYPE_PENDING);
            } else if (_savedList.getLabel().equalsIgnoreCase("completed")) {
                v.setData(EmptyCardView.PARAM_VIEW_TYPE_COMPLETED);
            } else if (_savedList.getLabel().equalsIgnoreCase("declined")) {
                v.setData(EmptyCardView.PARAM_VIEW_TYPE_DECLINED);
            }

            return new BaseHolder(v, BaseHolder.TYPE_EMPTY);
        }

        @Override
        public void onBindEmptyViewHolder(BaseHolder holder) {
            EmptyCardView view = (EmptyCardView) holder.itemView;
            if (_savedList.getLabel().equalsIgnoreCase("available")) {
                view.setData(EmptyCardView.PARAM_VIEW_TYPE_AVAILABLE);
            } else if (_savedList.getLabel().equalsIgnoreCase("routed")) {
                view.setData(EmptyCardView.PARAM_VIEW_TYPE_ROUTED);
            } else if (_savedList.getLabel().equalsIgnoreCase("requested")) {
                view.setData(EmptyCardView.PARAM_VIEW_TYPE_REQUESTED);
            } else if (_savedList.getLabel().equalsIgnoreCase("counter")) {
                view.setData(EmptyCardView.PARAM_VIEW_TYPE_COUNTER);
            } else if (_savedList.getLabel().equalsIgnoreCase("assigned")) {
                view.setData(EmptyCardView.PARAM_VIEW_TYPE_ASSIGNED);
            } else if (_savedList.getLabel().equalsIgnoreCase("pending")) {
                view.setData(EmptyCardView.PARAM_VIEW_TYPE_PENDING);
            } else if (_savedList.getLabel().equalsIgnoreCase("completed")) {
                view.setData(EmptyCardView.PARAM_VIEW_TYPE_COMPLETED);
            } else if (_savedList.getLabel().equalsIgnoreCase("declined")) {
                view.setData(EmptyCardView.PARAM_VIEW_TYPE_DECLINED);
            }
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