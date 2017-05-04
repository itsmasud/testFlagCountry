package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.Expense;
import com.fieldnation.v2.data.model.ExpenseCategories;
import com.fieldnation.v2.data.model.ExpenseCategory;
import com.fieldnation.v2.data.model.WorkOrder;

public class ExpenseView extends LinearLayout {
    private static final String TAG = "ExpenseView";

    // UI
    private TextView _descriptionTextView;
    private TextView _categoryTextView;
    private TextView _costTextView;

    // Data
    private WorkOrder _workOrder;
    private Expense _expense = null;
    private ExpenseCategory[] _categories;

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

        _descriptionTextView = (TextView) findViewById(R.id.description_textview);
        _categoryTextView = (TextView) findViewById(R.id.category_textview);
        _costTextView = (TextView) findViewById(R.id.cost_textview);

        ExpenseCategories categories = new ExpenseCategories(getContext());
        categories.setListener(_categoriesListener);
    }

    /*-*********************************-*/
    /*-				Event				-*/
    /*-*********************************-*/


    private final ExpenseCategories.Listener _categoriesListener = new ExpenseCategories.Listener() {
        @Override
        public void onHaveCategories(ExpenseCategory[] categories) {
            _categories = categories;
            refresh();
        }
    };


    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    public void setData(WorkOrder workOrder, Expense expense) {
        _expense = expense;
        _workOrder = workOrder;
        refresh();
    }

    public Expense getExpense() {
        return _expense;
    }

    private void refresh() {
        if (_expense == null)
            return;

        if (_categories == null)
            return;

        _descriptionTextView.setText(_expense.getDescription());
        _categoryTextView.setVisibility(View.GONE);
        if (_categories != null && _expense.getId() != null) {
            for (ExpenseCategory _category : _categories) {
                if (_category.getId().equals(_expense.getCategory().getId())) {
                    _categoryTextView.setText(_category.getName());
                    _categoryTextView.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        _costTextView.setText(misc.toCurrency(_expense.getAmount()));

    }
}
