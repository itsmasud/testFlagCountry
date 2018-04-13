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
import com.fieldnation.ui.ApatheticOnClickListener;
import com.fieldnation.ui.ApatheticOnMenuItemClickListener;
import com.fieldnation.ui.FnToolBarView;
import com.fieldnation.ui.OverScrollRecyclerView;
import com.fieldnation.v2.data.client.WorkordersWebApi;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.Expenses;
import com.fieldnation.v2.ui.ExpensesAdapter;

/**
 * Created by mc on 10/9/17.
 */

public class ExpenseListDialog extends FullScreenDialog {
    private static final String TAG = "ExpenseListDialog";

    private static final String DIALOG_DELETE_EXPENSE = TAG + ".deleteExpenseDialog";

    // Ui
    private FnToolBarView _fnToolbarView;
    private ActionMenuItemView _finishMenu;
    private OverScrollRecyclerView _list;

    // Data
    private int _workOrderId;
    private Expenses _expenses;
    private ExpensesAdapter _adapter = new ExpensesAdapter();

    public ExpenseListDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        View v = inflater.inflate(R.layout.dialog_v2_expenses_list, container, false);

        _fnToolbarView = v.findViewById(R.id.fnToolbar);
        _fnToolbarView.getToolbar().setNavigationIcon(R.drawable.back_arrow);
        _fnToolbarView.getToolbar().inflateMenu(R.menu.dialog);
        _fnToolbarView.getToolbar().setTitle("Expenses");

        _finishMenu = _fnToolbarView.getToolbar().findViewById(R.id.primary_menu);
        _finishMenu.setText(R.string.btn_add);

        _list = v.findViewById(R.id.list);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        _fnToolbarView.getToolbar().setOnMenuItemClickListener(_menu_onClick);
        _fnToolbarView.getToolbar().setNavigationOnClickListener(_toolbar_onClick);

        _list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        _list.setAdapter(_adapter);

        _adapter.setListener(_expenses_listener);

        _workOrdersApi.sub();

        TwoButtonDialog.addOnPrimaryListener(DIALOG_DELETE_EXPENSE, _twoButtonDialog_deleteExpense);

        LocalBroadcastManager.getInstance(App.get()).registerReceiver(_webTransactionChanged, new IntentFilter(WebTransaction.BROADCAST_ON_CHANGE));
    }

    @Override
    public void show(Bundle params, boolean animate) {
        super.show(params, animate);
        _workOrderId = params.getInt("workOrderId");
        WorkordersWebApi.getExpenses(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
        populateUi();
    }

    private void populateUi() {
        _finishMenu.setVisibility(View.GONE);

        if (_list == null)
            return;

        if (_expenses == null)
            return;

        if (_expenses.getActionsSet().contains(Expenses.ActionsEnum.ADD)) {
            _finishMenu.setVisibility(View.VISIBLE);
        }

        _adapter.setExpenses(_workOrderId, _expenses.getResults());
    }

    @Override
    public void onStop() {
        TwoButtonDialog.removeOnPrimaryListener(DIALOG_DELETE_EXPENSE, _twoButtonDialog_deleteExpense);
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
            WorkOrderTracker.onAddEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.EXPENSES);
            ExpenseDialog.show(App.get(), null, _workOrderId, false, true);
            return false;
        }
    };

    private final ExpensesAdapter.Listener _expenses_listener = new ExpensesAdapter.Listener() {
        @Override
        public void onLongClick(View v, Expense expense, WebTransaction webTransaction) {
            Bundle bundle = new Bundle();
            if (webTransaction != null)
                bundle.putParcelable("wt", webTransaction);
            bundle.putParcelable("exp", expense);

            TwoButtonDialog.show(App.get(), DIALOG_DELETE_EXPENSE,
                    R.string.dialog_delete_expense_title,
                    R.string.dialog_delete_expense_body,
                    R.string.btn_yes, R.string.btn_no, true, bundle);
        }
    };

    private final TwoButtonDialog.OnPrimaryListener _twoButtonDialog_deleteExpense = new TwoButtonDialog.OnPrimaryListener() {
        @Override
        public void onPrimary(Parcelable extraData) {
            if (((Bundle) extraData).containsKey("wt")) {
                WebTransaction webTransaction = ((Bundle) extraData).getParcelable("wt");
                WebTransaction.delete(webTransaction.getId());
                populateUi();
            } else {
                WorkOrderTracker.onDeleteEvent(App.get(), WorkOrderTracker.WorkOrderDetailsSection.EXPENSES);
                WorkordersWebApi.deleteExpense(App.get(), _workOrderId, (Expense) ((Bundle) extraData).getParcelable("exp"), App.get().getSpUiContext());
            }
        }
    };

    private final BroadcastReceiver _webTransactionChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (_expenses != null) {
                String op = intent.getStringExtra("op");
                if (intent.hasExtra("key")) {
                    String key = intent.getStringExtra("key");
                    if (key != null && (key.contains("addExpenseByWorkOrder") || key.contains("deleteExpenseByWorkOrderAndExpense")))
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
            return methodName.toLowerCase().contains("expense");
        }

        @Override
        public boolean onComplete(UUIDGroup uuidGroup, TransactionParams transactionParams, String methodName, Object successObject, boolean success, Object failObject, boolean isCached) {
            if (successObject != null && successObject instanceof Expenses) {
                Expenses expenses = (Expenses) successObject;
                if (_expenses != null) {
                    if (_expenses.getResults().length != expenses.getResults().length) {
                        _expenses = expenses;
                        populateUi();
                    } else {
                        for (int i = 0; i < _expenses.getResults().length; i++) {
                            if (_expenses.getResults()[i].getId() != expenses.getResults()[i].getId().intValue()) {
                                _expenses = expenses;
                                populateUi();
                                break;
                            }
                        }
                    }
                } else {
                    _expenses = expenses;
                    populateUi();
                }
            } else {
                WorkordersWebApi.getExpenses(App.get(), _workOrderId, true, WebTransaction.Type.NORMAL);
            }
            return super.onComplete(uuidGroup, transactionParams, methodName, successObject, success, failObject, isCached);
        }
    };

    public static void show(Context context, String uid, int workOrderId) {
        Bundle params = new Bundle();
        params.putInt("workOrderId", workOrderId);

        Controller.show(context, uid, ExpenseListDialog.class, params);
    }
}
