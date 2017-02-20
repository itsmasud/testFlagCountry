package com.fieldnation.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Expense;
import com.fieldnation.fntools.misc;
import com.fieldnation.v2.data.model.ExpenseCategories;
import com.fieldnation.v2.data.model.ExpenseCategory;

/**
 * Created by Michael Carver on 7/24/2015.
 */
public class ExpenseCounterOfferView extends RelativeLayout {
    private static final String TAG = "ExpenseCounterOfferView";

    // Ui
    private TextView _descriptionTextView;
    private TextView _costTextView;
    private TextView _categoryTextView;

    // Data
    private ExpenseCategory[] _categories;
    private Expense _expense;

    public ExpenseCounterOfferView(Context context) {
        super(context);
        init();
    }

    public ExpenseCounterOfferView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpenseCounterOfferView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_expense_counteroffer, this);

        if (isInEditMode())
            return;

        _descriptionTextView = (TextView) findViewById(R.id.description_textview);
        _categoryTextView = (TextView) findViewById(R.id.category_textview);
        _costTextView = (TextView) findViewById(R.id.cost_textview);

        ExpenseCategories categories = new ExpenseCategories(getContext());
        categories.setListener(_categoriesListener);

    }

    private final ExpenseCategories.Listener _categoriesListener = new ExpenseCategories.Listener() {
        @Override
        public void onHaveCategories(ExpenseCategory[] categories) {
            _categories = categories;
            populateUi();
        }
    };

    public void setExpense(Expense expense) {
        _expense = expense;
        populateUi();
    }

    private void populateUi() {
        if (_categories == null)
            return;

        if (_expense == null)
            return;

        if (_descriptionTextView == null)
            return;

        _descriptionTextView.setText(_expense.getDescription());
        _costTextView.setText(misc.toCurrency(_expense.getPrice()));

        _categoryTextView.setVisibility(GONE);
        if (_expense.getCategoryId() != null) {
            for (ExpenseCategory category : _categories) {
                if (category.getId().equals(_expense.getCategoryId())) {
                    _categoryTextView.setText(category.getName());
                    _categoryTextView.setVisibility(VISIBLE);
                }
            }

        }
    }
}
