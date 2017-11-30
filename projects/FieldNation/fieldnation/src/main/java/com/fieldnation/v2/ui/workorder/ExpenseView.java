package com.fieldnation.v2.ui.workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Expense;

public class ExpenseView extends LinearLayout {
    private static final String TAG = "ExpenseView";

    // UI
    private TextView _descriptionTextView;
    private TextView _categoryTextView;
    private TextView _costTextView;

    // Data
    private Expense _expense = null;

    /*-*************************************-*/
    /*-				Life Cycle				-*/
    /*-*************************************-*/
    public ExpenseView(Context context) {
        super(context);
        init();
    }

    public ExpenseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_expense, this);

        if (isInEditMode())
            return;

        _descriptionTextView = findViewById(R.id.description_textview);
        _categoryTextView = findViewById(R.id.category_textview);
        _costTextView = findViewById(R.id.cost_textview);
    }

    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    public void setData(Expense expense) {
        _expense = expense;
        refresh();
    }

    public Expense getExpense() {
        return _expense;
    }

    private void refresh() {
        if (_expense == null)
            return;

        if (_expense.getDescription() != null)
            _descriptionTextView.setText(_expense.getDescription());
        else
            _descriptionTextView.setText("NA");

        _categoryTextView.setVisibility(View.GONE);
        if (_expense.getCategory() != null && _expense.getCategory().getName() != null) {
            _categoryTextView.setText(_expense.getCategory().getName());
            _categoryTextView.setVisibility(View.VISIBLE);
        }
        if (_expense.getAmount() != null)
            _costTextView.setText(misc.toCurrency(_expense.getAmount()));
        else
            _costTextView.setText(misc.toCurrency(0));
    }
}
