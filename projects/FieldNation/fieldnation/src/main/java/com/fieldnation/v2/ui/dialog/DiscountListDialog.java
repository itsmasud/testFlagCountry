package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.fieldnation.analytics.trackers.WorkOrderTracker;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
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
    private RefreshView _refreshView;
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

        _refreshView = v.findViewById(R.id.refresh_view);

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
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        _finishMenu.setVisibility(View.GONE);
        _workOrderId = params.getInt("workOrderId");
        WorkordersWebApi.getDiscounts(App.get(), _workOrderId, true, false);
        populateUi();
    }

    private void populateUi() {
        if (_list == null) return;
        if (_discounts == null) return;

        if (_discounts.getActionsSet().contains(PayModifiers.ActionsEnum.ADD)) {
            _finishMenu.setVisibility(View.VISIBLE);
        } else {
            _finishMenu.setVisibility(View.GONE);
        }
        _adapter.setDiscounts(_discounts.getResults());
    }

    @Override
    public void onStop() {
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_DISCOUNT, _twoButtonDialog_deleteDiscount);
        _workOrdersApi.unsub();
        super.onStop();
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
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
            DiscountDialog.show(App.get(), null, _workOrderId, getContext().getString(R.string.dialog_add_discount_title));

            return false;
        }
    };

    private final DiscountsAdapter.Listener _discounts_listener = new DiscountsAdapter.Listener() {
        @Override
        public void onLongClick(View v, PayModifier discount) {
            TwoButtonDialog.show(App.get(), DIALOG_DELETE_DISCOUNT,
                    R.string.dialog_delete_discount_title,
                    R.string.dialog_delete_discount_body,
                    R.string.btn_yes, R.string.btn_no, true, discount);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteDiscount = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            AppMessagingClient.setLoading(true);
            WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.DISCOUNTS);
            WorkordersWebApi.deleteDiscount(App.get(), _workOrderId, ((PayModifier) extraData).getId(), App.get().getSpUiContext());
        }
    };

    private final WorkordersWebApi _workOrdersApi = new WorkordersWebApi() {
        @Override
        public boolean processTransaction(TransactionParams transactionParams, String methodName) {
            return methodName.toLowerCase().contains("discount");
        }

        @Override
        public void onComplete(TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject) {
            if (successObject != null && successObject instanceof PayModifiers) {
                PayModifiers discounts = (PayModifiers) successObject;
                _discounts = discounts;
                AppMessagingClient.setLoading(false);
                populateUi();
            } else {
                WorkordersWebApi.getDiscounts(App.get(), _workOrderId, true, false);
            }
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, DiscountListDialog.class, params);
    }
}
