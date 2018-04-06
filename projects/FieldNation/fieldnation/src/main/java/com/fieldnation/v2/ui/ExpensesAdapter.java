package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.transaction.WebTransaction;
import com.fieldnation.service.transaction.WebTransactionUtils;
import com.fieldnation.v2.data.listener.TransactionParams;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.PayModifier;
import com.fieldnation.v2.ui.workorder.ExpenseView;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mc on 10/10/17.
 */

public class ExpensesAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {
    private static final String TAG = "ExpensesAdapter";

    private List<Object> objects = new LinkedList<>();
    private Expense[] expenses;
    private List<Tuple> addedExpenses = new LinkedList<>();
    private Hashtable<Integer, WebTransaction> deletedExpenses = new Hashtable<>();
    private ExpensesAdapter.Listener _listener;
    private int _workOrderId;
    private int _running = 0;
    private boolean _runAgain = false;

    private static class Tuple {
        public WebTransaction webTransaction;
        public Expense expense;

        public Tuple(WebTransaction webTransaction, Expense expense) {
            this.webTransaction = webTransaction;
            this.expense = expense;
        }
    }

    public void setListener(ExpensesAdapter.Listener listener) {
        _listener = listener;
    }

    public void setExpenses(int workOrderId, Expense[] expenses) {
        this.expenses = expenses;
        this._workOrderId = workOrderId;
        if (_running == 0) {
            _running = 2;
            addedExpenses.clear();
            deletedExpenses.clear();
            WebTransactionUtils.setData(_addExpense, WebTransactionUtils.KeyType.ADD_EXPENSE, workOrderId);
            WebTransactionUtils.setData(_deleteExpense, WebTransactionUtils.KeyType.DELETE_EXPENSE, workOrderId);
        } else {
            _runAgain = true;
        }
    }

    private void rebuild() {
        if (_runAgain) {
            _runAgain = false;
            setExpenses(_workOrderId, expenses);
            return;
        }

        objects.clear();

        for (Tuple expense : addedExpenses) {
            objects.add(expense);
        }

        for (Expense expense : expenses) {
            if (deletedExpenses.containsKey(expense.getId()))
                continue;

            objects.add(expense);
        }

        notifyDataSetChanged();
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExpenseView v = new ExpenseView(parent.getContext());
        return new ExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        ExpenseView v = (ExpenseView) holder.itemView;
        v.setTag(objects.get(position));
        v.setOnLongClickListener(_expense_onLongClick);
        Object object = objects.get(position);
        if (object instanceof Tuple) {
            v.setAlert(true);
            v.setData(((Tuple) object).expense);
        } else if (object instanceof Expense) {
            v.setAlert(false);
            Expense expense = (Expense) object;
            v.setData(expense);
        }
    }

    private final View.OnLongClickListener _expense_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Object object = v.getTag();
            WebTransaction webTransaction = null;
            Expense expense = null;
            if (object instanceof Tuple) {
                expense = ((Tuple) object).expense;
                webTransaction = ((Tuple) object).webTransaction;
            } else if (object instanceof Expense) {
                expense = (Expense) object;
            }
            if (expense.getActionsSet().contains(Expense.ActionsEnum.DELETE) || webTransaction != null) {
                _listener.onLongClick(v, expense, webTransaction);
                return true;
            }
            return false;
        }
    };

    private final WebTransactionUtils.Listener _addExpense = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                Expense expense = Expense.fromJson(new JsonObject(tp.methodParams).getJsonObject("expense"));
                addedExpenses.add(new Tuple(webTransaction, expense));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running--;
            if (_running == 0) rebuild();
        }
    };

    private final WebTransactionUtils.Listener _deleteExpense = new WebTransactionUtils.Listener() {
        @Override
        public void onFoundWebTransaction(WebTransactionUtils.KeyType keyType, int workOrderId, WebTransaction webTransaction) {
            try {
                TransactionParams tp = TransactionParams.fromJson(new JsonObject(webTransaction.getListenerParams()));
                int id = new JsonObject(tp.methodParams).getInt("expenseId");
                deletedExpenses.put(id, webTransaction);
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }

        @Override
        public void onComplete() {
            _running--;
            if (_running == 0) rebuild();
        }
    };

    @Override
    public int getItemCount() {
        if (objects == null)
            return 0;
        return objects.size();
    }

    public interface Listener {
        void onLongClick(View v, Expense expense, WebTransaction webTransaction);
    }
}
