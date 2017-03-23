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
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.v2.AcceptBundleDialog;
import com.fieldnation.v2.data.client.BundlesWebApi;
import com.fieldnation.v2.data.model.Error;
import com.fieldnation.v2.data.model.WorkOrder;
import com.fieldnation.v2.data.model.WorkOrders;
import com.fieldnation.v2.ui.dialog.DeclineDialog;
import com.fieldnation.v2.ui.worecycler.BaseHolder;
import com.fieldnation.v2.ui.worecycler.WoPagingAdapter;
import com.fieldnation.v2.ui.worecycler.WorkOrderHolder;
import com.fieldnation.v2.ui.workorder.WorkOrderCard;

public class BundleDetailActivity extends AuthSimpleActivity {
    private static final String TAG = "BundleDetailActivity";

    public static final String INTENT_FIELD_BUNDLE_ID = "BundleDetailActivity:bundle_id";

    // Dialog tags
    private static final String UID_DIALOG_DECLINE = TAG + ".DeclineDialog";
    private static final String UID_DIALOG_ACCEPT_BUNDLE = TAG + ".AcceptBundleDialog";

    // UI
    private LinearLayout _buttonToolbar;
    private Button _notInterestedButton;
    private Button _okButton;
    private OverScrollRecyclerView _listview;
    private RefreshView _refreshView;

    // Data
    private int _bundleId = 0;
    private Location _location;

    // Services
    private BundlesWebApi _bundlesApi;
    private SimpleGps _simpleGps;

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

        AcceptBundleDialog.addOnAcceptedListener(UID_DIALOG_ACCEPT_BUNDLE, _acceptBundleDialog_onAccepted);
        AcceptBundleDialog.addOnRequestedListener(UID_DIALOG_ACCEPT_BUNDLE, _acceptBundleDialog_onRequested);

        DeclineDialog.addOnDeclinedListener(UID_DIALOG_DECLINE, _declineDialog_onDeclined);

        _bundlesApi = new BundlesWebApi(_bundlesWebApi_listener);
        _bundlesApi.connect(App.get());
    }

    @Override
    protected void onPause() {
        if (_bundlesApi != null && _bundlesApi.isConnected())
            _bundlesApi.disconnect(App.get());

        AcceptBundleDialog.removeOnAcceptedListener(UID_DIALOG_ACCEPT_BUNDLE, _acceptBundleDialog_onAccepted);
        AcceptBundleDialog.removeOnRequestedListener(UID_DIALOG_ACCEPT_BUNDLE, _acceptBundleDialog_onRequested);

        DeclineDialog.removeOnDeclinedListener(UID_DIALOG_DECLINE, _declineDialog_onDeclined);

        super.onPause();
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
            _notInterestedButton.setEnabled(true);
            _okButton.setEnabled(true);
        }
    }

    @Override
    public void onProfile(Profile profile) {
    }

    private final View.OnClickListener _notInterested_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
/*
TODO            DeclineDialog.show(App.get(), UID_DIALOG_DECLINE,
                    _woBundle.getWorkorder().length,
                    _woBundle.getWorkorder()[0].getWorkorderId().intValue(),
                    _woBundle.getWorkorder()[0].getCompanyId());
*/
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
/*
TODO            Workorder wo = _woBundle.getWorkorder()[0];

            if (wo.getWorkorderSubstatus() == WorkorderSubstatus.AVAILABLE) {
                AcceptBundleDialog.show(
                        App.get(),
                        UID_DIALOG_ACCEPT_BUNDLE,
                        _woBundle.getBundleId(),
                        _woBundle.getWorkorder().length,
                        wo.getWorkorderId(),
                        AcceptBundleDialog.TYPE_REQUEST);

            } else if (wo.getWorkorderSubstatus() == WorkorderSubstatus.ROUTED) {
                AcceptBundleDialog.show(
                        App.get(),
                        UID_DIALOG_ACCEPT_BUNDLE,
                        _woBundle.getBundleId(),
                        _woBundle.getWorkorder().length,
                        wo.getWorkorderId(),
                        AcceptBundleDialog.TYPE_ACCEPT);
            } else {
                // do nothing
            }
*/
        }
    };

    private final AcceptBundleDialog.OnAcceptedListener _acceptBundleDialog_onAccepted = new AcceptBundleDialog.OnAcceptedListener() {
        @Override
        public void onAccepted(long workOrderId) {
            setLoading(true);
        }
    };

    private final AcceptBundleDialog.OnRequestedListener _acceptBundleDialog_onRequested = new AcceptBundleDialog.OnRequestedListener() {
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

/*
TODO    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
            _workorderClient.subBundle();
            _workorderClient.subActions();
        }

        @Override
        public void onGetBundle(com.fieldnation.data.workorder.Bundle bundle, boolean failed) {
            if (bundle == null) {
                ToastClient.toast(WorkorderBundleDetailActivity.this, "Sorry, could not get bundle details.", Toast.LENGTH_LONG);
                finish();
                return;
            }
            _woBundle = bundle;
            Workorder wo = bundle.getWorkorder()[0];

            try {
                if (wo.getWorkorderSubstatus() == WorkorderSubstatus.AVAILABLE) {
                    _okButton.setText("REQUEST (" + _woBundle.getWorkorder().length + ")");
                    _buttonToolbar.setVisibility(View.VISIBLE);
                } else if (wo.getWorkorderSubstatus() == WorkorderSubstatus.ROUTED) {
                    _okButton.setText("ACCEPT (" + _woBundle.getWorkorder().length + ")");
                    _buttonToolbar.setVisibility(View.VISIBLE);
                } else {
                    _buttonToolbar.setVisibility(View.GONE);
                }
                setLoading(false);
            } catch (Exception ex) {
            }

            _adapter = new BundleAdapter(_woBundle, _wocard_listener);
            _listview.setAdapter(_adapter);
        }

        @Override
        public void onAction(long workorderId, String action, boolean failed) {
            setLoading(true);
            WorkorderClient.getBundle(App.get(), _bundleId, false, false);
        }
    };
*/

    /*
        private final WorkorderCardView.Listener _wocard_listener = new WorkorderCardView.DefaultListener() {
            @Override
            public void onClick(WorkorderCardView view, Workorder workorder) {
    // TODO            WorkOrderActivity.startNew(App.get(), workorder.getWorkorderId().intValue());
            }
        };
    */
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

    private final BundlesWebApi.Listener _bundlesWebApi_listener = new BundlesWebApi.Listener() {
        @Override
        public void onConnected() {
            _bundlesApi.subBundlesWebApi();
        }

        @Override
        public void onGetBundleWorkOrders(WorkOrders workOrders, boolean success, Error error) {
            _refreshView.refreshComplete();

            if (!success || workOrders == null || workOrders.getResults() == null) {
                return;
            }

            _adapter.addObjects(1, workOrders.getResults());
        }
    };

    private final WoPagingAdapter _adapter = new WoPagingAdapter() {
        @Override
        public BaseHolder onCreateObjectViewHolder(ViewGroup parent, int viewType) {
            WorkOrderCard view = new WorkOrderCard(parent.getContext());

            // TODO, hook up listener?
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
            BundlesWebApi.getBundleWorkOrders(App.get(), _bundleId, false, false);
        }
    };

    public static void startNew(Context context, int bundleId) {
        Intent intent = new Intent(context, BundleDetailActivity.class);
        intent.putExtra(BundleDetailActivity.INTENT_FIELD_BUNDLE_ID, bundleId);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}

