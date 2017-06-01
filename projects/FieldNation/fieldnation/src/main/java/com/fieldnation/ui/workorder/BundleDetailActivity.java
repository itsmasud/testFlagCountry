package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fngps.SimpleGps;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.v2.RequestBundleDialog;
import com.fieldnation.v2.data.client.BundlesWebApi;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Declines;
import com.fieldnation.v2.data.model.Request;
import com.fieldnation.v2.data.model.Requests;
import com.fieldnation.v2.data.model.Route;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.dialog.BundleEtaDialog;
import com.fieldnation.v2.ui.dialog.DeclineDialog;
import com.fieldnation.v2.ui.dialog.WithdrawRequestDialog;
import com.fieldnation.v2.ui.worecycler.BaseHolder;
import com.fieldnation.v2.ui.worecycler.WoPagingAdapter;
import com.fieldnation.v2.ui.worecycler.WorkOrderHolder;
import com.fieldnation.v2.ui.workorder.WorkOrderCard;

public class BundleDetailActivity extends AuthSimpleActivity {
    private static final String TAG = "BundleDetailActivity";

    public static final String INTENT_FIELD_BUNDLE_ID = "BundleDetailActivity:bundle_id";

    // Dialog tags
    private static final String UID_DIALOG_DECLINE = TAG + ".DeclineDialog";
    private static final String DIALOG_WITHDRAW = TAG + ".withdrawRequestDialog";
    private static final String UID_DIALOG_BUNDLE_ETA = TAG + ".BundleEtaDialog";
    private static final String UID_DIALOG_REQUEST_BUNDLE = TAG + ".RequestBundleDialog";


    // UI
    private LinearLayout _buttonToolbar;
    private Button _notInterestedButton;
    private Button _okButton;
    private OverScrollRecyclerView _listview;
    private RefreshView _refreshView;

    // Data
    private int _bundleId = 0;
    private Location _location;
    private WorkOrders _workOrders;

    // Services
    private BundlesWebApi _bundlesApi;
    private SimpleGps _simpleGps;
    private WorkorderClient _workOrderClient;
    private WorkordersWebApi _workOrdersApiClient;


    @Override
    public int getLayoutResource() {
        return R.layout.activity_bundle_detail;
    }

    @Override
    public void onFinishCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();

        if (intent == null) {
            finish();
            return;
        }

        if (intent.hasExtra(INTENT_FIELD_BUNDLE_ID)) {
            _bundleId = intent.getIntExtra(INTENT_FIELD_BUNDLE_ID, -1);
        } else {
            finish();
            return;
        }

        _buttonToolbar = (LinearLayout) findViewById(R.id.button_toolbar);
        _notInterestedButton = (Button) findViewById(R.id.notInterested_button);
        _notInterestedButton.setOnClickListener(_notInterested_onClick);
        _okButton = (Button) findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
        _refreshView.setListener(_refreshView_listener);

        _listview = (OverScrollRecyclerView) findViewById(R.id.items_listview);
        _listview.setOnOverScrollListener(_refreshView);
        _listview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        _listview.setAdapter(_adapter);

        _simpleGps = new SimpleGps(App.get())
                .updateListener(_gps_listener)
                .priority(SimpleGps.Priority.HIGHEST)
                .start(App.get());

        BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, false);
    }

    @Override
    public int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    public DialogManager getDialogManager() {
        return (DialogManager) findViewById(R.id.dialogManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLoading(true);

        BundleEtaDialog.addOnAcceptedListener(UID_DIALOG_BUNDLE_ETA, _acceptBundleDialog_onAccepted);
        BundleEtaDialog.addOnCancelListener(UID_DIALOG_BUNDLE_ETA, _acceptBundleDialog_onCancel);
        RequestBundleDialog.addOnRequestedListener(UID_DIALOG_REQUEST_BUNDLE, _requestBundleDialog_onRequested);
        WithdrawRequestDialog.addOnWithdrawListener(DIALOG_WITHDRAW, _withdrawRequestDialog_onWithdraw);
        DeclineDialog.addOnDeclinedListener(UID_DIALOG_DECLINE, _declineDialog_onDeclined);

        _bundlesApi = new BundlesWebApi(_bundlesWebApi_listener);
        _bundlesApi.connect(App.get());

        _workOrdersApiClient = new WorkordersWebApi(_workOrdersApiClient_listener);
        _workOrdersApiClient.connect(App.get());

        _workOrderClient = new WorkorderClient(_workOrderClient_listener);
        _workOrderClient.connect(App.get());
    }

    @Override
    protected void onPause() {
        if (_bundlesApi != null) _bundlesApi.disconnect(App.get());

        if (_workOrderClient != null) _workOrderClient.disconnect(App.get());

        if (_workOrdersApiClient != null) _workOrdersApiClient.disconnect(App.get());

        BundleEtaDialog.removeOnAcceptedListener(UID_DIALOG_BUNDLE_ETA, _acceptBundleDialog_onAccepted);
        BundleEtaDialog.removeOnCancelListener(UID_DIALOG_BUNDLE_ETA, _acceptBundleDialog_onCancel);
        RequestBundleDialog.removeOnRequestedListener(UID_DIALOG_REQUEST_BUNDLE, _requestBundleDialog_onRequested);
        WithdrawRequestDialog.removeOnWithdrawListener(DIALOG_WITHDRAW, _withdrawRequestDialog_onWithdraw);
        DeclineDialog.removeOnDeclinedListener(UID_DIALOG_DECLINE, _declineDialog_onDeclined);

        super.onPause();
    }

    private void populateUi() {
        for (WorkOrder workOrder : _workOrders.getResults()) {
            if (workOrder.getDeclines() != null
                    && workOrder.getDeclines().getActionsSet().contains(Declines.ActionsEnum.ADD)) {
                _notInterestedButton.setEnabled(true);
            } else {
                _notInterestedButton.setEnabled(false);
            }


            if (workOrder.getRoutes() != null
                    && workOrder.getRoutes().getUserRoute() != null
                    && workOrder.getRoutes().getUserRoute().getActionsSet().contains(Route.ActionsEnum.ACCEPT)) {
                _buttonToolbar.setVisibility(View.VISIBLE);
                _okButton.setText(R.string.btn_accept);
                break;
            } else if (workOrder.getRequests() != null
                    && workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.ADD)) {
                _buttonToolbar.setVisibility(View.VISIBLE);
                _okButton.setText(R.string.btn_request);
                break;
            } else if (workOrder.getRequests() != null
                    && workOrder.getRequests().getOpenRequest() != null
                    && workOrder.getRequests().getOpenRequest().getActionsSet().contains(Request.ActionsEnum.DELETE)) {
                _buttonToolbar.setVisibility(View.VISIBLE);
                _okButton.setText(R.string.btn_withdraw);
                break;
            } else {
                _buttonToolbar.setVisibility(View.GONE);
            }
        }
    }

    private void setLoading(boolean isloading) {
        if (isloading) {
            _refreshView.post(new Runnable() {
                @Override
                public void run() {
                    _refreshView.startRefreshing();
                }
            });
            _notInterestedButton.setEnabled(false);
            _okButton.setEnabled(false);
        } else {
            _refreshView.refreshComplete();
            _okButton.setEnabled(true);
        }
    }

    @Override
    public void onProfile(Profile profile) {
    }

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WorkOrder workOrder = (WorkOrder) _adapter.getObject(0);
            if (workOrder == null)
                return;

            if (workOrder.getRoutes() != null
                    && workOrder.getRoutes().getUserRoute() != null
                    && workOrder.getRoutes().getUserRoute().getActionsSet().contains(Route.ActionsEnum.ACCEPT)) {
                setLoading(true);
                BundleEtaDialog.show(App.get(), UID_DIALOG_BUNDLE_ETA, _bundleId);

            } else if (workOrder.getRequests() != null
                    && workOrder.getRequests().getActionsSet().contains(Requests.ActionsEnum.ADD)) {
                RequestBundleDialog.show(
                        App.get(),
                        UID_DIALOG_REQUEST_BUNDLE,
                        _bundleId,
                        _adapter.getItemCount(),
                        workOrder.getId(),
                        RequestBundleDialog.TYPE_REQUEST);
            } else if (workOrder.getRequests() != null
                    && workOrder.getRequests().getOpenRequest() != null
                    && workOrder.getRequests().getOpenRequest().getActionsSet().contains(Request.ActionsEnum.DELETE)) {
                WithdrawRequestDialog.show(App.get(), DIALOG_WITHDRAW, workOrder.getId(), workOrder.getRequests().getOpenRequest().getId());

            }
        }
    };

    private final View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_adapter.getItemCount() > 0 && _adapter.getObject(0) != null) {
                DeclineDialog.show(App.get(), UID_DIALOG_DECLINE,
                        _adapter.getItemCount(),
                        ((WorkOrder) _adapter.getObject(0)).getId(),
                        ((WorkOrder) _adapter.getObject(0)).getCompany().getId());
            }
        }
    };

    private final BundleEtaDialog.OnAcceptedListener _acceptBundleDialog_onAccepted = new BundleEtaDialog.OnAcceptedListener() {
        @Override
        public void onAccepted(int bundleId) {
            Log.v(TAG, "onAccepted");
            setLoading(true);
        }
    };

    private final BundleEtaDialog.OnCancelListener _acceptBundleDialog_onCancel = new BundleEtaDialog.OnCancelListener() {
        @Override
        public void onCancel() {
            Log.v(TAG, "onCancel");
            setLoading(false);

        }
    };

    private final RequestBundleDialog.OnRequestedListener _requestBundleDialog_onRequested = new RequestBundleDialog.OnRequestedListener() {
        @Override
        public void onRequested(long workOrderId) {
            setLoading(true);
        }
    };

    private final DeclineDialog.OnDeclinedListener _declineDialog_onDeclined = new DeclineDialog.OnDeclinedListener() {
        @Override
        public void onDeclined(long workOrderId) {
            setLoading(true);
        }
    };

    private final RefreshView.Listener _refreshView_listener = new RefreshView.Listener() {
        @Override
        public void onStartRefresh() {
            _adapter.refreshAll();
        }
    };

    private final SimpleGps.Listener _gps_listener = new SimpleGps.Listener() {
        @Override
        public void onLocation(SimpleGps simpleGps, Location location) {
            _location = location;
            _simpleGps.stop();
            _adapter.refreshAll();
        }

        @Override
        public void onFail(SimpleGps simpleGps) {
            ToastClient.toast(App.get(), R.string.could_not_get_gps_location, Toast.LENGTH_LONG);
        }
    };

    private final WorkorderClient.Listener _workOrderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workOrderClient.subActions();
        }

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            _adapter.refreshAll();
        }
    };

    private final BundlesWebApi.Listener _bundlesWebApi_listener = new BundlesWebApi.Listener() {
        @Override
        public void onConnected() {
            _bundlesApi.subBundlesWebApi();
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.equals("getBundleWorkOrders")) {
                WorkOrders workOrders = (WorkOrders) successObject;
                setLoading(false);

                if (!success || workOrders == null || workOrders.getResults() == null) {
                    return;
                }

                _adapter.addObjects(1, workOrders.getResults());
                _workOrders = workOrders;
                populateUi();
            }
        }
    };

    private final WorkordersWebApi.Listener _workOrdersApiClient_listener = new WorkordersWebApi.Listener() {
        @Override
        public void onConnected() {
            _workOrdersApiClient.subWorkordersWebApi();
        }

        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.contains("decline")
                    || methodName.contains("deleteRequest")
                    || methodName.contains("MassAcceptWorkOrder");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (methodName.contains("decline") && success) {
                ToastClient.toast(App.get(), "Bundle workorders declined successfully.", Toast.LENGTH_LONG);
                BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, false);
            }
            if (methodName.contains("deleteRequest") && success) {
                ToastClient.toast(App.get(), "You withdrew bundle successfully.", Toast.LENGTH_LONG);
                BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, false);
            }
            if (methodName.contains("MassAcceptWorkOrder") && success) {
                setLoading(false);
                ToastClient.toast(App.get(), "Bundle workorders accepted successfully.", Toast.LENGTH_LONG);
                BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, false);
            }

        }
    };

    private final WoPagingAdapter _adapter = new WoPagingAdapter() {
        @Override
        public BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType) {
            WorkOrderCard view = new WorkOrderCard(parent.getContext());
            return new WorkOrderHolder(view);
        }

        @Override
        public void onBindObjectViewHolder(BaseHolder holder, WorkOrder object) {
            WorkOrderHolder h = (WorkOrderHolder) holder;
            WorkOrderCard view = h.getView();
            view.setData(object, _location, "Bundle List");
        }

        @Override
        public boolean useHeader() {
            return false;
        }

        @Override
        public BaseHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return null;
        }

        @Override
        public void onBindHeaderViewHolder(BaseHolder holder) {
        }

        @Override
        public BaseHolder onCreateEmptyViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_no_work, parent, false);
            return new BaseHolder(v, BaseHolder.TYPE_EMPTY);
        }

        @Override
        public void onBindEmptyViewHolder(BaseHolder holder) {
        }

        @Override
        public void requestPage(int page, boolean allowCache) {
            Log.v(TAG, "requestPage");
            if (_bundleId != 0)
                BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, false);
        }
    };

    private final WithdrawRequestDialog.OnWithdrawListener _withdrawRequestDialog_onWithdraw = new WithdrawRequestDialog.OnWithdrawListener() {
        @Override
        public void onWithdraw(int workOrderId) {
            BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, false);
        }
    };


    public static void startNew(Context context, int bundleId) {
        Intent intent = new Intent(context, BundleDetailActivity.class);
        intent.putExtra(BundleDetailActivity.INTENT_FIELD_BUNDLE_ID, bundleId);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}

