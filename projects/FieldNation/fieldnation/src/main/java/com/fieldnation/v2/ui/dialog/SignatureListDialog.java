package com.fieldnation.v2.ui.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.SignOffActivity;
import com.fieldnation.ui.SignatureDisplayActivity;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Signature;
import com.fieldnation.v2.data.model.Signatures;
import com.fieldnation.v2.ui.SignatureAdapter;

/**
 * Created by Shoaib on 10/11/17.
 */

public class SignatureListDialog extends FullScreenDialog {
    private static final String TAG = "SignatureListDialog";

    private static final String DIALOG_DELETE_SIGNATURE = TAG + ".deleteSignatureDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId;
    private Signatures _signatures;
    private SignatureAdapter _adapter = new SignatureAdapter();

    public SignatureListDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_expenses_list, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle("Signatures");

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_add);
        _finishMenu.setVisibility(View.GONE);

        _list = v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _finishMenu.setVisibility(View.GONE);

        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _list.setAdapter(_adapter);

        _adapter.setListener(_signatures_listener);
        _workOrdersApi.sub();

        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_SIGNATURE, _twoButtonDialog_deleteSignature);
        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCAST_ON_CHANGE));
        populateUi();
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);

        _workOrderId = params.getInt("workOrderId");
        WorkordersWebApi.getSignatures(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
        populateUi();
    }

    @Override
    public void onStop() {
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_SIGNATURE, _twoButtonDialog_deleteSignature);
        LocalBroadcastManager.getInstance(App.get()).unregisterReceiver(_webTransactionChanged);

        _workOrdersApi.unsub();
        super.onStop();
    }

    private void populateUi() {
        if (_list == null)
            return;

        if (_signatures == null)
            return;

        if (_signatures.getActionsSet().contains(Signatures.ActionsEnum.ADD)) {
            _finishMenu.setVisibility(View.VISIBLE);
        } else {
            _finishMenu.setVisibility(View.GONE);
        }
        _adapter.setSignatures(_workOrderId, _signatures.getResults());

    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SIGNATURES);
            SignOffActivity.startSignOff(App.get(), _workOrderId);

            return false;
        }
    };

    private final SignatureAdapter.Listener _signatures_listener = new SignatureAdapter.Listener() {
        @Override
        public void onLongClick(View v, Signature signature, WebTransaction webTransaction) {
            Bundle bundle = new Bundle();
            if (webTransaction != null)
                bundle.putParcelable("wt", webTransaction);
            bundle.putParcelable("sign", signature);

            TwoButtonDialog.show(App.get(), DIALOG_DELETE_SIGNATURE,
                    R.string.dialog_delete_signature_title,
                    R.string.dialog_delete_signature_body,
                    R.string.btn_yes, R.string.btn_no, true, bundle);
        }

        @Override
        public void signatureOnClick(View v, Signature signature) {
            SignatureDisplayActivity.startIntent(App.get(), signature);

        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteSignature = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            if (((Bundle) extraData).containsKey("wt")) {
                WebTransaction webTransaction = ((Bundle) extraData).getParcelable("wt");
                WebTransaction.delete(webTransaction.getId());
                populateUi();
            } else {
                WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.SIGNATURES);
                WorkordersWebApi.deleteSignature(App.get(), _workOrderId, (Signature) ((Bundle) extraData).getParcelable("sign"), App.get().getSpUiContext());
            }

        }
    };

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (_signatures != null) {
                String op = intent.getStringExtra("op");
                if (intent.hasExtra("key")) {
                    String key = intent.getStringExtra("key");
                    if (key != null && (key.contains("addSignatureByWorkOrder") || key.contains("deleteSignatureByWorkOrderAndSignature")))
                        populateUi();
                }
            }
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName) {
            if (transactionParams.getMethodParamInt("workOrderId") == null
                    || transactionParams.getMethodParamInt("workOrderId") != _workOrderId)
                return false;

            return methodName.toLowerCase().contains("signature");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && successObject instanceof Signatures) {
                Signatures signatures = (Signatures) successObject;

                if (_signatures != null) {
                    if (_signatures.getResults().length != signatures.getResults().length) {
                        _signatures = signatures;
                        populateUi();
                    } else {
                        for (int i = 0; i < _signatures.getResults().length; i++) {
                            if (_signatures.getResults()[i].getId() != signatures.getResults()[i].getId().intValue()) {
                                _signatures = signatures;
                                populateUi();
                                break;
                            }
                        }
                    }
                } else {
                    _signatures = signatures;
                    populateUi();
                }
            } else {
                WorkordersWebApi.getSignatures(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, SignatureListDialog.class, params);
    }
}
