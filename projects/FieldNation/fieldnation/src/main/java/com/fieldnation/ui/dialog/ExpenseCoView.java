package com.fieldnation.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.ui.workorder.detail.ExpenseView;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.WorkOrder;

import java.util.List;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class ExpenseCoView extends RelativeLayout {
    //Ui
    private TextView _noExpensesTextView;
    private LinearLayout _expensesList;
    private Button _addButton;
    private Button _resetButton;

    // Data
    private WorkOrder _workOrder;
    private List<Expense> _expenses;
    private Listener _listener;

    public ExpenseCoView(Context context) {
        super(context);
        init();
    }

    public ExpenseCoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpenseCoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_co_expenses_tile, this);

        if (isInEditMode())
            return;

        _expensesList = (LinearLayout) findViewById(R.id.expenses_list_layout);
        _noExpensesTextView = (TextView) findViewById(R.id.no_expenses_textview);

        _addButton = (Button) findViewById(R.id.add_button);
        _addButton.setOnClickListener(_add_onClick);

        _resetButton = (Button) findViewById(R.id.reset_button);
        _resetButton.setOnClickListener(_reset_onClick);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setData(WorkOrder workOrder, List<Expense> expenses) {
        _expenses = expenses;
        _workOrder = workOrder;

        populateUi();
    }

    public void populateUi() {
        if (_noExpensesTextView == null)
            return;

        if (_expensesList == null)
            return;

        if (_expenses == null || _expenses.size() == 0) {
            _expensesList.setVisibility(View.GONE);
            _noExpensesTextView.setVisibility(View.VISIBLE);
            return;
        }
        _expensesList.setVisibility(View.VISIBLE);
        _noExpensesTextView.setVisibility(View.GONE);

        _expensesList.removeAllViews();
        for (int i = 0; i < _expenses.size(); i++) {
            ExpenseView v = new ExpenseView(getContext());
            v.setData(_workOrder, _expenses.get(i));
            v.setOnClickListener(_expense_onClick);
            v.setOnLongClickListener(_expense_onLongClick);
            _expensesList.addView(v);
        }
    }

    private final View.OnClickListener _reset_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.reset();
        }
    };

    private final View.OnClickListener _expense_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null) {
                _listener.editExpense(((ExpenseView) v).getExpense());
            }
        }
    };

    private final View.OnLongClickListener _expense_onLongClick = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (_listener != null) {
                _listener.removeExpense(((ExpenseView) v).getExpense());
                return true;
            }
            return false;
        }
    };

    private final View.OnClickListener _add_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.addExpense();
        }
    };

    public interface Listener {
        void addExpense();

        void removeExpense(Expense expense);

        void reset();

        void editExpense(Expense expense);
    }

}
