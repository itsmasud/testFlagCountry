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
import com.fieldnation.AppMessagingClient;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.UUIDGroup;
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Pay;
import com.fieldnation.v2.data.model.PayModifier;
import com.fieldnation.v2.data.model.PayModifiers;
import com.fieldnation.v2.ui.DiscountsAdapter;

/**
 * Created by mc on 10/9/17.
 */

public class DiscountListDialog extends FullScreenDialog {
    private static final String TAG = "DiscountListDialog";

    private static final String DIALOG_DELETE_DISCOUNT = TAG + ".deleteDiscountDialog";

    // Ui
    private Toolbar _toolbar;
    private ActionMenuItemView _finishMenu;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId;
    private PayModifiers _discounts;
    private DiscountsAdapter _adapter = new DiscountsAdapter();

    public DiscountListDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_discount_list, container, false);

        _toolbar = v.findViewById(R.id.toolbar);
        _toolbar.setNavigationIcon(R.drawable.back_arrow);
        _toolbar.inflateMenu(R.menu.dialog);
        _toolbar.setTitle("Discounts");

        _finishMenu = _toolbar.findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_add);

        _list = v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _toolbar.setOnMenuItemClickListener(_menu_onClick);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _list.setAdapter(_adapter);

        _adapter.setListener(_discounts_listener);

        _workOrdersApi.sub();

        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_DISCOUNT, _twoButtonDialog_deleteDiscount);

        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCAST_ON_CHANGE));
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        _finishMenu.setVisibility(View.GONE);
        _workOrderId = params.getInt("workOrderId");
        WorkordersWebApi.getDiscounts(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
        populateUi();
    }

    private void populateUi() {
        Log.v(TAG, "populateUi");
        if (_list == null) return;
        if (_discounts == null) return;

        if (_discounts.getActionsSet().contains(PayModifiers.ActionsEnum.ADD)) {
            _finishMenu.setVisibility(View.VISIBLE);
        } else {
            _finishMenu.setVisibility(View.GONE);
        }
        _adapter.setDiscounts(_workOrderId, _discounts.getResults());
    }

    @Override
    public void onStop() {
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_DISCOUNT, _twoButtonDialog_deleteDiscount);
        _workOrdersApi.unsub();
        LocalBroadcastManager.getInstance(App.get()).unregisterReceiver(_webTransactionChanged);
        super.onStop();
    }

    private final View.OnClickListener _toolbar_onClick = new ApatheticOnClickListener() {
        @Override
        public void onSingleClick(View v) {
            dismiss(true);
        }
    };

    private final Toolbar.OnMenuItemClickListener _menu_onClick = new ApatheticOnMenuItemClickListener() {
        @Override
        public boolean onSingleMenuItemClick(MenuItem item) {
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
            DiscountDialog.show(App.get(), null, _workOrderId, getContext().getString(R.string.dialog_add_discount_title));
            return false;
        }
    };

    private final DiscountsAdapter.Listener _discounts_listener = new DiscountsAdapter.Listener() {
        @Override
        public void onLongClick(View v, PayModifier discount, WebTransaction webTransaction) {
            Bundle bundle = new Bundle();
            if (webTransaction != null)
                bundle.putParcelable("wt", webTransaction);

            bundle.putParcelable("pm", discount);

            TwoButtonDialog.show(App.get(), DIALOG_DELETE_DISCOUNT,
                    R.string.dialog_delete_discount_title,
                    R.string.dialog_delete_discount_body,
                    R.string.btn_yes, R.string.btn_no, true, bundle);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteDiscount = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            if (((Bundle) extraData).containsKey("wt")) {
                WebTransaction webTransaction = ((Bundle) extraData).getParcelable("wt");
                WebTransaction.delete(webTransaction.getId());
                populateUi();
            } else {
                WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
                WorkordersWebApi.deleteDiscount(App.get(), _workOrderId, (PayModifier) ((Bundle) extraData).getParcelable("pm"), App.get().getSpUiContext());
            }
        }
    };

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (_discounts != null) {
                String op = intent.getStringExtra("op");
                if (intent.hasExtra("key")) {
                    String key = intent.getStringExtra("key");
                    if (key != null && (key.contains("addDiscountByWorkOrder") || key.contains("deleteDiscountByWorkOrder")))
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

            return methodName.toLowerCase().contains("discount");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && successObject instanceof PayModifiers) {
                PayModifiers discounts = (PayModifiers) successObject;
                if (_discounts != null) {

                    if (_discounts.getResults().length != discounts.getResults().length) {
                        _discounts = discounts;
                        populateUi();
                    } else {
                        for (int i = 0; i < _discounts.getResults().length; i++) {
                            if (_discounts.getResults()[i].getId() != discounts.getResults()[i].getId().intValue()) {
                                _discounts = discounts;
                                populateUi();
                                break;
                            }
                        }
                    }
                } else {
                    _discounts = discounts;
                    populateUi();
                }
            } else {
                WorkordersWebApi.getDiscounts(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, DiscountListDialog.class, params);
    }
}
