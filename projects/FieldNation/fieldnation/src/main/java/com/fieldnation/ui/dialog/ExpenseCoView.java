package com.fieldnation.ui.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by michael.carver on 11/5/2014.
 */
public class ExpenseCoView extends RelativeLayout {
    //Ui
    private TextView _noExpensesTextView;
    private LinearLayout _expensesList;
    private Button _addButton;

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

        _addButton = (Button)findViewById(R.id.add_button);
    }

}
