package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.data.workorder.ExpenseCategory;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

public class ExpenseView extends LinearLayout {
    private static final String TAG = "ui.workorder.detail.ExpenseView";

    // UI
    private TextView _descriptionTextView;
    private TextView _categoryTextView;
    private TextView _costTextView;
    private ImageButton _deleteImageButton;

    // Data
    private Workorder _workorder;
    private Listener _listener;
    private AdditionalExpense _expense = null;
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
        _deleteImageButton = (ImageButton) findViewById(R.id.delete_imagebutton);
        _deleteImageButton.setOnClickListener(_delete_onClick);

        ExpenseCategories categories = ExpenseCategories.getInstance(getContext());
        categories.setListener(_categoriesListener);
    }

    /*-*********************************-*/
    /*-				Event				-*/
    /*-*********************************-*/

    private ExpenseCategories.Listener _categoriesListener = new ExpenseCategories.Listener() {
        @Override
        public void onHaveCategories(ExpenseCategory[] categories) {
            _categories = categories;
            refresh();
        }
    };
    private View.OnClickListener _delete_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_listener != null)
                _listener.onDelete(ExpenseView.this, _expense);
        }
    };

    /*-*************************************-*/
    /*-				Mutators				-*/
    /*-*************************************-*/
    public void setData(Workorder workorder, AdditionalExpense expense) {
        _expense = expense;
        _workorder = workorder;
        refresh();
    }

    private void refresh() {
        if (_expense == null)
            return;

        _descriptionTextView.setText(_expense.getDescription());
        // TODO need to map the ID to a real string
        _categoryTextView.setVisibility(View.GONE);
        if (_categories != null && _expense.getCategoryId() != null) {
            for (int i = 0; i < _categories.length; i++) {
                if (_categories[i].getId() == _expense.getCategoryId()) {
                    _categoryTextView.setText(_categories[i].getName());
                    _categoryTextView.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        // TODO, need to get quantity and price per item numbers
        _costTextView.setText(misc.toCurrency(_expense.getPrice()));

        if (_workorder.canModify()) {
            _deleteImageButton.setVisibility(View.VISIBLE);
        } else {
            _deleteImageButton.setVisibility(View.INVISIBLE);
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public interface Listener {
        public void onDelete(ExpenseView view, AdditionalExpense expense);
    }
}
