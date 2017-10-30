package com.fieldnation.v2.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fieldnation.v2.ui.workorder.ExpenseView;
import com.fieldnation.v2.data.model.Expense;

/**
 * Created by mc on 10/10/17.
 */

public class ExpensesAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {
    private static final String TAG = "ExpensesAdapter";

    private Expense[] expenses;
    private ExpensesAdapter.Listener _listener;


    public void setListener(ExpensesAdapter.Listener listener) {
        _listener = listener;
    }

    public void setExpenses(Expense[] expenses) {
        this.expenses = expenses;
        rebuild();
    }

    private void rebuild() {
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
        v.setTag(expenses[position]);
        v.setOnLongClickListener(_expense_onLongClick);
        v.setData(expenses[position]);
    }

    private final View.OnLongClickListener _expense_onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Expense expense = (Expense) v.getTag();
            if (expense.getActionsSet().contains(Expense.ActionsEnum.DELETE)) {
                _listener.onLongClick(v, expense);
                return true;
            }
            return false;
        }
    };

    @Override
    public int getItemCount() {
        if (expenses == null)
            return 0;
        return expenses.length;
    }

    public interface Listener {
        void onLongClick(View v, Expense expense);
    }
}
