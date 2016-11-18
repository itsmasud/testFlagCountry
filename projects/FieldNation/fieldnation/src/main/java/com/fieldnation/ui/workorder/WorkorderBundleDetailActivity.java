package com.fieldnation.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.GlobalTopicClient;
import com.fieldnation.R;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.fndialog.DialogManager;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.AuthSimpleActivity;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.dialog.v2.AcceptBundleDialog;
import com.fieldnation.ui.dialog.v2.DeclineDialog;
import com.fieldnation.ui.dialog.v2.UpdateDialog;

public class WorkorderBundleDetailActivity extends AuthSimpleActivity {
    private static final String TAG = "WorkorderBundleDetailActivity";

    public static final String INTENT_FIELD_WORKORDER_ID = "WorkorderBundleDetailActivity:workorder_id";
    public static final String INTENT_FIELD_BUNDLE_ID = "WorkorderBundleDetailActivity:bundle_id";

    // Dialog tags
    private static final String UID_DIALOG_DECLINE = TAG + ".DeclineDialog";
    private static final String UID_DIALOG_ACCEPT_BUNDLE = TAG + ".AcceptBundleDialog";

    // UI
    private LinearLayout _buttonToolbar;
    private Button _notInterestedButton;
    private Button _okButton;
    private ListView _listview;
    private RefreshView _refreshView;

    // Data
    private long _workorderId = 0;
    private long _bundleId = 0;
    private WorkorderClient _workorderClient;
    private BundleAdapter _adapter;
    private com.fieldnation.data.workorder.Bundle _woBundle;
    private AcceptBundleDialog.Controller _acceptBundleDialog;
    private DeclineDialog.Controller _declineDialog;

    // Services
    private GlobalTopicClient _globalClient;


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

        if (intent.hasExtra(INTENT_FIELD_WORKORDER_ID)) {
            _workorderId = intent.getLongExtra(INTENT_FIELD_WORKORDER_ID, -1);
            _bundleId = intent.getLongExtra(INTENT_FIELD_BUNDLE_ID, -1);
        } else {
            finish();
            return;
        }

        if (_workorderId == -1) {
            finish();
            return;
        }

        _buttonToolbar = (LinearLayout) findViewById(R.id.button_toolbar);
        _notInterestedButton = (Button) findViewById(R.id.notInterested_button);
        _notInterestedButton.setOnClickListener(_notInterested_onClick);
        _okButton = (Button) findViewById(R.id.ok_button);
        _okButton.setOnClickListener(_ok_onClick);
        _listview = (ListView) findViewById(R.id.items_listview);
        _refreshView = (RefreshView) findViewById(R.id.refresh_view);
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

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        _acceptBundleDialog = new AcceptBundleDialog.Controller(App.get(), UID_DIALOG_ACCEPT_BUNDLE);
        _acceptBundleDialog.setListener(_acceptBundleDialog_listener);

        _declineDialog = new DeclineDialog.Controller(App.get(), UID_DIALOG_DECLINE);
        _declineDialog.setListener(_declineDialog_listener);

        _globalClient = new GlobalTopicClient(_globalClient_listener);
        _globalClient.connect(App.get());

        WorkorderClient.getBundle(this, _bundleId);
    }

    @Override
    protected void onPause() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        if (_globalClient != null && _globalClient.isConnected())
            _globalClient.disconnect(App.get());

        if (_acceptBundleDialog != null)
            _acceptBundleDialog.disconnect(App.get());

        if (_declineDialog != null)
            _declineDialog.disconnect(App.get());

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
            DeclineDialog.Controller.show(App.get(), UID_DIALOG_DECLINE,
                    _woBundle.getWorkorder().length,
                    _woBundle.getWorkorder()[0].getWorkorderId(),
                    _woBundle.getWorkorder()[0].getCompanyId());
        }
    };

    private final View.OnClickListener _ok_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Workorder wo = _woBundle.getWorkorder()[0];

            if (wo.getWorkorderSubstatus() == WorkorderSubstatus.AVAILABLE) {
                AcceptBundleDialog.Controller.show(
                        App.get(),
                        UID_DIALOG_ACCEPT_BUNDLE,
                        _woBundle.getBundleId(),
                        _woBundle.getWorkorder().length,
                        wo.getWorkorderId(),
                        AcceptBundleDialog.TYPE_REQUEST);

            } else if (wo.getWorkorderSubstatus() == WorkorderSubstatus.ROUTED) {
                AcceptBundleDialog.Controller.show(
                        App.get(),
                        UID_DIALOG_ACCEPT_BUNDLE,
                        _woBundle.getBundleId(),
                        _woBundle.getWorkorder().length,
                        wo.getWorkorderId(),
                        AcceptBundleDialog.TYPE_ACCEPT);
            } else {
                // do nothing
            }
        }
    };

    private final AcceptBundleDialog.ControllerListener _acceptBundleDialog_listener = new AcceptBundleDialog.ControllerListener() {

        @Override
        public void onRequested() {
            setLoading(true);
        }

        @Override
        public void onAccepted() {
            setLoading(true);
        }

        @Override
        public void onCanceled() {
            // don't care
        }
    };

    private final DeclineDialog.ControllerListener _declineDialog_listener = new DeclineDialog.ControllerListener() {
        @Override
        public void onDeclined(long workOrderId) {
            setLoading(true);
        }

        @Override
        public void onCancel() {

        }
    };

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
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

    private final WorkorderCardView.Listener _wocard_listener = new WorkorderCardView.DefaultListener() {
        @Override
        public void onClick(WorkorderCardView view, Workorder workorder) {
            WorkorderActivity.startNew(App.get(), workorder.getWorkorderId());
        }
    };

    private final GlobalTopicClient.Listener _globalClient_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalClient.subAppShutdown();
        }

        @Override
        public void onShutdown() {
            finish();
        }
    };


    public static void startNew(Context context, long workorderId, long bundleId) {
        Intent intent = new Intent(context, WorkorderBundleDetailActivity.class);
        intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_WORKORDER_ID, workorderId);
        intent.putExtra(WorkorderBundleDetailActivity.INTENT_FIELD_BUNDLE_ID, bundleId);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_right, R.anim.activity_slide_out_left);
    }
}

