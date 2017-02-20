package com.fieldnation.v2.data.model;

import android.content.Context;

import com.fieldnation.fntools.UniqueTag;

public class ExpenseCategories {
    private final String TAG = UniqueTag.makeTag("ExpenseCategories");

    private static ExpenseCategory[] _categories = null;

    static {
        _categories = new ExpenseCategory[4];
        _categories[0] = new ExpenseCategory(1, "Personal Material Costs (Other Materials)");
        _categories[1] = new ExpenseCategory(2, "Travel Expense");
        _categories[2] = new ExpenseCategory(3, "Scope-of-Work Change");
        _categories[3] = new ExpenseCategory(4, "Real Material Costs (attached to land/building)");
    }

    private final Context _context;
    private Listener _listener = null;

    public ExpenseCategories(Context context) {
        _context = context.getApplicationContext();
    }

    public void setListener(Listener listener) {
        _listener = listener;

        if (_categories != null) {
            _listener.onHaveCategories(_categories);
        }
    }

    public interface Listener {
        void onHaveCategories(ExpenseCategory[] categories);
    }
}
