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
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.detail.ExpenseView;

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
    private Workorder _workorder;
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

    public void setData(Workorder workorder, List<Expense> expenses) {
        _expenses = expenses;
        _workorder = workorder;

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
            v.setListener(_expense_listener);
            v.setData(_workorder, _expenses.get(i));
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

    private final ExpenseView.Listener _expense_listener = new ExpenseView.Listener() {
        @Override
        public void onDelete(ExpenseView view, Expense expense) {
            if (_listener != null)
                _listener.removeExpense(expense);
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
    }

}
