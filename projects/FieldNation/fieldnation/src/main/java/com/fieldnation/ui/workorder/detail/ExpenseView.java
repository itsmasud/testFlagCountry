package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fntools.misc;

public class ExpenseView extends LinearLayout {
    private static final String TAG = "ExpenseView";

    // UI
    private TextView _descriptionTextView;
    private TextView _categoryTextView;
    private TextView _costTextView;

    // Data
    private Workorder _workorder;
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
    public void setData(Workorder workorder, Expense expense) {
        _expense = expense;
        _workorder = workorder;
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
        // TODO need to map the ID to a real string
        _categoryTextView.setVisibility(View.GONE);
        if (_categories != null && _expense.getCategoryId() != null) {
            for (ExpenseCategory _category : _categories) {
                if (_category.getId().equals(_expense.getCategoryId())) {
                    _categoryTextView.setText(_category.getName());
                    _categoryTextView.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        // TODO, need to get quantity and price per item numbers
        _costTextView.setText(misc.toCurrency(_expense.getPrice()));

    }
}
